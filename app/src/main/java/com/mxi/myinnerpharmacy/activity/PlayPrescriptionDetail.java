package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class PlayPrescriptionDetail extends AppCompatActivity {

    ProgressDialog pDialog;
    VideoView videoview;
    // String main_video_url;
    String video_url, video_name;
    String VideoURL;

    LinearLayout ll_video, ll_audio, ll_text;
    TextView tv_title, tv_description;
    CommanClass cc;
    Toolbar toolbar;

    private Button b1, b2, b3, b4;
    private ImageView iv;
    private MediaPlayer mediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1, tx2, tx3;
    public static int oneTimeOnly = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_prescription_detail);
        cc = new CommanClass(this);
        ll_audio = (LinearLayout) findViewById(R.id.ll_audio);
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        ll_text = (LinearLayout) findViewById(R.id.ll_text);
        tv_title = (TextView) findViewById(R.id.tv_text_title);
        tv_description = (TextView) findViewById(R.id.tv_text_description);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getStringExtra("media_type").equals("Audio")) {
            //main_video_url = cc.loadPrefString("Server_path_audio");
            ll_video.setVisibility(View.GONE);
            ll_text.setVisibility(View.GONE);
            ll_audio.setVisibility(View.VISIBLE);

        } else if (getIntent().getStringExtra("media_type").equals("Video")) {
            // main_video_url = cc.loadPrefString("Server_path_video");
            ll_video.setVisibility(View.VISIBLE);
            ll_audio.setVisibility(View.GONE);
            ll_text.setVisibility(View.GONE);
        } else if (getIntent().getStringExtra("media_type").equals("Text")) {
            // main_video_url = cc.loadPrefString("Server_path_text");
            ll_video.setVisibility(View.GONE);
            ll_audio.setVisibility(View.GONE);
            ll_text.setVisibility(View.VISIBLE);
        }

        //  Log.e("main_video_url", main_video_url);

        video_name = getIntent().getStringExtra("media_name");
        video_url = getIntent().getStringExtra("media_file");
//        String VideoURL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
        VideoURL = video_url;
//        String AudioURL = main_audio_url + audio_url;
        Log.e("VideoURL ", VideoURL);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    myHandler = null;
                    mediaPlayer.release();
                    mediaPlayer = null;
                    UpdateSongTime = null;
                }
                onBackPressed();
            }
        });

        videoview = (VideoView) findViewById(R.id.vv_play);
        // Execute StreamVideo AsyncTask

        // Create a progressbar
        pDialog = new ProgressDialog(PlayPrescriptionDetail.this);
        // Set progressbar title
        pDialog.setTitle(video_name);
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();


        if (getIntent().getStringExtra("media_type").equals("Audio")) {
            playAudio();
        } else if (getIntent().getStringExtra("media_type").equals("Video")) {
            playVideo();
        } else if (getIntent().getStringExtra("media_type").equals("Text")) {
            setUpText();
        }


    }

    private void setUpText() {
        new GetData().execute();
    }

    private void playVideo() {

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    PlayPrescriptionDetail.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });

    }

    private void playAudio() {
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        iv = (ImageView) findViewById(R.id.imageView);

        tx1 = (TextView) findViewById(R.id.textView2);
        tx2 = (TextView) findViewById(R.id.textView3);
        tx3 = (TextView) findViewById(R.id.textView4);
        tx3.setText("Song.mp3");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Uri audio = Uri.parse(VideoURL);
        try {
            mediaPlayer.setDataSource(this, audio);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        b2.setEnabled(false);


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pDialog.cancel();
//                mediaPlayer.start();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                tx2.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );

                tx1.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );

                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
                b2.setEnabled(true);
                b3.setEnabled(false);
            }


        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                b2.setEnabled(false);
                b3.setEnabled(true);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(), "You have Jumped backward 5seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            try {
                startTime = mediaPlayer.getCurrentPosition();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Crash", e.toString());
                return;
            }

            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };


    private class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(VideoURL);
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();

                if (code == 200) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line = "";

                        while ((line = bufferedReader.readLine()) != null)
                            result += line;
                    }
                    in.close();
                }

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.cancel();
            tv_description.setText(result);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            myHandler = null;
            mediaPlayer.release();
            mediaPlayer = null;
            UpdateSongTime = null;
        }

    }

   /* @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();
        if (mediaPlayer != null) {
            myHandler = null;
            mediaPlayer.release();
            mediaPlayer = null;
            UpdateSongTime = null;
        }
        onBackPressed();
    }*/
}
