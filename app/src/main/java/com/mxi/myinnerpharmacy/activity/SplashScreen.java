package com.mxi.myinnerpharmacy.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.RegistrationCommanClass;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar;
    CommanClass cc;

    LinearLayout ll_linear;
    RegistrationCommanClass rcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        cc = new CommanClass(SplashScreen.this);
        rcc = new RegistrationCommanClass(SplashScreen.this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for marshmallow and newer versions
            checkReadWritePermission();
        } else {
            CountDown();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    CountDown();
                } else {
                    //  Toast.makeText(NewPrescriptionRequest.this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT).show();
                    showErrorDialog("Please allow the permission for better performance", true);
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkReadWritePermission() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                return;
            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        } else {
            CountDown();
        }
    }

    public void showErrorDialog(String msg, final boolean isFromPermission) {
        progressBar.setVisibility(View.INVISIBLE);
        AlertDialog.Builder alert = new AlertDialog.Builder(SplashScreen.this);
        alert.setTitle("MY INNER PHARMACY");
        alert.setMessage(msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isFromPermission) {
                    checkReadWritePermission();
                } else {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    public void CountDown() {
        CountDownTimer mCountDownTimer = new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                SharedPreferences settings = getSharedPreferences("LOGIN", 1);
                //  mboolean = settings.getBoolean("FIRST_RUN", false);
               /* if (!mboolean) {// do the thing for the first time
                    settings = getSharedPreferences("PREFS_NAME", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("FIRST_RUN", true);
                    editor.commit();
                }
*/
               /* if (!mboolean) {// do the thing for the first time
                    settings = getSharedPreferences("PREFS_NAME", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("FIRST_RUN", true);
                    editor.commit();
                }*/
                if (cc.isConnectingToInternet()) {

                    if (cc.loadPrefBoolean("islogin") && rcc.loadPrefBoolean("isReminderFreq")) {
                        Intent mIntent = new Intent(SplashScreen.this,
                                MainActivity.class);
                        startActivity(mIntent);
                        finish();

                    } else if (!rcc.loadPrefBoolean("isDisclaimer")) {// do the thing for the first time
                        Intent mIntent = new Intent(SplashScreen.this,
                                DisclaimerScreen.class);
                        startActivity(mIntent);
                        finish();
                    } else if (!rcc.loadPrefBoolean("SignIn")) {
                        Intent mIntent = new Intent(SplashScreen.this,
                                SignUpInSlider.class);
                        startActivity(mIntent);
                        finish();
                    } else if (!rcc.loadPrefBoolean("ISLOGIN")) {
                        Intent mIntent = new Intent(SplashScreen.this,
                                LoginScreen.class);
                        startActivity(mIntent);
                        finish();
                    } else if (!rcc.loadPrefBoolean("isKey")) {
                        Intent mIntent = new Intent(SplashScreen.this,
                                KeyText.class);
                        startActivity(mIntent);
                        finish();
                    } else if (!rcc.loadPrefBoolean("isQuestion")) {
                        Intent mIntent = new Intent(SplashScreen.this,
                                Questionnair.class);
                        startActivity(mIntent);
                        finish();
                    } else if (!rcc.loadPrefBoolean("isQuestionLevel")) {
                        Intent mIntent = new Intent(SplashScreen.this,
                                QuestionLevel.class);
                        startActivity(mIntent);
                        finish();
                    } else if (!rcc.loadPrefBoolean("isUploadPic")) {
                        Intent mIntent = new Intent(SplashScreen.this,
                                UploadPic.class);
                        startActivity(mIntent);
                        finish();
                    } else if (!rcc.loadPrefBoolean("isAfterUploadPic")) {
                        Intent mIntent = new Intent(SplashScreen.this,
                                AfterUploadPic.class);
                        startActivity(mIntent);
                        finish();
                    } else if (!rcc.loadPrefBoolean("isReminderFreq")) {
                        Intent mIntent = new Intent(SplashScreen.this,
                                ReminderFrequency.class);
                        startActivity(mIntent);
                        finish();
                    } else if (!cc.loadPrefBoolean("islogin")) {
                        Intent mIntent = new Intent(SplashScreen.this,
                                LoginScreen.class);
                        startActivity(mIntent);
                        finish();
                    }


                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    String mystring = getResources().getString(R.string.no_internet);
                    Snackbar snackbar = Snackbar
                            .make(ll_linear, mystring, Snackbar.LENGTH_LONG);
                    snackbar.show();


                }
            }
        };
        mCountDownTimer.start();
    }
}
