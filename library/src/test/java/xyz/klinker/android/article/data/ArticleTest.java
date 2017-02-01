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

import android.content.Intent;
import android.database.MatrixCursor;

import org.junit.Test;

import xyz.klinker.android.article.ArticleRobolectricSuite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArticleTest extends ArticleRobolectricSuite {

    @Test
    public void fillFromCursor() {
        MatrixCursor cursor = new MatrixCursor(new String[]{
                "_id",
                "alias",
                "url",
                "title",
                "description",
                "image",
                "content",
                "author",
                "source",
                "domain",
                "duration",
                "inserted_at",
                "is_article",
                "saved",
                "source_id"
        });

        cursor.addRow(new Object[]{
                1L,
                "alias",
                "http://test",
                "test title",
                "test description",
                "image url",
                "<p>test paragraph</p>",
                "jake klinker",
                "google.com",
                "google.com",
                1,
                2,
                1,
                0,
                2L
        });

        cursor.moveToFirst();
        Article article = new Article(cursor);

        assertEquals(1L, article.id);
        assertEquals("alias", article.alias);
        assertEquals("http://test", article.url);
        assertEquals("test title", article.title);
        assertEquals("test description", article.description);
        assertEquals("image url", article.image);
        assertEquals("<p>test paragraph</p>", article.content);
        assertEquals("jake klinker", article.author);
        assertEquals("google.com", article.source);
        assertEquals("google.com", article.domain);
        assertEquals(1, article.duration);
        assertEquals(2, article.insertedAt);
        assertTrue(article.isArticle);
        assertFalse(article.saved);
        assertEquals(2L, (long) article.sourceId);
    }

    @Test
    public void fillFromIntent() {
        Intent intent = new Intent();
        intent.putExtra("_id", 1L);
        intent.putExtra("alias", "alias");
        intent.putExtra("url", "http://test");
        intent.putExtra("title", "test title");
        intent.putExtra("description", "test description");
        intent.putExtra("image", "image url");
        intent.putExtra("content", "<p>test paragraph</p>");
        intent.putExtra("author", "jake klinker");
        intent.putExtra("source", "google.com");
        intent.putExtra("domain", "google.com");
        intent.putExtra("duration", 1);
        intent.putExtra("inserted_at", 2L);
        intent.putExtra("is_article", true);
        intent.putExtra("saved", false);

        Article article = new Article(intent);

        assertEquals(1L, article.id);
        assertEquals("alias", article.alias);
        assertEquals("http://test", article.url);
        assertEquals("test title", article.title);
        assertEquals("test description", article.description);
        assertEquals("image url", article.image);
        assertEquals("<p>test paragraph</p>", article.content);
        assertEquals("jake klinker", article.author);
        assertEquals("google.com", article.source);
        assertEquals("google.com", article.domain);
        assertEquals(1, article.duration);
        assertEquals(2, article.insertedAt);
        assertTrue(article.isArticle);
        assertFalse(article.saved);
    }

    @Test
    public void putIntoIntent() {
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

        Intent intent = new Intent();
        article.putIntoIntent(intent);

        assertEquals(1L, intent.getLongExtra("_id", 0L));
        assertEquals("alias", intent.getStringExtra("alias"));
        assertEquals("http://test", intent.getStringExtra("url"));
        assertEquals("test title", intent.getStringExtra("title"));
        assertEquals("test description", intent.getStringExtra("description"));
        assertEquals("image url", intent.getStringExtra("image"));
        assertEquals("<p>test paragraph</p>", intent.getStringExtra("content"));
        assertEquals("jake klinker", intent.getStringExtra("author"));
        assertEquals("google.com", intent.getStringExtra("source"));
        assertEquals("google.com", intent.getStringExtra("domain"));
        assertEquals(1, intent.getIntExtra("duration", 0));
        assertEquals(2, intent.getLongExtra("inserted_at", 0));
        assertTrue(intent.getBooleanExtra("is_article", false));
        assertFalse(intent.getBooleanExtra("saved", true));
    }
}
