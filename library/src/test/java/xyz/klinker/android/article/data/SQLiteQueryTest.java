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

import org.junit.Test;

import xyz.klinker.android.article.ArticleRealDataSuite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SQLiteQueryTest extends ArticleRealDataSuite {

    @Test
    public void getArticle() {
        Article article = source.getArticle("http://test.com/");

        assertNotNull(article);
        assertNotNull(article.alias);
        assertNotNull(article.url);
        assertNotNull(article.title);
        assertNotNull(article.description);
        assertNotNull(article.image);
        assertNotNull(article.author);
        assertNotNull(article.source);
        assertNotNull(article.domain);
        assertNotEquals(0, article.duration);
        assertNotEquals(0, article.insertedAt);
    }

    @Test
    public void getArticle_null() {
        Article article = source.getArticle("blah");
        assertNull(article);
    }

    @Test
    public void getAllArticles() {
        Cursor articles = source.getAllArticles();
        assertNotNull(articles);
        assertEquals(2, articles.getCount());
    }

    @Test
    public void getSavedArticles() {
        Cursor savedArticles = source.getSavedArticles();
        assertNotNull(savedArticles);
        assertEquals(1, savedArticles.getCount());
    }

    @Test
    public void updateSavedArticleState() {
        Article article = source.getArticle("http://test.com/2");
        article.saved = true;
        source.updateSavedArticleState(article);
        Cursor savedArticles = source.getSavedArticles();
        assertEquals(2, savedArticles.getCount());

        article = source.getArticle("http://test.com/");
        article.saved = false;
        source.updateSavedArticleState(article);
        savedArticles = source.getSavedArticles();
        assertEquals(1, savedArticles.getCount());
    }

    @Test
    public void insertArticle() {
        Article article = new Article();
        article.id = 1L;
        article.alias = "alias";
        article.url = "http://test";
        article.title = "test title";
        article.description = "test description";
        article.image = "image url";
        article.content = "<p>test paragraph</p>";
        article.author = "jake klinker";
        article.source = "google.com";
        article.domain = "google.com";
        article.duration = 1;
        article.insertedAt = 2;
        article.isArticle = true;
        article.saved = false;

        source.insertArticle(article);

        assertEquals(3, source.getAllArticles().getCount());
    }
}
