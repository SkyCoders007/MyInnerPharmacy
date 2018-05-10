package com.mxi.myinnerpharmacy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.PrescriptionListAdapter;
import com.mxi.myinnerpharmacy.model.PrescriptionDetails;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.util.ArrayList;

public class PrescriptionDetail extends AppCompatActivity implements View.OnClickListener {

    // public static boolean fromPrescriptionDetail = false;

    ArrayList<PrescriptionDetails> audioList, videoList, textList;
    PrescriptionListAdapter audioAdapter, videoAdapter, textAdapter;
    RecyclerView rv_text, rv_audio, rv_video;
    LinearLayoutManager llm1, llm2, llm3;
    Toolbar toolbar;
    ImageView iv_add_video, iv_add_audio, iv_add_text;

    TextView tv_video, tv_audio, tv_text, tv_done, tv_done_pres_detail;
    CommanClass cc;
    String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_detail);
        label = MedicalCabinets.priscriptionLable;

        cc = new CommanClass(PrescriptionDetail.this);

        init();

    }

    private void init() {
        tv_video = (TextView) findViewById(R.id.tv_video_pres_detail);
        tv_audio = (TextView) findViewById(R.id.tv_audio_pres_detail);
        tv_text = (TextView) findViewById(R.id.tv_text_pres_detail);
        tv_done = (TextView) findViewById(R.id.tv_done_pres_detail);
        tv_done_pres_detail = (TextView) findViewById(R.id.tv_done_pres_detail);

        iv_add_audio = (ImageView) findViewById(R.id.iv_add_audio);
        iv_add_video = (ImageView) findViewById(R.id.iv_add_video);
        iv_add_text = (ImageView) findViewById(R.id.iv_add_text);

        iv_add_audio.setVisibility(View.VISIBLE);
        iv_add_video.setVisibility(View.VISIBLE);
        iv_add_text.setVisibility(View.VISIBLE);

        rv_audio = (RecyclerView) findViewById(R.id.rv_audio_pres_detail);
        rv_video = (RecyclerView) findViewById(R.id.rv_video_pres_detail);
        rv_text = (RecyclerView) findViewById(R.id.rv_text_pres_detail);

        llm1 = new LinearLayoutManager(this);
        llm2 = new LinearLayoutManager(this);
        llm3 = new LinearLayoutManager(this);

        audioList = MedicalCabinets.audioPrescriptionList;
        videoList = MedicalCabinets.videoPrescriptionList;
        textList = MedicalCabinets.textPrescriptionList;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

        try {
            if (audioList.isEmpty()) {
                tv_text.setText("No" + " " + "TEXT");
                rv_audio.setVisibility(View.GONE);
            } else {
                rv_audio.setVisibility(View.VISIBLE);
                tv_text.setText(label + " " + "TEXT");
                audioAdapter = new PrescriptionListAdapter(PrescriptionDetail.this, audioList);
                rv_audio.setLayoutManager(llm1);
                rv_audio.setAdapter(audioAdapter);
            }
            if (videoList.isEmpty()) {
                tv_video.setText("No" + " " + "VIDEO");
                rv_video.setVisibility(View.GONE);
            } else {
                rv_video.setVisibility(View.VISIBLE);
                tv_video.setText(label + " " + "VIDEO");
                videoAdapter = new PrescriptionListAdapter(PrescriptionDetail.this, videoList);
                rv_video.setLayoutManager(llm2);
                rv_video.setAdapter(videoAdapter);
            }
            if (textList.isEmpty()) {
                tv_audio.setText("No" + " " + "AUDIO");
                rv_text.setVisibility(View.GONE);
            } else {
                rv_text.setVisibility(View.VISIBLE);
                tv_audio.setText(label + " " + "AUDIO");
                textAdapter = new PrescriptionListAdapter(PrescriptionDetail.this, textList);
                rv_text.setLayoutManager(llm3);
                rv_text.setAdapter(textAdapter);
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

        tv_done.setOnClickListener(this);
        iv_add_audio.setOnClickListener(this);
        iv_add_video.setOnClickListener(this);
        iv_add_text.setOnClickListener(this);

        //tv_done_pres_detail.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_done_pres_detail:
                //  fromPrescriptionDetail = true;
               /* startActivity(new Intent(PrescriptionDetail.this, StateCalibrationNext.class));
                finish();*/
                if (!MainActivity.isSkip) {
                    //  cc.savePrefString("After_Pres_Calibration_state_name",PrescriptionDetail.get(pager.getCurrentItem()).getName());
                    //  cc.savePrefString("After_Pres_Calibration_state_value",colibrationstate.get(pager.getCurrentItem()).getValue());
                    startActivity(new Intent(PrescriptionDetail.this, MainActivity.class));
                    finish();
                } else {
                    // cc.savePrefString("Calibration_state_name",colibrationstate.get(pager.getCurrentItem()).getName());
                    // cc.savePrefString("Calibration_state_value",colibrationstate.get(pager.getCurrentItem()).getValue());
                    MainActivity.StepLastColibration = true;
                    startActivity(new Intent(PrescriptionDetail.this, StateCalibrationNext.class));
                    finish();
                }
                break;
            case R.id.iv_add_audio:

                startActivity(new Intent(PrescriptionDetail.this, AddPrescriptions.class).
                        putExtra("title_text", getString(R.string.add_pres_audio_title)).
                        putExtra("browse", getString(R.string.add_audio)));
                break;
            case R.id.iv_add_video:
                startActivity(new Intent(PrescriptionDetail.this, AddPrescriptions.class).
                        putExtra("title_text", getString(R.string.add_pres_video_title)).
                        putExtra("browse", getString(R.string.add_video)));

                break;
            case R.id.iv_add_text:

                startActivity(new Intent(PrescriptionDetail.this, AddPrescriptions.class).
                        putExtra("title_text", getString(R.string.add_pres_text_title)).
                        putExtra("browse", getString(R.string.add_text)));
                break;
        }

    }

    @Override
    public void onBackPressed() {

       /* if (!MainActivity.isSkip) {

            startActivity(new Intent(PrescriptionDetail.this, MainActivity.class));
        } else {

        }*/
    }

  /*  @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.tv_done_pres_detail) {

            if (PrescriptionDetail.fromPrescriptionDetail) {
                PrescriptionDetail.fromPrescriptionDetail = false;
                //  cc.savePrefString("After_Pres_Calibration_state_name",PrescriptionDetail.get(pager.getCurrentItem()).getName());
                //  cc.savePrefString("After_Pres_Calibration_state_value",colibrationstate.get(pager.getCurrentItem()).getValue());
                startActivity(new Intent(PrescriptionDetail.this, MainActivity.class));
                finish();
            } else {
                // cc.savePrefString("Calibration_state_name",colibrationstate.get(pager.getCurrentItem()).getName());
                // cc.savePrefString("Calibration_state_value",colibrationstate.get(pager.getCurrentItem()).getValue());
                startActivity(new Intent(PrescriptionDetail.this, StateCalibrationNext.class));
                finish();
            }

            //onBackPressed();
        }
        return false;
    }*/
}
