package xyz.klinker.android.article.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import xyz.klinker.android.article.ArticleUtils;
import xyz.klinker.android.article.data.model.ArticleModel;
import xyz.klinker.android.article.data.model.CategoryModel;
import xyz.klinker.android.article.data.model.ContentModel;
import xyz.klinker.android.article.data.model.SourceModel;

/**
 * Handles interactions with database models.
 */
public class DataSource {

    protected Context context;
    private SQLiteDatabase database;
    private DatabaseSQLiteHelper dbHelper;
    private AtomicInteger openCounter = new AtomicInteger();

    /**
     * Gets a new instance of the DataSource.
     *
     * @param context the current application instance.
     * @return the data source.
     */
    public static DataSource get(Context context) {
        return new DataSource(context);
    }

    /**
     * Private constructor to force a singleton.
     *
     * @param context Current calling context
     */
    private DataSource(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseSQLiteHelper(context);
    }

    /**
     * Contructor to help with testing.
     *
     * @param helper Mock of the database helper
     */
    @VisibleForTesting
    DataSource(DatabaseSQLiteHelper helper) {
        this.dbHelper = helper;
    }

    /**
     * Constructor to help with testing.
     *
     * @param database Mock of the sqlite database
     */
    @VisibleForTesting
    public DataSource(SQLiteDatabase database) {
        this.database = database;
    }

    /**
     * Opens the database.
     */
    public synchronized void open() {
        if (openCounter.incrementAndGet() == 1) {
            database = dbHelper.getWritableDatabase();
        }
    }

    /**
     * Checks if the database is open.
     */
    public boolean isOpen() {
        return database != null && database.isOpen();
    }

    /**
     * Closes the database.
     */
    public synchronized void close() {
        if (openCounter.decrementAndGet() == 0) {
            dbHelper.close();
        }
    }

    /**
     * Get the currently open database
     *
     * @return sqlite database
     */
    @VisibleForTesting
    public SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * Deletes all data from the tables.
     */
    public void clearTables() {
        database.delete(ContentModel.TABLE, null, null);
        database.delete(ArticleModel.TABLE, null, null);
        database.delete(CategoryModel.TABLE, null, null);
        database.delete(SourceModel.TABLE, null, null);
    }

    /**
     * Begins a bulk transaction on the database.
     */
    public void beginTransaction() {
        database.beginTransaction();
    }

    /**
     * Executes a raw sql statement on the database. Can be used in conjunction with
     * beginTransaction and endTransaction if bulk.
     *
     * @param sql the sql statement.
     */
    public void execSql(String sql) {
        database.execSQL(sql);
    }

    /**
     * Execute a raw sql query on the database.
     *
     * @param sql the sql statement
     * @return cursor for the data
     */
    public Cursor rawQuery(String sql) {
        return database.rawQuery(sql, null);
    }

    /**
     * Sets the transaction into a successful state so that it can be committed to the database.
     * Should be used in conjunction with beginTransaction() and endTransaction().
     */
    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    /**
     * Ends a bulk transaction on the database.
     */
    public void endTransaction() {
        database.endTransaction();
    }

    /**
     * Inserts a single article into the database for caching purposes.
     *
     * @return the id of the inserted item.
     */
    public long insertArticle(Article article) {
        // remove any extra query parameters from the url
        article.url = ArticleUtils.removeUrlParameters(article.url);

        ContentValues values = new ContentValues(11);
        values.put(ArticleModel.COLUMN_ALIAS, article.alias);
        values.put(ArticleModel.COLUMN_URL, article.url);
        values.put(ArticleModel.COLUMN_TITLE, article.title);
        values.put(ArticleModel.COLUMN_DESCRIPTION, article.description);
        values.put(ArticleModel.COLUMN_IMAGE, article.image);
        values.put(ArticleModel.COLUMN_AUTHOR, article.author);
        values.put(ArticleModel.COLUMN_SOURCE, article.source);
        values.put(ArticleModel.COLUMN_DOMAIN, article.domain);
        values.put(ArticleModel.COLUMN_DURATION, article.duration);
        values.put(ArticleModel.COLUMN_IS_ARTICLE, article.isArticle);
        values.put(ArticleModel.COLUMN_SAVED, article.saved);
        values.put(ArticleModel.COLUMN_SOURCE_ID, article.sourceId);
        if (article.insertedAt == 0) {
            values.put(ArticleModel.COLUMN_INSERTED_AT, System.currentTimeMillis());
        } else {
            values.put(ArticleModel.COLUMN_INSERTED_AT, article.insertedAt);
        }

        long id = database.insert(ArticleModel.TABLE, null, values);

        values = new ContentValues(2);
        values.put(ContentModel.COLUMN_ARTICLE_ID, id);
        values.put(ContentModel.COLUMN_CONTENT, article.content);

        return database.insert(ContentModel.TABLE, null, values);
    }

    /**
     * Updates an article's saved state.
     */
    public void updateSavedArticleState(Article article) {
        ContentValues values = new ContentValues(1);
        values.put(ArticleModel.COLUMN_SAVED, article.saved);

        database.update(
                ArticleModel.TABLE, values, "_id=?", new String[] {Long.toString(article.id)});
    }

    /**
     * Updates an article's content.
     */
    public void updateArticleContent(Article article) {
        ContentValues values = new ContentValues(1);
        values.put(ContentModel.COLUMN_CONTENT, article.content);

        database.update(
                ContentModel.TABLE,
                values,
                "article_id=?",
                new String[] {Long.toString(article.id)});
    }

    /**
     * Gets a single article from the database. If there are multiple with the same URL, only the
     * first is returned.
     */
    public Article getArticle(String url) {
        // remove any extra query parameters from the url
        url = ArticleUtils.removeUrlParameters(url);

        Cursor cursor = database.query(
                ArticleModel.TABLE + " a left outer join " + ContentModel.TABLE + " c " +
                        "on a." + ArticleModel.COLUMN_ID + " = c." + ContentModel.COLUMN_ARTICLE_ID,
                new String[] {
                        "a." + ArticleModel.COLUMN_ID + " as " + ArticleModel.COLUMN_ID,
                        "a." + ArticleModel.COLUMN_ALIAS + " as " + ArticleModel.COLUMN_ALIAS,
                        "a." + ArticleModel.COLUMN_URL + " as " + ArticleModel.COLUMN_URL,
                        "a." + ArticleModel.COLUMN_TITLE + " as " + ArticleModel.COLUMN_TITLE,
                        "a." + ArticleModel.COLUMN_DESCRIPTION + " as " + ArticleModel.COLUMN_DESCRIPTION,
                        "a." + ArticleModel.COLUMN_IMAGE + " as " + ArticleModel.COLUMN_IMAGE,
                        "a." + ArticleModel.COLUMN_AUTHOR + " as " + ArticleModel.COLUMN_AUTHOR,
                        "a." + ArticleModel.COLUMN_SOURCE + " as " + ArticleModel.COLUMN_SOURCE,
                        "a." + ArticleModel.COLUMN_DOMAIN + " as " + ArticleModel.COLUMN_DOMAIN,
                        "a." + ArticleModel.COLUMN_DURATION + " as " + ArticleModel.COLUMN_DURATION,
                        "a." + ArticleModel.COLUMN_INSERTED_AT + " as " + ArticleModel.COLUMN_INSERTED_AT,
                        "a." + ArticleModel.COLUMN_IS_ARTICLE + " as " + ArticleModel.COLUMN_IS_ARTICLE,
                        "a." + ArticleModel.COLUMN_SAVED + " as " + ArticleModel.COLUMN_SAVED,
                        "c." + ContentModel.COLUMN_CONTENT + " as " + ContentModel.COLUMN_CONTENT,
                },
                ArticleModel.COLUMN_URL + "=?",
                new String[] { url },
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            Article article = new Article(cursor);
            cursor.close();
            return article;
        } else {
            return null;
        }
    }

    /**
     * Gets all articles in the database.
     *
     * NOTE: this method does not return the content associated with the article, that would be
     *       slow as some articles can get very large.
     */
    public Cursor getAllArticles() {
        return database.query(
                ArticleModel.TABLE + " a left outer join " + SourceModel.TABLE + " s " +
                    "on a." + ArticleModel.COLUMN_SOURCE_ID + " = s." + SourceModel.COLUMN_REMOTE_ID,
                null,
                null,
                null,
                null,
                null,
                ArticleModel.COLUMN_INSERTED_AT + " desc");
    }

    /**
     * Gets all saved articles in the database.
     *
     * NOTE: this method does not return the content associated with the article, that would be
     *       slow as some articles can get very large.
     */
    public Cursor getSavedArticles() {
        return database.query(
                ArticleModel.TABLE + " a left outer join " + SourceModel.TABLE + " s " +
                    "on a." + ArticleModel.COLUMN_SOURCE_ID + " = s." + SourceModel.COLUMN_REMOTE_ID,
                null,
                ArticleModel.COLUMN_SAVED + "=1",
                null,
                null,
                null,
                ArticleModel.COLUMN_INSERTED_AT + " desc");
    }

    /**
     * Gets all articles for a particular source.
     *
     * NOTE: this method does not return the content associated with the article, that would be
     *       slow as some articles can get very large.
     *
     * @param remoteSourceId the source to get articles for.
     * @return a cursor of articles.
     */
    public Cursor getArticlesForSource(long remoteSourceId) {
        return database.query(
                ArticleModel.TABLE + " a left outer join " + SourceModel.TABLE + " s " +
                    "on a." + ArticleModel.COLUMN_SOURCE_ID + " = s." + SourceModel.COLUMN_REMOTE_ID,
                null,
                ArticleModel.COLUMN_SOURCE_ID + "=?",
                new String[] {Long.toString(remoteSourceId)},
                null,
                null,
                ArticleModel.COLUMN_INSERTED_AT + " desc");
    }

    /**
     * Deletes an article and its content.
     *
     * @param article The article to delete.
     */
    public void deleteArticle(Article article) {
        deleteArticle(article.id);
    }

    /**
     * Deletes an article and its content.
     *
     * @param articleId the id of the article to delete.
     */
    public void deleteArticle(long articleId) {
        database.delete(
                ArticleModel.TABLE,
                ArticleModel.COLUMN_ID + "=?",
                new String[] {Long.toString(articleId)});
        database.delete(
                ContentModel.TABLE,
                ContentModel.COLUMN_ARTICLE_ID + "=?",
                new String[] {Long.toString(articleId)});
    }

    /**
     * Inserts a category into the database with the provided name.
     *
     * @param categoryName the category name.
     * @return the id of the inserted category.
     */
    public long insertCategory(String categoryName) {
        ContentValues values = new ContentValues(1);
        values.put(CategoryModel.COLUMN_NAME, categoryName);
        return database.insert(CategoryModel.TABLE, null, values);
    }

    /**
     * Checks to see if a category already exists in the database.
     *
     * @param name the category name to search for.
     * @return true if it exists, false otherwise.
     */
    @VisibleForTesting
    boolean categoryExists(String name) {
        return getCategoryId(name) != null;
    }

    /**
     * Gets the id of a category.
     */
    @VisibleForTesting
    Long getCategoryId(String name) {
        Cursor cursor = database.query(
                CategoryModel.TABLE,
                null,
                CategoryModel.COLUMN_NAME + "=?",
                new String[] {name},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex(CategoryModel.COLUMN_ID));
            cursor.close();
            return id;
        } else {
            return null;
        }
    }

    /**
     * Inserts a source into the database. Also will insert a category if that category does not
     * already exist.
     *
     * @param source the source to insert.
     */
    public void insertSource(Source source) {
        if (!categoryExists(source.categoryName)) {
            source.categoryId = insertCategory(source.categoryName);
        } else if (source.categoryId == null) {
            source.categoryId = getCategoryId(source.categoryName);
        }

        ContentValues values = new ContentValues(4);
        values.put(SourceModel.COLUMN_NAME, source.name);
        values.put(SourceModel.COLUMN_IMAGE_URL, source.imageUrl);
        values.put(SourceModel.COLUMN_REMOTE_ID, source.remoteId);
        values.put(SourceModel.COLUMN_CATEGORY_ID, source.categoryId);
        database.insert(SourceModel.TABLE, null, values);
    }

    /**
     * Deletes a source from the database depending on its name.
     */
    public void deleteSource(String name) {
        database.delete(SourceModel.TABLE, SourceModel.COLUMN_NAME + "=?", new String[] {name});
    }

    /**
     * Gets all sources in the database.
     *
     * @return a list of all sources.
     */
    public List<Source> getSources() {
        Cursor cursor = database.query(
                SourceModel.TABLE + " s left outer join " + CategoryModel.TABLE + " c on " +
                        "s." + SourceModel.COLUMN_CATEGORY_ID + " = " +
                        "c." + CategoryModel.COLUMN_ID,
                new String[] {
                        "s." + SourceModel.COLUMN_ID + " as s" + SourceModel.COLUMN_ID,
                        "s." + SourceModel.COLUMN_NAME + " as s" + SourceModel.COLUMN_NAME,
                        "s." + SourceModel.COLUMN_IMAGE_URL + " as s" + SourceModel.COLUMN_IMAGE_URL,
                        "s." + SourceModel.COLUMN_REMOTE_ID + " as s" + SourceModel.COLUMN_REMOTE_ID,
                        "c." + CategoryModel.COLUMN_ID + " as c" + CategoryModel.COLUMN_ID,
                        "c." + CategoryModel.COLUMN_NAME + " as c" + CategoryModel.COLUMN_NAME
                },
                null,
                null,
                null,
                null,
                "s" + SourceModel.COLUMN_NAME + " asc");

        List<Source> sources = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                sources.add(new Source(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return sources;
    }

    /**
     * Gets a list of categories and the number of articles that each contains, since the provided
     * timestamp.
     *
     * @param timestamp the timestamp to query articles starting at.
     * @return a list of categories.
     */
    public CategoryCounts getCategoryCounts(long timestamp) {
        Cursor cursor = database.query(
                ArticleModel.TABLE + " a join " + SourceModel.TABLE + " s on a." +
                        ArticleModel.COLUMN_SOURCE_ID + " = s." + SourceModel.COLUMN_REMOTE_ID +
                        " join " + CategoryModel.TABLE + " c on s." +
                        SourceModel.COLUMN_CATEGORY_ID + " = c." + CategoryModel.COLUMN_ID,
                new String[] {
                        "c." + CategoryModel.COLUMN_NAME + " as " + CategoryModel.COLUMN_NAME,
                        "count(c." + CategoryModel.COLUMN_ID + ") as count"
                },
                "a." + ArticleModel.COLUMN_INSERTED_AT + " > ? and " +
                        "a." + ArticleModel.COLUMN_SOURCE_ID + " not null",
                new String[] { Long.toString(timestamp) },
                "c." + CategoryModel.COLUMN_ID,
                null,
                "count desc"
        );

        List<Category> categories = new ArrayList<>();
        int total = 0;
        if (cursor != null & cursor.moveToFirst()) {
            do {
                Category category = new Category(cursor);
                total += category.numberArticles;
                categories.add(category);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return new CategoryCounts(categories, total);
    }
}
