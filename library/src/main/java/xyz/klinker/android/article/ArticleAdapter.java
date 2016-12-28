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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.stream.StreamStringLoader;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import xyz.klinker.android.article.data.Article;

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
class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER_IMAGE = 1;
    private static final int TYPE_TITLE = 2;
    private static final int TYPE_PARAGRAPH = 3;
    private static final int TYPE_INLINE_IMAGE = 4;
    private static final int TYPE_HEADER_1 = 5;
    private static final int TYPE_HEADER_2 = 6;
    private static final int TYPE_HEADER_3 = 7;
    private static final int TYPE_HEADER_4 = 8;
    private static final int TYPE_HEADER_5 = 9;
    private static final int TYPE_HEADER_6 = 10;
    private static final int TYPE_BLOCKQUOTE = 11;
    private static final int TYPE_PRE = 12;
    private static final int TYPE_UNORDERED_LIST_ITEM = 13;
    private static final int TYPE_ORDERED_LIST_ITEM = 14;
    private static final int TYPE_OTHER = 15;
    private static final int MIN_IMAGE_WIDTH = 200; // px
    private static final int MIN_IMAGE_HEIGHT = 100; // px

    private Article article;
    private Elements elements;
    private GenericRequestBuilder<String, InputStream, BitmapFactory.Options, BitmapFactory.Options>
            sizeRequest;
    private int accentColor;
    private int textSize;
    private int imageWidth;
    private int imageHeight;

    ArticleAdapter(Article article, int accentColor, int textSize) {
        this.article = article;
        this.accentColor = accentColor;
        this.textSize = textSize;
    }

    private void initSizeRequest(Context context) {
        Resources resources = context.getResources();
        imageWidth = resources.getDimensionPixelSize(R.dimen.article_articleWidth);
        if (imageWidth <= 0) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            imageWidth = size.x;
        }

        imageHeight = resources.getDimensionPixelSize(R.dimen.article_imageParallax) +
                resources.getDimensionPixelSize(R.dimen.article_imageHeight);

        sizeRequest = Glide
                .with(context)
                .using(new StreamStringLoader(context), InputStream.class)
                .from(String.class)
                .as(BitmapFactory.Options.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new BitmapSizeDecoder())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE);
    }

    void addElements(Elements elements) {
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
            case TYPE_HEADER_IMAGE:         return new HeaderImageViewHolder(view);
            case TYPE_TITLE:                return new TitleTextViewHolder(view, accentColor);
            case TYPE_INLINE_IMAGE:         return new ImageViewHolder(view);
            case TYPE_BLOCKQUOTE:           return new BlockQuoteViewHolder(view, accentColor);
            case TYPE_HEADER_1:
            case TYPE_HEADER_2:
            case TYPE_HEADER_3:
            case TYPE_HEADER_4:
            case TYPE_HEADER_5:
            case TYPE_HEADER_6:             return new SubtitleTextViewHolder(view, accentColor);
            case TYPE_UNORDERED_LIST_ITEM:
            case TYPE_ORDERED_LIST_ITEM:
            case TYPE_PARAGRAPH:
            case TYPE_PRE:
            default:                        return new TextViewHolder(view, accentColor);
        }
    }

    private int getItemResourceFromType(int viewType) {
        switch (viewType) {
            case TYPE_HEADER_IMAGE:         return R.layout.article_item_header;
            case TYPE_TITLE:                return R.layout.article_item_title;
            case TYPE_PARAGRAPH:            return R.layout.article_item_paragraph;
            case TYPE_INLINE_IMAGE:         return R.layout.article_item_image;
            case TYPE_HEADER_1:             return R.layout.article_item_header_1;
            case TYPE_HEADER_2:             return R.layout.article_item_header_2;
            case TYPE_HEADER_3:             return R.layout.article_item_header_3;
            case TYPE_HEADER_4:             return R.layout.article_item_header_4;
            case TYPE_HEADER_5:             return R.layout.article_item_header_5;
            case TYPE_HEADER_6:             return R.layout.article_item_header_6;
            case TYPE_BLOCKQUOTE:           return R.layout.article_item_blockquote;
            case TYPE_PRE:                  return R.layout.article_item_pre;
            case TYPE_UNORDERED_LIST_ITEM:  return R.layout.article_item_unordered_list_item;
            case TYPE_ORDERED_LIST_ITEM:    return R.layout.article_item_ordered_list_item;
            default:                        return R.layout.article_item_other;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int topItemCount = getTopItemCount();
        if (position >= topItemCount) {
            if (holder instanceof ImageViewHolder) {
                String src = ArticleUtils
                        .decodeImageUrl(elements.get(position - topItemCount).attr("src"));
                final ImageView image = ((ImageViewHolder) holder).image;

                if (src.startsWith("data:")) {
                    // bad image data from the server, it didn't give us a url
                    image.setVisibility(View.GONE);
                    return;
                }

                image.setVisibility(View.VISIBLE);

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                        image.getLayoutParams();

                if (position - topItemCount - 1 >= 0 &&
                        !elements.get(position - topItemCount - 1).tagName().equals("img")) {
                    params.topMargin = image.getContext().getResources()
                            .getDimensionPixelSize(R.dimen.article_extraImagePadding);
                } else {
                    params.topMargin = 0;
                }

                if (position != getItemCount() - 1 &&
                        !elements.get(position - topItemCount + 1).tagName().equals("img")) {
                    params.bottomMargin = image.getContext().getResources()
                            .getDimensionPixelSize(R.dimen.article_extraImagePadding);
                } else {
                    params.bottomMargin = 0;
                }

                if (sizeRequest == null) {
                    initSizeRequest(image.getContext());
                }

                sizeRequest.load(src)
                        .into(new SimpleTarget<BitmapFactory.Options>() {
                            @Override
                            public void onResourceReady(BitmapFactory.Options resource,
                                                        GlideAnimation<? super BitmapFactory.Options> glideAnimation) {
                                if (resource.outWidth < MIN_IMAGE_WIDTH ||
                                        resource.outHeight < MIN_IMAGE_HEIGHT) {
                                    image.setVisibility(View.GONE);
                                }
                            }
                        });

                Log.v("ArticleAdapter", "loading url at " + src);

                ((ImageViewHolder) holder).url = src;
                Glide.with(image.getContext())
                        .load(src)
                        .override(imageWidth, imageHeight)
                        .placeholder(R.color.article_imageBackground)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image);

            } else if (holder instanceof TextViewHolder) {
                String text = elements.get(position - topItemCount).text().trim();
                TextView textView = ((TextViewHolder) holder).text;

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                        textView.getLayoutParams();

                if (position == getItemCount() - 1) {
                    params.bottomMargin = textView.getContext().getResources()
                            .getDimensionPixelSize(R.dimen.article_extraBottomPadding);
                } else {
                    params.bottomMargin = 0;
                }

                textView.setText(text);
            }
        } else {
            if (holder instanceof HeaderImageViewHolder) {
                ImageView image = ((HeaderImageViewHolder) holder).image;

                if (sizeRequest == null) {
                    initSizeRequest(image.getContext());
                }

                String src = ArticleUtils.decodeImageUrl(article.image);
                ((HeaderImageViewHolder) holder).url = src;
                Glide.with(image.getContext())
                        .load(src)
                        .override(imageWidth, imageHeight)
                        .placeholder(R.color.article_imageBackground)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image);
            } else if (holder instanceof TitleTextViewHolder) {
                ((TitleTextViewHolder) holder).text.setText(article.title);

                if (article.author == null) {
                    ((TitleTextViewHolder) holder).author.setVisibility(View.GONE);
                } else {
                    ((TitleTextViewHolder) holder).author.setText(article.author);
                }

                if (article.source == null) {
                    ((TitleTextViewHolder) holder).source.setVisibility(View.GONE);
                } else {
                    ((TitleTextViewHolder) holder).source.setText(article.domain);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int topItemCount = getTopItemCount();
        if (position >= topItemCount) {
            Element element = elements.get(position - topItemCount);
            String tag = element.tagName();
            if (tag.equals("li")) {
                tag = element.parent().tagName() + "." + tag;
            }

            return getItemTypeForTag(tag);
        } else {
            if (position == 0) {
                return TYPE_HEADER_IMAGE;
            } else {
                return TYPE_TITLE;
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

            if (article.title != null || article.author != null) {
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
            case "pre":         return TYPE_PRE;
            case "ul.li":       return TYPE_UNORDERED_LIST_ITEM;
            case "ol.li":       return TYPE_ORDERED_LIST_ITEM;
            default:            return TYPE_OTHER;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        private TextViewHolder(View itemView, int accentColor) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.article_text);
            this.text.setTextSize(textSize);
            this.text.setTextIsSelectable(true);
            Utils.changeTextSelectionHandleColors(this.text, accentColor);
        }
    }

    private class BlockQuoteViewHolder extends TextViewHolder {
        private BlockQuoteViewHolder(View itemView, int accentColor) {
            super(itemView, accentColor);
            text.setTextColor(accentColor);
            text.setTextSize(textSize + 5);
        }
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public String url;

        private ImageViewHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.article_image);

            this.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(image.getContext(), ImageViewActivity.class);
                    intent.setData(Uri.parse(url));
                    image.getContext().startActivity(intent);
                }
            });
        }
    }

    private class HeaderImageViewHolder extends ImageViewHolder {
        private HeaderImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class TitleTextViewHolder extends TextViewHolder {
        public TextView author;
        public TextView source;

        private TitleTextViewHolder(View itemView, int accentColor) {
            super(itemView, accentColor);
            author = (TextView) itemView.findViewById(R.id.article_author);
            source = (TextView) itemView.findViewById(R.id.article_source);

            text.setTextSize(textSize + 9);
        }
    }

    private class SubtitleTextViewHolder extends TextViewHolder {
        private SubtitleTextViewHolder(View itemView, int accentColor) {
            super(itemView, accentColor);
            text.setTextSize(textSize + 3);
        }
    }

    private class BitmapSizeDecoder implements ResourceDecoder<File, BitmapFactory.Options> {
        @Override
        public Resource<BitmapFactory.Options> decode(File source,
                                                      int width,
                                                      int height) throws IOException {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(source.getAbsolutePath(), options);
            return new SimpleResource<>(options);
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

}
