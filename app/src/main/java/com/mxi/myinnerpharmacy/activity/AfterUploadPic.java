package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.model.Idealstateimage;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.RegistrationCommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AfterUploadPic extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    ImageView iv_choose;
    TextView tv_choose_text, tv_choose_photo;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath = "";
    long totalSize = 0;
    CommanClass cc;
    GridView gridView;
    ProgressDialog pDialog;
    String achieve_image, question_level, question_achive;
    ArrayList<Idealstateimage> stateimagelist;
    Integer selected_position = -1;
    RegistrationCommanClass rcc;
    TextView btn_back, btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_upload_pic);
        cc = new CommanClass(AfterUploadPic.this);
        rcc = new RegistrationCommanClass(AfterUploadPic.this);
        pDialog = new ProgressDialog(AfterUploadPic.this);

        btn_back = (TextView) findViewById(R.id.btn_back);
        btn_next = (TextView) findViewById(R.id.btn_next);
        tv_choose_text = (TextView) findViewById(R.id.tv_choose_text);
        tv_choose_photo = (TextView) findViewById(R.id.tv_choose_photo);
        iv_choose = (ImageView) findViewById(R.id.iv_choose);
        gridView = (GridView) findViewById(R.id.gridView);

        try {
            question_achive = getIntent().getStringExtra("question_achive");
            achieve_image = getIntent().getStringExtra("achieve_image");
            question_level = getIntent().getStringExtra("question_level");
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        tv_choose_photo.setOnTouchListener(this);

        String text_login = "<font color=#FFFFFF><u>" + "&quot;" + getString(R.string.after_upload_pic_text) + "&quot;" + "</u></font>";
        tv_choose_text.setText(Html.fromHtml(text_login));
        if (cc.isConnectingToInternet()) {
            pDialog.setMessage("Please wait...");
            pDialog.show();
            makeJsonIdealImage();
        } else {
            pDialog.dismiss();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (selectedImagePath.equals("")) {
                    cc.showSnackbar(gridView, getString(R.string.select_image));
                } else {

                    rcc.savePrefBoolean("isAfterUploadPic", true);

                    startActivity(new Intent(AfterUploadPic.this, ReminderFrequency.class).
                            putExtra("question_achive", question_achive)
                            .putExtra("question_level", question_level)
                            .putExtra("achieve_image", achieve_image).
                                    putExtra("current_image", selectedImagePath));
                    finish();
                }

                break;
            case R.id.btn_back:
                startActivity(new Intent(AfterUploadPic.this, UploadPic.class));
                finish();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.tv_choose_photo:
                if (!selectedImagePath.equals("")) {
                    selected_position = -1;
                    IdealStateImageAdapter adapter = new IdealStateImageAdapter(AfterUploadPic.this, stateimagelist);
                    gridView.setAdapter(adapter);
                }
                selectfile();
                break;
        }
        return false;
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
        if (resultCode == AfterUploadPic.this.RESULT_OK) {
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
                        new AlertDialog.Builder(AfterUploadPic.this)
                                .setMessage("You can't upload more than 10 MB file")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        Uri uri = Uri.fromFile(new File(selectedImagePath));

                        Glide.with(AfterUploadPic.this)
                                .load(uri)
                                //.placeholder(R.drawable.ic_profile)
                                .into(iv_choose);

                        if (orientation != 0) {

                            GenerateImage(retVal);

                        } else {
                            // new UploadFileToServer().execute();
                        }

                    }


                } catch (URISyntaxException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = AfterUploadPic.this.getContentResolver().query(uri,
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

        //new UploadFileToServer().execute();
    }


    private void makeJsonIdealImage() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_ideal_state_images,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_ideal_state_images", response);
                        jsonParseMatchList(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(gridView, getString(R.string.ws_error));
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

    private void jsonParseMatchList(String response) {

        stateimagelist = new ArrayList<Idealstateimage>();
        try {

            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();

            if (jsonObject.getString("status").equals("200")) {
                String thumb = jsonObject.getString("ideal_state_imagethumb");
                JSONArray images = jsonObject.getJSONArray("images");
                for (int i = 0; i < images.length(); i++) {

                    JSONObject data = images.getJSONObject(i);
                    Idealstateimage si = new Idealstateimage();

                    si.setMedia_file(thumb + data.getString("media_file"));
                    stateimagelist.add(si);

                }
                if (!stateimagelist.isEmpty()) {
                    IdealStateImageAdapter adapter = new IdealStateImageAdapter(AfterUploadPic.this, stateimagelist);
                    gridView.setAdapter(adapter);
                }

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            //e.printStackTrace();
        }

    }


    public class IdealStateImageAdapter extends BaseAdapter {
        private Context context;
        ArrayList<Idealstateimage> stateimagelist;
        private LayoutInflater mInflater;

        public IdealStateImageAdapter(Context context, ArrayList<Idealstateimage> stateimagelist) {
            this.context = context;
            this.stateimagelist = stateimagelist;
        }

        @Override
        public int getCount() {
            return stateimagelist.size(); //returns total of items in the list
        }

        @Override
        public Object getItem(int position) {
            return stateimagelist.get(position); //returns list item at the specified position
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.row_ideal_state_image, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.checkBox1.setTag(position);

            Glide.with(context)
                    .load(stateimagelist.get(position).getMedia_file())
                    .placeholder(R.mipmap.choose_image)
                    .into(viewHolder.iv_choose);

            if (position == selected_position) {
                viewHolder.checkBox1.setChecked(true);
            } else viewHolder.checkBox1.setChecked(false);

            viewHolder.checkBox1.setOnClickListener(onStateChangedListener(viewHolder.checkBox1, position));


           /* viewHolder.iv_choose.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (position == selected_position) {
                        selected_position = position;
                        Glide.with(context)
                                .load(stateimagelist.get(selected_position).getMedia_file())
                                .into(iv_choose);
                    } else {
                        selected_position = -1;
                        Glide.with(context)
                                .load(R.mipmap.choose_image)
                                .into(iv_choose);
                    }
                    notifyDataSetChanged();

                }
            });*/

            return convertView;
        }

        private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        selected_position = position;
                        Glide.with(context)
                                .load(stateimagelist.get(selected_position).getMedia_file())
                                .into(iv_choose);
                        selectedImagePath = stateimagelist.get(selected_position).getMedia_file();
                    } else {
                        selected_position = -1;
                        Glide.with(context)
                                .load(R.mipmap.choose_image)
                                .into(iv_choose);
                        selectedImagePath = "";
                    }
                    notifyDataSetChanged();
                }
            };
        }

        private class ViewHolder {
            protected ImageView iv_choose;
            CheckBox checkBox1;

            public ViewHolder(View view) {

                iv_choose = (ImageView) view.findViewById(R.id.iv_choose);
                checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
            }
        }
    }
}
