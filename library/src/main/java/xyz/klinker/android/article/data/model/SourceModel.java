/*
 * Copyright (C) 2017 Jacob Klinker
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
 * Model containing source data from the server.
 */
public class SourceModel implements DatabaseTable {

    public static final String TABLE = "source";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REMOTE_ID = "remote_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_CATEGORY_ID = "category_id";

    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE + " (" +
            COLUMN_ID + " integer primary key, " +
            COLUMN_REMOTE_ID + " integer not null, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_IMAGE_URL + " text not null, " +
            COLUMN_CATEGORY_ID + " integer not null" +
            ");";

    private static final String[] INDEXES = {
            "create index if not exists category_id_source_index on " + TABLE +
                    " (" + COLUMN_CATEGORY_ID + ");"
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
