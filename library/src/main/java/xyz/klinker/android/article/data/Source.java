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
import xyz.klinker.android.article.data.model.SourceModel;

/**
 * Model holding information about a source, extracted and joined from the database.
 */
public class Source implements DatabaseModel {

    public long id;
    public String name;
    public String imageUrl;
    public String categoryName;
    public Long categoryId;
    public long remoteId;

    /**
     * Creates a blank source that can be filled manually.
     */
    public Source() {

    }

    /**
     * Creates a source that is filled automatically from a provided cursor.
     *
     * @param cursor the cursor to fill the source from.
     */
    public Source(Cursor cursor) {
        fillFromCursor(cursor);
    }

    @Override
    public void fillFromCursor(Cursor cursor) {
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            String column = cursor.getColumnName(i);

            if (column.equals("s" + SourceModel.COLUMN_ID)) {
                this.id = cursor.getLong(i);
            } else if (column.equals("s" + SourceModel.COLUMN_NAME)) {
                this.name = cursor.getString(i);
            } else if (column.equals("s" + SourceModel.COLUMN_IMAGE_URL)) {
                this.imageUrl = cursor.getString(i);
            } else if (column.equals("s" + SourceModel.COLUMN_REMOTE_ID)) {
                this.remoteId = cursor.getLong(i);
            } else if (column.equals("c" + CategoryModel.COLUMN_ID)) {
                this.categoryId = cursor.getLong(i);
            } else if (column.equals("c" + CategoryModel.COLUMN_NAME)) {
                this.categoryName = cursor.getString(i);
            }
        }
    }
}
