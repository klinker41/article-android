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

package xyz.klinker.android.article;

import android.content.Context;
import android.content.Intent;

import xyz.klinker.android.article.data.Article;

/**
 * Helper for social functions such as sharing articles.
 */
public class SocialUtils {

    /**
     * Starts an intent chooser to share the article's url.
     *
     * @param context the activity context.
     * @param article the article to share.
     */
    public static void shareArticle(Context context, Article article) {
        if (context == null || article == null) {
            return;
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, article.url);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, article.title);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent,
                context.getResources().getText(R.string.article_share_with)));
    }
}
