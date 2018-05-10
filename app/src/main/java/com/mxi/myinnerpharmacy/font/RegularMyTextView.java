package com.mxi.myinnerpharmacy.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by admin1 on 23/3/16.
 */
public class RegularMyTextView extends TextView {

    public RegularMyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RegularMyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegularMyTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Raleway-Regular.ttf");
        setTypeface(tf);
    }

}
