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

package xyz.klinker.android.article.data.model;

/**
 * Model containing article data from the server.
 */
public final class ArticleModel implements DatabaseTable {

    public static final String TABLE = "article";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ALIAS = "alias";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_SOURCE = "source";
    public static final String COLUMN_DOMAIN = "domain";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_INSERTED_AT = "inserted_at";
    public static final String COLUMN_IS_ARTICLE = "is_article";
    public static final String COLUMN_SAVED = "saved";

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE + " (" +
            COLUMN_ID + " integer primary key, " +
            COLUMN_ALIAS + " text, " +
            COLUMN_URL + " text not null, " +
            COLUMN_TITLE + " text, " +
            COLUMN_DESCRIPTION + " text, " +
            COLUMN_IMAGE + " text, " +
            COLUMN_AUTHOR + " text, " +
            COLUMN_SOURCE + " text, " +
            COLUMN_DOMAIN + " text, " +
            COLUMN_DURATION + " integer, " +
            COLUMN_INSERTED_AT + " integer not null, " +
            COLUMN_IS_ARTICLE + " integer not null, " +
            COLUMN_SAVED + " integer not null" +
            ");";

    private static final String[] INDEXES = {
            "create index if not exists url_article_index on " + TABLE +
                    " (" + COLUMN_URL + ");"
    };

    @Override
    public String getCreateStatement() {
        return DATABASE_CREATE;
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String[] getIndexStatements() {
        return INDEXES;
    }

}
