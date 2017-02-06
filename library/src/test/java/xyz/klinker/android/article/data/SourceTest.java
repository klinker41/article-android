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

import android.database.MatrixCursor;

import org.junit.Test;

import xyz.klinker.android.article.ArticleRobolectricSuite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SourceTest extends ArticleRobolectricSuite {

    @Test
    public void fillFromCursor() {
        MatrixCursor cursor = new MatrixCursor(new String[] {
                "s_id",
                "sremote_id",
                "sname",
                "simage_url",
                "cname",
                "c_id"
        });

        cursor.addRow(new Object[] {
                1L,
                2L,
                "test name",
                "test url",
                "test category",
                3L
        });

        cursor.moveToFirst();
        Source source = new Source(cursor);

        assertEquals(1L, source.id);
        assertEquals(2L, source.remoteId);
        assertEquals(3L, (long) source.categoryId);
        assertEquals("test name", source.name);
        assertEquals("test url", source.imageUrl);
        assertEquals("test category", source.categoryName);
    }

    @Test
    public void equals() {
        Source source = newSource(1L, "test", "test url", "test category", 1L, 1L);
        Source other = newSource(1L, "test", "test url", "test category", 1L, 1L);
        assertEquals(source, other);
    }

    @Test
    public void notEquals_differentType() {
        Source source = newSource(1L, "test", "test url", "test category", 1L, 1L);
        assertNotEquals("test", source);
    }

    @Test
    public void notEquals() {
        Source source = newSource(1L, "test", "test url", "test category", 1L, 1L);
        Source other = newSource(2L, "test 2", "test url", "test category", 1L, 1L);
        assertNotEquals(source, other);
    }

    private Source newSource(
            long id,
            String name,
            String imageUrl,
            String categoryName,
            Long categoryId,
            long remoteId) {
        Source source = new Source();
        source.id = id;
        source.name = name;
        source.imageUrl = imageUrl;
        source.categoryName = categoryName;
        source.categoryId = categoryId;
        source.remoteId = remoteId;
        return source;
    }
}
