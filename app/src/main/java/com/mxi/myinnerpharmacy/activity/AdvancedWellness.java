package com.mxi.myinnerpharmacy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.model.AdvanceWellnessDevelopmentRecord;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdvancedWellness extends AppCompatActivity implements View.OnTouchListener {

    CommanClass cc;
    LinearLayout ll_advance_wellness;
    TextView tv_hours, tv_sugary, tv_play, tv_exercise, tv_stillness, tv_tv, tv_computer, tv_smart_phone, tv_portion, tv_portions, tv_date, tv_wellness_done;

    Toolbar toolbar;
    final String[] sleephours = {"1", "2", "3", "4", "5", "6", "7", "8","9","10"};
    final String[] sugary = {"1", "2", "3", "4", "5", "6", "7", "8","9","10"};
    final String[] play = {"15", "20", "30", "45", "60", "75", "90", "105","120","140"};
    final String[] exercise = {"15", "20", "30", "45", "60", "75", "90", "105","120","140"};
    final String[] stillness = {"15", "20", "30", "45", "60", "75", "90", "105","120","140"};
    final String[] tv = {"1", "2", "3", "4", "5", "6", "7", "8","9","10"};
    final String[] computer = {"1", "2", "3", "4", "5", "6", "7", "8","9","10"};
    final String[] smart_phone ={"1", "2", "3", "4", "5", "6", "7", "8","9","10"};
    final String[] portion = {"1", "2", "3", "4", "5", "6", "7", "8","9","10"};
    final String[] portions ={"1", "2", "3", "4", "5", "6", "7", "8","9","10"};

    String sleephoursTypeTitle = "Select Type: ";
    //String sugaryTitle = "Select Sugary: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_wellness);
        cc=new CommanClass(this);
        ll_advance_wellness=(LinearLayout)findViewById(R.id.ll_advance_wellness);
        tv_hours = (TextView) findViewById(R.id.tv_hours);
        tv_sugary = (TextView) findViewById(R.id.tv_sugary);
        tv_play = (TextView) findViewById(R.id.tv_play);
        tv_exercise = (TextView) findViewById(R.id.tv_exercise);
        tv_stillness = (TextView) findViewById(R.id.tv_stillness);
        tv_tv = (TextView) findViewById(R.id.tv_tv);
        tv_computer = (TextView) findViewById(R.id.tv_computer);
        tv_smart_phone = (TextView) findViewById(R.id.tv_smart_phone);
        tv_portion = (TextView) findViewById(R.id.tv_portion);
        tv_portions = (TextView) findViewById(R.id.tv_portions);

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_wellness_done = (TextView) findViewById(R.id.tv_wellness_done);

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

        tv_hours.setOnTouchListener(this);
        tv_sugary.setOnTouchListener(this);
        tv_play.setOnTouchListener(this);
        tv_exercise.setOnTouchListener(this);
        tv_stillness.setOnTouchListener(this);
        tv_tv.setOnTouchListener(this);
        tv_computer.setOnTouchListener(this);
        tv_smart_phone.setOnTouchListener(this);
        tv_portion.setOnTouchListener(this);
        tv_portions.setOnTouchListener(this);

        tv_wellness_done.setOnTouchListener(this);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());

        String texm_text = "<font color=#FFFFFF><u>" + getString(R.string.date) + " " + ":" + " " + formattedDate + "</u></font>";
        tv_date.setText(Html.fromHtml(texm_text));

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

            case R.id.tv_hours:

                showPoupupMenu(getString(R.string.hours), sleephoursTypeTitle, sleephours);
                break;
            case R.id.tv_sugary:

                showPoupupMenu(getString(R.string.sugary_drunk), sleephoursTypeTitle, sugary);
                break;
            case R.id.tv_play:

                showPoupupMenu(getString(R.string.play), sleephoursTypeTitle, play);
                break;
            case R.id.tv_exercise:

                showPoupupMenu(getString(R.string.exercise), sleephoursTypeTitle, exercise);
                break;
            case R.id.tv_stillness:

                showPoupupMenu(getString(R.string.stillness), sleephoursTypeTitle, stillness);
                break;
            case R.id.tv_tv:

                showPoupupMenu(getString(R.string.tv), sleephoursTypeTitle, tv);
                break;
            case R.id.tv_computer:

                showPoupupMenu(getString(R.string.computer), sleephoursTypeTitle, computer);
                break;
            case R.id.tv_smart_phone:

                showPoupupMenu(getString(R.string.smart_phone), sleephoursTypeTitle, smart_phone);
                break;
            case R.id.tv_portion:

                showPoupupMenu(getString(R.string.portions), sleephoursTypeTitle, portion);
                break;
            case R.id.tv_portions:

                showPoupupMenu(getString(R.string.portions_of), sleephoursTypeTitle, portions);
                break;
            case R.id.tv_wellness_done:
/*
hours,sugary,play,exercise,stillness,tv,computer,smart_phone,portion,portions

                String hours = tv_hours.getText().toString().trim();
                String sugary = tv_sugary.getText().toString().trim();
                String play = tv_play.getText().toString().trim();
                String exercise = tv_exercise.getText().toString().trim();
                String stillness = tv_stillness.getText().toString().trim();
                String tv = tv_tv.getText().toString().trim();
                String computer = tv_computer.getText().toString().trim();
                String smart_phone = tv_smart_phone.getText().toString().trim();
                String portion = tv_portion.getText().toString().trim();
                String portions = tv_portions.getText().toString().trim();
*/

                if (!cc.isConnectingToInternet()) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.no_internet));
                } else if (tv_hours.getText().toString().trim().equals("")) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.enter_hours_sleep));
                } else if (tv_sugary.getText().toString().trim().equals("")) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.enter_sugary_drinks));
                } else if (tv_play.getText().toString().trim().equals("")) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.enter_play));
                } else if (tv_exercise.getText().toString().trim().equals("")) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.enter_exercise));
                } else if ( tv_stillness.getText().toString().trim().equals("")) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.enter_stillness));
                } else if (tv_tv.getText().toString().trim().equals("")) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.enter_tv));
                } else if (tv_computer.getText().toString().trim().equals("")) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.enter_computer));
                } else if (tv_smart_phone.getText().toString().trim().equals("")) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.enter_phone));
                } else if ( tv_portion.getText().toString().trim().equals("")) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.enter_fruits));
                } else if (tv_portions.getText().toString().trim().equals("")) {
                    cc.showSnackbar(ll_advance_wellness, getString(R.string.enter_veggies));
                } else {

//                    AdvanceWellnessDevelopmentRecord.deleteAll(AdvanceWellnessDevelopmentRecord.class);
                    Bundle bundle=new Bundle();

                    String date=tv_date.getText().toString().trim();
                    String sugaryDrinks=tv_sugary.getText().toString().trim();
                    String sleepHour=tv_hours.getText().toString().trim();
                    String playMinutes=convertMinToHour(tv_play.getText().toString().trim());
                    String exerciseMinutes= convertMinToHour(tv_exercise.getText().toString().trim());
                    String stillnessMinutes=convertMinToHour(tv_stillness.getText().toString().trim());
                    String tvHour=tv_tv.getText().toString().trim();
                    String computerHour=tv_computer.getText().toString().trim();
                    String phoneHour=tv_smart_phone.getText().toString().trim();
                    String portion=tv_portion.getText().toString().trim();
                    String portions=tv_portions.getText().toString().trim();

                    AdvanceWellnessDevelopmentRecord record= new AdvanceWellnessDevelopmentRecord();
                    record.setDate(date);
                    record.setHours(sleepHour);
                    record.setSugary(sugaryDrinks);
                    record.setPlay(playMinutes);
                    record.setExercise(exerciseMinutes);
                    record.setStillness(stillnessMinutes);
                    record.setTv(tvHour);
                    record.setComputer(computerHour);
                    record.setSmart_phone(phoneHour);
                    record.setPortion(portion);
                    record.setPortions(portions);
                    record.save();

                    bundle.putString("hours",sleepHour);
                    bundle.putString("sugary",sugaryDrinks);
                    bundle.putString("play",playMinutes);
                    bundle.putString("exercise" ,exerciseMinutes);
                    bundle.putString("stillness", stillnessMinutes);
                    bundle.putString("tv",tvHour);
                    bundle.putString("computer" ,computerHour);
                    bundle.putString("smart_phone" ,phoneHour);
                    bundle.putString("portion" ,portion);
                    bundle.putString("portions", portions);


                    Intent intent=new Intent(AdvancedWellness.this,GraphActivity.class);
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                    finish();

                }

                break;
        }

        return false;
    }

    public String convertMinToHour(final String min){
        float minute = Integer.parseInt(min);

        minute=minute/60;

        return minute+"";
    }

    private void showPoupupMenu(final String popupDialogType, final String title, final String[] items) {
        //List of items to be show in  alert Dialog are stored in array of strings/char sequences
        AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedWellness.this);
        //set the title for alert dialog
        builder.setTitle(title);
        //set items to alert dialog. i.e. our array , which will be shown as list view in alert dialog
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // setting the button text to the selected itenm from the list
                if (popupDialogType.equalsIgnoreCase(getString(R.string.hours))) {
                    tv_hours.setText(items[item]);
                } else if (popupDialogType.equalsIgnoreCase(getString(R.string.sugary_drunk))) {
                    tv_sugary.setText(items[item]);
                } else if (popupDialogType.equalsIgnoreCase(getString(R.string.play))) {
                    tv_play.setText(items[item]);
                } else if (popupDialogType.equalsIgnoreCase(getString(R.string.exercise))) {
                    tv_exercise.setText(items[item]);
                } else if (popupDialogType.equalsIgnoreCase(getString(R.string.stillness))) {
                    tv_stillness.setText(items[item]);
                } else if (popupDialogType.equalsIgnoreCase(getString(R.string.tv))) {
                    tv_tv.setText(items[item]);
                } else if (popupDialogType.equalsIgnoreCase(getString(R.string.computer))) {
                    tv_computer.setText(items[item]);
                } else if (popupDialogType.equalsIgnoreCase(getString(R.string.smart_phone))) {
                    tv_smart_phone.setText(items[item]);
                } else if (popupDialogType.equalsIgnoreCase(getString(R.string.portions))) {
                    tv_portion.setText(items[item]);
                } else if (popupDialogType.equalsIgnoreCase(getString(R.string.portions_of))) {
                    tv_portions.setText(items[item]);
                }
            }
        });
        //Creating CANCEL button in alert dialog, to dismiss the dialog box when nothing is selected
        builder.setCancelable(false)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //When clicked on CANCEL button the dalog will be dismissed
                        dialog.dismiss();
                    }
                });
        // Creating alert dialog
        AlertDialog alert = builder.create();
        //Showing alert dialog
        alert.show();
    }
}
