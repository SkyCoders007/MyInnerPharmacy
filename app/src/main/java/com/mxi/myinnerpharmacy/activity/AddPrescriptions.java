package com.mxi.myinnerpharmacy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.net.URISyntaxException;

public class AddPrescriptions extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_linear, ll_add_text;
    TextView tv_name, tv_add_file, tv_add_text, tv_upload;
    EditText et_title, et_add_text;
    Toolbar toolbar;
    private final int SELECT_MUSIC = 2;
    private final int SELECT_VIDEO = 3;
    private final int SELECT_TEXT = 4;
    long totalSize = 0;
    Uri selectedPath = null;
    CommanClass cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescriptions);
        cc = new CommanClass(AddPrescriptions.this);

        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
        ll_add_text = (LinearLayout) findViewById(R.id.ll_add_text);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_add_file = (TextView) findViewById(R.id.tv_add_file);
        et_add_text = (EditText) findViewById(R.id.et_add_text);
        tv_upload = (TextView) findViewById(R.id.tv_upload);

        et_title = (EditText) findViewById(R.id.et_title);

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

        final String title_text = getIntent().getStringExtra("title_text");
        String browse_text = getIntent().getStringExtra("browse");

        tv_name.setText(title_text);
        tv_add_file.setText(browse_text);

        if (browse_text.equals(getString(R.string.add_text))) {
            ll_add_text.setVisibility(View.VISIBLE);
        } else {
            ll_add_text.setVisibility(View.GONE);
        }

        tv_add_file.setOnClickListener(this);

       /* tv_upload.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String title = et_title.getText().toString().trim();
                String add_file = tv_add_file.getText().toString().trim();
                String add_text = et_add_text.getText().toString().trim();

               *//* if (add_file.equals(getString(R.string.add_text)) || add_text.equals(getString(R.string.add_text_file))) {
                    add_file = add_text;
                }*//*
                if (!cc.isConnectingToInternet()) {
                    cc.showSnackbar(ll_linear, getString(R.string.no_internet));

                } else if (title.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.enter_pres_title));

                } else if (selectedPath == null || selectedPath.toString().equals("")) {

                    if (title_text.equals(getString(R.string.add_pres_video_title))) {
                        cc.showSnackbar(ll_linear, getString(R.string.select_pres_video));

                    } else if (title_text.equals(getString(R.string.add_pres_audio_title))) {
                        cc.showSnackbar(ll_linear, getString(R.string.select_pres_audio));

                    } else if (title_text.equals(getString(R.string.add_pres_text_title)) && (add_file.equals(add_text) && add_text.equals(add_file))) {

                        cc.showSnackbar(ll_linear, getString(R.string.select_pres_text) + "/" + getString(R.string.add_text_file));

                    }
                } else {


                    cc.showToast("completed");
                    onBackPressed();
                }

                return false;
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_file:

                if (tv_name.getText().toString().trim().equals(getString(R.string.add_pres_video_title))) {

                    selectFile("video");
                } else if (tv_name.getText().toString().trim().equals(getString(R.string.add_pres_audio_title))) {

                    selectFile("audio");
                } else if (tv_name.getText().toString().trim().equals(getString(R.string.add_pres_text_title))) {

                    selectFile("text");

                }

                break;
        }
    }

    private void selectFile(String data) {
        if (data.equals("audio")) {
            try {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECT_MUSIC);
            } catch (Exception e) {
            }

        } else if (data.equals("video")) {
            try {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECT_VIDEO);

            } catch (Exception e) {
            }

        } else if (data.equals("text")) {
            try {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*|text/*");
                startActivityForResult(intent, SELECT_TEXT);

            } catch (Exception e) {
            }
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == AddPrescriptions.this.RESULT_OK) {
            if (requestCode == SELECT_MUSIC) {

                selectedPath = data.getData();
                //  selectedPath = getPath(selectedMusicUri);
                tv_add_file.setText(selectedPath.toString());

                // Log.e("Selected Music File", selectedMusicPath);

            } else if (requestCode == SELECT_VIDEO) {

                selectedPath = data.getData();

                // selectedVideoPath = getPath(selectedVideoUri);
                Log.e("video_path", selectedPath.toString());

                tv_add_file.setText(selectedPath.toString());

                // Log.e("Selected Music File", selectedMusicPath);
            } else if (requestCode == SELECT_TEXT) {

                selectedPath = data.getData();

                if (selectedPath.toString().substring(selectedPath.toString().lastIndexOf('.')).trim().equals(".txt")) {
                    tv_add_file.setText(selectedPath.toString());
                } else {
                    new AlertDialog.Builder(AddPrescriptions.this)
                            .setMessage("Only select text file")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })

                            .show();
                }

            }
        }
    }

    public String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = AddPrescriptions.this.getContentResolver().query(uri,
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

}
