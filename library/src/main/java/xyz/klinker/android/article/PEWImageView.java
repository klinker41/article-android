package xyz.klinker.android.article;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by luke on 11/6/16.
 */

public class PEWImageView extends com.fmsirvent.ParallaxEverywhere.PEWImageView {
    public PEWImageView(Context context) {
        super(context);
        init();
    }

    public PEWImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PEWImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setReverseY(true);
    }
}
