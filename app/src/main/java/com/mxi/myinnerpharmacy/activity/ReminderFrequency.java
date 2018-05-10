package com.mxi.myinnerpharmacy.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.model.Firstregistrationstep;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.RegistrationCommanClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReminderFrequency extends AppCompatActivity implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener {

    TextView tv_1, tv_2, tv_3, tv_4, tv_done, tv_sleeping_time, tv_wakeup_time;
    SeekBar seekBar_1, seekBar_2, seekBar_3, seekBar_4;
    String question_level = "", achieve_image = "", current_image = "", question_achive = "";
    LinearLayout ll_linear;
    CommanClass cc;
    RegistrationCommanClass rcc;
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    String sleeping_time = "", wakeup_time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_frequency);
        cc = new CommanClass(ReminderFrequency.this);
        rcc = new RegistrationCommanClass(ReminderFrequency.this);

        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_sleeping_time = (TextView) findViewById(R.id.tv_sleeping_time);
        tv_wakeup_time = (TextView) findViewById(R.id.tv_wakeup_time);

        seekBar_1 = (SeekBar) findViewById(R.id.seekBar_1);
        seekBar_2 = (SeekBar) findViewById(R.id.seekBar_2);
        seekBar_3 = (SeekBar) findViewById(R.id.seekBar_3);
        seekBar_4 = (SeekBar) findViewById(R.id.seekBar_4);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);

        tv_done.setOnTouchListener(this);
        tv_wakeup_time.setOnTouchListener(this);
        tv_sleeping_time.setOnTouchListener(this);

        seekBar_1.setOnSeekBarChangeListener(this);
        seekBar_2.setOnSeekBarChangeListener(this);
        seekBar_3.setOnSeekBarChangeListener(this);
        seekBar_4.setOnSeekBarChangeListener(this);

        seekBar_1.setProgress(4);
        seekBar_2.setProgress(4);
        seekBar_3.setProgress(4);
        seekBar_4.setProgress(4);

        String text_1 = "<font color=#FFFFFF>" + getString(R.string.state_colibration) + "</font>&nbsp;" + ":" + "&nbsp;" + "<font color=#FFFFFF><b>" + "4" + getString(R.string.hours) + "</b></font>";
        tv_1.setText(Html.fromHtml(text_1));
        String text_2 = "<font color=#FFFFFF>" + getString(R.string.heart_rate_monit) + "</font>&nbsp;" + ":" + "&nbsp;" + "<font color=#FFFFFF><b>" + "4" + getString(R.string.hours) + "</b></font>";
        tv_2.setText(Html.fromHtml(text_2));
        String text_3 = "<font color=#FFFFFF>" + getString(R.string.breathing_analysis) + "</font>&nbsp;" + ":" + "&nbsp;" + "<font color=#FFFFFF><b>" + "4" + getString(R.string.hours) + "</b></font>";
        tv_3.setText(Html.fromHtml(text_3));
        String text_4 = "<font color=#FFFFFF>" + getString(R.string.pause_task) + "</font>&nbsp;" + ":" + "&nbsp;" + "<font color=#FFFFFF><b>" + "4" + getString(R.string.hours) + "</b></font>";
        tv_4.setText(Html.fromHtml(text_4));

       /* question_achive = getIntent().getStringExtra("question_achive");
        question_level = getIntent().getStringExtra("question_level");
        achieve_image = getIntent().getStringExtra("achieve_image");
        current_image = getIntent().getStringExtra("current_image");*/

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.tv_done:

                rcc.savePrefBoolean("isReminderFreq", true);

                String text_s = tv_1.getText().toString().trim();
                String text_sc = text_s.substring(1 + text_s.lastIndexOf(":") + 1);

                Log.e("text_sc_l", text_sc.length() + "");
                Log.e("text_sc", text_sc);

                String text_h = tv_2.getText().toString().trim();
                String text_hrm = text_h.substring(1 + text_h.lastIndexOf(":") + 1);

                Log.e("text_hrm", text_hrm);

                String text_b = tv_3.getText().toString().trim();
                String text_ba = text_b.substring(1 + text_b.lastIndexOf(":") + 1);

                Log.e("text_ba", text_ba);

                String text_p = tv_4.getText().toString().trim();
                String text_pt = text_p.substring(1 + text_p.lastIndexOf(":") + 1);

                Log.e("text_pt", text_pt);

               /* if (text_sc.equals("0hr")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_state_colibration));
                } else if (text_hrm.equals("0hr")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_heart_rate_monit));
                } else if (text_ba.equals("0hr")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_breathing_analysis));
                } else if (text_pt.equals("0hr")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_pause_task));
                } else {*/
                try {
                    Firstregistrationstep frs = new Firstregistrationstep();

                    frs.setQuestion_achieve(question_achive);
                    frs.setQuestion_level(question_level);
                    frs.setCurrent_image(current_image);
                    frs.setAchieve_image(achieve_image);
                    frs.setState_colibration(text_sc);
                    frs.setHeart_ratemonitor(text_hrm);
                    frs.setBreathing_analysis(text_ba);
                    frs.setPause_task(text_pt);
                    frs.setBackup_time(sleeping_time);
                    frs.setSleeping_time(wakeup_time);

                    frs.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(ReminderFrequency.this, MainActivity.class));
                finish();
                //  }

                break;
            case R.id.tv_sleeping_time:

                mTimePicker = new TimePickerDialog(ReminderFrequency.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_sleeping_time.setText("Sleeping time" + " - " + selectedHour + ":" + selectedMinute);

                        try {
                            String[] bits = tv_sleeping_time.getText().toString().split(" - ");
                            sleeping_time = bits[bits.length - 1];
                            Log.e("substring", sleeping_time);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                // mTimePicker.setTitle("Select Time");

                mTimePicker.show();

                break;
            case R.id.tv_wakeup_time:

                mTimePicker = new TimePickerDialog(ReminderFrequency.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_wakeup_time.setText("Wakeup Time" + " - " + selectedHour + ":" + selectedMinute);

                        try {
                            String[] bits = tv_sleeping_time.getText().toString().split(" - ");
                            wakeup_time = bits[bits.length - 1];
                            Log.e("substring", wakeup_time);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();

                break;
        }
        return false;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBar_1:
                String text_1 = "<font color=#FFFFFF>" + getString(R.string.state_colibration) + "</font>&nbsp;" + ":" + "&nbsp;" + "<font color=#FFFFFF><b>" + progress + "" + getString(R.string.hours) + "</b></font>";
                tv_1.setText(Html.fromHtml(text_1));

                break;

            case R.id.seekBar_2:
                String text_2 = "<font color=#FFFFFF>" + getString(R.string.heart_rate_monit) + "</font>&nbsp;" + ":" + "&nbsp;" + "<font color=#FFFFFF><b>" + progress + "" + getString(R.string.hours) + "</b></font>";
                tv_2.setText(Html.fromHtml(text_2));

                break;
            case R.id.seekBar_3:

                String text_3 = "<font color=#FFFFFF>" + getString(R.string.breathing_analysis) + "</font>&nbsp;" + ":" + "&nbsp;" + "<font color=#FFFFFF><b>" + progress + "" + getString(R.string.hours) + "</b></font>";
                tv_3.setText(Html.fromHtml(text_3));
                break;
            case R.id.seekBar_4:

                String text_4 = "<font color=#FFFFFF>" + getString(R.string.pause_task) + "</font>&nbsp;" + ":" + "&nbsp;" + "<font color=#FFFFFF><b>" + progress + "" + getString(R.string.hours) + "</b></font>";
                tv_4.setText(Html.fromHtml(text_4));
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
