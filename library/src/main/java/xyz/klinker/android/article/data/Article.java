/*
 * Copyright (C) 2016 Jacob Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.klinker.android.article.data;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import xyz.klinker.android.article.data.model.ArticleModel;
import xyz.klinker.android.article.data.model.ContentModel;
import xyz.klinker.android.article.data.model.DatabaseModel;
import xyz.klinker.android.article.data.model.SourceModel;

/**
 * Model holding all possible elements in a response from the server.
 */
public final class Article implements DatabaseModel {

    public long id;
    public String alias;
    public String url;
    public String title;
    public String description;
    public String image;
    public String content;
    public String author;
    public String source;
    public String domain;
    public int duration;
    public long insertedAt;
    public boolean isArticle;
    public boolean saved;
    public Long sourceId;
    public String sourceModelName;
    public String sourceModelImageUrl;

    /**
     * Creates a blank article that can be filled manually.
     */
    public Article() {

    }

    /**
     * Creates an article that is filled automatically from a provided cursor.
     *
     * @param cursor the cursor to fill the article from.
     */
    public Article(Cursor cursor) {
        fillFromCursor(cursor);
    }

    /**
     * Creates an article that is filled automatically from the intent, which was filled with
     * @{link Article.putIntoIntent()}.
     *
     * @param intent the intent to fill the article from.
     */
    public Article(Intent intent) {
        fillFromIntent(intent);
    }

    @Override
    public void fillFromCursor(Cursor cursor) {
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String column = cursor.getColumnName(i);

            if (column.equals(ArticleModel.COLUMN_ID)) {
                this.id = cursor.getLong(i);
            } else if (column.equals(ArticleModel.COLUMN_ALIAS)) {
                this.alias = cursor.getString(i);
            } else if (column.equals(ArticleModel.COLUMN_URL)) {
                this.url = cursor.getString(i);
            } else if (column.equals(ArticleModel.COLUMN_TITLE)) {
                this.title = cursor.getString(i);
            } else if (column.equals(ArticleModel.COLUMN_DESCRIPTION)) {
                this.description = cursor.getString(i);
            } else if (column.equals(ArticleModel.COLUMN_IMAGE)) {
                this.image = cursor.getString(i);
            } else if (column.equals(ContentModel.COLUMN_CONTENT)) {
                this.content = cursor.getString(i);
            } else if (column.equals(ArticleModel.COLUMN_AUTHOR)) {
                this.author = cursor.getString(i);
            } else if (column.equals(ArticleModel.COLUMN_SOURCE)) {
                this.source = cursor.getString(i);
            } else if (column.equals(ArticleModel.COLUMN_DOMAIN)) {
                this.domain = cursor.getString(i);
            } else if (column.equals(ArticleModel.COLUMN_DURATION)) {
                this.duration = cursor.getInt(i);
            } else if (column.equals(ArticleModel.COLUMN_INSERTED_AT)) {
                this.insertedAt = cursor.getLong(i);
            } else if (column.equals(ArticleModel.COLUMN_IS_ARTICLE)) {
                this.isArticle = cursor.getInt(i) == 1;
            } else if (column.equals(ArticleModel.COLUMN_SAVED)) {
                this.saved = cursor.getInt(i) == 1;
            } else if (column.equals(ArticleModel.COLUMN_SOURCE_ID)) {
                this.sourceId = cursor.getLong(i);
            } else if (column.equals(SourceModel.COLUMN_NAME)) {
                this.sourceModelName = cursor.getString(i);
            } else if (column.equals(SourceModel.COLUMN_IMAGE_URL)) {
                this.sourceModelImageUrl = cursor.getString(i);
            }
        }
    }

    /**
     * Fills an article from an Intent.
     */
    public void fillFromIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        this.id = extras.getLong(ArticleModel.COLUMN_ID);
        this.alias = extras.getString(ArticleModel.COLUMN_ALIAS);
        this.url = extras.getString(ArticleModel.COLUMN_URL);
        this.title = extras.getString(ArticleModel.COLUMN_TITLE);
        this.description = extras.getString(ArticleModel.COLUMN_DESCRIPTION);
        this.image = extras.getString(ArticleModel.COLUMN_IMAGE);
        this.content = extras.getString(ContentModel.COLUMN_CONTENT);
        this.author = extras.getString(ArticleModel.COLUMN_AUTHOR);
        this.source = extras.getString(ArticleModel.COLUMN_SOURCE);
        this.domain = extras.getString(ArticleModel.COLUMN_DOMAIN);
        this.duration = extras.getInt(ArticleModel.COLUMN_DURATION);
        this.insertedAt = extras.getLong(ArticleModel.COLUMN_INSERTED_AT);
        this.isArticle = extras.getBoolean(ArticleModel.COLUMN_IS_ARTICLE);
        this.saved = extras.getBoolean(ArticleModel.COLUMN_SAVED);
    }

    /**
     * Adds article data to an intent so that it can be sent over a broadcast.
     */
    public void putIntoIntent(Intent intent) {
        intent.putExtra(ArticleModel.COLUMN_ID, this.id);
        intent.putExtra(ArticleModel.COLUMN_ALIAS, this.alias);
        intent.putExtra(ArticleModel.COLUMN_URL, this.url);
        intent.putExtra(ArticleModel.COLUMN_TITLE, this.title);
        intent.putExtra(ArticleModel.COLUMN_DESCRIPTION, this.description);
        intent.putExtra(ArticleModel.COLUMN_IMAGE, this.image);
        intent.putExtra(ContentModel.COLUMN_CONTENT, this.content);
        intent.putExtra(ArticleModel.COLUMN_AUTHOR, this.author);
        intent.putExtra(ArticleModel.COLUMN_SOURCE, this.source);
        intent.putExtra(ArticleModel.COLUMN_DOMAIN, this.domain);
        intent.putExtra(ArticleModel.COLUMN_DURATION, this.duration);
        intent.putExtra(ArticleModel.COLUMN_INSERTED_AT, this.insertedAt);
        intent.putExtra(ArticleModel.COLUMN_IS_ARTICLE, this.isArticle);
        intent.putExtra(ArticleModel.COLUMN_SAVED, this.saved);
    }

}
