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

import java.util.List;

/**
 * Model containing a list of categories plus the total sum of each of their counts.
 */
public class CategoryCounts {

    private List<Category> categories;
    private int totalCount;

    public CategoryCounts(List<Category> categories, int totalCount) {
        this.categories = categories;
        this.totalCount = totalCount;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
