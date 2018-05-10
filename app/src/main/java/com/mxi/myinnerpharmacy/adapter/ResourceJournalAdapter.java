package com.mxi.myinnerpharmacy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.activity.ResourceJournalDetail;
import com.mxi.myinnerpharmacy.model.ResourceJournalItem;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.util.ArrayList;

/**
 * Created by android on 19/1/17.
 */
public class ResourceJournalAdapter extends RecyclerView.Adapter<ResourceJournalAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    ArrayList<ResourceJournalItem> resourceJournallist;
    CommanClass cc;

    public ResourceJournalAdapter(Context context, ArrayList<ResourceJournalItem> resourceJournallist) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.resourceJournallist = resourceJournallist;
        cc = new CommanClass(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;

        View view = inflater.inflate(R.layout.row_resource_journal, parent, false);
        holder = new MyViewHolder(view, viewType);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (resourceJournallist.get(position).isFromMydeads())
            holder.iv_audio.setVisibility(View.GONE);

        holder.tv_title_text.setText(resourceJournallist.get(position).getTitle());
        if (resourceJournallist.get(position).getAudio_file_path().equals("")) {
            holder.iv_audio.setVisibility(View.INVISIBLE);
        } else {
            holder.iv_audio.setVisibility(View.VISIBLE);
        }
         holder.tv_text.setText(resourceJournallist.get(position).getDescription());
         holder.tv_text.setVisibility(View.GONE);


        holder.ll_row_resource_journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ResourceJournalDetail.class);
                intent.putExtra("res_title", resourceJournallist.get(position).getTitle());

                intent.putExtra("res_descr", resourceJournallist.get(position).getDescription());

               /* if (!resourceJournallist.get(position).isFromMydeads() || !resourceJournallist.get(position).getAudio_file_path().equals("")) {
                    intent.putExtra("res_audio", cc.loadPrefString("resource_journal_file_path") + resourceJournallist.get(position).getAudio_file_path());
                }*/

                if (!resourceJournallist.get(position).isFromMydeads()) {
                    intent.putExtra("res_from", "ResourceJournal");
                    intent.putExtra("res_audio", cc.loadPrefString("resource_journal_file_path") + resourceJournallist.get(position).getAudio_file_path());
                    intent.putExtra("res_audio_name", resourceJournallist.get(position).getAudio_file_path());
                } else {
                    intent.putExtra("res_audio", " ");
                    intent.putExtra("res_from", "Mydeads");
                }
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return resourceJournallist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title_text, tv_text;
        ImageView iv_audio;
        LinearLayout ll_row_resource_journal;

        public MyViewHolder(View itemView, int ViewType) {
            super(itemView);

            tv_title_text = (TextView) itemView.findViewById(R.id.tv_title_raw_resource_journal);
            tv_text = (TextView) itemView.findViewById(R.id.tv_description_raw_resource_journal);
            ll_row_resource_journal = (LinearLayout) itemView.findViewById(R.id.ll_row_resource_journal);
            iv_audio = (ImageView) itemView.findViewById(R.id.iv_audio_row_resource_journal);
        }
    }

}
