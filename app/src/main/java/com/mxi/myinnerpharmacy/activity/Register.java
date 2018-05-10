package com.mxi.myinnerpharmacy.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.AndroidMultiPartEntity;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.RegistrationCommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    EditText et_name, et_height, et_weight, et_email, et_password, et_confirm_password;
    TextView tv_dob, tv_term_condition, tv_login, tv_register, tv_gender;
    CheckBox checkBox;
    CircleImageView iv_profile_image;
    CommanClass cc;
    LinearLayout ll_linear, ll_gender;
    int to_year, to_month, to_day;
    Calendar cal;
    final String[] gender = {"Male", "Female", "Others"};

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath = "";
    long totalSize = 0;
    ProgressDialog pDialog;
    public boolean checked = false;
    RegistrationCommanClass rcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cc = new CommanClass(Register.this);
        rcc = new RegistrationCommanClass(Register.this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        et_name = (EditText) findViewById(R.id.et_name);
//        et_height = (EditText) findViewById(R.id.et_height);
//        et_weight = (EditText) findViewById(R.id.et_weight);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        tv_dob = (TextView) findViewById(R.id.tv_dob);
        tv_term_condition = (TextView) findViewById(R.id.tv_term_condition);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        iv_profile_image = (CircleImageView) findViewById(R.id.iv_profile_image);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
        ll_gender = (LinearLayout) findViewById(R.id.ll_gender);

        String text_login = "<font color=#FFFFFF><u>" + getString(R.string.user_sign_in) + "</u></font>";
        tv_register.setText(Html.fromHtml(text_login));

        String text_term = "<font color=#FFFFFF>" + getString(R.string.i_agree) + "</font>&nbsp;<font color=#FFFFFF><u>" + getString(R.string.term_condition) + "</u></font>";
        tv_term_condition.setText(Html.fromHtml(text_term));

        String device_id = Settings.Secure.getString(Register.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        cc.savePrefString("device_id", device_id);

        tv_dob.setOnTouchListener(this);
        tv_gender.setOnTouchListener(this);
        tv_register.setOnTouchListener(this);
        tv_login.setOnTouchListener(this);
        tv_term_condition.setOnTouchListener(this);
        iv_profile_image.setOnClickListener(this);

       /* et_name.setText("Sonali");
        et_height.setText("40");
        et_weight.setText("40");
        et_email.setText("sonali@mxicoders.com");
        et_password.setText("mxi123");
        et_confirm_password.setText("mxi123");
        tv_dob.setText("20/6/1993");
        checked = true;
        checkBox.setChecked(true);*/

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                                                    if (checked) {
                                                        checked = false;
                                                        checkBox.setChecked(false);
                                                        Log.e("checkbox_false", checked + "");

                                                    } else {
                                                        checked = true;
                                                        checkBox.setChecked(true);

                                                        Log.e("checkbox_true", checked + "");

                                                    }


                                                }
                                            }
        );


    }

    private void register() {
        String name = et_name.getText().toString().trim();
//        String height = et_height.getText().toString().trim();
//        String weight = et_weight.getText().toString().trim();
        String height = "";
        String weight = "";
        String gender = tv_gender.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String confirm_password = et_confirm_password.getText().toString().trim();
        String dob = tv_dob.getText().toString().trim();

        if (!cc.isConnectingToInternet()) {
            cc.showSnackbar(ll_linear, getString(R.string.no_internet));
        } else if (selectedImagePath.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.select_image));
        } else if (name.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_full_name));
        } else if (dob.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_dob));
        } /*else if (height.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_height));
        } else if (weight.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_weight));
        }*/ else if (gender.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_gender));
        } else if (email.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_email));
        } else if (password.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_password));
        } else if (confirm_password.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_confirm_password));
        } else if (!password.matches(confirm_password)) {
            cc.showSnackbar(ll_linear, getString(R.string.match_pass));
        } else if (checked != true) {
            cc.showSnackbar(ll_linear, getString(R.string.select_term));
        } else {

            new UploadFileToServer(name, height, weight, gender, email, password, dob).execute();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(Register.this, LoginScreen.class));
                break;
            case R.id.tv_login:
                //startActivity(new Intent(Register.this, KeyText.class));
                register();
                break;
            case R.id.tv_dob:

                cal = Calendar.getInstance();
                to_year = cal.get(Calendar.YEAR);
                ;
                to_month = cal.get(Calendar.MONTH);
                to_day = cal.get(Calendar.DAY_OF_MONTH);

                showDialog(0);

                break;
            case R.id.tv_gender:

                showPoupupMenu(getString(R.string.select_gender), gender);

                break;
            case R.id.tv_term_condition:

                ShowTermAndCondition();
                break;


        }
        return false;
    }

    private void showPoupupMenu(final String title, final String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                tv_gender.setText(items[item]);

            }
        });
        builder.setCancelable(false)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case 0:

                android.app.DatePickerDialog _date2 = new android.app.DatePickerDialog(this, to_dateListener, to_year, to_month, to_day);

                Calendar c1 = Calendar.getInstance();
                c1.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), (cal.get(Calendar.DATE)) + 30);
                // c1.add(Calendar.YEAR, -25);

                _date2.getDatePicker().setMaxDate(c1.getTimeInMillis());

                return _date2;


        }
        return null;
    }

    android.app.DatePickerDialog.OnDateSetListener to_dateListener = new android.app.DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

            // String selected_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

            Log.e("arg,,,arg2,,,,arg3", "    " + arg1 + " " + arg2 + " " + arg3);

            String selected_date = arg1 + "-" + (arg2 + 1) + "-" + arg3;

            Log.e("set selected date", selected_date);

            Date date = null;
            SimpleDateFormat formate_to = null;
            SimpleDateFormat formate_to1 = null;
            SimpleDateFormat formate_to_day = null;
            try {

                SimpleDateFormat formate = new SimpleDateFormat("yyyy-M-d");
                date = formate.parse(selected_date);
                formate_to = new SimpleDateFormat("yyyy-MM-dd");

            } catch (ParseException e) {

            }

            try {

                String str_date_to = formate_to.format(date);

                formate_to1 = new SimpleDateFormat("dd MMM yyyy");
                String str_date = formate_to1.format(date);
                tv_dob.setText(str_date);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
           /* case R.id.ll_gender:
                showPoupupMenu(getString(R.string.select_gender), gender);
                break;*/
            case R.id.iv_profile_image:

                selectfile();

                break;
        }
    }

    //    getSupportActionBar().setDisplayShowTitleEnabled(false);
    public void ShowTermAndCondition() {


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Register.this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//getLayoutInflater();
        builder.setCancelable(false);

        final View dialogView = inflater.inflate(R.layout.dialog_terms_conditions, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alert = builder.create();

        alert.setCanceledOnTouchOutside(true);


        Button btn_ok = (Button) dialogView.findViewById(R.id.btn_ok_terms_conditions);
        TextView tv_terms = (TextView) dialogView.findViewById(R.id.tv_terms_conditions);
//        tv_terms.setText(Html.fromHtml(GetPhoneAddress()));     //Use this when convert the text file to string
        tv_terms.setText(R.string.disclaimer_text);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        alert.show();
    }


    private String GetPhoneAddress() {
        String line = null;
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(getAssets().open("term_condition.txt")));

            while ((line = br.readLine()) != null) {

                text.append(line);
            }
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();
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
        if (resultCode == Register.this.RESULT_OK) {
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
                        new AlertDialog.Builder(Register.this)
                                .setMessage("You can't upload more than 10 MB file")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        Uri uri = Uri.fromFile(new File(selectedImagePath));

                        Glide.with(Register.this)
                                .load(uri)
                                .into(iv_profile_image);

                        if (orientation != 0) {

                            GenerateImage(retVal);

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
                cursor = Register.this.getContentResolver().query(uri,
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

    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        HttpClient httpclient;
        HttpPost httppost;
        String name, height, weight, gender, email, password, dob;

        public UploadFileToServer(String name, String height, String weight, String gender, String email, String password, String dob) {
            this.weight = weight;
            this.name = name;
            this.height = height;
            this.gender = gender;
            this.email = email;
            this.password = password;
            this.dob = dob;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            // pDialog.setMessage("Please wait...");
            pDialog.show();
            //progressDialog.show();
            //  progressDialog.setContentView(R.layout.custom_progressdialog);
            // progressBar_tv = (TextView) progressDialog.findViewById(R.id.progressBar_tv);
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
            httppost = new HttpPost(URL.Url_registration);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (selectedImagePath != null || !selectedImagePath.equals("")) {
                    File sourceFile = new File(selectedImagePath);
                    entity.addPart("image", new FileBody(sourceFile));
                }

                entity.addPart("name", new StringBody(name));
                entity.addPart("dob", new StringBody(dob));
                entity.addPart("height", new StringBody(height));
                entity.addPart("weight", new StringBody(weight));
                entity.addPart("gender", new StringBody(gender));
                entity.addPart("email", new StringBody(email));
                entity.addPart("password", new StringBody(password));
                entity.addPart("device_id", new StringBody(cc.loadPrefString("device_id")));

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

                    rcc.savePrefBoolean("ISREGISTER", true);
                    Intent mIntent = new Intent(Register.this, LoginScreen.class);
                    startActivity(mIntent);
                    finish();
                    //  makeJsonlogin(email, password);

                } else {
                    cc.showToast(jObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }

    }

    private void makeJsonlogin(final String email, final String password) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_login,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("login", response);
                        jsonParseMatchList(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(ll_linear, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_id", email);
                params.put("password", password);
                params.put("device_id", cc.loadPrefString("device_id"));
                params.put("login_with", "android");
                params.put("gcm_id", cc.loadPrefString("device_id"));
                params.put("login_by", "User");

                Log.i("request login", params.toString());

                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String mip_token = response.headers.get("mip-token ");
                Log.e("mip-token", mip_token);
                cc.savePrefString("mip-token", mip_token);
                return super.parseNetworkResponse(response);

            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseMatchList(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                JSONArray userdetail = jsonObject.getJSONArray("userdetail");
                JSONObject data = userdetail.getJSONObject(0);

                cc.savePrefString("name", data.getString("name"));
                cc.savePrefString("email", data.getString("email"));
                cc.savePrefString("gender", data.getString("gender"));
                cc.savePrefString("dob", data.getString("dob"));
                cc.savePrefString("avatar", data.getString("avatar"));
                cc.savePrefString("height", data.getString("height"));
                cc.savePrefString("weight", data.getString("weight"));
                cc.savePrefString("sigupfrom", data.getString("sigupfrom"));

                cc.savePrefBoolean("islogin", true);

                Intent mIntent = new Intent(Register.this, KeyText.class);
                startActivity(mIntent);
                finish();


            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            //e.printStackTrace();
        }
    }


}
