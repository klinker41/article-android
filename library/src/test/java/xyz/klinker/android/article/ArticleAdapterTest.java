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

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.Robolectric;

import xyz.klinker.android.article.data.Article;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ArticleAdapterTest extends ArticleRobolectricSuite {

    private Article article;
    private ArticleAdapter adapter;

    @Mock
    private Elements elements;

    @Before
    public void setUp() {
        article = new Article();
        adapter = new ArticleAdapter(article, 0, 0, 1);
    }

    @Test
    public void getItemResourceFromType_headerImage() {
        assertEquals(
                R.layout.article_item_header,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_HEADER_IMAGE));
    }

    @Test
    public void getItemResourceFromType_title() {
        assertEquals(
                R.layout.article_item_title,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_TITLE));
    }

    @Test
    public void getItemResourceFromType_paragraph() {
        assertEquals(
                R.layout.article_item_paragraph,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_PARAGRAPH));
    }

    @Test
    public void getItemResourceFromType_inlineImage() {
        assertEquals(
                R.layout.article_item_image,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_INLINE_IMAGE));
    }

    @Test
    public void getItemResourceFromType_header1() {
        assertEquals(
                R.layout.article_item_header_1,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_HEADER_1));
    }

    @Test
    public void getItemResourceFromType_header2() {
        assertEquals(
                R.layout.article_item_header_2,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_HEADER_2));
    }

    @Test
    public void getItemResourceFromType_header3() {
        assertEquals(
                R.layout.article_item_header_3,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_HEADER_3));
    }

    @Test
    public void getItemResourceFromType_header4() {
        assertEquals(
                R.layout.article_item_header_4,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_HEADER_4));
    }

    @Test
    public void getItemResourceFromType_header5() {
        assertEquals(
                R.layout.article_item_header_5,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_HEADER_5));
    }

    @Test
    public void getItemResourceFromType_header6() {
        assertEquals(
                R.layout.article_item_header_6,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_HEADER_6));
    }

    @Test
    public void getItemResourceFromType_blockquote() {
        assertEquals(
                R.layout.article_item_blockquote,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_BLOCKQUOTE));
    }

    @Test
    public void getItemResourceFromType_pre() {
        assertEquals(
                R.layout.article_item_pre,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_PRE));
    }

    @Test
    public void getItemResourceFromType_unorderedList() {
        assertEquals(
                R.layout.article_item_unordered_list_item,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_UNORDERED_LIST_ITEM));
    }

    @Test
    public void getItemResourceFromType_orderedList() {
        assertEquals(
                R.layout.article_item_ordered_list_item,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_ORDERED_LIST_ITEM));
    }

    @Test
    public void getItemResourceFromType_other() {
        assertEquals(
                R.layout.article_item_other,
                adapter.getItemResourceFromType(ArticleAdapter.TYPE_OTHER));
    }

    @Test
    public void getItemTypeForTag_p() {
        assertEquals(ArticleAdapter.TYPE_PARAGRAPH, adapter.getItemTypeForTag("p"));
    }

    @Test
    public void getItemTypeForTag_h1() {
        assertEquals(ArticleAdapter.TYPE_HEADER_1, adapter.getItemTypeForTag("h1"));
    }

    @Test
    public void getItemTypeForTag_h2() {
        assertEquals(ArticleAdapter.TYPE_HEADER_2, adapter.getItemTypeForTag("h2"));
    }

    @Test
    public void getItemTypeForTag_h3() {
        assertEquals(ArticleAdapter.TYPE_HEADER_3, adapter.getItemTypeForTag("h3"));
    }

    @Test
    public void getItemTypeForTag_h4() {
        assertEquals(ArticleAdapter.TYPE_HEADER_4, adapter.getItemTypeForTag("h4"));
    }

    @Test
    public void getItemTypeForTag_h5() {
        assertEquals(ArticleAdapter.TYPE_HEADER_5, adapter.getItemTypeForTag("h5"));
    }

    @Test
    public void getItemTypeForTag_h6() {
        assertEquals(ArticleAdapter.TYPE_HEADER_6, adapter.getItemTypeForTag("h6"));
    }

    @Test
    public void getItemTypeForTag_img() {
        assertEquals(ArticleAdapter.TYPE_INLINE_IMAGE, adapter.getItemTypeForTag("img"));
    }

    @Test
    public void getItemTypeForTag_blockquote() {
        assertEquals(ArticleAdapter.TYPE_BLOCKQUOTE, adapter.getItemTypeForTag("blockquote"));
    }

    @Test
    public void getItemTypeForTag_pre() {
        assertEquals(ArticleAdapter.TYPE_PRE, adapter.getItemTypeForTag("pre"));
    }

    @Test
    public void getItemTypeForTag_ulli() {
        assertEquals(ArticleAdapter.TYPE_UNORDERED_LIST_ITEM, adapter.getItemTypeForTag("ul.li"));
    }

    @Test
    public void getItemTypeForTag_olli() {
        assertEquals(ArticleAdapter.TYPE_ORDERED_LIST_ITEM, adapter.getItemTypeForTag("ol.li"));
    }

    @Test
    public void getItemCount_noArticleNoElements() {
        assertEquals(0, new ArticleAdapter(null, 0, 0, 1).getItemCount());
    }

    @Test
    public void getItemCount_headerNoTitleNoElements() {
        assertEquals(1, adapter.getItemCount());
    }

    @Test
    public void getItemCount_headerNoElements() {
        article.title = "test title";
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void getItemCount_headerAndElements() {
        when(elements.size()).thenReturn(12);
        adapter.addElements(elements);
        article.title = "test title";
        assertEquals(14, adapter.getItemCount());
    }

    @Test
    public void onCreateViewHolder_headerImage() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_HEADER_IMAGE)
                        instanceof ArticleAdapter.HeaderImageViewHolder);
    }

    @Test
    public void onCreateViewHolder_title() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_TITLE)
                        instanceof ArticleAdapter.TitleTextViewHolder);
    }

    @Test
    public void onCreateViewHolder_inlineImage() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_INLINE_IMAGE)
                        instanceof ArticleAdapter.ImageViewHolder);
    }

    @Test
    public void onCreateViewHolder_blockquote() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_BLOCKQUOTE)
                        instanceof ArticleAdapter.BlockQuoteViewHolder);
    }

    @Test
    public void onCreateViewHolder_header1() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_HEADER_1)
                        instanceof ArticleAdapter.SubtitleTextViewHolder);
    }

    @Test
    public void onCreateViewHolder_header2() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_HEADER_2)
                        instanceof ArticleAdapter.SubtitleTextViewHolder);
    }

    @Test
    public void onCreateViewHolder_header3() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_HEADER_3)
                        instanceof ArticleAdapter.SubtitleTextViewHolder);
    }

    @Test
    public void onCreateViewHolder_header4() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_HEADER_4)
                        instanceof ArticleAdapter.SubtitleTextViewHolder);
    }

    @Test
    public void onCreateViewHolder_header5() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_HEADER_5)
                        instanceof ArticleAdapter.SubtitleTextViewHolder);
    }

    @Test
    public void onCreateViewHolder_header6() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_HEADER_6)
                        instanceof ArticleAdapter.SubtitleTextViewHolder);
    }

    @Test
    public void onCreateViewHolder_unorderedList() {
        assertTrue(
                adapter.onCreateViewHolder(
                        generateViewGroup(), ArticleAdapter.TYPE_UNORDERED_LIST_ITEM)
                                instanceof ArticleAdapter.TextViewHolder);
    }

    @Test
    public void onCreateViewHolder_orderedList() {
        assertTrue(
                adapter.onCreateViewHolder(
                        generateViewGroup(), ArticleAdapter.TYPE_ORDERED_LIST_ITEM)
                                instanceof ArticleAdapter.TextViewHolder);
    }

    @Test
    public void onCreateViewHolder_paragraph() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_PARAGRAPH)
                        instanceof ArticleAdapter.TextViewHolder);
    }

    @Test
    public void onCreateViewHolder_pre() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_PRE)
                        instanceof ArticleAdapter.TextViewHolder);
    }

    @Test
    public void onCreateViewHolder_other() {
        assertTrue(
                adapter.onCreateViewHolder(generateViewGroup(), ArticleAdapter.TYPE_OTHER)
                        instanceof ArticleAdapter.TextViewHolder);
    }

    private ViewGroup generateViewGroup() {
        Activity activity = Robolectric.setupActivity(Activity.class);
        LinearLayout linearLayout = new LinearLayout(activity);
        activity.setContentView(linearLayout);
        return linearLayout;
    }
}
