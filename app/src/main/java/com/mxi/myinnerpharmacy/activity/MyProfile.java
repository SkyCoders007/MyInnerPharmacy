package com.mxi.myinnerpharmacy.activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.CommanClass;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {

    Toolbar toolbar;
    TextView tv_name, tv_dob, tv_gender, tv_email;
    CommanClass cc;
    CircleImageView iv_profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        cc = new CommanClass(MyProfile.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_dob = (TextView) findViewById(R.id.tv_dob);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_email = (TextView) findViewById(R.id.tv_email);
        iv_profile_image = (CircleImageView) findViewById(R.id.iv_profile_image);

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
        tv_name.setText(cc.loadPrefString("name"));
        tv_dob.setText(cc.loadPrefString("dob"));
        tv_gender.setText(cc.loadPrefString("gender"));
        tv_email.setText(cc.loadPrefString("email"));
        Glide.with(MyProfile.this)
                .load(cc.loadPrefString("avatar"))
                .into(iv_profile_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_edit) {

            ShowDialogEditProfile();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ShowDialogEditProfile() {

        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MyProfile.this);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setCancelable(false);

        final View dialogView = inflater.inflate(R.layout.edit_profile, null);
        builder.setView(dialogView);
        final AlertDialog alert = builder.create();

        alert.setCanceledOnTouchOutside(true);

        final CircleImageView iv_profile_image = (CircleImageView) dialogView.findViewById(R.id.iv_profile_image);
        final EditText et_name = (EditText) dialogView.findViewById(R.id.et_name);
        ImageView iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);
        Button btn_save = (Button) dialogView.findViewById(R.id.btn_save);

        et_name.setText(cc.loadPrefString("name"));
        Glide.with(MyProfile.this)
                .load(cc.loadPrefString("avatar"))
                .into(iv_profile_image);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                if (!cc.isConnectingToInternet()) {
                    cc.showToast(getString(R.string.no_internet));
                } else if (name.equals("")) {
                    cc.showToast(getString(R.string.enter_email));
                } else {

                }

            }
        });
        alert.show();
    }
}
