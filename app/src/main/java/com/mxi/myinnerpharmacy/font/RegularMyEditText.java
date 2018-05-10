package com.mxi.myinnerpharmacy.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by sonali on 13/12/16.
 */
public class RegularMyEditText extends EditText {

    public RegularMyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RegularMyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegularMyEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Raleway-Regular.ttf");
        setTypeface(tf);
    }
}