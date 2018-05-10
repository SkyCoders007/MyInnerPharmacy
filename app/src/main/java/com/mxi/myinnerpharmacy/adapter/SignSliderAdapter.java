package com.mxi.myinnerpharmacy.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxi.myinnerpharmacy.R;


/**
 * Created by admin1 on 26/5/16.
 */
public class SignSliderAdapter extends PagerAdapter {


    private LayoutInflater inflater;
    Context mcontext;
    String[] image_list;


    public SignSliderAdapter(Context context, String[] imagelist) {
        // TODO Auto-generated constructor stub
        this.mcontext = context;
        this.image_list = imagelist;

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
        return image_list.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stu
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        this.inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View imageLayout = inflater.inflate(R.layout.row_sign_slider,
                container, false);

        ViewHolder viewholder = null;
        viewholder = new ViewHolder();
        viewholder.tv_text = (TextView) imageLayout.findViewById(R.id.tv_text);

        viewholder.tv_text.setText(image_list[position]);

        ((ViewPager) container).addView(imageLayout, 0);


        return imageLayout;
    }

    class ViewHolder {
        TextView tv_text;
    }


}