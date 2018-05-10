package com.mxi.myinnerpharmacy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.model.AchiveQuestion;
import com.mxi.myinnerpharmacy.model.DifficultyLevel;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.RegistrationCommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionLevel extends AppCompatActivity implements View.OnClickListener {
    ColorStateList colorStateList;
    TextView tv_question_text, tv_question;
    //    RadioButton button_1, button_2, button_3, button_4, button_5;
    RadioGroup radio_group;
    Typeface font;
    LinearLayout ll_linear;
    String question_level = "";
    CommanClass cc;
    String question_achive;
    RegistrationCommanClass rcc;
    ArrayList<DifficultyLevel> difficultyArrayList;
    TextView btn_next, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_level);

        cc = new CommanClass(QuestionLevel.this);
        rcc = new RegistrationCommanClass(QuestionLevel.this);

        tv_question_text = (TextView) findViewById(R.id.tv_question_text);
        tv_question = (TextView) findViewById(R.id.tv_question);
        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);

        btn_next = (TextView) findViewById(R.id.btn_next);
        btn_back = (TextView) findViewById(R.id.btn_back);

        radio_group = (RadioGroup) findViewById(R.id.radio_group);

        font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");

        jsonCallDifficultyLevel();
        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        try {
            question_achive = getIntent().getStringExtra("question_achive");

        } catch (Exception e) {
            e.printStackTrace();
        }

        colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{getResources().getColor(R.color.color_white)}
        );

        tv_question.setText(Html.fromHtml("<font color=#FFFFFF><i>" + question_achive + "</i></font>"));
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton choose_answer = (RadioButton) group.findViewById(checkedId);

                if (null != choose_answer && checkedId > -1) {
                    question_level = choose_answer.getText().toString();
                    Log.e("question_level", question_level);
                    for (int i = 0; i < difficultyArrayList.size(); i++) {
                        if (difficultyArrayList.get(i).getText().equals(question_level)) {
                            cc.savePrefString("difficulty_text", difficultyArrayList.get(i).getText());
                            cc.savePrefString("difficulty_id", difficultyArrayList.get(i).getId());
                            Log.e("question_level", cc.loadPrefString("difficulty_id"));
                        }
                    }

                }

            }
        });

    }

    private void jsonCallDifficultyLevel() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_questionaries_level,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_get_Difficulty", response);
                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {
                                jsonParseMatchList(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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

        try {
            JSONObject jsonObject = new JSONObject(response);
            Typeface font = Typeface.createFromAsset(QuestionLevel.this.getAssets(), "fonts/Raleway-Regular.ttf");
            if (jsonObject.getString("status").equals("200")) {
                difficultyArrayList = new ArrayList<DifficultyLevel>();

                JSONArray options = jsonObject.getJSONArray("options");
                for (int i = 0; i < options.length(); i++) {
                    JSONObject jsonObject1 = options.getJSONObject(i);
                    DifficultyLevel level = new DifficultyLevel();
                    level.setText(jsonObject1.getString("text"));
                    level.setId(jsonObject1.getString("id"));
                    difficultyArrayList.add(level);

                    AppCompatRadioButton button = new AppCompatRadioButton(this);
                    button.setId(i);
                    button.setText(jsonObject1.getString("text"));
                    button.setTypeface(font);
                    radio_group.addView(button);
                    button.setSupportButtonTintList(colorStateList);
                    button.setTextColor(Color.parseColor("#FFFFFF"));
                    button.setHighlightColor(Color.WHITE);
                    button.setTextSize(16);
                }
            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (question_level.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_question));
                } else {
                    rcc.savePrefBoolean("isQuestionLevel", true);

                    startActivity(new Intent(QuestionLevel.this, UploadPic.class).putExtra("question_achive", question_achive).
                            putExtra("question_level", question_level));
                    finish();
                }
                break;
            case R.id.btn_back:
                startActivity(new Intent(QuestionLevel.this, Questionnair.class));
                finish();
                break;
        }
    }
}
