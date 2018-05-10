package com.mxi.myinnerpharmacy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.activity.PlayPrescriptionDetail;
import com.mxi.myinnerpharmacy.model.PrescriptionDetails;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.util.ArrayList;

/**
 * Created by android on 21/1/17.
 */
public class PrescriptionListAdapter extends RecyclerView.Adapter<PrescriptionListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    ArrayList<PrescriptionDetails> prescriptionlist;
    CommanClass cc;

    public PrescriptionListAdapter(Context context, ArrayList<PrescriptionDetails> prescriptionlist) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.prescriptionlist = prescriptionlist;
        cc = new CommanClass(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;

        View view = inflater.inflate(R.layout.row_prescription_list, parent, false);
        holder = new MyViewHolder(view, viewType);

        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_text.setText(prescriptionlist.get(position).getMedia_name());
        if (position == getItemCount() - 1) {
            holder.view_line.setVisibility(View.INVISIBLE);
        }
        holder.tv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prescriptionlist.get(position).getMedia_type().equals("Video")) {
                    Intent intent = new Intent(context, PlayPrescriptionDetail.class);
                    intent.putExtra("media_name", prescriptionlist.get(position).getMedia_name());
                    intent.putExtra("media_type", prescriptionlist.get(position).getMedia_type());
                    intent.putExtra("media_file", prescriptionlist.get(position).getMedia_file());
                    context.startActivity(intent);
                } else if (prescriptionlist.get(position).getMedia_type().equals("Audio")) {
                    Intent intent = new Intent(context, PlayPrescriptionDetail.class);
                    intent.putExtra("media_name", prescriptionlist.get(position).getMedia_name());
                    intent.putExtra("media_type", prescriptionlist.get(position).getMedia_type());
                    intent.putExtra("media_file", prescriptionlist.get(position).getMedia_file());
                    context.startActivity(intent);
                } else if (prescriptionlist.get(position).getMedia_type().equals("Text")) {
                    Intent intent = new Intent(context, PlayPrescriptionDetail.class);
                    intent.putExtra("media_name", prescriptionlist.get(position).getMedia_name());
                    intent.putExtra("media_type", prescriptionlist.get(position).getMedia_type());
                    intent.putExtra("media_file", prescriptionlist.get(position).getMedia_file());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return prescriptionlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;
        ImageView iv_edit, iv_delete;
        View view_line;

        public MyViewHolder(View itemView, int ViewType) {
            super(itemView);


            tv_text = (TextView) itemView.findViewById(R.id.tv_row_pres_list);
            iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit_row_prescription);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete_row_prescription);
            view_line = (View) itemView.findViewById(R.id.view_row_prescription_list);

        }
    }

}
