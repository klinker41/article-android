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

package xyz.klinker.android.article;

import android.os.Handler;

import xyz.klinker.android.article.api.Api;
import xyz.klinker.android.article.api.Article;

/**
 * Helper for working with the article apis.
 */
public class ArticleUtils {

    private Api api;

    public ArticleUtils() {
        this.api = new Api();
    }

    /**
     * Loads an article from the server.
     *
     * @param url the url to load the article from.
     * @param callback the callback to receive after loading completes.
     */
    public void loadArticle(final String url, final ArticleLoadedListener callback) {
        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Article article = api.article().parse(url);

                if (callback != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onArticleLoaded(article);
                        }
                    });
                }
            }
        }).start();
    }
}
