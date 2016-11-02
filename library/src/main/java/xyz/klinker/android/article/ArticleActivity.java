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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.jsoup.select.Elements;

import xyz.klinker.android.article.api.Article;

/**
 * Activity that will display an article grabbed from the server or redirect to a chrome custom
 * tab if the article cannot be displayed appropriately or the content is not an article at all.
 *
 * Requires a string extra in the Intent of type ArticleActivity.EXTRA_URL which is the URL of the
 * article you wish to load.
 */
public class ArticleActivity extends AppCompatActivity implements ArticleLoadedListener {

    public static final String EXTRA_URL = "url";

    private static final String TAG = "ArticleActivity";
    private static final boolean DEBUG = true;

    private ArticleUtils utils;
    private RecyclerView recyclerView;
    private ArticleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = getIntent().getStringExtra(EXTRA_URL);

        if (url == null) {
            throw new RuntimeException("EXTRA_URL must not be null.");
        }

        if (DEBUG) {
            Log.v(TAG, "loading article: " + url);
        }

        this.utils = new ArticleUtils();
        this.utils.loadArticle(url, this);

        setContentView(R.layout.activity_article);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }
    }

    @Override
    public void onArticleLoaded(Article article) {
        if (article == null || !article.isArticle) {
            if (DEBUG) {
                Log.v(TAG, "not an article or couldn't fetch url");
            }

            // TODO(klinker41): forward to chrome custom tab instead of finishing.
            finish();
        } else {
            if (DEBUG) {
                Log.v(TAG, "finished loading article at " + article.url);
                Log.v(TAG, "\t" + article.title);
                Log.v(TAG, "\t" + article.author);
                Log.v(TAG, "\t" + article.description);
            }

            adapter = new ArticleAdapter(article);
            recyclerView.setAdapter(adapter);

            utils.parseArticleContent(article, this);
        }
    }

    @Override
    public void onArticleParsed(Elements elements) {
        adapter.addElements(elements);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return false;
    }

}
