package com.mxi.myinnerpharmacy.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by sonali on 9/2/17.
 */
public class Button extends android.widget.Button {

    public Button(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Button(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Raleway-Bold.ttf");
        setTypeface(tf);
    }

}
