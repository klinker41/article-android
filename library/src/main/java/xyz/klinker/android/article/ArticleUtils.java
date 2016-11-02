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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xyz.klinker.android.article.api.Api;
import xyz.klinker.android.article.api.Article;

/**
 * Helper for working with the article apis.
 */
public class ArticleUtils {

    private static final String SELECTOR = "p, h1, h2, h3, h4, h5, h6, img, blockquote";

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

    /**
     * Parses the article content into a elements object using jsoup and the @link{SELECTOR}.
     *
     * @param article the article to parse content from.
     * @param callback the callback to receive after parsing completes.
     */
    public void parseArticleContent(final Article article, final ArticleLoadedListener callback) {
        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = Jsoup.parse(article.content);
                final Elements elements = doc.select(SELECTOR);

                removeUnnecessaryElements(elements, article);

                if (callback != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onArticleParsed(elements);
                        }
                    });
                }
            }
        }).start();
    }

    private void removeUnnecessaryElements(Elements elements, Article article) {
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);

            if (i == 0 && (!element.tagName().equals("p") || element.text().contains(article.title))) {
                elements.remove(i--);
                continue;
            }

            if (element.tagName().equals("img")) {
                String src = element.attr("src");
                if (src == null || src.length() == 0 || !isImageUrl(src) ||
                        src.equals(article.image)) {
                    elements.remove(i--);
                }
            } else {
                String text = element.text().trim();
                if (text.length() == 0 || text.equals("Advertisement") || text.equals("Sponsored") ) {
                    elements.remove(i--);
                }
            }
        }
    }

    private boolean isImageUrl(String src) {
        return src.contains("jpg") || src.contains("png") || src.contains("gif");
    }

}
