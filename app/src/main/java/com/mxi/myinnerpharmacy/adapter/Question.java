package com.mxi.myinnerpharmacy.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.activity.Questionnair;
import com.mxi.myinnerpharmacy.model.Questionoption;
import com.mxi.myinnerpharmacy.network.CommanClass;

import java.util.ArrayList;


/**
 * Created by sonali on 2/1/17.
 */
public class Question extends RecyclerView.Adapter<Question.CustomViewHolder> {
    private Context context;
    ArrayList<Questionoption> questionlist;
    private LayoutInflater mInflater;
    CommanClass cc;

    public Question(Questionnair context, ArrayList<Questionoption> questionlist) {
        this.context = context;
        this.questionlist = questionlist;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.row_questions, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        //    holder.radio_group.setTag(i);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Regular.ttf");

        holder.button_1.setTypeface(font);

        final Questionoption list = questionlist.get(i);
        // holder.radio_group.setTag(i);
        holder.button_1.setText(list.getText());

        /*if (list.checkedId > -1) {
            RadioButton choose_answer = (RadioButton) holder.radio_group.findViewById(list.checkedId);
            choose_answer.setChecked(true);
            holder.button_1.setText(list.getText());
        } else {
            holder.radio_group.clearCheck();
            holder.button_1.setText(list.getText());
        }*/

       /* holder.radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position = Integer.parseInt(group.getTag().toString());
                questionlist.get(position).checkedId = checkedId;
                RadioButton choose_answer = (RadioButton) group.findViewById(checkedId);

                if (null != choose_answer && checkedId > -1) {

                    questionlist.get(position).setAnswer(choose_answer.getText().toString());

                }

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return (null != questionlist ? questionlist.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected RadioGroup radio_group;
        protected RadioButton button_1 = null;

        public CustomViewHolder(View convertView) {
            super(convertView);

            button_1 = (RadioButton) convertView.findViewById(R.id.button_1);
            radio_group = (RadioGroup) convertView.findViewById(R.id.radio_group);
            cc = new CommanClass(context);

        }
    }

}