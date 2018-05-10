package com.mxi.myinnerpharmacy.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.activity.RapidResponsePrescription;
import com.mxi.myinnerpharmacy.model.Pausetask;
import com.mxi.myinnerpharmacy.model.PrescriptionDetails;
import com.mxi.myinnerpharmacy.network.AppController;
import com.mxi.myinnerpharmacy.network.CommanClass;
import com.mxi.myinnerpharmacy.network.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by android on 23/1/17.
 */
public class RapidResponseAdapter extends RecyclerView.Adapter<RapidResponseAdapter.MyViewHolder> {
    ProgressDialog pDialog;
    public static ArrayList<PrescriptionDetails> rapid_audioPrescriptionList;
    public static ArrayList<PrescriptionDetails> rapid_textPrescriptionList;
    public static ArrayList<PrescriptionDetails> rapid_videoPrescriptionList;
    private LayoutInflater inflater;
    private Context context;
    ArrayList<Pausetask> rapidresponcelist;
    CommanClass cc;

    public RapidResponseAdapter(Context context, ArrayList<Pausetask> rapidresponcelist) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.rapidresponcelist = rapidresponcelist;
        cc = new CommanClass(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;

        View view = inflater.inflate(R.layout.row_pause_task, parent, false);
        holder = new MyViewHolder(view, viewType);

        return holder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_text.setText(rapidresponcelist.get(position).getTask_name());
        holder.tv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callWs(rapidresponcelist.get(position).getTask_id(), position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return rapidresponcelist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;

        public MyViewHolder(View itemView, int ViewType) {
            super(itemView);


            tv_text = (TextView) itemView.findViewById(R.id.tv_text);

        }
    }

    public void callWs(String prescription_id, int pos) {

        try {
            if (cc.isConnectingToInternet()) {
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Please wait...");
                pDialog.show();
                makeJsonGetRapidResponsePrescription(prescription_id, pos);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void makeJsonGetRapidResponsePrescription(final String prescription_id, final int pos) {
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL.Url_get_quick_remedy_prescription,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Url_getquick_remedy_pre", response);
                        jsonGetRapidResponsePrescription(response, pos);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
//                cc.showSnackbar(recycler_view, getString(R.string.ws_error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("prescription_id", prescription_id);

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

    private void jsonGetRapidResponsePrescription(String response, int pos) {
        try {
            rapid_audioPrescriptionList = new ArrayList<PrescriptionDetails>();
            rapid_videoPrescriptionList = new ArrayList<PrescriptionDetails>();
            rapid_textPrescriptionList = new ArrayList<PrescriptionDetails>();
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                String video = jsonObject.getString("prescription_videopath");
                String audio = jsonObject.getString("prescription_audiopath");
                String text = jsonObject.getString("prescription_textpath");

                JSONArray prescription = jsonObject.getJSONArray("remedy");

                for (int i = 0; i < prescription.length(); i++) {
                    JSONObject jsonObject1 = prescription.getJSONObject(i);

                    PrescriptionDetails pk = new PrescriptionDetails();
                    pk.setMedia_type(jsonObject1.getString("media_type"));
                    pk.setMedia_name(jsonObject1.getString("media_name"));
                    pk.setMedia_id(jsonObject1.getString("media_id"));

                    String media = jsonObject1.getString("media_type");
                    if (media.equals("Audio")) {
                        // pk.setPrescription_audiopath(audio);
                        pk.setMedia_file(audio + jsonObject1.getString("media_file"));
                        rapid_audioPrescriptionList.add(pk);
                    } else if (media.equals("Video")) {
                        //pk.setPrescription_videopath(video);
                        pk.setMedia_file(video + jsonObject1.getString("media_file"));
                        rapid_videoPrescriptionList.add(pk);
                    } else if (media.equals("Text")) {
                        //pk.setPrescription_textpath(text);
                        pk.setMedia_file(text + jsonObject1.getString("media_file"));
                        rapid_textPrescriptionList.add(pk);
                    }

                }

                Log.e("rapid_audioList", rapid_audioPrescriptionList.size() + "");
                Log.e("rapid_videoList", rapid_videoPrescriptionList.size() + "");
                Log.e("rapid_textList", rapid_textPrescriptionList.size() + "");

                Intent intent = new Intent(context, RapidResponsePrescription.class);
                intent.putExtra("Rapid_response_id", rapidresponcelist.get(pos).getTask_id());
                intent.putExtra("Rapid_response_name", rapidresponcelist.get(pos).getTask_name());
                context.startActivity(intent);

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
