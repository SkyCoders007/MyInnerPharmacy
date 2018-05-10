package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.ColabrationStateNextAdapter;
import com.mxi.myinnerpharmacy.model.Colibrationstate;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StateCalibrationNext extends AppCompatActivity {

    LinearLayout ll_text;
    ProgressDialog pDialog;
    CommanClass cc;
    ViewPager pager;
    LinearLayout ll_linear;
    Toolbar toolbar;
    ArrayList<Colibrationstate> colibrationstate;
    TextView tv_next;
    TextView[] myTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_calibration_next);
        cc = new CommanClass(StateCalibrationNext.this);

        ll_text = (LinearLayout) findViewById(R.id.ll_text);
        tv_next = (TextView) findViewById(R.id.tv_next);
        pager = (ViewPager) findViewById(R.id.pager);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (!MainActivity.isSkip) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // onBackPressed();
                    startActivity(new Intent(StateCalibrationNext.this, StateCalibration.class));
                    finish();
                }
            });
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);

        }

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!MainActivity.isSkip) {
                    //  PrescriptionDetail.fromPrescriptionDetail = false;
                    cc.savePrefString("After_Pres_Calibration_state_name", colibrationstate.get(pager.getCurrentItem()).getName());
                    cc.savePrefString("After_Pres_Calibration_state_value", colibrationstate.get(pager.getCurrentItem()).getValue());
                    startActivity(new Intent(StateCalibrationNext.this, MainActivity.class));
                    finish();
                } else {
                    if (MainActivity.StepLastColibration) {
                        startActivity(new Intent(StateCalibrationNext.this, MainActivity.class));
                        finish();
                    } else {
                        cc.savePrefString("Calibration_state_name", colibrationstate.get(pager.getCurrentItem()).getName());
                        cc.savePrefString("Calibration_state_value", colibrationstate.get(pager.getCurrentItem()).getValue());
                        startActivity(new Intent(StateCalibrationNext.this, HeartRate.class));
                        finish();
                    }
                }

            }
        });
        if (cc.isConnectingToInternet()) {
            pDialog = new ProgressDialog(StateCalibrationNext.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            makeJsonColabrationState();
        } else {
            cc.showSnackbar(ll_linear, getString(R.string.no_internet));
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                try {
                    for (int i = 0; i < myTextViews.length; i++) {
                        if (position == i) {
                            myTextViews[i].setTextColor(Color.parseColor("#FFFFFF"));
                        } else {
                            myTextViews[i].setTextColor(Color.parseColor("#3E3E49"));
                        }
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                }


            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
                // pager.setCurrentItem(Integer.parseInt(pos), false);
            }
        });


    }

    private void makeJsonColabrationState() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_calibration_state,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_calibration_state", response);
                        jsonParseColibration(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(ll_linear, getString(R.string.ws_error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    private void jsonParseColibration(String response) {
        try {
            colibrationstate = new ArrayList<Colibrationstate>();
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            Typeface font = Typeface.createFromAsset(StateCalibrationNext.this.getAssets(), "fonts/Raleway-Bold.ttf");
            if (jsonObject.getString("status").equals("200")) {
                String imagepath = jsonObject.getString("calibrationstateImagethumbPath");

                final JSONArray calibrationstate = jsonObject.getJSONArray("calibrationstate");

                for (int i = 0; i < calibrationstate.length(); i++) {
                    JSONObject jsonObject1 = calibrationstate.getJSONObject(i);

                    Colibrationstate cs = new Colibrationstate();
                    cs.setName(jsonObject1.getString("name"));
                    cs.setValue(jsonObject1.getString("value"));
                    cs.setImage(imagepath + jsonObject1.getString("image"));

                    colibrationstate.add(cs);

                }
                if (!colibrationstate.isEmpty()) {

                    ColabrationStateNextAdapter adapter = new ColabrationStateNextAdapter(StateCalibrationNext.this, colibrationstate);
                    pager.setAdapter(adapter);

                    try {
                        myTextViews = new TextView[colibrationstate.size()]; // create an empty array;
                        for (int i = 0; i < colibrationstate.size(); i++) {

                            TextView tv1 = new TextView(this);
                            tv1.setText(i + "");
                            tv1.setTextSize(20);
                            tv1.setId(i);
                            tv1.setGravity(Gravity.CENTER_VERTICAL);
                            tv1.setPadding(10, 0, 10, 0);
                            //tv1.setTypeface(font);
                            tv1.setTextColor(Color.parseColor("#3E3E49"));
                            ll_text.addView(tv1);
                            myTextViews[i] = tv1;
                            myTextViews[i].setOnClickListener(onclicklistener);
                            myTextViews[0].setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }

                }


            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    View.OnClickListener onclicklistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            try {
                for (int i = 0; i < myTextViews.length; i++) {
                    if (v.getId() == i) {
                        pager.setCurrentItem(v.getId());

                        myTextViews[i].setTextColor(Color.parseColor("#FFFFFF"));

                    } else {
                        myTextViews[i].setTextColor(Color.parseColor("#3E3E49"));
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }

        }
    };

    @Override
    public void onBackPressed() {

        if (!MainActivity.isSkip) {

            startActivity(new Intent(StateCalibrationNext.this, StateCalibration.class));
        } else {

        }
    }
}
