package com.mxi.myinnerpharmacy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.model.heartrate;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.util.ArrayList;

/**
 * Created by sonali on 18/3/17.
 */
public class AdapterHeartRateHistory extends RecyclerView.Adapter<AdapterHeartRateHistory.CustomViewHolder> {
    private Context context;
    ArrayList<heartrate> heartlist;
    private LayoutInflater mInflater;
    CommanClass cc;

    public AdapterHeartRateHistory(Context context, ArrayList<heartrate> heartlist) {
        this.context = context;
        this.heartlist = heartlist;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.ow_heartrate_history, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

            final heartrate list = heartlist.get(i);
        holder.tv_heartrate.setText(list.getId() + "  " + "  " + "  " + "  " +"  "+"  "+ list.getHeart_rate());
        holder.tv_date.setText(list.getDate());
    }



    @Override
    public int getItemCount() {
        return (null != heartlist ? heartlist.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_heartrate, tv_date;

        public CustomViewHolder(View convertView) {
            super(convertView);

            tv_heartrate = (TextView) convertView.findViewById(R.id.tv_heartrate);
            tv_date = (TextView) convertView.findViewById(R.id.tv_date);

        }
    }

}