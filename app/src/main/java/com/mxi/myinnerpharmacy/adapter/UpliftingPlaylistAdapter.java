package com.mxi.myinnerpharmacy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.model.uplifting_playlist;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.util.ArrayList;

/**
 * Created by sonali on 25/2/17.
 */
public class UpliftingPlaylistAdapter extends RecyclerView.Adapter<UpliftingPlaylistAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    ArrayList<uplifting_playlist> playlist;
    CommanClass cc;

    public UpliftingPlaylistAdapter(Context context, ArrayList<uplifting_playlist> pausetasklist) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        playlist = pausetasklist;
        cc = new CommanClass(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;

        View view = inflater.inflate(R.layout.row_uplifting_playlist, parent, false);
        holder = new MyViewHolder(view, viewType);

        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_text.setText(playlist.get(position).getAudio_title());

    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;
        // ImageView iv_image;

        public MyViewHolder(View itemView, int ViewType) {
            super(itemView);


            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            // iv_image = (ImageView) itemView.findViewById(R.id.iv_image);

        }
    }
}