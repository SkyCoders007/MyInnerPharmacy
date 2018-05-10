package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.RapidResponseAdapter;
import com.mxi.myinnerpharmacy.model.Pausetask;
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

public class RapidResponse extends AppCompatActivity {

    TextView tv_date;
    RecyclerView rv_rapid_response;
    Toolbar toolbar;

    LinearLayoutManager mLayoutManager;
    ProgressDialog pDialog;
    ArrayList<Pausetask> upliftMusicList;
    CommanClass cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapid_response);
        cc=new CommanClass(this);


        rv_rapid_response = (RecyclerView) findViewById(R.id.rv_rapid_response);
        tv_date = (TextView) findViewById(R.id.tv_date);
        /*
        tv_relax = (TextView) findViewById(R.id.tv_relax);*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        tv_relax.setOnTouchListener(this);

        String texm_text = "<font color=#FFFFFF><u>" + getString(R.string.touch_bottle) + "</u></font>";
        tv_date.setText(Html.fromHtml(texm_text));
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(RapidResponse.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                makeJsonRapidResponse();
            } else {
                cc.showSnackbar(rv_rapid_response, getString(R.string.no_internet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeJsonRapidResponse() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_quick_remedy,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_get_quick_remedy", response);
                        jsonParseRapidResponse(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(rv_rapid_response, getString(R.string.ws_error));
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

    private void jsonParseRapidResponse(String response) {
        try {
            upliftMusicList = new ArrayList<Pausetask>();
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {



//                cc.savePrefString("resource_journal_file_path", jsonObject.getString("pausetaskaudio"));
//                Log.e("Res_Journal_file_path", jsonObject.getString("pausetaskaudio")+"");
                JSONArray jsonMyDeadsArray = jsonObject.getJSONArray("remedy");

                for (int i = 0; i < jsonMyDeadsArray.length(); i++) {
                    JSONObject jsonObject1 = jsonMyDeadsArray.getJSONObject(i);

                    Pausetask pk = new Pausetask();
                    pk.setTask_name(jsonObject1.getString("text"));
                    pk.setTask_id(jsonObject1.getString("prescription_id"));
//                    pk.setAudio_file_path(jsonObject1.getString("journal_audio"));
//                    pk.setFromMydeads(true);

                    upliftMusicList.add(pk);

                }
                if (!upliftMusicList.isEmpty()) {


                    rv_rapid_response.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(RapidResponse.this, LinearLayoutManager.VERTICAL, false);
                    rv_rapid_response.setLayoutManager(mLayoutManager);
                    RapidResponseAdapter mAdapter = new RapidResponseAdapter(RapidResponse.this, upliftMusicList);
                    rv_rapid_response.setAdapter(mAdapter);

                }

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
