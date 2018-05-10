package com.mxi.myinnerpharmacy.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.io.IOException;

/**
 * Created by android on 19/1/17.
 */
public class ResourceJournalDetail extends AppCompatActivity implements View.OnClickListener {

    TextView tv_screen_title, tv_title, tv_description, tv_audio, tv_play, tv_pause;
    LinearLayout ll_audio;
    Toolbar toolbar;

    boolean fromMyDeads = false;

    String audio = "", audio_name = "";
    String title, description, from;
    MediaPlayer mediaPlayer;
    CommanClass cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_journal_detail);
        cc = new CommanClass(ResourceJournalDetail.this);
        mediaPlayer = new MediaPlayer();
        init();

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
                    // e.printStackTrace();
                }
                onBackPressed();
            }
        });

        title = getIntent().getStringExtra("res_title");
        description = getIntent().getStringExtra("res_descr");
        from = getIntent().getStringExtra("res_from");

        if (from.equals("Mydeads")) fromMyDeads = true;

        if (!fromMyDeads) {
            audio = getIntent().getStringExtra("res_audio");
            audio_name = getIntent().getStringExtra("res_audio_name");
            tv_screen_title.setText(R.string.rj_resource_journal_detail);
            if (audio_name.equals("")) {
                ll_audio.setVisibility(View.GONE);
            } else {
                ll_audio.setVisibility(View.VISIBLE);
            }
        } else {
            ll_audio.setVisibility(View.GONE);
            tv_screen_title.setText(R.string.md_resource_journal_detail);
        }
        tv_title.setText(title);
        tv_description.setText(description);
        tv_audio.setText(audio_name);

    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_audio = (TextView) findViewById(R.id.tv_audio_name_resource_journal_detail);
        tv_screen_title = (TextView) findViewById(R.id.tv_screen_title_resource_journal_detail);
        tv_title = (TextView) findViewById(R.id.tv_title_resource_journal_detail);
        tv_description = (TextView) findViewById(R.id.tv_description_resource_journal_detail);
        tv_play = (TextView) findViewById(R.id.tv_play_resource_journal_detail);
        tv_pause = (TextView) findViewById(R.id.tv_pause_resource_journal_detail);
        ll_audio = (LinearLayout) findViewById(R.id.ll_audio_resource_journal_detail);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_play.setOnClickListener(this);
        tv_pause.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_play_resource_journal_detail:
                // if (mediaPlayer == null) {
                new AsyncMusic().execute();
                // }
                break;
            case R.id.tv_pause_resource_journal_detail:
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
                break;
        }
    }

    public class AsyncMusic extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

             mediaPlayer = new MediaPlayer();
           /* if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {

            }*/
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                // String url = cc.loadPrefString("resource_journal_file_path") + audio_name;
                Log.e("songs_url", audio);
                mediaPlayer.setDataSource(audio);

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

            } catch (Exception e) {
                // e.printStackTrace();
            }

        }
    }
}
