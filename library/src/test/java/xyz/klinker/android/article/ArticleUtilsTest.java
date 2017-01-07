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

package xyz.klinker.android.article;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ArticleUtilsTest extends ArticleRobolectricSuite {

    @Test
    public void jpgIsImageUrl() {
        assertTrue(ArticleUtils.isImageUrl("http://google.com/image.jpg"));
    }

    @Test
    public void pngIsImageUrl() {
        assertTrue(ArticleUtils.isImageUrl("http://google.com/image.png"));
    }

    @Test
    public void gifIsImageUrl() {
        assertTrue(ArticleUtils.isImageUrl("http://google.com/image.gif"));
    }

    @Test
    public void isNotImageUrl() {
        assertFalse(ArticleUtils.isImageUrl("http://google.com/image"));
    }

    @Test
    public void removeUrlParameters_one() {
        assertEquals(
                "http://google.com", ArticleUtils.removeUrlParameters("http://google.com?test=1"));
    }

    @Test
    public void removeUrlParameters_two() {
        assertEquals(
                "http://google.com",
                ArticleUtils.removeUrlParameters("http://google.com?test=2&again=2"));
    }

    @Test
    public void removeUrlParameters_none() {
        assertEquals("http://google.com", ArticleUtils.removeUrlParameters("http://google.com"));
    }

    @Test
    public void removeUrlParameters_null() {
        assertNull(ArticleUtils.removeUrlParameters(null));
    }
}
