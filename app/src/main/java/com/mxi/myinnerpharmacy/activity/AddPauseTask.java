package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class AddPauseTask extends AppCompatActivity implements View.OnTouchListener {

    TextView tv_add_image, tv_add_audio_file, tv_upload, tv_add_pause_text, tv_add_video_file;
    EditText et_title;
    CommanClass cc;
    ProgressDialog pDialog;
    LinearLayout ll_linear;
    private static final int SELECT_PICTURE = 1;
    private final int SELECT_MUSIC = 2;
    private final int SELECT_VIDEO = 3;
    private String selectedImagePath = "";
    String selectedVideoPath = "";
    long totalSize = 0;
    String selectedMusicPath = "";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pause_task);
        cc = new CommanClass(AddPauseTask.this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        et_title = (EditText) findViewById(R.id.et_title);
        tv_add_image = (TextView) findViewById(R.id.tv_add_image);
        tv_add_audio_file = (TextView) findViewById(R.id.tv_add_audio_file);
        tv_upload = (TextView) findViewById(R.id.tv_upload);
        tv_add_pause_text = (TextView) findViewById(R.id.tv_add_pause_text);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
        tv_add_video_file = (TextView) findViewById(R.id.tv_add_video_file);

        tv_add_image.setOnTouchListener(this);
        tv_add_audio_file.setOnTouchListener(this);
        tv_upload.setOnTouchListener(this);
        tv_add_video_file.setOnTouchListener(this);

        String text_login = "<u>" + getString(R.string.pause_add_text) + "<b>" + "?" + "</b>" + "</u>";
        tv_add_pause_text.setText(Html.fromHtml(text_login));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
       /* if (cc.isConnectingToInternet()) {
            pDialog = new ProgressDialog(AddPauseTask.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            // makeJsonPauseTask();
        } else {
            cc.showSnackbar(ll_linear, getString(R.string.no_internet));
        }*/


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_add_image:

                selectfile();
                break;
            case R.id.tv_add_audio_file:

                selectMusicFile("audio");
                break;
            case R.id.tv_upload:

                String title = et_title.getText().toString().trim();
                if (!cc.isConnectingToInternet()) {

                } else if (title.equals("")) {

                    cc.showSnackbar(ll_linear, getString(R.string.enter_pause_title));
                } else if (selectedMusicPath.equals("") && selectedVideoPath.equals("")) {

                    cc.showSnackbar(ll_linear, getString(R.string.enter_pause_add_file));
                } else if (!selectedMusicPath.equals("") && selectedImagePath.equals("")) {

                    cc.showSnackbar(ll_linear, getString(R.string.enter_pause_add_image));
                } else {

                    if (!selectedMusicPath.equals("") || !selectedImagePath.equals("")) {
                        selectedVideoPath = "";
                    } else if (!selectedVideoPath.equals("")) {
                        selectedMusicPath = "";
                        selectedImagePath = "";
                    }
                    new UploadFileToServer(title).execute();
                }
                break;
            case R.id.tv_add_video_file:

                selectMusicFile("video");
                break;
        }

        return false;
    }


    private void selectMusicFile(String path) {
        try {
            if (path.equals("audio")) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECT_MUSIC);

            } else if (path.equals("video")) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECT_VIDEO);
            }
        } catch (Exception e) {
        }
    }

    private void selectfile() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, SELECT_PICTURE);


        } catch (Exception e) {
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == AddPauseTask.this.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                try {
                    selectedImagePath = getPath(selectedImageUri);
                    Log.e("Selected File", selectedImagePath);

                    ExifInterface ei = null;
                    Bitmap mybitmap = null;
                    Bitmap retVal = null;
                    try {
                        ei = new ExifInterface(selectedImagePath);
                        mybitmap = BitmapFactory.decodeFile(selectedImagePath);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Matrix matrix = new Matrix();
                    int orientation = ei.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    Log.e("Oriention", orientation + "");

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_NORMAL:
                            matrix.postRotate(0);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_90:

                            matrix.postRotate(90);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:

                            matrix.postRotate(180);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:

                            matrix.postRotate(270);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;

                    }

                    File file = new File(selectedImagePath);
                    long fileSizeInBytes = file.length();

                    long fileSizeInKB = fileSizeInBytes / 1024;

                    long fileSizeInMB = fileSizeInKB / 1024;

                    if (fileSizeInMB > 10) {
                        selectedImagePath = "";
                        new AlertDialog.Builder(AddPauseTask.this)
                                .setMessage("You can't upload more than 10 MB file")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        // Uri uri = Uri.fromFile(new File(selectedImagePath));

                        tv_add_image.setText(selectedImagePath);

                        if (orientation != 0) {

                            GenerateImage(retVal);

                        }

                    }

                } catch (URISyntaxException e) {

                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_MUSIC) {

                Uri selectedMusicUri = data.getData();
                try {
                    selectedMusicPath = getPath(selectedMusicUri);

                    tv_add_audio_file.setText(selectedMusicPath);

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Log.e("Selected Music File", selectedMusicPath);
            } else if (requestCode == SELECT_VIDEO) {
                Uri selectedVideoUri = data.getData();

                try {
                    // Log.e("selectedVideoUri", selectedVideoUri + "");
                    selectedVideoPath = getPath(selectedVideoUri);

                    tv_add_video_file.setText(selectedVideoPath);

                    Log.e("Selected video File", selectedVideoPath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public String getPath1(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = AddPauseTask.this.getContentResolver().query(uri,
                        projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
            // cursor.close();
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private void GenerateImage(Bitmap bm) {

        OutputStream fOut = null;
        Uri outputFileUri;
        try {
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, "MIP.jpg");
            outputFileUri = Uri.fromFile(file);
            fOut = new FileOutputStream(file);
        } catch (Exception e) {

        }
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
        }

        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "MIP.jpg");
        selectedImagePath = file.toString();
        tv_add_image.setText(selectedImagePath);

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
            pDialog = new ProgressDialog(AddPauseTask.this);
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
            httppost = new HttpPost(URL.Url_add_pause_task);
            httppost.addHeader("mip-token", cc.loadPrefString("mip-token"));

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                entity.addPart("pausetaskname", new StringBody(title));

                if (!selectedImagePath.equals("")) {
                    File sourceFile = new File(selectedImagePath);
                    entity.addPart("pausetaskimage", new FileBody(sourceFile));
                    Log.e("selectedImagePath", selectedImagePath);
                }
                if (!selectedMusicPath.equals("")) {
                    File sourceMusicFile = new File(selectedMusicPath);
                    entity.addPart("pausetaskaudio", new FileBody(sourceMusicFile));
                    Log.e("selectedMusicPath", selectedMusicPath);
                } else if (!selectedVideoPath.equals("")) {
                    File sourceVideoFile = new File(selectedVideoPath);
                    Log.e("selectedVideoPath", selectedVideoPath);
                    entity.addPart("pausetaskvideo", new FileBody(sourceVideoFile));

                }
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
            Log.i("Add_Pause_Task: result", "Response from server: " + result);

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
