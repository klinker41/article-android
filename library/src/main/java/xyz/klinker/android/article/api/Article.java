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

/**
 * Model holding all possible elements in a response from the server.
 */
public class Article {

    public String alias;
    public String url;
    public String[] canonicals;
    public String title;
    public String description;
    public String image;
    public String content;
    public String author;
    public String source;
    public String domain;
    public int duration;
    public boolean isArticle;

}
