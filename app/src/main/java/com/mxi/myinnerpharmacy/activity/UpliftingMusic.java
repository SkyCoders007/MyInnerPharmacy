package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.PauseTaskAdapter;
import com.mxi.myinnerpharmacy.model.Pausetask;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpliftingMusic extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recycler_view;
    LinearLayoutManager mLayoutManager;
    ProgressDialog pDialog;
    ArrayList<Pausetask> upliftMusicList;
    Button fab;
    CommanClass cc;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uplifting_music);

        cc = new CommanClass(this);
        recycler_view = (RecyclerView) findViewById(R.id.rv_music);
        fab = (Button) findViewById(R.id.fab_music);

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

        recycler_view.addOnItemTouchListener(new RecyclerTouchListener(UpliftingMusic.this, recycler_view, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                // displayView(position);
                startActivity(new Intent(UpliftingMusic.this, UpliftingPlaylist.class).putExtra("playlist_id", upliftMusicList.get(position).getTask_id()).
                        putExtra("playlist_title", upliftMusicList.get(position).getTask_name()));

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(UpliftingMusic.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                makeJsonUpliftingMusic();
            } else {
                cc.showSnackbar(recycler_view, getString(R.string.no_internet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeJsonUpliftingMusic() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_playlist,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_get_playlist", response);
                        jsonParseUpliftingMusic(response);
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

    private void jsonParseUpliftingMusic(String response) {
        try {
            upliftMusicList = new ArrayList<Pausetask>();
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray playlist = jsonObject.getJSONArray("playlist");
                for (int i = 0; i < playlist.length(); i++) {
                    JSONObject jsonObject1 = playlist.getJSONObject(i);

                    Pausetask pk = new Pausetask();
                    pk.setTask_name(jsonObject1.getString("playlist_title"));
                    pk.setTask_id(jsonObject1.getString("playlist_id"));

                    upliftMusicList.add(pk);

                }
                if (!upliftMusicList.isEmpty()) {

                    recycler_view.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(UpliftingMusic.this, LinearLayoutManager.VERTICAL, false);
                    recycler_view.setLayoutManager(mLayoutManager);
                    PauseTaskAdapter mAdapter = new PauseTaskAdapter(UpliftingMusic.this, upliftMusicList);
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
        if (v.getId() == R.id.fab_music) {

            // startActivity(new Intent(UpliftingMusic.this, AddUpliftingMusic.class));
            AddPlaylist();

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

    private void AddPlaylist() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UpliftingMusic.this);
        LayoutInflater inflater = (LayoutInflater) UpliftingMusic.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//getLayoutInflater();
        builder.setCancelable(false);
        View dialogView = inflater.inflate(R.layout.add_uplifting_playlist_name, null);

        builder.setView(dialogView);
        final AlertDialog alert = builder.create();
        WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();

        wmlp.gravity = Gravity.CENTER | Gravity.CENTER;

        alert.setCanceledOnTouchOutside(true);

        final EditText et_name = (EditText) dialogView.findViewById(R.id.et_name);
        TextView btn_cancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        TextView btn_ok = (TextView) dialogView.findViewById(R.id.btn_ok);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                if (name.equals("")) {
                    cc.showToast("Please enter song title name");
                } else {

                    try {
                        if (cc.isConnectingToInternet()) {
                            pDialog = new ProgressDialog(UpliftingMusic.this);
                            pDialog.setMessage("Please wait...");
                            pDialog.show();
                            makeJsonCreatePlayLIst(name);
                            alert.dismiss();
                        } else {
                            cc.showSnackbar(recycler_view, getString(R.string.no_internet));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });
        alert.show();

    }


    private void makeJsonCreatePlayLIst(final String name) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_create_playlist,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_create_playlist", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {

                                cc.showToast(jsonObject.getString("message"));

                                makeJsonUpliftingMusic();

                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();

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

                params.put("playlist_title", name);
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
