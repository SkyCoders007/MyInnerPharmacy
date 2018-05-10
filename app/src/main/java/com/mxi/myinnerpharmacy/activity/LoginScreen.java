package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.database.SQLiteTD;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.RegistrationCommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity implements View.OnTouchListener, CompoundButton.OnCheckedChangeListener {

    EditText et_email, et_password;
    TextView tv_forgot_password, tv_login, tv_register;
    ImageView iv_facebook, iv_twitter, iv_google;
    ProgressDialog pDialog;
    CommanClass cc;
    LinearLayout ll_linear;
    RegistrationCommanClass rcc;
    GoogleCloudMessaging gcmObj;
    String regId = "";
    SQLiteTD db;
    CheckBox cb_remember_me;
    public boolean checked;
    String rememberMe;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        cc = new CommanClass(LoginScreen.this);
        rcc = new RegistrationCommanClass(LoginScreen.this);
        db = new SQLiteTD(LoginScreen.this);

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        iv_facebook = (ImageView) findViewById(R.id.iv_facebook);
        iv_twitter = (ImageView) findViewById(R.id.iv_twitter);
        iv_google = (ImageView) findViewById(R.id.iv_google);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
        cb_remember_me = (CheckBox) findViewById(R.id.cb_remember_me);

        tv_forgot_password.setOnTouchListener(this);
        tv_register.setOnTouchListener(this);
        tv_login.setOnTouchListener(this);

        // et_email.setText("sonali@mxicoders.com");
        //  et_password.setText("mxi123");
        String texm_text = "<font color=#FFFFFF><u>" + getString(R.string.new_user_login) + "</u></font>";
        tv_register.setText(Html.fromHtml(texm_text));

        String device_id = Settings.Secure.getString(LoginScreen.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        cc.savePrefString("device_id", device_id);

        cb_remember_me.setOnCheckedChangeListener(this);
        //================================ Remember me========================================================================
        rememberMe();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_remember_me:
                cb_remember_me.setChecked(isChecked);
                checked = isChecked;
                if (checked) {
                    rememberMe = "Yes";
                    Log.e("checkbox_false", rememberMe + "");

                } else {
                    rememberMe = "No";
                    Log.e("checkbox_true", rememberMe + "");

                }
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

            case R.id.tv_forgot_password:

                showForgotPasswordDialog();
                break;
            case R.id.tv_register:

                startActivity(new Intent(LoginScreen.this, Register.class));
                break;

            case R.id.tv_login:
                login();
                break;
        }
        return false;
    }

    private void rememberMe() {

        try {
            Cursor c = db.getLogin();
            if (c.getCount() != 0 && c != null) {
                c.moveToFirst();
                do {

                    // c.getString(0);
                    if (c.getString(3).equals("Yes")) {
                        checked = true;
                        cb_remember_me.setChecked(true);
                        et_email.setText(c.getString(1));
                        et_password.setText(c.getString(2));
                    } else {
                        checked = false;
                        cb_remember_me.setChecked(false);
                        et_email.setText("");
                        et_password.setText("");
                    }

                } while (c.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (!cc.isConnectingToInternet()) {
            cc.showSnackbar(ll_linear, getString(R.string.no_internet));
        } else if (email.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_email));
        } else if (password.equals("")) {
            cc.showSnackbar(ll_linear, getString(R.string.enter_password));
        } else {
            pDialog = new ProgressDialog(LoginScreen.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();

            if (checkPlayServices()) {
                // Register Device in GCM Server
                registerInBackground(email, password);
            }

        }


    }

    //--------------Forgot Password-----------------------------------------------------------------------------------------
    private void showForgotPasswordDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreen.this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//getLayoutInflater();
        builder.setCancelable(true);
        final View dialogView = inflater.inflate(R.layout.forgot_password, null);

        builder.setView(dialogView);
        final AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final EditText et_email = (EditText) dialogView.findViewById(R.id.et_email);
        ImageView iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        Button btn_send = (Button) dialogView.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = et_email.getText().toString().trim();
                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));
                } else if (email.equals("")) {
                    cc.showToast(getString(R.string.enter_forgot_email));
                } else {
                    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    pDialog = new ProgressDialog(LoginScreen.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.show();
                    makeJsonForgotPassword(email);
                    alert.dismiss();
                }
            }
        });

        alert.show();
    }


    // Check if Google Playservices is installed in Device or not
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        LoginScreen.this,
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } /*else {
            Toast.makeText(
                    LoginScreen.this,
                    "This device supports Play services, App will work normally",
                    Toast.LENGTH_LONG).show();
        }*/
        return true;
    }

    // AsyncTask to register Device in GCM Server
    private void registerInBackground(final String email, final String password) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(LoginScreen.this);
                    }
                    regId = gcmObj
                            .register(URL.GOOGLE_PROJ_ID);
                    msg = regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

                if (msg.isEmpty()) {
                    pDialog.dismiss();
                    cc.showToast(getString(R.string.ws_error));
                } else {
                    // regId = msg;
                    Log.e("regisId", msg);
                    makeJsonlogin(email, password, msg);
                }

            }
        }.execute(null, null, null);
    }

    private void makeJsonlogin(final String email, final String password, final String msg) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_login,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("login", response);
                        jsonParseMatchList(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.i("error", error.toString());
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
                params.put("gcm_id", msg);
                params.put("login_by", "User");

                Log.i("request login", params.toString());

                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                try {
                    String mip_token = response.headers.get("mip-token ");
                    Log.e("mip-token", mip_token);
                    cc.savePrefString("mip-token", mip_token);
                } catch (Exception e) {
                    Log.e("Error In Volly", e.toString());
                }

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

                rcc.savePrefBoolean("ISLOGIN", true);

                db.inseartLogin(cc.loadPrefString("email"), et_password.getText().toString().trim(), rememberMe);
                /*if (!rcc.loadPrefBoolean("isKey")) {
                    Intent mIntent = new Intent(LoginScreen.this,
                            KeyText.class);
                    startActivity(mIntent);
                    finish();
                } else  {
                    Intent mIntent = new Intent(LoginScreen.this,
                            MainActivity.class);
                    startActivity(mIntent);
                    finish();

                }*/
                Intent mIntent = new Intent(LoginScreen.this,
                        Questionnair.class);
                startActivity(mIntent);
                finish();

               /* else if (cc.loadPrefBoolean("islogin") && rcc.loadPrefBoolean("isReminderFreq")) {
                    Intent mIntent = new Intent(LoginScreen.this,
                            MainActivity.class);
                    startActivity(mIntent);
                    finish();

                }*/

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            //e.printStackTrace();
        }
    }


    //---------------------------------Forgot Password--------------------------------------------------

    private void makeJsonForgotPassword(final String email) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_forgotpassword,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("login", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            if (jsonObject.getString("status").equals("200")) {

                                cc.showToast(jsonObject.getString("message"));

                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            //e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_id", email);

                Log.i("request login", params.toString());

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }


}
