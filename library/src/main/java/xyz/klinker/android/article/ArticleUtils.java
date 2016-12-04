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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xyz.klinker.android.article.api.ArticleApi;
import xyz.klinker.android.article.data.Article;
import xyz.klinker.android.article.data.DataSource;

/**
 * Helper for working with the article apis.
 */
public class ArticleUtils {

    private static final String SELECTOR = "p, h1, h2, h3, h4, h5, h6, img, blockquote, pre, li";

    private ArticleApi api;

    public ArticleUtils(String apiToken) {
        this.api = new ArticleApi(apiToken);
    }

    /**
     * Loads an article from the server.
     *
     * @param url the url to load the article from.
     * @param source the data source.
     * @param callback the callback to receive after loading completes.
     */
    void loadArticle(final String url, final DataSource source,
                     final ArticleLoadedListener callback) {
        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadArticleSync(url, source, callback, handler);
            }
        }).start();
    }

    /**
     * Preloads an article from the server so that it is cached on the device and immediately
     * available when a user tries to view it without making any network calls. This includes
     * downloading the article body along with precaching any images with Glide.
     *
     * @param context the current application context.
     * @param url the url to try and preload.
     * @param callback the callback to be invoked when preloading has finished.
     */
    public void preloadArticle(final Context context, final String url,
                               final ArticleLoadedListener callback) {
        DataSource source = DataSource.getInstance(context);
        loadArticle(url, source, new ArticleLoadedListener() {
            @Override
            public void onArticleLoaded(final Article article) {
                if (callback != null) {
                    callback.onArticleLoaded(article);
                }

                if (article != null && article.isArticle && article.content != null) {
                    parseArticleContent(article, new ArticleParsedListener() {
                        @Override
                        public void onArticleParsed(final Elements elements) {
                            cacheImages(context, article, elements);
                        }
                    });
                }
            }
        });
    }

    /**
     * Fetch an article from the server so that it is cached on the device and immediately
     * available when a user tries to view it without making any network calls. This includes
     * downloading the article body along with precaching any images with Glide.
     *
     * This api will return the article result without a callback. It will run syncronously on
     * whatever thread it is called from.
     *
     * @param context the current application context.
     * @param url the url to try and preload.
     */
    public Article fetchArticle(final Context context, final String url) {
        DataSource source = DataSource.getInstance(context);
        final Article article = loadArticleSync(url, source, null, null);

        if (article != null && article.isArticle && article.content != null) {
            parseArticleContent(article, new ArticleParsedListener() {
                @Override
                public void onArticleParsed(final Elements elements) {
                    cacheImages(context, article, elements);
                }
            });
        }

        return article;
    }

    /**
     * Gets all trending articles from the server and returns them to the provided listener.
     *
     * @param listener the listener to call back to when articles are loaded.
     */
    public void loadTrending(final TrendingLoadedListener listener) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Article[] articles = api.article().trending();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onArticlesLoaded(articles);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * Gets all trending articles. Do not call from main UI thread.
     *
     * @return the trending articles.
     */
    public Article[] fetchTrending() {
        return api.article().trending();
    }

    /**
     * Loads an article from the server.
     *
     * @param url the url to load the article from.
     * @param source the data source.
     * @param callback the callback to receive after loading completes.
     * @param handler UI thread handler to use when performing the callback.
     */
    private Article loadArticleSync(final String url, final DataSource source,
                                    final ArticleLoadedListener callback, final Handler handler) {
        source.open();
        Article loadedArticle = source.getArticle(url);

        final Article article;
        if (loadedArticle != null) {
            article = loadedArticle;
        } else {
            article = api.article().parse(url);

            if (article != null) {
                // the server will resolve the url when it is shortened or something like
                // that so we want to instead save the original so that it is findable by
                // that url again later.
                article.url = url;

                source.insertArticle(article);
            }
        }

        source.close();

        if (callback != null && handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onArticleLoaded(article);
                }
            });
        }

        return article;
    }

    private void cacheImages(final Context context, final Article article,
                             final Elements elements) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int[] dimens = getCacheWidthAndHeight(context);

                if (article.image != null) {
                    try {
                        Glide.with(context)
                                .load(article.image)
                                .downloadOnly(dimens[0], dimens[1])
                                .get();
                        Log.v("ArticleUtils", "cached header image");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (elements != null) {
                    for (Element element : elements) {
                        if (element.tagName().equals("img")) {
                            String src = element.attr("src");

                            try {
                                Glide.with(context)
                                        .load(src)
                                        .downloadOnly(dimens[0], dimens[1])
                                        .get();
                                Log.v("ArticleUtils", "cached image");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private int[] getCacheWidthAndHeight(Context context) {
        Resources resources = context.getResources();
        int imageWidth = resources.getDimensionPixelSize(R.dimen.article_articleWidth);
        if (imageWidth <= 0) {
            WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = window.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            imageWidth = size.x;
        }

        int imageHeight = resources.getDimensionPixelSize(R.dimen.article_imageParallax) +
                resources.getDimensionPixelSize(R.dimen.article_imageHeight);

        return new int[] {imageWidth, imageHeight};
    }

    /**
     * Parses the article content into a elements object using jsoup and the @link{SELECTOR}.
     *
     * @param article the article to parse content from.
     * @param callback the callback to receive after parsing completes.
     */
    void parseArticleContent(final Article article, final ArticleParsedListener callback) {
        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = Jsoup.parse(article.content);
                final Elements elements = removeUnnecessaryElements(doc.select(SELECTOR), article);

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

    @Nullable
    private Elements removeUnnecessaryElements(Elements elements, Article article) {
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
                } else if (i > 0 && text.equals(elements.get(i-1).text().trim())) {
                    elements.remove(i--);
                }
            }
        }

        if (elements.size() > 0) {
            String lastTag = elements.last().tagName();
            while (!lastTag.equals("p") && !lastTag.equals("img")) {
                elements.remove(elements.size() - 1);
                lastTag = elements.last().tagName();
            }

            // if not many paragraphs and text is small, then don't show anything
            if (elements.size() < 7 && elements.text().trim().length() < 100) {
                elements = null;
            }
        }

        return elements;
    }

    private static boolean isImageUrl(String src) {
        return src.contains("jpg") || src.contains("png") || src.contains("gif");
    }

    /**
     * Strips a string of all of it's parameters contained in the URL.
     *
     * @param url the url string.
     * @return the url without any parameters.
     */
    public static String removeUrlParameters(String url) {
        return url.split("\\?")[0];
    }

    static String decodeImageUrl(String url) {
        if (url == null) {
            return "";
        } else {
            String parsedUrl = Uri.decode(url.split(",")[0]).split(" ")[0];
            return isImageUrl(parsedUrl) ? parsedUrl : url;
        }
    }

}
