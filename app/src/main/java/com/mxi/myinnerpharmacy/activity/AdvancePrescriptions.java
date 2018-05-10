package com.mxi.myinnerpharmacy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.PrescriptionListAdapter;
import com.mxi.myinnerpharmacy.model.PrescriptionDetails;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.util.ArrayList;

public class AdvancePrescriptions extends AppCompatActivity {

    RecyclerView rv_text,rv_audio,rv_video;
    LinearLayoutManager llm1,llm2,llm3;
    Toolbar toolbar;
    PrescriptionListAdapter audioAdapter,videoAdapter,textAdapter;
    ArrayList<PrescriptionDetails> audioList,videoList,textList;
    TextView tv_video,tv_audio,tv_text;
    CommanClass cc;
    String label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_prescriptions);
        label=MedicalCabinets.priscriptionLable;


        cc = new CommanClass(AdvancePrescriptions.this);


        init();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




    }

    private void init() {
        tv_video=(TextView)findViewById(R.id.tv_video_adv_presc);
        tv_audio=(TextView)findViewById(R.id.tv_audio_adv_presc);
        tv_text=(TextView)findViewById(R.id.tv_text_adv_presc);

        rv_audio=(RecyclerView)findViewById(R.id.rv_audio_adv_presc);
        rv_video=(RecyclerView)findViewById(R.id.rv_video_adv_presc);
        rv_text=(RecyclerView)findViewById(R.id.rv_text_adv_presc);

        llm1=new LinearLayoutManager(this);
        llm2=new LinearLayoutManager(this);
        llm3=new LinearLayoutManager(this);


// Call Webservice to get the data for Array List

//        audioList=MedicalCabinets.audioPrescriptionList;
//        videoList=MedicalCabinets.videoPrescriptionList;
//        textList=MedicalCabinets.textPrescriptionList;

        tv_text.setText(label+" "+"TEXT");
        tv_video.setText(label+" "+"VIDEO");
        tv_audio.setText(label+" "+"AUDIO");
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        audioAdapter=new PrescriptionListAdapter(AdvancePrescriptions.this,audioList);
        videoAdapter=new PrescriptionListAdapter(AdvancePrescriptions.this,videoList);
        textAdapter=new PrescriptionListAdapter(AdvancePrescriptions.this,textList);

        rv_audio.setLayoutManager(llm1);
        rv_video.setLayoutManager(llm2);
        rv_text.setLayoutManager(llm3);

        rv_audio.setAdapter(audioAdapter);
        rv_video.setAdapter(videoAdapter);
        rv_text.setAdapter(textAdapter);

    }
}
