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
 * Model containing article content from the server. This is separate from the article model
 * so that we can keep that table small and light. This one will store blobs of text and each
 * entry could get quite large potentially.
 */
public class ContentModel implements DatabaseTable {

    public static final String TABLE = "content";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ARTICLE_ID = "article_id";
    public static final String COLUMN_CONTENT = "content";

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE + " (" +
            COLUMN_ID + " integer primary key, " +
            COLUMN_ARTICLE_ID + " integer not null, " +
            COLUMN_CONTENT + " text" +
            ");";

    private static final String[] INDEXES = {
            "create index if not exists article_content_index on " + TABLE +
                    " (" + COLUMN_ARTICLE_ID + ");"
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
