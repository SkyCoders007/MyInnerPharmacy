package com.mxi.myinnerpharmacy.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.database.SQLiteTD;
import com.mxi.myinnerpharmacy.heartrate.HeartRateMonitor;
import com.mxi.myinnerpharmacy.model.heartrate;

import java.util.ArrayList;

public class HeartRate extends AppCompatActivity implements View.OnTouchListener {

    TextView tv_next, tv_skip, tv_heart_name, tv_heart_rate_text, tv_heart_rate_history;
    Toolbar toolbar;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);

        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_skip = (TextView) findViewById(R.id.tv_skip);
        tv_heart_name = (TextView) findViewById(R.id.tv_heart_name);
        tv_heart_rate_text = (TextView) findViewById(R.id.tv_heart_rate_text);
        tv_heart_rate_history = (TextView) findViewById(R.id.tv_heart_rate_history);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!MainActivity.isSkip) {
            tv_skip.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    // startActivity(new Intent(HeartRate.this, ));
                }
            });

        } else {
            tv_skip.setVisibility(View.VISIBLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);

        }
        String skip = "<u>" + getString(R.string.skip) + "</u>";
        tv_skip.setText(Html.fromHtml(skip));

        String heart_name = "<u>" + getString(R.string.heart_process) + "</u>";
        tv_heart_name.setText(Html.fromHtml(heart_name));

        String heart_rate_text = "<u>" + "Heart Rate History" + "</u>";
        tv_heart_rate_history.setText(Html.fromHtml(heart_rate_text));

        tv_next.setOnTouchListener(this);
        tv_skip.setOnTouchListener(this);
        tv_heart_rate_history.setOnTouchListener(this);


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.tv_next:

                checkCameraPermission();
                break;
            case R.id.tv_skip:

                if (!MainActivity.isSkip) {
                    startActivity(new Intent(HeartRate.this, MainActivity.class));
                    finish();

                } else {
                    startActivity(new Intent(HeartRate.this, PrescriptionDetail.class));
                    finish();
                }

                break;
            case R.id.tv_heart_rate_history:
                startActivity(new Intent(HeartRate.this, HeartRateHistory.class));
                break;
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void checkCameraPermission() {

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
            return;
        } else {
            startActivity(new Intent(HeartRate.this, HeartRateMonitor.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startActivity(new Intent(HeartRate.this, HeartRateMonitor.class));
                    finish();

                } else {

                    checkCameraPermission();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (!MainActivity.isSkip) {

            startActivity(new Intent(HeartRate.this, MainActivity.class));
        } else {

        }
    }

}
