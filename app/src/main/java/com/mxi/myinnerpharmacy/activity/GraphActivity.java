package com.mxi.myinnerpharmacy.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.mxi.myinnerpharmacy.R;
import com.mxi.myinnerpharmacy.model.AdvanceWellnessDevelopmentRecord;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends ActionBarActivity {


    List<AdvanceWellnessDevelopmentRecord> recordsList;
    String hours,sugary,play,exercise,stillness,tv,computer,smart_phone,portion,portions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        recordsList= AdvanceWellnessDevelopmentRecord.listAll(AdvanceWellnessDevelopmentRecord.class);
        getDataFromIntent();


        BarChart chart = (BarChart) findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("Advance Wellness Development Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }


    private void getDataFromIntent() {

        Bundle bundle=getIntent().getBundleExtra("bundle");
        hours=bundle.getString("hours");
        sugary=bundle.getString("sugary");
        play=bundle.getString("play");
        exercise=bundle.getString("exercise");
        stillness=bundle.getString("stillness");
        tv=bundle.getString("tv");
        computer=bundle.getString("computer");
        smart_phone=bundle.getString("smart_phone");
        portion=bundle.getString("portion");
        portions=bundle.getString("portions");

    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        ArrayList<BarEntry> valueSet4 = new ArrayList<>();
        ArrayList<BarEntry> valueSet5 = new ArrayList<>();
        ArrayList<BarEntry> valueSet6 = new ArrayList<>();
        ArrayList<BarEntry> valueSet7 = new ArrayList<>();
        ArrayList<BarEntry> valueSet8 = new ArrayList<>();


        Log.e("LENGTH",recordsList.size()+"");
        for (int i = 0; i < recordsList.size(); i++) {

            AdvanceWellnessDevelopmentRecord record =recordsList.get(i);



//        BarEntry v1e1 = new BarEntry(Float.parseFloat(hours), i); // Jan
        BarEntry v1e1 = new BarEntry(Float.parseFloat(record.getHours()), i); // Jan
//      Set for loop here
        valueSet1.add(v1e1);


        BarEntry v2e1 = new BarEntry(Float.parseFloat(record.getSugary()), i); // Jan
//      Set for loop here
        valueSet2.add(v2e1);


        BarEntry v3e1 = new BarEntry(Float.parseFloat(record.getPlay()), i); // Jan
//      Set for loop here
        valueSet3.add(v3e1);


        BarEntry v4e1 = new BarEntry(Float.parseFloat(record.getExercise()), i); // Jan
//      Set for loop here
        valueSet4.add(v4e1);


        BarEntry v5e1 = new BarEntry(Float.parseFloat(record.getStillness()), i); // Jan
//      Set for loop here
        valueSet5.add(v5e1);


        BarEntry v6e1 = new BarEntry(Float.parseFloat(record.getTv()), i); // Jan
//      Set for loop here
        valueSet6.add(v6e1);


        BarEntry v7e1 = new BarEntry(Float.parseFloat(record.getComputer()), i); // Jan
//      Set for loop here
        valueSet7.add(v7e1);


        BarEntry v8e1 = new BarEntry(Float.parseFloat(record.getSmart_phone()), i); // Jan
//      Set for loop here
        valueSet8.add(v8e1);

        }
//hours,sugary,play,exercise,stillness,tv,computer,smart_phone,portion,portions

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Sleep");
        barDataSet1.setColor(Color.rgb(102, 255, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Sugary Drinks");
        barDataSet2.setColor(Color.rgb(255, 55, 51));
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Play");
        barDataSet3.setColor(Color.rgb(155, 155, 0));
        BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Exercise");
        barDataSet4.setColor(Color.rgb(204, 102, 0));
        BarDataSet barDataSet5 = new BarDataSet(valueSet5, "Stillness");
        barDataSet5.setColor(Color.rgb(51, 255, 153));
        BarDataSet barDataSet6 = new BarDataSet(valueSet6, "Television");
        barDataSet6.setColor(Color.rgb(102, 102, 255));
        BarDataSet barDataSet7 = new BarDataSet(valueSet7, "Computer");
        barDataSet7.setColor(Color.rgb(255, 150, 255));
        BarDataSet barDataSet8 = new BarDataSet(valueSet8, "Smart Phone");
        barDataSet8.setColor(Color.rgb(0, 160,150));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        dataSets.add(barDataSet4);
        dataSets.add(barDataSet5);
        dataSets.add(barDataSet6);
        dataSets.add(barDataSet7);
        dataSets.add(barDataSet8);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();

        for (int i = 0; i < recordsList.size() ; i++) {
            xAxis.add(recordsList.get(i).getDate());
        }
        return xAxis;
    }
}
