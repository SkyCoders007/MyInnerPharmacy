package com.mxi.myinnerpharmacy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.RegistrationCommanClass;

public class KeyText extends AppCompatActivity implements View.OnTouchListener {

    TextView tv_accept_term_condition;
    RegistrationCommanClass rcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_text);
        rcc = new RegistrationCommanClass(KeyText.this);
        tv_accept_term_condition = (TextView) findViewById(R.id.tv_accept_term_condition);

        tv_accept_term_condition.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_accept_term_condition:

                rcc.savePrefBoolean("isKey", true);
                startActivity(new Intent(KeyText.this, Questionnair.class));
                finish();

                break;
        }
        return false;
    }
}
