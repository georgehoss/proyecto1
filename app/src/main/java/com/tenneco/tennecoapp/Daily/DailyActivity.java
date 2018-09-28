package com.tenneco.tennecoapp.Daily;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.DailyAdapter;
import com.tenneco.tennecoapp.Adapter.EndShiftPositionAdapter;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyActivity extends AppCompatActivity implements DailyContract.View,DailyAdapter.ItemInteraction {

    private DailyContract.Presenter mPresenter;
    private DatabaseReference dbLine;
    private Line mLine;
    private DailyAdapter mAdapter;
    private String lineId;
    private ArrayList<WorkHour> mHours;

    @BindView(R.id.tv_name)TextView mTvName;
    @BindView(R.id.tv_date)TextView mTvDate;
    @BindView(R.id.rv_lines) RecyclerView mRvLine;
    @BindView(R.id.tv_a_s1) TextView mTvActS1;
    @BindView(R.id.tv_t_s1) TextView mTvTS1;
    @BindView(R.id.tv_a_s2) TextView mTvActS2;
    @BindView(R.id.tv_t_s2) TextView mTvTS2;
    @BindView(R.id.tv_a_s3) TextView mTvActS3;
    @BindView(R.id.tv_t_s3) TextView mTvTS3;
    @BindView(R.id.bt_counter) Button mBtCounter;



    @OnClick(R.id.bt_hour) void repor(){

        if (mPresenter.reportHour(mHours)!=24)
            showActualsDialog(mHours.get(mPresenter.reportHour(mHours)),mLine,mPresenter.reportHour(mHours),this);

    }

    @OnClick(R.id.bt_end_s1) void endS1(){
        if (!mLine.getFirst().isClosed())
            showFinishFirst();
    }

    @OnClick(R.id.bt_end_s2) void endS2(){
        if (!mLine.getSecond().isClosed())
            showFinishSecond();
    }

    @OnClick(R.id.bt_end_s3) void endS3(){
        if (!mLine.getThird().isClosed())
            showFinishThird();
    }

    @OnClick(R.id.bt_counter) void count(){
        mPresenter.incrementCount(mLine);
    }

    @OnClick(R.id.bt_downtime) void down(){
        showDowntimeDialog(mLine.getDowntime(),this);
    }

    @OnClick(R.id.bt_event) void scrap(){
        showScrapDialog(mLine.getScrapReasons(),this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        ButterKnife.bind(this);
        dbLine = FirebaseDatabase.getInstance().getReference(Line.DB_PRODUCTION_LINE);
        if (mPresenter == null)
            mPresenter = new DailyPresenter(this);
        else
            mPresenter.bindView(this);

        mHours = new ArrayList<>();
        mRvLine.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DailyAdapter(mHours,this);
        mRvLine.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getExtras()!=null && getIntent().getExtras().getString("id")!=null)
             lineId = getIntent().getExtras().getString("id");
        getLine();
    }

    @Override
    public void bindPresenter(DailyContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void getLine() {
        Query postsQuery;
        postsQuery = dbLine.child(lineId);
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLine = dataSnapshot.getValue(Line.class);
                if (mLine!=null) {
                    setLine();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setLine() {
        mTvName.setText(mLine.getName());
        mTvDate.setText(mLine.getDate());
        mTvActS1.setText(mLine.getFirst().getCumulativeActual());
        mTvTS1.setText(mLine.getFirst().getCumulativePlanned());
        mTvActS2.setText(mLine.getSecond().getCumulativeActual());
        mTvTS2.setText(mLine.getSecond().getCumulativePlanned());
        mTvActS3.setText(mLine.getThird().getCumulativeActual());
        mTvTS3.setText(mLine.getThird().getCumulativePlanned());
        mHours = new ArrayList<>();
        mHours.addAll(mLine.getFirst().getHours());
        mHours.addAll(mLine.getSecond().getHours());
        mHours.addAll(mLine.getThird().getHours());
        mAdapter.setHours(mHours);
        mAdapter.notifyDataSetChanged();
        mPresenter.showCount(mLine);
    }

    @Override
    public void showActualsDialog(final WorkHour workHour, Line line, final int position, Context context) {

        if (!mLine.getDowntime().isSet())
        {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_actual, null);
            alertDialogBuilder.setView(view);
            alertDialogBuilder.setCancelable(false);
            //alertDialogBuilder.setTitle(Window.FEATURE_NO_TITLE);
            TextView mTvName = view.findViewById(R.id.tv_name);
            mTvName.setText(line.getName());
            TextView mTvDate = view.findViewById(R.id.tv_date);
            mTvDate.setText(line.getDate());
            TextView mTvShift = view.findViewById(R.id.tv_shift);
            if (position<=7)
                mTvShift.setText(getString(R.string.add_1st_shift));
            if (position>=8 && position<=15)
                mTvShift.setText(getString(R.string.add_2nd_shift));
            if (position>15)
                mTvShift.setText(getString(R.string.add_3rd_shift));
            TextView mTvTime = view.findViewById(R.id.tv_time);
            String time = workHour.getStartHour() + " - " + workHour.getEndHour();
            mTvTime.setText(time);
            TextView mTvTarget = view.findViewById(R.id.tv_target);
            mTvTarget.setText(workHour.getTarget());
            final EditText mEtActual = view.findViewById(R.id.et_actual);
            if (workHour.getActuals()!=null)
                mEtActual.setText(workHour.getActuals());
            final EditText mEtComments = view.findViewById(R.id.et_comments);
            if (workHour.getComments()!=null)
                mEtComments.setText(workHour.getComments());
            Button mBtSave = view.findViewById(R.id.bt_save);
            Button mBtCancel = view.findViewById(R.id.bt_cancel);
            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();

            mBtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            mBtSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String actual = mEtActual.getText().toString().trim();
                    String comment = mEtComments.getText().toString().trim();
                    if (mPresenter.validateActual(actual))
                    {
                        mEtActual.setError("Please, introduce the actual value!");
                        mEtActual.requestFocus();
                    }
                    else
                    if (mPresenter.validateComment(comment,actual,workHour.getTarget()))
                    {
                        mEtComments.setError("You must introduce a comment!");
                        mEtComments.requestFocus();
                        mEtActual.setTextColor(getResources().getColor(R.color.colorRed));
                        Toast.makeText(DailyActivity.this, "Actual value is below target!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mPresenter.saveLine(mLine, mHours, position, actual, comment);
                        dialog.dismiss();
                    }
                }
            });
        }
        else
            showDowntimeDialog(mLine.getDowntime(),this);


    }

    @Override
    public void showEndShiftDialog(final Line line, final int shift, final Context context, final boolean close) {
        if (!mLine.getDowntime().isSet())
        {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_end_shift, null);
            alertDialogBuilder.setView(view);
            alertDialogBuilder.setCancelable(false);
            TextView mTvName = view.findViewById(R.id.tv_name);
            mTvName.setText(line.getName());
            TextView mTvDate = view.findViewById(R.id.tv_date);
            mTvDate.setText(line.getDate());
            TextView mTvShift = view.findViewById(R.id.tv_shift);
            Shift eShitf = new Shift();
            String title="";
            if (shift==1) {
                title = "End of "+getString(R.string.add_1st_shift)+ " Transfer";
                eShitf = line.getFirst();
            }
            if (shift==2) {
                title = "End of "+getString(R.string.add_2nd_shift)+ " transfer";
                eShitf = line.getSecond();
            }
            if (shift==3) {
                title = "End of "+getString(R.string.add_3rd_shift)+ " transfer";
                eShitf = line.getThird();
            }




            mTvShift.setText(title);

            TextView mTvActual = view.findViewById(R.id.tv_actual);
            mTvActual.setText(eShitf.getCumulativeActual());
            TextView mTvTarget = view.findViewById(R.id.tv_target);
            mTvTarget.setText(eShitf.getCumulativePlanned());

            RecyclerView recyclerView = view.findViewById(R.id.rv_position);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            final EndShiftPositionAdapter adapter = new EndShiftPositionAdapter(this,mLine.getPositions(),eShitf.getEmployees());
            recyclerView.setAdapter(adapter);



            Button mBtSave = view.findViewById(R.id.bt_save);
            Button mBtCancel = view.findViewById(R.id.bt_cancel);

            if (!close)
                mBtSave.setText("Save");

            if (eShitf.isClosed()) {
                mBtSave.setVisibility(View.GONE);
                mBtCancel.setText("OK");
            }

            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();

            mBtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            mBtSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean error = false;
                    ArrayList<EmployeePosition> employeePositions = adapter.getPositions();
                    for (EmployeePosition position : employeePositions)
                        if ((position.getOperator()==null || position.getOperator().isEmpty()|| position.getOperator().equals("-Select Operator-")) && !error) {
                            Toast.makeText(context, "Select an operator for position: " + position.getName(), Toast.LENGTH_LONG).show();
                            error = true;
                        }
                    if (!error)
                    {
                        Shift finalShift = new Shift();
                        switch (shift){
                            case 1:
                                finalShift = line.getFirst();
                                break;
                            case 2:
                                finalShift = line.getSecond();
                                break;
                            case 3:
                                finalShift = line.getThird();
                                break;
                        }


                        if (close) {

                            ArrayList<WorkHour> hours = finalShift.getHours();
                            for (WorkHour hour : hours) {
                                hour.setClosed(true);
                           /* if (hour.getActuals()==null || hour.getActuals().isEmpty())
                            {
                                hour.setActuals("0");
                                hour.setComments("End of Shift");
                            } */
                            }

                            finalShift.setClosed(true);
                            finalShift.setHours(hours);
                        }

                        switch (shift){
                            case 1:
                                line.setFirst(finalShift);
                                break;
                            case 2:
                                line.setSecond(finalShift);
                                break;
                            case 3:
                                line.setThird(finalShift);
                                break;
                        }

                        updateLine(line);
                        dialog.dismiss();

                    }
                }
            });

        }
        else
            showDowntimeDialog(mLine.getDowntime(),this);
    }

    @Override
    public void updateLine(Line line) {
        dbLine.child(line.getId()).setValue(line);
    }

    @Override
    public void hideLeakCounter() {
        mBtCounter.setVisibility(View.GONE);
    }

    @Override
    public void setCount(int count) {

        String title = getString(R.string.daily_leak_failure_counter);

        if(count>0)
            title = title + " " + count;

        mBtCounter.setText(title);
    }

    @Override
    public void showDowntimeDialog(final Downtime downtime, final Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int zone=0,location=0,reason=0;
        if (downtime.getZone()>0)
            zone=downtime.getZone();
        if (downtime.getLocation()>0)
            location=downtime.getLocation();
        if (downtime.getReason()>0)
            reason=downtime.getReason();
        View view = inflater.inflate(R.layout.dialog_downtime, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        Spinner spZone = view.findViewById(R.id.sp_zone);
        final Spinner spLocation = view.findViewById(R.id.sp_location);
        Spinner spReason = view.findViewById(R.id.sp_reason);
        final ArrayList<Reason> mReasons = new ArrayList<>();
        mReasons.add(new Reason("- Select a Reason -"));
        mReasons.addAll(downtime.getReasons());
        ArrayAdapter<Reason> mAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,mReasons);
        spReason.setAdapter(mAdapter);
        spReason.setSelection(reason);
        ArrayList<Zone> mZones = new ArrayList<>();
        mZones.add(new Zone("- Select a Zone -",null));
        mZones.addAll(downtime.getZones());
        ArrayAdapter<Zone> mAdapterZone = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,mZones);
        spZone.setAdapter(mAdapterZone);
        spZone.setSelection(zone);


        final int finalLocation = location;
        spZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Zone zone = (Zone) adapterView.getItemAtPosition(i);
                if (zone!=null && zone.getLocations()!=null) {
                    ArrayList<Location> mLocations = new ArrayList<>();
                    mLocations.add(new Location("- Select a Location -"));
                    mLocations.addAll(zone.getLocations());
                    ArrayAdapter<Location> mAdapterLocat = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mLocations);
                    spLocation.setAdapter(mAdapterLocat);
                    spLocation.setSelection(finalLocation);
                    mLine.getDowntime().setZone(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Location location = (Location) adapterView.getItemAtPosition(i);
                if (location!=null)
                {
                    mLine.getDowntime().setLocation(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Reason reason = (Reason) adapterView.getItemAtPosition(i);
                if (reason!=null)
                    mLine.getDowntime().setReason(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button mBtStart = view.findViewById(R.id.bt_start);
        Button mBtEnd = view.findViewById(R.id.bt_end);
        final TextView mTvStart = view.findViewById(R.id.tv_start);
        final TextView mTvEnd = view.findViewById(R.id.tv_end);
        ImageView btClose = view.findViewById(R.id.bt_close);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        if (downtime.getStartTime()!=null && downtime.isSet())
            mTvStart.setText(downtime.getStartTime());


        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        mBtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = Utils.getTimeString();
                mTvStart.setText(time);
                mLine.getDowntime().setStartTime(time);
                mLine.getDowntime().setSet(true);
                updateLine(mLine);
            }
        });

        mBtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = Utils.getTimeString();
                mTvEnd.setText(time);
                mLine.getDowntime().setEndTime(time);
                mLine.getDowntime().setSet(false);
                mLine.getDowntime().setZone(0);
                mLine.getDowntime().setLocation(0);
                mLine.getDowntime().setReason(0);
                updateLine(mLine);
            }
        });
    }

    @Override
    public void showScrapDialog(ArrayList<Reason> reasons, final Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_scrapreason, null);
        alertDialogBuilder.setView(view);
        final Spinner spinner = view.findViewById(R.id.sp_reason);
        ArrayList<Reason> mReasons = new ArrayList<>();
        mReasons.add(new Reason("- Select a Reason -"));
        mReasons.addAll(reasons);
        ArrayAdapter<Reason> mAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,mReasons);
        spinner.setAdapter(mAdapter);


        Button mBtReport = view.findViewById(R.id.bt_report);


        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();


        mBtReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reason reason = (Reason) spinner.getSelectedItem();

                if (reason!=null && reason.getName().equals("- Select a Reason -"))
                    Toast.makeText(context, "You must select a reason to make the Report!", Toast.LENGTH_LONG).show();
                else
                    dialog.dismiss();

            }
        });

    }

    @Override
    public void onTargetClick(int position) {
        showActualsDialog(mHours.get(position),mLine,position,this);
    }

    @Override
    public void onOwnerClick(int position) {

    }

    @Override
    public void showFinishFirst() {
        showEndShiftDialog(mLine,1,this,true);
    }

    @Override
    public void showFinishSecond() {
        showEndShiftDialog(mLine,2,this,true);
    }

    @Override
    public void showFinishThird() {
        showEndShiftDialog(mLine,3,this,true);
    }
}
