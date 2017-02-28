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

import java.util.List;

import xyz.klinker.android.article.ArticleRealDataSuite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertEquals(8, articles.getCount());

        articles.moveToFirst();
        Article article = new Article(articles);
        assertNotNull(article.alias);
        assertNotNull(article.url);
        assertNotNull(article.title);
        assertNotNull(article.description);
        assertNotNull(article.image);
        assertNull(article.content);
        assertNotNull(article.author);
        assertNotNull(article.source);
        assertNotNull(article.domain);
        assertNotNull(article.sourceId);
        assertNotNull(article.sourceModelName);
        assertNotNull(article.sourceModelImageUrl);
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

        assertEquals(9, source.getAllArticles().getCount());
    }

    @Test
    public void insertDuplicateArticle() {
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
        assertEquals(9, source.getAllArticles().getCount());

        source.insertArticle(article);
        assertEquals(9, source.getAllArticles().getCount());
    }

    @Test
    public void insertArticle_withSourceId() {
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
        article.sourceId = 101L;

        source.insertArticle(article);

        assertEquals(3, source.getArticlesForSource(101L).getCount());
    }

    @Test
    public void getArticleWithSourceId() {
        assertEquals(3, source.getArticlesForSource(100L).getCount());
        assertEquals(2, source.getArticlesForSource(101L).getCount());
        assertEquals(1, source.getArticlesForSource(102L).getCount());
    }

    @Test
    public void updateArticleContent() {
        Article article = source.getArticle("http://test.com/");
        article.content = "blah";
        source.updateArticleContent(article);

        article = source.getArticle("http://test.com/");
        assertEquals("blah", article.content);
    }

    @Test
    public void insertCategory() {
        assertFalse(source.categoryExists("test category"));
        source.insertCategory("test category");
        assertTrue(source.categoryExists("test category"));
    }

    @Test
    public void categoryDoesNotExist() {
        assertFalse(source.categoryExists("blah"));
    }

    @Test
    public void categoryExists() {
        assertTrue(source.categoryExists("test"));
    }

    @Test
    public void getCategoryId() {
        assertNull(source.getCategoryId("blah"));
    }

    @Test
    public void getCategoryId_null() {
        assertNotNull(source.getCategoryId("test"));
    }

    @Test
    public void getSource() {
        Source s = source.getSources().get(0);
        assertNotNull(s);
        assertEquals(100L, s.remoteId);
        assertEquals("test 1", s.name);
        assertEquals("http://test1", s.imageUrl);
        assertEquals(1L, (long) s.categoryId);
        assertEquals("test", s.categoryName);
    }

    @Test
    public void getSources() {
        List<Source> sources = source.getSources();
        assertEquals(3, sources.size());
        assertEquals("test 1", sources.get(0).name);
        assertEquals("test 2", sources.get(1).name);
        assertEquals("test 3", sources.get(2).name);
    }

    @Test
    public void getCategoriesCount() {
        CategoryCounts counts = source.getCategoryCounts(0);
        assertEquals(6, counts.getTotalCount());
        assertEquals(2, counts.getCategories().size());
        assertEquals(5, counts.getCategories().get(0).numberArticles);
        assertEquals("test", counts.getCategories().get(0).name);
        assertEquals(1, counts.getCategories().get(1).numberArticles);
        assertEquals("test 2", counts.getCategories().get(1).name);
    }

    @Test
    public void insertSource_existingCategory() {
        assertEquals(2, source.getCategoryCounts(0).getCategories().size());
        assertEquals(3, source.getSources().size());

        Source s = new Source();
        s.remoteId = 4L;
        s.name = "test source";
        s.categoryName = "test";
        s.imageUrl = "http://test";
        source.insertSource(s);

        assertEquals(2, source.getCategoryCounts(0).getCategories().size());
        assertEquals(4, source.getSources().size());
    }

    @Test
    public void insertSource_nonExistantCategory() {
        assertEquals(2, source.getCategoryCounts(0).getCategories().size());
        assertEquals(3, source.getSources().size());

        Source s = new Source();
        s.remoteId = 4L;
        s.name = "test source";
        s.categoryName = "new test source";
        s.imageUrl = "http://test";
        source.insertSource(s);

        // need to add an article for this source so that the category counts picks it up
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
        article.sourceId = 4L;
        source.insertArticle(article);

        assertEquals(3, source.getCategoryCounts(0).getCategories().size());
        assertEquals(4, source.getSources().size());
    }

    @Test
    public void deleteSource() {
        int count = source.getSources().size();
        source.deleteSource("test 1");
        assertEquals(count - 1, source.getSources().size());
    }

    @Test
    public void deleteSource_nonExistant() {
        int count = source.getSources().size();
        source.deleteSource("non existant");
        assertEquals(count, source.getSources().size());
    }

    @Test
    public void deleteArticle() {
        int count = source.getAllArticles().getCount();
        source.deleteArticle(1L);
        assertEquals(count - 1, source.getAllArticles().getCount());
    }

    @Test
    public void deleteArticle_nonExistant() {
        int count = source.getAllArticles().getCount();
        source.deleteArticle(-1L);
        assertEquals(count, source.getAllArticles().getCount());
    }
}
