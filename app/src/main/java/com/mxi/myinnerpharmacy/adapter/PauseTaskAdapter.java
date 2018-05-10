package com.mxi.myinnerpharmacy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.model.Pausetask;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.util.ArrayList;

/**
 * Created by sonali on 6/1/17.
 */
public class PauseTaskAdapter extends RecyclerView.Adapter<PauseTaskAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    ArrayList<Pausetask> pausetasklist;
    CommanClass cc;

    public PauseTaskAdapter(Context context, ArrayList<Pausetask> pausetasklist) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.pausetasklist = pausetasklist;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_text.setText(pausetasklist.get(position).getTask_name());

    }

    @Override
    public int getItemCount() {
        return pausetasklist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;

        public MyViewHolder(View itemView, int ViewType) {
            super(itemView);


            tv_text = (TextView) itemView.findViewById(R.id.tv_text);

        }
    }

}