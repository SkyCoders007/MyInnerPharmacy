package com.mxi.myinnerpharmacy.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.adapter.NavigationDrawerAdapter;
import com.mxi.myinnerpharmacy.fragment.Home;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    DrawerLayout drawer;
    CommanClass cc;
    CircleImageView iv_profile_image;
    TextView tv_name, tv_logout;
    LinearLayout ll_profile;
    RecyclerView mRecyclerView;
    NavigationDrawerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    public static Boolean isSkip = false;
    public static Boolean StepLastColibration = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cc = new CommanClass(MainActivity.this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        tv_logout = (TextView) findViewById(R.id.tv_logout);

        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_profile_image = (CircleImageView) findViewById(R.id.iv_profile_image);
        ll_profile = (LinearLayout) findViewById(R.id.ll_profile);

        tv_name.setText(cc.loadPrefString("name"));
        Glide.with(MainActivity.this)
                .load(cc.loadPrefString("avatar"))
                //.placeholder(R.mipmap.choose_image)
                .into(iv_profile_image);
        tv_logout.setOnTouchListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        String TITLES[] = getResources().getStringArray(R.array.nav_drawer_items);
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NavigationDrawerAdapter(MainActivity.this, TITLES, navMenuIcons);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                displayView(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyProfile.class));
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, new Home());
        fragmentTransaction.commit();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_logout:

                makeJsonLogout();

                break;
        }

        return false;
    }

    @SuppressLint("NewApi")
    private void displayView(int menuItem) {
        Fragment fragment = null;
        switch (menuItem) {

            case 0:
                // fragment = new Home();
                startActivity(new Intent(MainActivity.this, RapidResponse.class));

                break;
            case 1:

                startActivity(new Intent(MainActivity.this, PauseTask.class));

                break;
            case 2:
                startActivity(new Intent(MainActivity.this, CurrentPrescriptionRequest.class));
                break;
            case 3:

                startActivity(new Intent(MainActivity.this, StateCalibration.class));
                break;
            case 4:

                startActivity(new Intent(MainActivity.this, HeartRate.class));
                break;
            case 5:

                break;
            case 6:
                startActivity(new Intent(MainActivity.this, ResourceJournal.class));
                break;

            case 7:

                startActivity(new Intent(MainActivity.this, UpliftingMusic.class));
                break;
            case 8:

                startActivity(new Intent(MainActivity.this, AdvancedWellness.class));
                break;

            case 9:
                startActivity(new Intent(MainActivity.this, ReminderFrequency.class));
                break;
            default:


        }
        try {
            if (fragment != null) {
                drawer.closeDrawers();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment);
                fragmentTransaction.commit();
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Error", e.getMessage());
            drawer.closeDrawers();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        drawer.closeDrawers();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private void makeJsonLogout() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_logout,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("logout", response);
                        jsonParseMatchList(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cc.showSnackbar(mRecyclerView, error.getMessage().toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("device_id", cc.loadPrefString("device_id"));

                Log.i("request logout", params.toString());

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

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

                cc.logoutapp();
                startActivity(new Intent(MainActivity.this, LoginScreen.class));
                finish();


            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
