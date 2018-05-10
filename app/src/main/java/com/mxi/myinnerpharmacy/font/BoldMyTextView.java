package com.mxi.myinnerpharmacy.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sonali on 29/12/16.
 */
public class BoldMyTextView extends TextView {

    public BoldMyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BoldMyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoldMyTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Raleway-Bold.ttf");
        setTypeface(tf);
    }

}
