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

package xyz.klinker.android.article.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.klinker.android.article.data.Article;

/**
 * Retrofit definition for the article service. you can call this will an added parameter for
 * whether or not you want to use the cache to provide the article. By default, the cache will
 * be used as it provides for significantly improved load times if an article has already been
 * loaded.
 *
 * Your API token will automatically be appended to the request as well, assuming one was provided
 * to the {@link ArticleApi}.
 */
public interface ArticleService {

    /**
     * Gets an article from the provided url and caches it.
     *
     * @param url the url to get an article from.
     * @return the parsed article.
     */
    @GET("parse")
    Article parse(@Query("url") String url);

    /**
     * Gets an article from the provided url and provides the option to ignore the cache if
     * desired.
     *
     * @param url the url to get an article from.
     * @param noCache if true, do not inspect the cache for a previously gotten article with the
     *                same url.
     * @return the parsed article.
     */
    @GET("parse")
    Article parse(@Query("url") String url,
                  @Query("no_cache") boolean noCache);

    /**
     * Gets the trending articles that are currently popular on our server.
     *
     * @return the trending articles.
     */
    @GET("trending")
    Article[] trending();

    /**
     * Prepares an article on the server so that it can be served immediately to any requesting
     * client without having to parse again.
     *
     * @param url the url to get an article from.
     */
    void prepare(@Query("url") String url);

}
