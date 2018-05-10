package com.mxi.myinnerpharmacy.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.network.CommanClass;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private String mNavTitles[];
    private TypedArray mIcons;
    CommanClass cc;

    public NavigationDrawerAdapter(Context context, String Titles[], TypedArray Icons) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mNavTitles = Titles;
        mIcons = Icons;
        cc = new CommanClass(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;

        View view = inflater.inflate(R.layout.row_navigation, parent, false);
        holder = new MyViewHolder(view, viewType);

        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.title.setText(mNavTitles[position]);
        holder.rowIcon.setImageResource(mIcons.getResourceId(position, 0));

    }

    @Override
    public int getItemCount() {
        return mNavTitles.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView rowIcon;

        public MyViewHolder(View itemView, int ViewType) {
            super(itemView);


            title = (TextView) itemView.findViewById(R.id.rowText);
            rowIcon = (ImageView) itemView.findViewById(R.id.rowIcon);

        }
    }

}