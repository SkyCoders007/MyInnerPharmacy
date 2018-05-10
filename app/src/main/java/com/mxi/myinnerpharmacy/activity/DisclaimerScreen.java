package com.mxi.myinnerpharmacy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.RegistrationCommanClass;

public class DisclaimerScreen extends AppCompatActivity {

    TextView tv_accept_term_condition, tv_disclaimer;
    RegistrationCommanClass rcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer_screen);
        rcc = new RegistrationCommanClass(DisclaimerScreen.this);

        tv_accept_term_condition = (TextView) findViewById(R.id.tv_accept_term_condition);
        tv_disclaimer = (TextView) findViewById(R.id.tv_disclaimer);

        tv_accept_term_condition.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                rcc.savePrefBoolean("isDisclaimer", true);
                startActivity(new Intent(DisclaimerScreen.this, SignUpInSlider.class));
                finish();


                return false;
            }
        });

        String text_login = "<font color=#FFFFFF><u>" + getString(R.string.disclaimer) + "</u></font>";
        tv_disclaimer.setText(Html.fromHtml(text_login));

    }
}
