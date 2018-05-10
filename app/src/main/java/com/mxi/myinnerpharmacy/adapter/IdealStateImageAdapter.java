/*
package com.mxi.myinnerpharmacy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mxi.myinnerpharmacy.R;

import com.mxi.myinnerpharmacy.model.Idealstateimage;

import java.util.ArrayList;

*/
/**
 * Created by sonali on 4/1/17.
 *//*

public class IdealStateImageAdapter extends BaseAdapter {
    private Context context;
    ArrayList<Idealstateimage> stateimagelist;
    private LayoutInflater mInflater;
    Integer selected_position = -1;

    public IdealStateImageAdapter(Context context, ArrayList<Idealstateimage> stateimagelist) {
        this.context = context;
        this.stateimagelist = stateimagelist;
    }

    @Override
    public int getCount() {
        return stateimagelist.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return stateimagelist.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_ideal_state_image, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context)
                .load(stateimagelist.get(position).getMedia_file())
                .placeholder(R.mipmap.choose_image)
                .into(viewHolder.iv_choose);

        viewHolder.checkBox1.setChecked(position == selected_position);

        viewHolder.checkBox1
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        //  stateimagelist.get(position).checked = isChecked;
                        if (isChecked) {
                            selected_position = position;
                        } else {
                            selected_position = -1;
                        }
                        notifyDataSetChanged();

                    }
                });
       */
/* viewHolder.iv_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (stateimagelist.get(position).checked) {
                    stateimagelist.get(position).checked = false;
                    viewHolder.checkBox1.setChecked(false);

                } else {
                    stateimagelist.get(position).checked = true;
                    viewHolder.checkBox1.setChecked(true);

                }

            }
        });*//*


        return convertView;
    }

    private class ViewHolder {
        protected ImageView iv_choose;
        CheckBox checkBox1;

        public ViewHolder(View view) {

            iv_choose = (ImageView) view.findViewById(R.id.iv_choose);
            checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
        }
    }
}*/
