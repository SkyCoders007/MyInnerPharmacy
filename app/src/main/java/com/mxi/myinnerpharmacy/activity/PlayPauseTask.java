package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.ImageLoadedCallback;
import com.mxi.myinnerpharmacy.network.URL;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayPauseTask extends AppCompatActivity {

    ImageView iv_music_image;
    Button fab_mute, fab_back;
    CommanClass cc;
    ProgressDialog pDialog;
    LinearLayout ll_linear;
    MediaPlayer mediaPlayer;
    ProgressBar progressBar;
    // Boolean mboolean;
    // SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pause_task);
        cc = new CommanClass(PlayPauseTask.this);

        iv_music_image = (ImageView) findViewById(R.id.iv_music_image);
        fab_mute = (Button) findViewById(R.id.fab_mute);
        fab_back = (Button) findViewById(R.id.fab_back);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mediaPlayer = new MediaPlayer();


        String task_id = getIntent().getStringExtra("task_id");

        if (cc.isConnectingToInternet()) {
           /* pDialog = new ProgressDialog(PlayPauseTask.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();*/
            progressBar.setVisibility(View.VISIBLE);
            makeJsonPlayPause(task_id);
        } else {
            cc.showSnackbar(ll_linear, getString(R.string.no_internet));
        }

        try {
            Log.e("songstype", cc.loadPrefBoolean("Mute") + "");
            if (!cc.loadPrefBoolean("Mute")) {
                mediaPlayer.setVolume(0, 0);
                fab_mute.setBackgroundResource(R.mipmap.mute);
                cc.savePrefBoolean("isMute", false);
                Log.e("1_mute", cc.loadPrefBoolean("isMute") + "");
            } else {
                mediaPlayer.setVolume(1, 1);
                fab_mute.setBackgroundResource(R.mipmap.paushtaskview);
                cc.savePrefBoolean("isMute", true);
                Log.e("1_Unmute", cc.loadPrefBoolean("isMute") + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fab_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (cc.loadPrefBoolean("isMute")) {
                        mediaPlayer.setVolume(0, 0);
                        fab_mute.setBackgroundResource(R.mipmap.mute);
                        cc.savePrefBoolean("isMute", false);
                        Log.e("mute", cc.loadPrefBoolean("isMute") + "");
                    } else {
                        mediaPlayer.setVolume(1, 1);
                        fab_mute.setBackgroundResource(R.mipmap.paushtaskview);
                        cc.savePrefBoolean("isMute", true);
                        Log.e("Unmute", cc.loadPrefBoolean("isMute") + "");

                    }

                    cc.savePrefBoolean("Mute", cc.loadPrefBoolean("isMute"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // cc.showToast("destroy");
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;

            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

    }

   /* @Override
    public void onPause() {
        super.onPause();
        // cc.showToast("destroy");
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }*/

    // }

    private void makeJsonPlayPause(final String task_id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_pause_task_play,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_pause_task_play", response);
                        jsonParsePlay(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                cc.showSnackbar(ll_linear, getString(R.string.ws_error));
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

    private void jsonParsePlay(String response) {
        try {

            JSONObject jsonObject = new JSONObject(response);
            progressBar.setVisibility(View.GONE);
            if (jsonObject.getString("status").equals("200")) {
                String pausetaskimg = jsonObject.getString("pausetaskimg");
                String pausetaskaudio = jsonObject.getString("pausetaskaudio");

               /* JSONArray pausetask = jsonObject.getJSONArray("pausetask");
                JSONObject data = pausetask.getJSONObject(0);
                String task_id = data.getString("task_id");
                String task_name = data.getString("task_name");
                String image = data.getString("image");
                String music = data.getString("music");*/
               /* Glide.with(PlayPauseTask.this).
                        load(pausetaskimg).
                        into(iv_music_image);*/
                Picasso.with(PlayPauseTask.this)
                        .load(pausetaskimg)
                        .into(iv_music_image, new ImageLoadedCallback(progressBar) {
                            @Override
                            public void onSuccess() {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);

                                }
                            }
                        });

                new AsyncMusic(pausetaskaudio).execute();

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class AsyncMusic extends AsyncTask<String, Void, Void> {
        String pausetaskaudio;

        public AsyncMusic(String pausetaskaudio) {
            this.pausetaskaudio = pausetaskaudio;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            // mediaPlayer = new MediaPlayer();
           /* if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {

            }*/
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(pausetaskaudio);

                Log.e("songs url", pausetaskaudio);

            } catch (IllegalArgumentException e) {
                // Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                // Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                // Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IllegalStateException e) {
                //Toast.makeText(mContext, "You might not set the URI correctly!", Toast.LENGTH_SHORT).show();
                //cc.showToast("You might not set the URI correctly!");
            } catch (IOException e) {
                // Toast.makeText(mContext, "You might not set the URI correctly!", Toast.LENGTH_SHORT).show();
                //cc.showToast("You might not set the URI correctly!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                mediaPlayer.start();
                SharedPreferences settings = getSharedPreferences("Mute", 1);
                Boolean mboolean = settings.getBoolean("FIRST_Mute", false);

                if (!mboolean) {
                    settings = getSharedPreferences("Mute", 1);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("FIRST_Mute", true);
                    editor.commit();
                    cc.savePrefBoolean("isMute", mboolean);

                }

                //  Log.e("response", mboolean + "");

               /* if (cc.loadPrefBoolean("isMute")) {
                    cc.savePrefBoolean("isMute", false);
                } else {
                    cc.savePrefBoolean("isMute", true);

                }*/

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        new AsyncMusic(pausetaskaudio).execute();
                    }
                });
            } catch (Exception e) {
                // e.printStackTrace();
            }

        }
    }
}
