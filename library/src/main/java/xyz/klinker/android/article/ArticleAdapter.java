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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.select.Elements;

import xyz.klinker.android.article.api.Article;

/**
 * Recycler adapter responsible for displaying the article in a recycler view. This will
 * implement a variety of different view types including:
 *
 * 1. Header Image
 * 2. Title
 * 3. Author
 * 4. Paragraph
 * 5. Inline Image
 * 6. Paragraph Headers
 * 7. Block quotes
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER_IMAGE = 1;
    private static final int TYPE_TITLE = 2;
    private static final int TYPE_AUTHOR = 3;
    private static final int TYPE_PARAGRAPH = 4;
    private static final int TYPE_INLINE_IMAGE = 5;
    private static final int TYPE_HEADER_1 = 6;
    private static final int TYPE_HEADER_2 = 7;
    private static final int TYPE_HEADER_3 = 8;
    private static final int TYPE_HEADER_4 = 9;
    private static final int TYPE_HEADER_5 = 10;
    private static final int TYPE_HEADER_6 = 11;
    private static final int TYPE_BLOCKQUOTE = 12;
    private static final int TYPE_OTHER = 13;

    private Article article;
    private Elements elements;

    public ArticleAdapter(Article article) {
        this.article = article;
    }

    public void addElements(Elements elements) {
        this.elements = elements;
        notifyItemRangeInserted(getTopItemCount(), elements.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(getItemResourceFromType(viewType),
                        parent, false);

        switch (viewType) {
            case TYPE_HEADER_IMAGE:     return new HeaderImageViewHolder(view);
            case TYPE_TITLE:            return new TitleTextViewHolder(view);
            case TYPE_AUTHOR:           return new AuthorTextViewHolder(view);
            case TYPE_INLINE_IMAGE:     return new ImageViewHolder(view);
            case TYPE_PARAGRAPH:
            case TYPE_HEADER_1:
            case TYPE_HEADER_2:
            case TYPE_HEADER_3:
            case TYPE_HEADER_4:
            case TYPE_HEADER_5:
            case TYPE_HEADER_6:
            case TYPE_BLOCKQUOTE:
            default:                    return new TextViewHolder(view);
        }
    }

    private int getItemResourceFromType(int viewType) {
        switch (viewType) {
            case TYPE_HEADER_IMAGE:     return R.layout.item_header;
            case TYPE_TITLE:            return R.layout.item_title;
            case TYPE_AUTHOR:           return R.layout.item_author;
            case TYPE_PARAGRAPH:        return R.layout.item_paragraph;
            case TYPE_INLINE_IMAGE:     return R.layout.item_image;
            case TYPE_HEADER_1:         return R.layout.item_header_1;
            case TYPE_HEADER_2:         return R.layout.item_header_2;
            case TYPE_HEADER_3:         return R.layout.item_header_3;
            case TYPE_HEADER_4:         return R.layout.item_header_4;
            case TYPE_HEADER_5:         return R.layout.item_header_5;
            case TYPE_HEADER_6:         return R.layout.item_header_6;
            case TYPE_BLOCKQUOTE:       return R.layout.item_blockquote;
            default:                    return R.layout.item_other;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int topItemCount = getTopItemCount();
        if (position >= topItemCount) {
            if (holder instanceof ImageViewHolder) {
                String src = elements.get(position - topItemCount).attr("src");
                ImageView image = ((ImageViewHolder) holder).image;

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                        image.getLayoutParams();

                if (position - topItemCount - 1 > 0 &&
                        !elements.get(position - topItemCount - 1).tagName().equals("img")) {
                    params.topMargin = image.getContext().getResources()
                            .getDimensionPixelSize(R.dimen.extraImagePadding);
                } else {
                    params.topMargin = 0;
                }

                if (position != getItemCount() - 1 &&
                        !elements.get(position - topItemCount + 1).tagName().equals("img")) {
                    params.bottomMargin = image.getContext().getResources()
                            .getDimensionPixelSize(R.dimen.extraImagePadding);
                } else {
                    params.bottomMargin = 0;
                }

                if (src == null || src.trim().length() == 0 || !isImageUrl(src)) {
                    image.setVisibility(View.GONE);
                } else {
                    image.setVisibility(View.VISIBLE);
                    Glide.with(((ImageViewHolder) holder).image.getContext())
                            .load(src)
                            .placeholder(R.color.imageBackground)
                            .into(image);
                }
            } else if (holder instanceof TextViewHolder) {
                String text = elements.get(position - topItemCount).text().trim();
                TextView textView = ((TextViewHolder) holder).text;

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                        textView.getLayoutParams();

                if (position == getItemCount() - 1) {
                    params.bottomMargin = textView.getContext().getResources()
                            .getDimensionPixelSize(R.dimen.extraBottomPadding);
                } else {
                    params.bottomMargin = 0;
                }

                if (text.length() > 0) {
                    textView.setText(text);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
        } else {
            if (holder instanceof HeaderImageViewHolder) {
                Glide.with(((HeaderImageViewHolder) holder).image.getContext())
                        .load(article.image)
                        .placeholder(R.color.imageBackground)
                        .into(((HeaderImageViewHolder) holder).image);
            } else if (holder instanceof TitleTextViewHolder) {
                ((TitleTextViewHolder) holder).text.setText(article.title);
            } else if (holder instanceof AuthorTextViewHolder) {
                ((AuthorTextViewHolder) holder).text.setText(article.author);
            }
        }
    }

    private boolean isImageUrl(String src) {
        return src.contains("jpg") || src.contains("png") || src.contains("gif");
    }

    @Override
    public int getItemViewType(int position) {
        int topItemCount = getTopItemCount();
        if (position >= topItemCount) {
            return getItemTypeForTag(elements.get(position - topItemCount).tagName());
        } else {
            if (position == 0) {
                return TYPE_HEADER_IMAGE;
            } else if (position == 1 && article.title != null) {
                return TYPE_TITLE;
            } else {
                return TYPE_AUTHOR;
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;

        count += getTopItemCount();

        if (elements != null) {
            count += elements.size();
        }

        return count;
    }

    private int getTopItemCount() {
        int count = 0;
        if (article != null) {
            count += 1; // header image always present at top, even when one isn't available.

            if (article.title != null) {
                count += 1;
            }

            if (article.author != null) {
                count += 1;
            }
        }

        return count;
    }

    private int getItemTypeForTag(String tag) {
        switch(tag) {
            case "p":           return TYPE_PARAGRAPH;
            case "h1":          return TYPE_HEADER_1;
            case "h2":          return TYPE_HEADER_2;
            case "h3":          return TYPE_HEADER_3;
            case "h4":          return TYPE_HEADER_4;
            case "h5":          return TYPE_HEADER_5;
            case "h6":          return TYPE_HEADER_6;
            case "img":         return TYPE_INLINE_IMAGE;
            case "blockquote":  return TYPE_BLOCKQUOTE;
            default:            return TYPE_OTHER;
        }
    }

    private class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        private TextViewHolder(View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        private ImageViewHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    private class HeaderImageViewHolder extends ImageViewHolder {
        private HeaderImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class TitleTextViewHolder extends TextViewHolder {
        private TitleTextViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class AuthorTextViewHolder extends TextViewHolder {
        private AuthorTextViewHolder(View itemView) {
            super(itemView);
        }
    }

}
