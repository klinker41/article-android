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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import xyz.klinker.android.drag_dismiss.activity.DragDismissActivity;

/**
 * Activity for viewing full size images from an article. Images are zoomable. You can pass in the
 * image url from an intent with .setData(url).
 */
public final class ImageViewActivity extends DragDismissActivity {

    @Override
    protected View onCreateContent(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.article_activity_image_view, parent, false);

        ImageView imageView = (ImageView) root.findViewById(R.id.article_image_view);
        View statusBar = findViewById(R.id.dragdismiss_status_bar);
        String url = ArticleUtils.decodeImageUrl(getIntent().getDataString());

        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        statusBar.setBackgroundColor(Color.BLACK);

        return root;
    }

}
