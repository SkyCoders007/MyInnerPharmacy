package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.AndroidMultiPartEntity;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class AddUpliftingPlaylist extends AppCompatActivity implements View.OnTouchListener {

    Toolbar toolbar;
    TextView tv_add_audio_file, tv_upload, tv_add_pause_text;
    EditText et_title;
    CommanClass cc;
    ProgressDialog pDialog;
    LinearLayout ll_linear;
    private final int SELECT_MUSIC = 2;
    long totalSize = 0;
    String selectedMusicPath = "";
    String playlist_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_uplifting_music);
        cc = new CommanClass(AddUpliftingPlaylist.this);

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
        playlist_id = getIntent().getStringExtra("playlist_id");
        et_title = (EditText) findViewById(R.id.et_title);
        tv_add_audio_file = (TextView) findViewById(R.id.tv_add_audio_file);
        tv_upload = (TextView) findViewById(R.id.tv_upload);
        tv_add_pause_text = (TextView) findViewById(R.id.tv_add_pause_text);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);

        tv_add_audio_file.setOnTouchListener(this);
        tv_upload.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

            case R.id.tv_add_audio_file:

                selectFile("audio");
                break;
            case R.id.tv_upload:

                String title = et_title.getText().toString().trim();
                if (!cc.isConnectingToInternet()) {

                } else if (title.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.enter_pause_title));
                } else if (selectedMusicPath.equals("")) {

                    cc.showSnackbar(ll_linear, getString(R.string.enter_pause_add_file));
                } else {

                    new UploadFileToServer(title).execute();
                }
                break;
        }

        return false;
    }

    private void selectFile(String select) {
        if (select.equals("audio")) {

            try {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECT_MUSIC);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == AddUpliftingPlaylist.this.RESULT_OK) {
            if (requestCode == SELECT_MUSIC) {

                Uri selectedMusicUri = data.getData();
                try {
                    selectedMusicPath = getPath(selectedMusicUri);

                    tv_add_audio_file.setText(selectedMusicPath);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Log.e("Selected Music File", selectedMusicPath);
            }
        }
    }

    public String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = AddUpliftingPlaylist.this.getContentResolver().query(uri,
                        projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        HttpClient httpclient;
        HttpPost httppost;
        String title;

        public UploadFileToServer(String title) {
            this.title = title;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddUpliftingPlaylist.this);
            pDialog.show();
            pDialog.setCancelable(false);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage(String.valueOf("Loading..." + progress[0])
                    + " %");

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(URL.Url_upload_playlist_audio);
            httppost.addHeader("mip-token", cc.loadPrefString("mip-token"));

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                if (selectedMusicPath != null || !selectedMusicPath.equals("")) {

                    File sourceMusicFile = new File(selectedMusicPath);
                    entity.addPart("audio_file", new FileBody(sourceMusicFile));
                }
                entity.addPart("audio_title", new StringBody(title));
                entity.addPart("playlist_id", new StringBody(playlist_id));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    return responseString;
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Register: result", "Response from server: " + result);
            try {
                pDialog.dismiss();
                JSONObject jObject = new JSONObject(result);
                if (jObject.getString("status").equals("200")) {

                    cc.showToast(jObject.getString("message"));
                    finish();

                } else {
                    cc.showToast(jObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }

    }

}
