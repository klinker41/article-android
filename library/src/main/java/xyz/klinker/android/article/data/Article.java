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

import android.database.Cursor;

import xyz.klinker.android.article.data.model.ArticleModel;
import xyz.klinker.android.article.data.model.ContentModel;
import xyz.klinker.android.article.data.model.DatabaseModel;

/**
 * Model holding all possible elements in a response from the server.
 */
public class Article implements DatabaseModel {

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
    public boolean isArticle;

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
            } else if (column.equals(ArticleModel.COLUMN_IS_ARTICLE)) {
                this.isArticle = cursor.getInt(i) == 1;
            }
        }
    }

}
