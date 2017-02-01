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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import xyz.klinker.android.article.data.model.ArticleModel;
import xyz.klinker.android.article.data.model.CategoryModel;
import xyz.klinker.android.article.data.model.ContentModel;
import xyz.klinker.android.article.data.model.DatabaseTable;
import xyz.klinker.android.article.data.model.SourceModel;

/**
 * Handles creating and updating a database.
 */
public class DatabaseSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "articles.db";
    private static final int DATABASE_VERSION = 3;

    private DatabaseTable[] tables = {
            new ArticleModel(),
            new ContentModel(),
            new SourceModel(),
            new CategoryModel()
    };

    /**
     * Construct a new database helper.
     *
     * @param context the current application context.
     */
    public DatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (DatabaseTable table : tables) {
            db.execSQL(table.getCreateStatement());

            for (String index : table.getIndexStatements()) {
                db.execSQL(index);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            try {
                db.execSQL("ALTER TABLE article ADD COLUMN saved integer not null DEFAULT 0");
            } catch(Exception e) { }
        }

        if (oldVersion < 3) {
            try {
                db.execSQL(tables[2].getCreateStatement());
                db.execSQL(tables[3].getCreateStatement());
                db.execSQL(tables[2].getIndexStatements()[0]);
                db.execSQL(tables[2].getIndexStatements()[1]);
                db.execSQL(tables[3].getIndexStatements()[0]);
                db.execSQL("ALTER TABLE article ADD COLUMN source_id integer");
                db.execSQL(tables[0].getIndexStatements()[1]);
            } catch(Exception e) { }
        }
    }

    public void onDrop(SQLiteDatabase db) {
        for (DatabaseTable table : tables) {
            db.execSQL("drop table if exists " + table.getTableName());
        }
    }

}
