package com.mxi.myinnerpharmacy.activity;

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
import com.mxi.myinnerpharmacy.adapter.RapidResponseAdapter;
import com.mxi.myinnerpharmacy.model.PrescriptionDetails;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.util.ArrayList;

public class RapidResponsePrescription extends AppCompatActivity {


    ArrayList<PrescriptionDetails> audioList, videoList, textList;
    PrescriptionListAdapter audioAdapter, videoAdapter, textAdapter;
    RecyclerView rv_text, rv_audio, rv_video;
    LinearLayoutManager llm1, llm2, llm3;

    ImageView iv_video_add, iv_audio_add, iv_text_add;
    TextView tv_done, tv_vid, tv_aud, tv_text;
    CommanClass cc;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapid_response_prescription);
        cc = new CommanClass(RapidResponsePrescription.this);
        init();

    }

    private void init() {
        String label = getIntent().getStringExtra("Rapid_response_name");
        String label_id = getIntent().getStringExtra("Rapid_response_id");

        iv_video_add = (ImageView) findViewById(R.id.iv_video_add);
        iv_audio_add = (ImageView) findViewById(R.id.iv_audio_add);
        iv_text_add = (ImageView) findViewById(R.id.iv_text_add);
        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_vid = (TextView) findViewById(R.id.tv_1_rapid_response);
        tv_aud = (TextView) findViewById(R.id.tv_2_rapid_response);
        tv_text = (TextView) findViewById(R.id.tv_3_rapid_response);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_vid.setText(label + "  Video");
        tv_aud.setText(label + "  Audio");
        tv_text.setText(label + "  Text");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        rv_audio = (RecyclerView) findViewById(R.id.rv_audio_rapid_res);
        rv_video = (RecyclerView) findViewById(R.id.rv_video_rapid_res);
        rv_text = (RecyclerView) findViewById(R.id.rv_text_rapid_res);

        llm1 = new LinearLayoutManager(this);
        llm2 = new LinearLayoutManager(this);
        llm3 = new LinearLayoutManager(this);

        audioList = RapidResponseAdapter.rapid_audioPrescriptionList;
        videoList = RapidResponseAdapter.rapid_videoPrescriptionList;
        textList = RapidResponseAdapter.rapid_textPrescriptionList;

        if (!audioList.isEmpty()) {
            rv_text.setVisibility(View.VISIBLE);
            audioAdapter = new PrescriptionListAdapter(RapidResponsePrescription.this, audioList);
        } else {
            rv_text.setVisibility(View.GONE);
        }

        if (!videoList.isEmpty()) {
            rv_video.setVisibility(View.VISIBLE);
            videoAdapter = new PrescriptionListAdapter(RapidResponsePrescription.this, videoList);
        } else {
            rv_video.setVisibility(View.GONE);
        }

        if (!textList.isEmpty()) {
            rv_text.setVisibility(View.VISIBLE);
            textAdapter = new PrescriptionListAdapter(RapidResponsePrescription.this, textList);
        } else {
            rv_text.setVisibility(View.GONE);
        }

        rv_audio.setLayoutManager(llm1);
        rv_video.setLayoutManager(llm2);
        rv_text.setLayoutManager(llm3);

        rv_audio.setAdapter(audioAdapter);
        rv_video.setAdapter(videoAdapter);
        rv_text.setAdapter(textAdapter);


    }
}
