package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.activity.PlayPauseTask;
import com.mxi.myinnerpharmacy.adapter.PauseTaskAdapter;
import com.mxi.myinnerpharmacy.model.Pausetask;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.ImageLoadedCallback;
import com.mxi.myinnerpharmacy.network.URL;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PauseTask extends AppCompatActivity {

    RecyclerView recycler_view;

    TextView tv_pause_choose_text;
    CommanClass cc;
    ProgressDialog pDialog;
    ArrayList<Pausetask> pausetasklist;
    RecyclerView.LayoutManager mLayoutManager;
    private Button fab;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pause_task);
        cc = new CommanClass(PauseTask.this);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        tv_pause_choose_text = (TextView) findViewById(R.id.tv_pause_choose_text);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (Button) findViewById(R.id.fab);
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

        String texm_text = "<u>" + getString(R.string.pause_choose) + "</u>";
        tv_pause_choose_text.setText(Html.fromHtml(texm_text));

        recycler_view.addOnItemTouchListener(new RecyclerTouchListener(PauseTask.this, recycler_view, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                // displayView(position);
                if (!pausetasklist.get(position).getVideo().equals("yes")) {
                    startActivity(new Intent(PauseTask.this, PlayPauseTask.class).putExtra(("task_id"), pausetasklist.get(position).getTask_id()));
                } else {
                    pDialog.show();
                    makeJsonPlayVideo(pausetasklist.get(position).getTask_id(), pausetasklist.get(position).getTask_name());
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PauseTask.this, AddPauseTask.class));
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

        try {
            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(PauseTask.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                makeJsonPauseTask();
            } else {
                cc.showSnackbar(recycler_view, getString(R.string.no_internet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeJsonPauseTask() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_pause_task,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_calibration_state", response);
                        jsonParsePauseTask(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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

    private void jsonParsePauseTask(String response) {
        try {
            pausetasklist = new ArrayList<Pausetask>();
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray pausetask = jsonObject.getJSONArray("pausetask");

                for (int i = 0; i < pausetask.length(); i++) {
                    JSONObject jsonObject1 = pausetask.getJSONObject(i);

                    Pausetask pk = new Pausetask();
                    pk.setTask_id(jsonObject1.getString("task_id"));
                    pk.setTask_name(jsonObject1.getString("task_name"));
                    pk.setVideo(jsonObject1.getString("video"));

                    pausetasklist.add(pk);

                }
                if (!pausetasklist.isEmpty()) {

                    recycler_view.setHasFixedSize(true);
                    PauseTaskAdapter mAdapter = new PauseTaskAdapter(PauseTask.this, pausetasklist);
                    recycler_view.setAdapter(mAdapter);
                    mLayoutManager = new LinearLayoutManager(PauseTask.this, LinearLayoutManager.VERTICAL, false);
                    recycler_view.setLayoutManager(mLayoutManager);

                }

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private void makeJsonPlayVideo(final String task_id, final String video_name) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_pause_task_play,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Response: video_pause", response);
                        // jsonParsePlay(response);
                        try {
                            pDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {
                                String pausetaskvideo = jsonObject.getString("pausetaskvideo");
                                Intent intent = new Intent(PauseTask.this, PlayPrescriptionDetail.class);
                                intent.putExtra("media_name", video_name);
                                intent.putExtra("media_type", "Video");
                                intent.putExtra("media_file", pausetaskvideo);
                                startActivity(intent);
                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // progressBar.setVisibility(View.GONE);
                pDialog.dismiss();
                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("task_id", task_id);
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


}
