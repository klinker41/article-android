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

package xyz.klinker.article;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;

import xyz.klinker.android.article.ArticleIntent;

public class SampleActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button link1 = (Button) findViewById(R.id.link1);
        final Button link2 = (Button) findViewById(R.id.link2);
        final Button link3 = (Button) findViewById(R.id.link3);
        final Button link4 = (Button) findViewById(R.id.link4);
        final Button link5 = (Button) findViewById(R.id.link5);
        final Button link6 = (Button) findViewById(R.id.link6);
        final Button link7 = (Button) findViewById(R.id.link7);
        final Button link8 = (Button) findViewById(R.id.link8);
        final Button link9 = (Button) findViewById(R.id.link9);
        final Button link10 = (Button) findViewById(R.id.link10);
        final EditText url = (EditText) findViewById(R.id.edit_text);
        final Button go = (Button) findViewById(R.id.go);

        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticle(link1.getText().toString());
            }
        });

        link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticle(link2.getText().toString());
            }
        });

        link3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticle(link3.getText().toString());
            }
        });

        link4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticle(link4.getText().toString());
            }
        });

        link5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticle(link5.getText().toString());
            }
        });

        link6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticle(link6.getText().toString());
            }
        });

        link7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticle(link7.getText().toString());
            }
        });

        link8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticle(link8.getText().toString());
            }
        });

        link9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticle(link9.getText().toString());
            }
        });

        link10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticle(link10.getText().toString());
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url.getText().length() > 0) {
                    openArticle(url.getText().toString());
                }
            }
        });

        handleIntent();
    }

    private void openArticle(String url) {
        ArticleIntent intent = new ArticleIntent.Builder(this)
                .setToolbarColor(getResources().getColor(R.color.articlePrimary))
                .setAccentColor(getResources().getColor(R.color.articleAccent))
                .setTheme(ArticleIntent.THEME_AUTO)
                .build();

        intent.launchUrl(this, Uri.parse(url));
    }

    private void handleIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            Matcher matcher = Patterns.WEB_URL.matcher(text);
            if (matcher.find()) {
                String url = matcher.group();
                openArticle(url);
                finish();
            }
        }
    }

}
