package com.mxi.myinnerpharmacy.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.AdapterHeartRateHistory;
import com.mxi.myinnerpharmacy.database.SQLiteTD;
import com.mxi.myinnerpharmacy.model.heartrate;

import java.util.ArrayList;

public class HeartRateHistory extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv_recyclerview;
    ArrayList<heartrate> heartlist;
    TextView tv_history;
    SQLiteTD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_history);
        db = new SQLiteTD(HeartRateHistory.this);

        rv_recyclerview = (RecyclerView) findViewById(R.id.rv_recyclerview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_history = (TextView) findViewById(R.id.tv_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // startActivity(new Intent(HeartRate.this, ));
            }
        });
        tv_history.setText(Html.fromHtml("<font color=#FFFFFF><u>" + "Heart Rate History" + "</u></font>"));
        //===================================SET ADAPTER========================================================================
        rememberMe();
    }

    private void rememberMe() {

        try {
            Cursor c = db.getHeartrate();
            heartlist = new ArrayList<heartrate>();
            if (c.getCount() != 0 && c != null) {
                c.moveToFirst();
                do {
                    heartrate rt = new heartrate();
                    rt.setId(c.getInt(0));
                    rt.setDate(c.getString(1));
                    rt.setHeart_rate(c.getString(2));
                    heartlist.add(rt);

                } while (c.moveToNext());

            }

            rv_recyclerview.setHasFixedSize(true);
            rv_recyclerview.setAdapter(new AdapterHeartRateHistory(HeartRateHistory.this, heartlist));
            rv_recyclerview.setLayoutManager(new LinearLayoutManager(HeartRateHistory.this, LinearLayoutManager.VERTICAL, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
