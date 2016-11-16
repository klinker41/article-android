package xyz.klinker.android.article;

import org.jsoup.select.Elements;

interface ArticleParsedListener {
    void onArticleParsed(Elements elements);
}
