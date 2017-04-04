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

package xyz.klinker.android.article.data.model;

import org.junit.Before;
import org.junit.Test;

import xyz.klinker.android.article.ArticleSuite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ContentModelTest extends ArticleSuite {

    private ContentModel model;

    @Before
    public void setUp() {
        model = new ContentModel();
    }

    @Test
    public void createStatementNotNull() {
        assertNotNull(model.getCreateStatement());
    }

    @Test
    public void indexesNotNull() {
        assertNotNull(model.getIndexStatements());
    }

    @Test
    public void tableName() {
        assertEquals("content", model.getTableName());
    }
}
