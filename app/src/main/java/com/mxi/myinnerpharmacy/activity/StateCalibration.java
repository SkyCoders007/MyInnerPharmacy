package com.mxi.myinnerpharmacy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.CommanClass;

public class StateCalibration extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    TextView tv_next, tv_self_image, tv_personal_relationship, tv_physical_health, tv_community, tv_family, tv_finance, tv_work;
    Toolbar toolbar;
    EditText et_enter_problem;
    TextView iv_calibration;
    CommanClass cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_state_calibration);
        cc = new CommanClass(StateCalibration.this);

        tv_next = (TextView) findViewById(R.id.tv_next);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        et_enter_problem = (EditText) findViewById(R.id.et_enter_problem);
        iv_calibration = (TextView) findViewById(R.id.iv_calibration);
        tv_self_image = (TextView) findViewById(R.id.tv_self_image);
        tv_personal_relationship = (TextView) findViewById(R.id.tv_personal_relationship);
        tv_physical_health = (TextView) findViewById(R.id.tv_physical_health);
        tv_community = (TextView) findViewById(R.id.tv_community);
        tv_family = (TextView) findViewById(R.id.tv_family);
        tv_finance = (TextView) findViewById(R.id.tv_finance);
        tv_work = (TextView) findViewById(R.id.tv_work);

        if (!MainActivity.isSkip) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);

        }

        tv_next.setOnTouchListener(this);
        tv_self_image.setOnClickListener(this);
        tv_personal_relationship.setOnClickListener(this);
        tv_physical_health.setOnClickListener(this);
        tv_community.setOnClickListener(this);
        tv_family.setOnClickListener(this);
        tv_finance.setOnClickListener(this);
        tv_work.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {

        if (!MainActivity.isSkip) {

            // onBackPressed();
            startActivity(new Intent(StateCalibration.this, MainActivity.class));
        } else {

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_next:
                String enter_text = et_enter_problem.getText().toString().trim();
                cc.savePrefString("calibration_problem_text", enter_text);
                startActivity(new Intent(StateCalibration.this, StateCalibrationNext.class));
                finish();
                break;

        }

        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_self_image:
                et_enter_problem.setText("Self Image");
                break;
            case R.id.tv_personal_relationship:
                et_enter_problem.setText("Personal Relationship");
                break;
            case R.id.tv_physical_health:
                et_enter_problem.setText("Physical Health");
                break;
            case R.id.tv_community:
                et_enter_problem.setText("Community");
                break;
            case R.id.tv_family:
                et_enter_problem.setText("Family");
                break;
            case R.id.tv_finance:
                et_enter_problem.setText("Finance");
                break;
            case R.id.tv_work:
                et_enter_problem.setText("Work");
                break;
        }
    }
}
