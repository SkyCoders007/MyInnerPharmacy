package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.UpliftingPlaylistAdapter;
import com.mxi.myinnerpharmacy.model.uplifting_playlist;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UpliftingPlaylist extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recycler_view;
    LinearLayoutManager mLayoutManager;
    ProgressDialog pDialog;
    ArrayList<uplifting_playlist> upliftPlaylist;
    CommanClass cc;
    Toolbar toolbar;
    TextView tv_text;
    Button fab_playlist;
    String playlist_id, playlist_title;

    private Button b1, b2, b3, b4;
    private MediaPlayer mediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1, tx2, tx3;
    public static int oneTimeOnly = 0;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uplifting_playlist);

        cc = new CommanClass(this);
        recycler_view = (RecyclerView) findViewById(R.id.rv_music);
        tv_text = (TextView) findViewById(R.id.tv_text);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab_playlist = (Button) findViewById(R.id.fab_playlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        tx1 = (TextView) findViewById(R.id.textView2);
        tx2 = (TextView) findViewById(R.id.textView3);
        tx3 = (TextView) findViewById(R.id.textView4);
        seekbar = (SeekBar) findViewById(R.id.seekBar);

        b2.setEnabled(true);
        b3.setEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        playlist_id = getIntent().getStringExtra("playlist_id");
        playlist_title = getIntent().getStringExtra("playlist_title");
        tv_text.setText(playlist_title);
        fab_playlist.setOnClickListener(this);

        recycler_view.addOnItemTouchListener(new RecyclerTouchListener(UpliftingPlaylist.this, recycler_view, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                tx3.setText(upliftPlaylist.get(position).getAudio_title());
                i = position;
                new AsyncMusic(upliftPlaylist.get(position).getAudio_file()).execute();

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
                pDialog = new ProgressDialog(UpliftingPlaylist.this);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                makeJsonUpliftingPlaylist(playlist_id);
            } else {
                cc.showSnackbar(recycler_view, getString(R.string.no_internet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_playlist:

                startActivity(new Intent(UpliftingPlaylist.this, AddUpliftingPlaylist.class).putExtra("playlist_id", playlist_id));
                break;
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


    private void makeJsonUpliftingPlaylist(final String playlist_id) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_playlist_audio,
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

                params.put("playlist_id", playlist_id);
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
            upliftPlaylist = new ArrayList<uplifting_playlist>();
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {
                String upload_path = jsonObject.getString("upload_path");
                JSONArray playlist = jsonObject.getJSONArray("playlist");
                for (int i = 0; i < playlist.length(); i++) {
                    JSONObject jsonObject1 = playlist.getJSONObject(i);

                    uplifting_playlist pk = new uplifting_playlist();
                    pk.setAudio_id(jsonObject1.getString("audio_id"));
                    pk.setAudio_title(jsonObject1.getString("audio_title"));
                    pk.setAudio_file(upload_path + jsonObject1.getString("audio_file"));

                    upliftPlaylist.add(pk);

                }
                if (!upliftPlaylist.isEmpty()) {

                    recycler_view.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(UpliftingPlaylist.this, LinearLayoutManager.VERTICAL, false);
                    recycler_view.setLayoutManager(mLayoutManager);
                    UpliftingPlaylistAdapter mAdapter = new UpliftingPlaylistAdapter(UpliftingPlaylist.this, upliftPlaylist);
                    recycler_view.setAdapter(mAdapter);

                }

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class AsyncMusic extends AsyncTask<String, Void, Void> {
        String url_music;

        public AsyncMusic(String path) {
            this.url_music = path;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(url_music);
                // Log.e("songs url", url_music);

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
            // Log.e("POST	", i + "");
            i++;
            mediaPlayer.start();
            finalTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();

            if (oneTimeOnly == 0) {
                seekbar.setMax((int) finalTime);
                oneTimeOnly = 1;
            }
            tx2.setText(String.format("%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    finalTime)))
            );
            tx1.setText(String.format("%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                    startTime)))
            );
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(UpdateSongTime, 100);

            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                        mediaPlayer.start();

                        finalTime = mediaPlayer.getDuration();
                        startTime = mediaPlayer.getCurrentPosition();

                        if (oneTimeOnly == 0) {
                            seekbar.setMax((int) finalTime);
                            oneTimeOnly = 1;
                        }

                        tx2.setText(String.format("%d : %d",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                finalTime)))
                        );

                        tx1.setText(String.format("%d : %d",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                startTime)))
                        );

                        seekbar.setProgress((int) startTime);
                        myHandler.postDelayed(UpdateSongTime, 100);
                        b2.setEnabled(true);
                        b3.setEnabled(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                        mediaPlayer.pause();
                        b2.setEnabled(false);
                        b3.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int temp = (int) startTime;

                        if ((temp + forwardTime) <= finalTime) {
                            startTime = startTime + forwardTime;
                            mediaPlayer.seekTo((int) startTime);
                            Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            b4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int temp = (int) startTime;

                        if ((temp - backwardTime) > 0) {
                            startTime = startTime - backwardTime;
                            mediaPlayer.seekTo((int) startTime);
                            Toast.makeText(getApplicationContext(), "You have Jumped backward 5seconds", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    try {
                        if (i < upliftPlaylist.size()) {
                            // Log.e("next_play", i + "");
                            tx3.setText(upliftPlaylist.get(i).getAudio_title());
                            new AsyncMusic(upliftPlaylist.get(i).getAudio_file()).execute();

                        } else {
                            i = i - 1;
                            if (i == 0) {

                            } else {
                                i--;
                            }
                            // Log.e("next_privious", i + "");
                            tx3.setText(upliftPlaylist.get(i).getAudio_title());
                            new AsyncMusic(upliftPlaylist.get(i).getAudio_file()).execute();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            try {
                startTime = mediaPlayer.getCurrentPosition();
            } catch (Exception e) {
                e.printStackTrace();
                // Log.e("Crash", e.toString());
                return;
            }

            tx1.setText(String.format("%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
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


}
