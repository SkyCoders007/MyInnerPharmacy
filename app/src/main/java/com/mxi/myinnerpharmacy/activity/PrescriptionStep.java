package com.mxi.myinnerpharmacy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.CommanClass;

public class PrescriptionStep extends AppCompatActivity implements View.OnTouchListener {

    TextView tv_prescription_text, tv_prescription_1, tv_prescription_2, tv_prescription_3, tv_start;
    Toolbar toolbar;
    CommanClass cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_step);
        cc = new CommanClass(PrescriptionStep.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_prescription_text = (TextView) findViewById(R.id.tv_prescription_text);
        tv_prescription_1 = (TextView) findViewById(R.id.tv_prescription_1);
        tv_prescription_2 = (TextView) findViewById(R.id.tv_prescription_2);
        tv_prescription_3 = (TextView) findViewById(R.id.tv_prescription_3);
        tv_start = (TextView) findViewById(R.id.tv_start);

        String text_login = "<u>" + getString(R.string.pres_prep_text) + " " + MedicalCabinets.priscriptionLable
                + " " + getString(R.string.Prescription) + "</u>";
        tv_prescription_text.setText(Html.fromHtml(text_login));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_prescription_1.setOnTouchListener(this);
        tv_prescription_2.setOnTouchListener(this);
        tv_prescription_3.setOnTouchListener(this);
        tv_start.setOnTouchListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        MainActivity.isSkip = false;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {

            case R.id.tv_prescription_1:
//                startActivity(new Intent(PrescriptionStep.this, StateCalibrationNext.class));
                break;
            case R.id.tv_prescription_2:

                //startActivity(new Intent(PrescriptionStep.this, StateCalibrationNext.class));
                break;
            case R.id.tv_prescription_3:
//                startActivity(new Intent(PrescriptionStep.this, HeartRate.class));
                break;
            case R.id.tv_start:
                MainActivity.isSkip = true;
                startActivity(new Intent(PrescriptionStep.this, StateCalibration.class));
                break;
        }
        return false;
    }
}
