package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.PrescriptionListAdapter;
import com.mxi.myinnerpharmacy.model.PrescriptionDetails;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CurrentPrescriptionRequest extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog pDialog;
    ArrayList<PrescriptionDetails> audioList, videoList, textList;
    PrescriptionListAdapter audioAdapter, videoAdapter, textAdapter;
    RecyclerView rv_text, rv_audio, rv_video;
    LinearLayoutManager llm1, llm2, llm3;
    Toolbar toolbar;
    TextView tv_video, tv_audio, tv_text, tv_done, tv_prescription_detail_title;
    CommanClass cc;
    String label = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_detail);

        // label = MedicalCabinets.priscriptionLable;
        cc = new CommanClass(CurrentPrescriptionRequest.this);

        init();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void init() {
        tv_prescription_detail_title = (TextView) findViewById(R.id.tv_prescription_detail_title);
        tv_video = (TextView) findViewById(R.id.tv_video_pres_detail);
        tv_audio = (TextView) findViewById(R.id.tv_audio_pres_detail);
        tv_text = (TextView) findViewById(R.id.tv_text_pres_detail);
        tv_done = (TextView) findViewById(R.id.tv_done_pres_detail);

        rv_audio = (RecyclerView) findViewById(R.id.rv_audio_pres_detail);
        rv_video = (RecyclerView) findViewById(R.id.rv_video_pres_detail);
        rv_text = (RecyclerView) findViewById(R.id.rv_text_pres_detail);

        llm1 = new LinearLayoutManager(this);
        llm2 = new LinearLayoutManager(this);
        llm3 = new LinearLayoutManager(this);

//        audioList=MedicalCabinets.audioPrescriptionList;
//        videoList=MedicalCabinets.videoPrescriptionList;
//        textList=MedicalCabinets.textPrescriptionList;

        tv_prescription_detail_title.setText(R.string.current_pres_req);
        Log.e("label", label);
        if (label.equals("null") || label.equals("")) {

            tv_text.setText("No" + " " + "TEXT");
            tv_video.setText("No" + " " + "VIDEO");
            tv_audio.setText("No" + " " + "AUDIO");

        } else {
            tv_text.setText(label + " " + "TEXT");
            tv_video.setText(label + " " + "VIDEO");
            tv_audio.setText(label + " " + "AUDIO");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_done.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_done_pres_detail:
                startActivity(new Intent(CurrentPrescriptionRequest.this, StateCalibrationNext.class));
                finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(CurrentPrescriptionRequest.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                makeJsonGetCurrentPrescriptionRequest();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeJsonGetCurrentPrescriptionRequest() {

        // get_current_prescription

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_current_prescription,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_get_current_pres", response);
                        jsonGetCurrentPrescriptionRequest(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
//                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("acheivement_id", cc.loadPrefString("selected_question_id"));
                params.put("difficulty_id", cc.loadPrefString("difficulty_id"));
                Log.e("Current_pres_request", params.toString() + "");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("mip-token", cc.loadPrefString("mip-token"));
                Log.i("request header", headers.toString());
                return headers;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    private void jsonGetCurrentPrescriptionRequest(String response) {

        try {
            audioList = new ArrayList<PrescriptionDetails>();
            videoList = new ArrayList<PrescriptionDetails>();
            textList = new ArrayList<PrescriptionDetails>();
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray prescription = jsonObject.getJSONArray("prescription");

                for (int i = 0; i < prescription.length(); i++) {
                    JSONObject jsonObject1 = prescription.getJSONObject(i);

                    PrescriptionDetails pk = new PrescriptionDetails();
                    pk.setMedia_type(jsonObject1.getString("media_type"));
                    pk.setMedia_file(jsonObject1.getString("media_file"));
                    pk.setMedia_name(jsonObject1.getString("media_name"));
                    pk.setMedia_id(jsonObject1.getString("media_id"));

                    String media = jsonObject1.getString("media_type");
                    if (media.equals("Audio")) {
                        audioList.add(pk);
                    } else if (media.equals("Video")) {
                        videoList.add(pk);
                    } else if (media.equals("Text")) {
                        textList.add(pk);
                    }

                }

                Log.e("audioCurPresList", audioList.size() + "");
                Log.e("videoCurPresList", videoList.size() + "");
                Log.e("textCurPresList", textList.size() + "");

                if (audioList.isEmpty()) {
                    tv_text.setText("No" + " " + "TEXT");
                    rv_audio.setVisibility(View.GONE);
                } else {
                    rv_audio.setVisibility(View.VISIBLE);
                    audioAdapter = new PrescriptionListAdapter(CurrentPrescriptionRequest.this, audioList);
                    rv_audio.setLayoutManager(llm1);
                    rv_audio.setAdapter(audioAdapter);
                }
                if (videoList.isEmpty()) {
                    tv_video.setText("No" + " " + "VIDEO");
                    rv_video.setVisibility(View.GONE);
                } else {
                    rv_video.setVisibility(View.VISIBLE);
                    videoAdapter = new PrescriptionListAdapter(CurrentPrescriptionRequest.this, videoList);
                    rv_video.setLayoutManager(llm2);
                    rv_video.setAdapter(videoAdapter);
                }
                if (textList.isEmpty()) {
                    tv_audio.setText("No" + " " + "AUDIO");
                    rv_text.setVisibility(View.GONE);
                } else {
                    rv_text.setVisibility(View.VISIBLE);
                    textAdapter = new PrescriptionListAdapter(CurrentPrescriptionRequest.this, textList);
                    rv_text.setLayoutManager(llm3);
                    rv_text.setAdapter(textAdapter);
                }

            } else {

                cc.showToast(jsonObject.getString("message"));
                tv_text.setText("No" + " " + "TEXT");
                rv_audio.setVisibility(View.GONE);
                tv_video.setText("No" + " " + "VIDEO");
                rv_video.setVisibility(View.GONE);
                tv_audio.setText("No" + " " + "AUDIO");
                rv_text.setVisibility(View.GONE);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
