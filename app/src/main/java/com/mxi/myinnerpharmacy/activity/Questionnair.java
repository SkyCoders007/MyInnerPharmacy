package com.mxi.myinnerpharmacy.activity;

import android.app.ProgressDialog;
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

public class Questionnair extends AppCompatActivity implements View.OnTouchListener {

    ArrayList<AchiveQuestion> questionsArrayList;


    TextView tv_achieve_text;
    LinearLayout ll_linear;
    CommanClass cc;
    ProgressDialog pDialog;
    RadioGroup radio_group;
    String answer = "";
    ColorStateList colorStateList;
    RegistrationCommanClass rcc;
    TextView btn_next, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnair);
        cc = new CommanClass(Questionnair.this);
        rcc = new RegistrationCommanClass(Questionnair.this);

        tv_achieve_text = (TextView) findViewById(R.id.tv_achieve_text);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);

        ll_linear = (LinearLayout) findViewById(R.id.ll_linear);
        btn_next = (TextView) findViewById(R.id.btn_next);
        btn_back = (TextView) findViewById(R.id.btn_back);

        btn_next.setOnTouchListener(this);
        btn_back.setOnTouchListener(this);

        colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{getResources().getColor(R.color.color_white)}
        );

        if (cc.isConnectingToInternet()) {
            pDialog = new ProgressDialog(Questionnair.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            makeJsonQuestion();
        } else {
            cc.showSnackbar(ll_linear, getString(R.string.no_internet));
        }

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton choose_answer = (RadioButton) group.findViewById(checkedId);
                Log.e("checkedId", checkedId + "");
                if (null != choose_answer && checkedId > -1) {
                    answer = choose_answer.getText().toString().trim();
                    Log.e("answer", answer);

                    Log.e("Matched_id", questionsArrayList.get(checkedId).getAchive_id());
                    cc.savePrefString("selected_question_id", questionsArrayList.get(checkedId).getAchive_id());
                    cc.savePrefString("selected_question_text", questionsArrayList.get(checkedId).getAchive_question());

                    /*for (int i = 0; i < questionsArrayList.size(); i++) {
                        if(questionsArrayList.get(i).getAchive_question().equals(answer)){
                            Log.e("Matched_id",questionsArrayList.get(i).getAchive_id());
                            cc.savePrefString("selected_question_id",questionsArrayList.get(i).getAchive_id());
                            cc.savePrefString("selected_question_text",questionsArrayList.get(i).getAchive_question());
                        }
                    }*/

                }

            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.btn_next:
                if (answer.equals("")) {
                    cc.showSnackbar(ll_linear, getString(R.string.select_question));
                } else {

                    rcc.savePrefBoolean("isQuestion", true);

//                    makeJsonSendAnswer();

                    startActivity(new Intent(Questionnair.this, QuestionLevel.class).putExtra("question_achive", answer));
                    finish();


                }
                break;
            case R.id.btn_back:
                startActivity(new Intent(Questionnair.this, LoginScreen.class));
                finish();
                break;
        }
        return false;
    }

/*    private void makeJsonSendAnswer() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_send_want_achivement,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_want_achivement", response);

                        startActivity(new Intent(Questionnair.this, QuestionLevel.class).putExtra("question_achive", answer));
                        finish();
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
                params.put("prescription_text",answer);
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
    }*/

    private void makeJsonQuestion() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_question_achive,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_question_achive", response);
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
            //  questionlist = new ArrayList<Questionoption>();
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            Typeface font = Typeface.createFromAsset(Questionnair.this.getAssets(), "fonts/Raleway-Regular.ttf");
            if (jsonObject.getString("status").equals("200")) {
                questionsArrayList = new ArrayList<AchiveQuestion>();
                JSONArray question = jsonObject.getJSONArray("question");
                JSONObject data = question.getJSONObject(0);
                String text_login = "<font color=#FFFFFF><i>" + data.getString("text") + "</i></font>";
                tv_achieve_text.setText(Html.fromHtml(text_login));
                JSONArray options = jsonObject.getJSONArray("options");
                for (int i = 0; i < options.length(); i++) {
                    JSONObject jsonObject1 = options.getJSONObject(i);
                    AchiveQuestion achiveQuestion = new AchiveQuestion();
                    achiveQuestion.setAchive_question(jsonObject1.getString("text"));
                    achiveQuestion.setAchive_id(jsonObject1.getString("id"));
                    questionsArrayList.add(achiveQuestion);

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
}
