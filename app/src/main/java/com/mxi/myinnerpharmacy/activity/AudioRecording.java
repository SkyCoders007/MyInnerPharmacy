package com.mxi.myinnerpharmacy.activity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.io.IOException;

public class AudioRecording extends AppCompatActivity implements View.OnClickListener {
    Button btn_start, btn_play, btn_stop, btn_stop_playing;
    TextView tv_upload, tv_recording_time;
    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    // private String outputFile = null;

    Toolbar toolbar;
    private long startHTime = 0L;
    private Handler customHandler;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    CommanClass cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recording);

        cc = new CommanClass(AudioRecording.this);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop_playing = (Button) findViewById(R.id.btn_stop_playing);
        btn_play = (Button) findViewById(R.id.btn_play);
        tv_upload = (TextView) findViewById(R.id.tv_upload);
        tv_recording_time = (TextView) findViewById(R.id.tv_recording_time);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        tv_upload.setOnClickListener(this);
        btn_stop_playing.setOnClickListener(this);

        customHandler = new Handler();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    customHandler.removeCallbacks(updateTimerThread);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cc.savePrefBoolean("audioReco", true);
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:

                AddResourceJournal.outputFile = Environment.getExternalStorageDirectory().
                        getAbsolutePath() + "/Resource_Journal" + ResourceJournal.resourceJournaList.size() + ".mp3";

                myRecorder = new MediaRecorder();
                myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                myRecorder.setOutputFile(AddResourceJournal.outputFile);
                //  myRecorder.setOnInfoListener(infoListener);
                Log.e("audio_recording_path", AddResourceJournal.outputFile);
                try {
                    // myRecorder.setMaxDuration(300000);
                    myRecorder.prepare();
                    myRecorder.start();
                } catch (IllegalStateException e) {
                    // start:it is called before prepare()
                    // prepare: it is called after start() or before setOutputFormat()
                    e.printStackTrace();
                } catch (IOException e) {
                    // prepare() fails
                    e.printStackTrace();
                }
                // tv_recording_time.setText(myRecorder.get);
                btn_start.setEnabled(false);
                btn_stop.setEnabled(true);
                btn_play.setEnabled(false);
                btn_start.setBackgroundResource(R.mipmap.ic_reco_play_disable);
                btn_stop.setBackgroundResource(R.mipmap.ic_stop_recording);
                btn_play.setBackgroundResource(R.mipmap.ic_disablestop);

                startHTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);

               /* Toast.makeText(getApplicationContext(), "Start recording...",
                        Toast.LENGTH_SHORT).show();*/

                break;
            case R.id.btn_play:

                try {
                    myPlayer = new MediaPlayer();
                    myPlayer.setDataSource(AddResourceJournal.outputFile);
                    myPlayer.prepare();
                    myPlayer.start();

                    btn_stop.setEnabled(false);
                    btn_start.setEnabled(false);
                    btn_play.setEnabled(false);
                    btn_stop_playing.setEnabled(true);

                    btn_start.setBackgroundResource(R.mipmap.ic_reco_play_disable);
                    btn_stop.setBackgroundResource(R.mipmap.ic_disablestop);
                    btn_play.setBackgroundResource(R.mipmap.ic_recordplay_disable);
                    btn_stop_playing.setBackgroundResource(R.mipmap.ic_pausebutton);
                    //text.setText("Recording Point: Playing");

                   /* Toast.makeText(getApplicationContext(), "Start play the recording...",
                            Toast.LENGTH_SHORT).show();*/
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;
            case R.id.btn_stop:

                try {
                    // timeSwapBuff += timeInMilliseconds;
                    timeSwapBuff = 0L;
                    customHandler.removeCallbacks(updateTimerThread);
                    myRecorder.stop();
                    myRecorder.release();
                    myRecorder = null;

                    btn_stop.setEnabled(false);
                    btn_play.setEnabled(true);
                    btn_start.setEnabled(false);

                    btn_start.setBackgroundResource(R.mipmap.ic_reco_play_disable);
                    btn_stop.setBackgroundResource(R.mipmap.ic_disablestop);
                    btn_play.setBackgroundResource(R.mipmap.ic_recordplay);
                    // text.setText("Recording Point: Stop recording");

                   /* Toast.makeText(getApplicationContext(), "Stop recording...",
                            Toast.LENGTH_SHORT).show();*/
                } catch (IllegalStateException e) {
                    //  it is called before start()
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    // no valid audio/video data has been received
                    e.printStackTrace();
                }
                //btn_play.setEnabled(false);
                break;
            case R.id.btn_stop_playing:

                try {
                    if (myPlayer != null) {
                        myPlayer.stop();
                        myPlayer.release();
                        myPlayer = null;
                        btn_stop.setEnabled(false);
                        btn_start.setEnabled(true);
                        btn_play.setEnabled(false);
                        btn_stop_playing.setEnabled(false);

                        btn_start.setBackgroundResource(R.mipmap.ic_recordstart);
                        btn_stop.setBackgroundResource(R.mipmap.ic_disablestop);
                        btn_play.setBackgroundResource(R.mipmap.ic_recordplay_disable);
                        btn_stop_playing.setBackgroundResource(R.mipmap.ic_play_pause_disa);
                       /* Toast.makeText(getApplicationContext(), "Stop playing the recording...",
                                Toast.LENGTH_SHORT).show();*/
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.tv_upload:
                try {
                    customHandler.removeCallbacks(updateTimerThread);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cc.savePrefBoolean("audioReco", true);
                onBackPressed();
                break;
        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            if (tv_recording_time != null)
                tv_recording_time.setText("" + String.format("%02d", mins) + " : "
                        + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };


    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            // AppLog.logString("Warning: " + what + ", " + extra);
            Log.e("recording", extra + "");
            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                Log.e("VIDEOCAPTURE", "Maximum Duration Reached");
                mr.stop();
                try {
                    btn_stop.setEnabled(false);
                    btn_play.setEnabled(true);
                    btn_start.setEnabled(false);
                    //btn_play.setEnabled(false);

                    Toast.makeText(AudioRecording.this, "Recording Completed",
                            Toast.LENGTH_LONG).show();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    };
   /*public void onInfo(MediaRecorder mr, int what, int extra) {
        Log.e("recording", extra + "");
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            Log.e("VIDEOCAPTURE", "Maximum Duration Reached");
            mr.stop();
            try {
                 count.cancel();
                btn_stop.setEnabled(false);
                btn_play.setEnabled(true);
                btn_start.setEnabled(false);
                //btn_play.setEnabled(false);

                Toast.makeText(AudioRecording.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }*/

}
