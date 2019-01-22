package com.tenneco.tennecoapp.Graphics;

import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.Reject;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;
import com.tenneco.tennecoapp.Utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GraphicActivity extends AppCompatActivity {
    private DatabaseReference dbPLines;
    private DatabaseReference dbLine;
    private ArrayList<Line> mLines;
    ArrayList<BarEntry> actual1;
    ArrayList<BarEntry> actual2;
    ArrayList<BarEntry> actual3;
    ArrayList<BarEntry> lineBar;
    private String lineId;
    private Line mLine;
    private String[] xasis;
    private int selector=0;
    private   Query postsQuery;
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mLines = new ArrayList<>();
            actual1 = new ArrayList<>();
            actual2 = new ArrayList<>();
            actual3 = new ArrayList<>();
            lineBar = new ArrayList<>();
            int i=0;
            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
            {
                Line line = itemSnapshot.getValue(Line.class);
                if (line!=null) {
                    mLines.add(line);

                }
            }

            xasis = new String[mLines.size()+1];
            for ( i=0; i<mLines.size(); i++)
                xasis[i]= mLines.get(i).getDate();
            xasis[i]= Utils.getDateString();
                /*mLines = new ArrayList<>();
                for (int size = lines.size()-1; size>=0 ; size--)
                    mLines.add(lines.get(size));

                    */
            selector();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    @BindView(R.id.barChart) BarChart mBarChart;
    @BindView(R.id.tv_name) TextView mTvTitle;
    @BindView(R.id.sp_selector) Spinner mSpSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);
        ButterKnife.bind(this);
        dbPLines = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(this)).child(Line.DB_PRODUCTION_LINE);
        dbLine =  FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(this)).child(Line.DB_LINE);
        if (getIntent().getExtras()!= null)
            lineId = getIntent().getExtras().getString("lineId");
        else
            finish();
        configChart();
        getLine();
        getLines();
        initSpinner();
    }

    public void initSpinner(){
        mSpSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selector=i;
                selector();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void getLines() {


        postsQuery = dbPLines.orderByChild("parentId").equalTo(lineId);
//            postsQuery = dbPLines.orderByChild("parentId").equalTo(lineId).limitToLast(2);
        postsQuery.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        postsQuery.removeEventListener(valueEventListener);
    }

    public void getLine() {
        Query postsQuery;
        postsQuery = dbLine.child(lineId);
        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLine = dataSnapshot.getValue(Line.class);
                if (mLine!=null) {
                    String text = mLine.getCode()+" " + mLine.getName();
                    mTvTitle.setText(text);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void configChart(){
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setDrawGridBackground(false);
        mBarChart.getAxisLeft().setDrawGridLines(false);
        mBarChart.getAxisRight().setDrawGridLines(false);
        //mBarChart.getXAxis().setDrawGridLines(false);
        mBarChart.setPinchZoom(false);
        int screen_density = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);

        if (screen_density == Configuration.SCREENLAYOUT_SIZE_LARGE || screen_density == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            //The device is a tablet or at least has the screen density of a tablet
            //mBarChart.getXAxis().setLabelRotationAngle(-35);
            mBarChart.getXAxis().setDrawLabels(true);
            mBarChart.getXAxis().setTextSize(20);
            //Log.e("Logger:Utility", "setMyDevice-IT IS A TABLET");
        } else {
           // mBarChart.getXAxis().setLabelRotationAngle(-55);
            mBarChart.getXAxis().setTextSize(10);
            //Log.e("Logger:Utility", "setMyDevice-IT IS A MOBILE");
        }

    }

    /*
    if (mBarChart.getData() != null && mBarChart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mBarChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mBarChart.getData().getDataSetByIndex(2);
            set4 = (BarDataSet) mBarChart.getData().getDataSetByIndex(3);
            set1.setValues(lineBar);
            set2.setValues(actual1);
            set3.setValues(actual2);
            set4.setValues(actual3);

            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();

        } else {
     */

    public void updateChart(){
        BarDataSet set1, set2, set3,set4;
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSe


        // create 4 DataSets
        set1 = new BarDataSet(lineBar, "Cell");
        set1.setColor(Color.rgb(255, 102, 0));
        set2 = new BarDataSet(actual1, "1st Shift");
        set2.setColor(Color.rgb(104, 241, 175));
        set3 = new BarDataSet(actual2, "2nd Shift");
        set3.setColor(Color.rgb(164, 228, 251));
        set4 = new BarDataSet(actual3, "3rd Shift");
        set4.setColor(Color.rgb(242, 247, 158));
        BarData data = new BarData(set1, set2, set3,set4);
        data.setBarWidth(barWidth);
        mBarChart.setData(data);


        mBarChart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setValueFormatter(new MyXAsisDate(xasis));
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(actual1.size());
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setVisibleXRangeMaximum(3); // allow 20 values to be displayed at once on the x-axis, not more
        mBarChart.moveViewToX(actual1.size());
        mBarChart.getAxisLeft().setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mBarChart.getAxisRight().setAxisMinimum(0f);

        //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
       // mBarChart.getXAxis().setAxisMaximum(startYear + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        mBarChart.groupBars(0, groupSpace,  barSpace);
        mBarChart.invalidate();
    }

    public void setDowntime(){

        int i=0;
        for (Line line : mLines)
        {
            float v1=0,v2=0,v3=0;

            if (line.getDowntimes()!=null ) {
                lineBar.add(new BarEntry(i,line.getDowntimes().size()));
                for (Downtime downtime : line.getDowntimes())
                    if (downtime.getShift()==1)
                        v1++;
                    else
                    if (downtime.getShift()==2)
                        v2++;
                    else
                    if (downtime.getShift()==3)
                        v3++;
            }
            else
                lineBar.add(new BarEntry(i,v1+v2+v3));


            actual1.add(new BarEntry(i, v1));
            actual2.add(new BarEntry(i,v2));
            actual3.add(new BarEntry(i, v3));

            i++;
        }

    }

    public void setRejects(){

        int i=0;
        for (Line line : mLines)
        {
            float v1=0,v2=0,v3=0;

            if (line.getFirst().getRejects()!=null)
                v1 = line.getFirst().getRejects().size();


            if (line.getSecond().getRejects()!=null)
                v2= line.getSecond().getRejects().size();


            if (line.getThird().getRejects()!=null)
                v3 =line.getThird().getRejects().size();


            lineBar.add(new BarEntry(i,v1+v2+v3));
            actual1.add(new BarEntry(i, v1));
            actual2.add(new BarEntry(i, v2));
            actual3.add(new BarEntry(i, v3));
            i++;
        }

    }

    public void setProduction(){
        int i=0;
        for (Line line : mLines)
        {
            float v1=0,v2=0,v3=0;
            if (line.getFirst().getCumulativeActual()!=null &&!line.getFirst().getCumulativeActual().isEmpty() )
                v1 = Float.valueOf(line.getFirst().getCumulativeActual());


            if (line.getSecond().getCumulativeActual()!=null &&!line.getSecond().getCumulativeActual().isEmpty() )
                v2= Float.valueOf(line.getSecond().getCumulativeActual());


            if (line.getThird().getCumulativeActual()!=null &&!line.getThird().getCumulativeActual().isEmpty() )
                v3 = Float.valueOf(line.getThird().getCumulativeActual());


            lineBar.add(new BarEntry(i,v1+v2+v3));
            actual1.add(new BarEntry(i, v1));
            actual2.add(new BarEntry(i, v2));
            actual3.add(new BarEntry(i, v3));
            i++;
        }
    }

    public void setLeak(){
        int i=0;
        for (Line line : mLines)
        {
            float v1=0,v2=0,v3=0;
            if (line.getFirst().getCumulativeFTQ()!=null &&!line.getFirst().getCumulativeFTQ().isEmpty() )
                v1 = Float.valueOf(line.getFirst().getCumulativeFTQ());


            if (line.getSecond().getCumulativeFTQ()!=null &&!line.getSecond().getCumulativeFTQ().isEmpty() )
                v2= Float.valueOf(line.getSecond().getCumulativeFTQ());


            if (line.getThird().getCumulativeFTQ()!=null &&!line.getThird().getCumulativeFTQ().isEmpty() )
                v3 = Float.valueOf(line.getThird().getCumulativeFTQ());


            lineBar.add(new BarEntry(i,v1+v2+v3));
            actual1.add(new BarEntry(i, v1));
            actual2.add(new BarEntry(i, v2));
            actual3.add(new BarEntry(i, v3));
            i++;
        }
    }


    public void selector(){
        actual1 = new ArrayList<>();
        actual2 = new ArrayList<>();
        actual3 = new ArrayList<>();
        lineBar = new ArrayList<>();

        switch (selector)
        {
            default:
                setProduction();
                break;
            case 1:
                setRejects();
                break;
            case 2:
                setDowntime();
                break;
            case 3:
                setLeak();
                break;
        }

        updateChart();
    }

}
