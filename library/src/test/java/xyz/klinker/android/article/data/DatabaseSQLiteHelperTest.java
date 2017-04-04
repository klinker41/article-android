/*
 * Copyright (C) 2017 Jake Klinker
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

import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;

import xyz.klinker.android.article.ArticleRobolectricSuite;
import xyz.klinker.android.article.data.model.ArticleModel;
import xyz.klinker.android.article.data.model.CategoryModel;
import xyz.klinker.android.article.data.model.ContentModel;
import xyz.klinker.android.article.data.model.SourceModel;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class DatabaseSQLiteHelperTest extends ArticleRobolectricSuite {

    private DatabaseSQLiteHelper helper;

    @Mock
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        helper = new DatabaseSQLiteHelper(RuntimeEnvironment.application);
    }

    @Test
    public void onCreate() {
        helper.onCreate(database);
        verifyCreateStatement();
    }

    @Test
    public void onUpgrade_1to2() {
        helper.onUpgrade(database, 1, 2);
        verify2Upgrade();
    }

    @Test
    public void onUpgrade1to3() {
        helper.onUpgrade(database, 1, 3);
        verify2Upgrade();
        verify3Upgrade();
    }

    @Test
    public void onUpgrade2to3() {
        helper.onUpgrade(database, 2, 3);
        verify3Upgrade();
    }

    @Test
    public void onUpgrade1to4() {
        helper.onUpgrade(database, 1, 4);
        verify2Upgrade();
        verify3Upgrade();
        verify4Upgrade();
    }

    @Test
    public void onUpgrade2to4() {
        helper.onUpgrade(database, 2, 4);
        verify3Upgrade();
        verify4Upgrade();
    }

    @Test
    public void onUpgrade3to4() {
        helper.onUpgrade(database, 3, 4);
        verify4Upgrade();
    }

    @Test
    public void onDrop() {
        helper.onDrop(database);
        verifyDropStatement();
    }

    private void verifyCreateStatement() {
        verify(database).execSQL(new ArticleModel().getCreateStatement());
        verify(database).execSQL(new ContentModel().getCreateStatement());
        verify(database).execSQL(new SourceModel().getCreateStatement());
        verify(database).execSQL(new CategoryModel().getCreateStatement());
        verify(database).execSQL(new ArticleModel().getIndexStatements()[0]);
        verify(database).execSQL(new ArticleModel().getIndexStatements()[1]);
        verify(database).execSQL(new ContentModel().getIndexStatements()[0]);
        verify(database).execSQL(new SourceModel().getIndexStatements()[0]);
        verify(database).execSQL(new SourceModel().getIndexStatements()[1]);
        verify(database).execSQL(new CategoryModel().getIndexStatements()[0]);
        verifyNoMoreInteractions(database);
    }

    private void verify2Upgrade() {
        verify(database).execSQL("ALTER TABLE article ADD COLUMN saved integer not null DEFAULT 0");
    }

    private void verify3Upgrade() {
        verify(database).execSQL(new SourceModel().getCreateStatement());
        verify(database).execSQL(new CategoryModel().getCreateStatement());
        verify(database).execSQL(new SourceModel().getIndexStatements()[0]);
        verify(database).execSQL(new SourceModel().getIndexStatements()[1]);
        verify(database).execSQL(new CategoryModel().getIndexStatements()[0]);
        verify(database).execSQL("ALTER TABLE article ADD COLUMN source_id integer");
        verify(database).execSQL(new ArticleModel().getIndexStatements()[1]);
    }

    private void verify4Upgrade() {
        // do nothing for now, fill with more database migrations.
    }

    private void verifyDropStatement() {
        verify(database).execSQL("drop table if exists article");
        verify(database).execSQL("drop table if exists content");
        verify(database).execSQL("drop table if exists source");
        verify(database).execSQL("drop table if exists category");
        verifyNoMoreInteractions(database);
    }
}
