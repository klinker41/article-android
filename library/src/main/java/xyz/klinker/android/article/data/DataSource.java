package xyz.klinker.android.article.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

import xyz.klinker.android.article.data.model.ArticleModel;
import xyz.klinker.android.article.data.model.ContentModel;

/**
 * Handles interactions with database models.
 */
public class DataSource {

    private static volatile DataSource instance;

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
    public static DataSource getInstance(Context context) {
        if (instance == null) {
            instance = new DataSource(context);
        }

        return instance;
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
     * Deletes all data from the tables.
     */
    public void clearTables() {
        database.delete(ContentModel.TABLE, null, null);
        database.delete(ArticleModel.TABLE, null, null);
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

}
