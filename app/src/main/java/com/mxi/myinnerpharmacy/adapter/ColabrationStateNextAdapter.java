package com.mxi.myinnerpharmacy.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.model.Colibrationstate;
import com.mxi.myinnerpharmacy.network.ImageLoadedCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonali on 5/1/17.
 */
public class ColabrationStateNextAdapter extends PagerAdapter {


    private LayoutInflater inflater;
    Context mcontext;
    ArrayList<Colibrationstate> colibrationstate;


    public ColabrationStateNextAdapter(Context context, ArrayList<Colibrationstate> colibrationstate) {
        // TODO Auto-generated constructor stub
        this.mcontext = context;
        this.colibrationstate = colibrationstate;

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    @Override
    public void finishUpdate(View container) {
    }

    @Override
    public int getCount() {
        return colibrationstate.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stu
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        //final ViewHolder viewholder = null;
        this.inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View imageLayout = inflater.inflate(R.layout.row_calibration_slide,
                container, false);


        final ViewHolder viewholder = new ViewHolder();
        viewholder.tv_name = (TextView) imageLayout.findViewById(R.id.tv_name);
        viewholder.iv_colib_state_image = (ImageView) imageLayout.findViewById(R.id.iv_colib_state_image);
        viewholder.progressBar = (ProgressBar) imageLayout.findViewById(R.id.progressBar);

        viewholder.tv_name.setText(colibrationstate.get(position).getName());
        Picasso.with(mcontext).
                load(colibrationstate.get(position).getImage()).
                into(viewholder.iv_colib_state_image, new ImageLoadedCallback(viewholder.progressBar) {
                    @Override
                    public void onSuccess() {
                        if (viewholder.progressBar != null) {
                            viewholder.progressBar.setVisibility(View.GONE);

                        }
                    }
                });

        ((ViewPager) container).addView(imageLayout, 0);


        return imageLayout;
    }

    class ViewHolder {
        TextView tv_name, tv_no;
        ImageView iv_colib_state_image;
        ProgressBar progressBar;
    }


}