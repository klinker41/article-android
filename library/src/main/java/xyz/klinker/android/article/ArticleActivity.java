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

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jsoup.select.Elements;

import xyz.klinker.android.article.data.Article;
import xyz.klinker.android.article.data.DataSource;
import xyz.klinker.android.drag_dismiss.activity.DragDismissRecyclerViewActivity;

/**
 * Activity that will display an article grabbed from the server or redirect to a chrome custom
 * tab if the article cannot be displayed appropriately or the content is not an article at all.
 *
 * You should create this activity using the {@link ArticleIntent.Builder}, not invoke it directly.
 *
 * NOTE: Not all options in the builder by be applied in this activity. However, all options will
 * be forwarded to a chrome custom tab if the user chooses to view it there.
 */
public final class ArticleActivity extends DragDismissRecyclerViewActivity
        implements ArticleLoadedListener, ArticleParsedListener {

    public static final String PERMISSION_SAVED_ARTICLE =
            "xyz.klinker.android.article.SAVED_ARTICLE";
    public static final String ACTION_SAVED_ARTICLE =
            "xyz.klinker.android.article.ARTICLE_SAVED";

    private static final String TAG = "ArticleActivity";
    private static final boolean DEBUG = false;

    private static final int MIN_NUM_ELEMENTS = 1;

    private Article article;
    private String url;
    private ArticleUtils utils;
    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private int accentColor;
    private int textSize;

    @Override
    public void setupRecyclerView(RecyclerView recyclerView) {
        this.url = getIntent().getDataString();

        if (DEBUG) {
            Log.v(TAG, "loading article: " + url);
        }

        DataSource source = DataSource.get(this);
        this.utils = new ArticleUtils(getIntent().getStringExtra(ArticleIntent.EXTRA_API_TOKEN));
        this.utils.loadArticle(url, source, this);

        this.accentColor = getIntent().getIntExtra(ArticleIntent.EXTRA_ACCENT_COLOR,
                getResources().getColor(R.color.article_colorAccent));
        this.textSize = getIntent().getIntExtra(ArticleIntent.EXTRA_TEXT_SIZE, 15);

        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(
                new ArticleScrollListener(delegate.getToolbar(), delegate.getStatusBar(), delegate.getPrimaryColor()));

        showProgressBar();
    }

    @Override
    public void onArticleLoaded(Article article) {
        hideProgressBar();

        if (article == null || !article.isArticle || article.content == null) {
            if (DEBUG) {
                Log.v(TAG, "not an article or couldn't fetch url");
            }

            openChromeCustomTab();
        } else {
            this.article = article;

            if (DEBUG) {
                Log.v(TAG, "finished loading article at " + article.url);
                Log.v(TAG, "\t" + article.title);
                Log.v(TAG, "\t" + article.author);
                Log.v(TAG, "\t" + article.description);
            }

            adapter = new ArticleAdapter(article, accentColor, textSize,
                    getIntent().getIntExtra(ArticleIntent.EXTRA_THEME, ArticleIntent.THEME_AUTO));
            recyclerView.setAdapter(adapter);

            utils.parseArticleContent(article, this);
            delegate.getProgressBar().setVisibility(View.GONE);

            invalidateOptionsMenu();
        }
    }

    @Override
    public void onArticleParsed(Elements elements) {
        if (elements != null && elements.size() >= MIN_NUM_ELEMENTS) {
            adapter.addElements(elements);
        } else {
            openChromeCustomTab();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_activity_article, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        updateSaveMenuItem(menu, menu.findItem(R.id.article_save));
        return super.onPrepareOptionsMenu(menu);
    }

    private void updateSaveMenuItem(Menu menu, MenuItem save) {
        if (article != null && save != null) {
            if (getIntent().hasExtra(ArticleIntent.EXTRA_FAVORITE_SERVICE)) {
                if (article.saved) {
                    save.setIcon(R.drawable.article_ic_star);
                } else {
                    save.setIcon(R.drawable.article_ic_star_border);
                }
            } else {
                menu.removeItem(0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.article_share) {
            SocialUtils.shareArticle(this, article);
        } else if (item.getItemId() == R.id.article_open_in_chrome) {
            openChromeCustomTab();
        } else if (item.getItemId() == R.id.article_save) {
            saveArticle();
        }

        return true;
    }

    private void openChromeCustomTab() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent intent = builder.build();

        // get the extras that we passed into this activity (these will be all that were created
        // in the intent builder)
        intent.intent.putExtras(getIntent().getExtras());

        // launch the url with the specified intent
        try {
            intent.launchUrl(this, Uri.parse(url));
        } catch (SecurityException e) {
            // this throws an exception on Android Wear (yes you can use this library on android wear)
            // since webkit is not supported, and there is no Google Chrome on Wear.
            Toast.makeText(this, R.string.article_not_supported, Toast.LENGTH_SHORT).show();
        }

        // finish the current activity so that the back button takes us back
        finish();
    }

    private void saveArticle() {
        article.saved = !article.saved;
        invalidateOptionsMenu();

        new Thread(new Runnable() {
            @Override
            public void run() {
                DataSource source = DataSource.get(ArticleActivity.this);
                source.open();
                source.updateSavedArticleState(article);
                source.close();
            }
        }).start();

        Intent intent = new Intent(ACTION_SAVED_ARTICLE);
        intent.setClassName(this, getIntent().getStringExtra(ArticleIntent.EXTRA_FAVORITE_SERVICE));
        article.putIntoIntent(intent);
        startService(intent);
    }

    private View.OnClickListener sideClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

}
