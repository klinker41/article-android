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

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.lang.Math;

public class ArticleScrollListener extends RecyclerView.OnScrollListener {

    private static final int ANIMATION_DURATION = 100;

    private Toolbar toolbar;

    public ArticleScrollListener(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int minDistance = toolbar.getContext().getResources()
                .getDimensionPixelSize(R.dimen.minToolbarScroll);
        if (Math.abs(dy) < minDistance) {
            return;
        }

        if (dy > 0 && toolbar.getTranslationY() == 0) {
            toolbar.animate()
                    .translationY(-1 * toolbar.getHeight())
                    .setDuration(ANIMATION_DURATION)
                    .setInterpolator(new AccelerateInterpolator());
        } else if (dy < 0 && toolbar.getTranslationY() != 0) {
            toolbar.animate()
                    .translationY(0)
                    .setDuration(ANIMATION_DURATION)
                    .setInterpolator(new DecelerateInterpolator());
        }

        // TODO(klinker41): change the color of the toolbar and status bar as it scrolls up
        //                  to the primary color and if we scroll down and the first item is
        //                  visible, change back to transparent.
    }
}
