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

package xyz.klinker.android.article.data;

import android.database.Cursor;

import xyz.klinker.android.article.data.model.CategoryModel;
import xyz.klinker.android.article.data.model.DatabaseModel;

/**
 * Model holding information about a category, extracted from the database.
 */
public class Category implements DatabaseModel {

    public String name;
    public int numberArticles;

    /**
     * Creates a blank category taht can be filled manually.
     */
    public Category() {

    }

    /**
     * Creates a category that is filled automatically from a provided cursor.
     *
     * @param cursor the cursor to fill the category from.
     */
    public Category(Cursor cursor) {
        fillFromCursor(cursor);
    }

    @Override
    public void fillFromCursor(Cursor cursor) {
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String column = cursor.getColumnName(i);

            if (column.equals(CategoryModel.COLUMN_NAME)) {
                this.name = cursor.getString(i);
            } else if (column.equals("count")) {
                this.numberArticles = cursor.getInt(i);
            }
        }
    }
}
