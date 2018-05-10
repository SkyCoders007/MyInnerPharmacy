package com.mxi.myinnerpharmacy.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.activity.MainActivity;
import com.mxi.myinnerpharmacy.activity.MedicalCabinets;
import com.mxi.myinnerpharmacy.activity.MyDeads;
import com.mxi.myinnerpharmacy.activity.ResourceJournal;
import com.mxi.myinnerpharmacy.model.Pausetask;
import com.mxi.myinnerpharmacy.model.PrescriptionTag;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends Fragment implements View.OnClickListener {

    ImageView iv_book, iv_redbtn, iv_resource_journal, iv_my_deads;
    TextView tv_top, tv_center, tv_2_center_1, tv_2_center_2, tv_bottom, tv_my_deads;
    ProgressDialog pDialog;
    CommanClass cc;
    ArrayList<PrescriptionTag> prescriptionlist;
    LinearLayout ll_linear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        cc = new CommanClass(getContext());

//        iv_book = (ImageView) rootView.findViewById(R.id.iv_book);
        tv_top = (TextView) rootView.findViewById(R.id.tv_home_top);
        tv_center = (TextView) rootView.findViewById(R.id.tv_home_first_center);
        tv_2_center_1 = (TextView) rootView.findViewById(R.id.tv_1_home_second_center);
        tv_2_center_2 = (TextView) rootView.findViewById(R.id.tv_2_home_second_center);
        tv_bottom = (TextView) rootView.findViewById(R.id.tv_home_bottom_most);
        iv_my_deads = (ImageView) rootView.findViewById(R.id.iv_home_my_deads);
        iv_redbtn = (ImageView) rootView.findViewById(R.id.iv_home_redbtn);
        iv_resource_journal = (ImageView) rootView.findViewById(R.id.iv_home_resource_journal);
        ll_linear = (LinearLayout) rootView.findViewById(R.id.ll_linear);

        initLsiteners();


        return rootView;
    }

    private void initLsiteners() {
//        iv_book.setOnClickListener(this);
        tv_top.setOnClickListener(this);
        tv_center.setOnClickListener(this);
        tv_2_center_1.setOnClickListener(this);
        tv_2_center_2.setOnClickListener(this);
        tv_bottom.setOnClickListener(this);
        iv_my_deads.setOnClickListener(this);
        iv_resource_journal.setOnClickListener(this);
        iv_redbtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.iv_book:
//                // startActivity(new Intent(getActivity(), MedicalCabinets.class));
//
//                break;
            case R.id.tv_home_top:
                startMedicalCabinateActivity(0, 1, 2);
               // Toast.makeText(getContext(), "Top", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_home_first_center:
                startMedicalCabinateActivity(3, 4, 5);
              //  Toast.makeText(getContext(), "FirstCenter", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_1_home_second_center:
                startMedicalCabinateActivity(6, 7, 8);
               // Toast.makeText(getContext(), "SecondCenter1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_2_home_second_center:
                startMedicalCabinateActivity(9, 10, 0);
              //  Toast.makeText(getContext(), "SecondCenter2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_home_bottom_most:
                startMedicalCabinateActivity(1, 2, 3);
               // Toast.makeText(getContext(), "Bottom Most", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_home_redbtn:
//                startActivity(new Intent(getActivity(), MedicalCabinets.class));
              //  Toast.makeText(getContext(), "red Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_home_resource_journal:
                startActivity(new Intent(getActivity(), ResourceJournal.class));
               // Toast.makeText(getContext(), "Resource Journal", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_home_my_deads:
                startActivity(new Intent(getActivity(), MyDeads.class));
               // Toast.makeText(getContext(), "My D.E.A.D's", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void startMedicalCabinateActivity(int i, int i1, int i2) {
        Intent intent = new Intent(getActivity(), MedicalCabinets.class);
        intent.putExtra("firstPrescription", prescriptionlist.get(i).getPrescription_name());
        intent.putExtra("firstPrescription_id", prescriptionlist.get(i).getTag_id());

        intent.putExtra("secondPrescription", prescriptionlist.get(i1).getPrescription_name());
        intent.putExtra("secondPrescription_id", prescriptionlist.get(i1).getTag_id());

        intent.putExtra("thirdPrescription", prescriptionlist.get(i2).getPrescription_name());
        intent.putExtra("thirdPrescription_id", prescriptionlist.get(i2).getTag_id());

        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            MainActivity.isSkip = false;
            MainActivity.StepLastColibration = false;
            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(getContext());
                pDialog.setMessage("Please wait...");
                pDialog.show();
                makeJsonGetPrescription();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void makeJsonGetPrescription() {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_prescription_tags,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_get_pres_tags", response);
                        jsonParsePauseTask(response);
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

    private void jsonParsePauseTask(String response) {
        try {
            pDialog.dismiss();
            prescriptionlist = new ArrayList<PrescriptionTag>();
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("200")) {

                JSONArray prescription = jsonObject.getJSONArray("prescription");

                for (int i = 0; i < prescription.length(); i++) {
                    JSONObject jsonObject1 = prescription.getJSONObject(i);

                    PrescriptionTag pk = new PrescriptionTag();//prescription_id,text
                    pk.setTag_id(jsonObject1.getString("prescription_id"));
                    pk.setPrescription_name(jsonObject1.getString("text"));

                    prescriptionlist.add(pk);

                }

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
