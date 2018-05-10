package com.mxi.myinnerpharmacy.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.SignSliderAdapter;
import com.mxi.myinnerpharmacy.network.RegistrationCommanClass;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class SignUpInSlider extends AppCompatActivity implements View.OnTouchListener {

    TextView tv_signup, tv_signin;
    PageIndicator mIndicator;
    CirclePageIndicator indicator;
    SignSliderAdapter welcomeScreenAdapter;
    ViewPager viewPager;
    RegistrationCommanClass rcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_slider);

        rcc = new RegistrationCommanClass(SignUpInSlider.this);

        tv_signin = (TextView) findViewById(R.id.tv_signin);
        tv_signup = (TextView) findViewById(R.id.tv_signup);
        viewPager = (ViewPager) findViewById(R.id.pager);
        indicator = (CirclePageIndicator) findViewById(R.id.circle_indicator);

        String[] image_list = {"I control my inner pharmacy by knowing how to" +
                "activate my biochemical substances needed to stay healthy," +
                "relieve stress and feel good while being focused and confident.",
                "I control my inner pharmacy by knowing how to" +
                        "activate my biochemical substances needed to stay healthy," +
                        "relieve stress and feel good while being focused and confident.",
                "I control my inner pharmacy by knowing how to" +
                        "activate my biochemical substances needed to stay healthy," +
                        "relieve stress and feel good while being focused and confident."};

        welcomeScreenAdapter = new SignSliderAdapter(SignUpInSlider.this, image_list);
        viewPager.setAdapter(welcomeScreenAdapter);
        mIndicator = indicator;
        indicator.setViewPager(viewPager);

        tv_signin.setOnTouchListener(this);
        tv_signup.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

            case R.id.tv_signin:
                rcc.savePrefBoolean("SignIn", true);
                startActivity(new Intent(SignUpInSlider.this, LoginScreen.class));
                finish();
                break;

            case R.id.tv_signup:
                rcc.savePrefBoolean("SignIn", true);
                startActivity(new Intent(SignUpInSlider.this, Register.class));
                finish();
                break;
        }

        return false;
    }
}
