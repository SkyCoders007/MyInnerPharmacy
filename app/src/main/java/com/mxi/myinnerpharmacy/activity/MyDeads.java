package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.ResourceJournalAdapter;
import com.mxi.myinnerpharmacy.model.ResourceJournalItem;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by android on 19/1/17.
 */
public class MyDeads extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recycler_view;
    LinearLayoutManager mLayoutManager;
    ProgressDialog pDialog;
    ArrayList<ResourceJournalItem> resourceJournaList;
    Button fab;
    CommanClass cc;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_journal);
        cc = new CommanClass(MyDeads.this);

        fab = (Button) findViewById(R.id.fab);
        recycler_view = (RecyclerView) findViewById(R.id.rv_resource_journal);
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

        fab.setOnClickListener(this);

    }

    private void makeJsonMyDeads() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_my_dead,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_get_my_deads", response);
                        jsonParseMyDeads(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
                pDialog.dismiss();
                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
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


    private void jsonParseMyDeads(String response) {
        try {
            resourceJournaList = new ArrayList<ResourceJournalItem>();
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {


//                cc.savePrefString("resource_journal_file_path", jsonObject.getString("pausetaskaudio"));
//                Log.e("Res_Journal_file_path", jsonObject.getString("pausetaskaudio")+"");
                JSONArray jsonMyDeadsArray = jsonObject.getJSONArray("my_dead");

                for (int i = 0; i < jsonMyDeadsArray.length(); i++) {
                    JSONObject jsonObject1 = jsonMyDeadsArray.getJSONObject(i);

                    ResourceJournalItem pk = new ResourceJournalItem();
                    pk.setTitle(jsonObject1.getString("my_dead_title"));
                    pk.setDescription(jsonObject1.getString("my_dead_text"));
//                    pk.setAudio_file_path(jsonObject1.getString("journal_audio"));
                    pk.setFromMydeads(true);

                    resourceJournaList.add(pk);

                }
                if (!resourceJournaList.isEmpty()) {


                    recycler_view.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(MyDeads.this, LinearLayoutManager.VERTICAL, false);
                    recycler_view.setLayoutManager(mLayoutManager);
                    ResourceJournalAdapter mAdapter = new ResourceJournalAdapter(MyDeads.this, resourceJournaList);
                    recycler_view.setAdapter(mAdapter);

                }

            } else {

                cc.showToast(jsonObject.getString("message"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent =new Intent(MyDeads.this, AddResourceJournal.class);
                intent.putExtra("from","MyDeads");
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(MyDeads.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                makeJsonMyDeads();
            } else {
                cc.showSnackbar(recycler_view, getString(R.string.no_internet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
