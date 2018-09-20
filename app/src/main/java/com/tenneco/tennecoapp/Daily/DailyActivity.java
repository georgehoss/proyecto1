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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.DailyAdapter;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

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


    @OnClick(R.id.bt_hour) void repor(){

        if (mPresenter.reportHour(mHours)!=24)
            showActualsDialog(mHours.get(mPresenter.reportHour(mHours)),mLine,mPresenter.reportHour(mHours),this);
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
    }

    @Override
    public void showActualsDialog(WorkHour workHour, Line line, final int position, Context context) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_actual, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        //alertDialogBuilder.setTitle(Window.FEATURE_NO_TITLE);
        TextView mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvName.setText(line.getName());
        TextView mTvDate = (TextView) view.findViewById(R.id.tv_date);
        mTvDate.setText(line.getDate());
        TextView mTvShift = (TextView) view.findViewById(R.id.tv_shift);
        if (position<=7)
            mTvShift.setText(getString(R.string.add_1st_shift));
        if (position>=8 && position<=15)
            mTvShift.setText(getString(R.string.add_2nd_shift));
        if (position>15)
            mTvShift.setText(getString(R.string.add_3rd_shift));
        TextView mTvTime = (TextView) view.findViewById(R.id.tv_time);
        String time = workHour.getStartHour() + " - " + workHour.getEndHour();
        mTvTime.setText(time);
        TextView mTvTarget = (TextView) view.findViewById(R.id.tv_target);
        mTvTarget.setText(workHour.getTarget());
        final EditText mEtActual = (EditText) view.findViewById(R.id.et_actual);
        if (workHour.getActuals()!=null)
            mEtActual.setText(workHour.getActuals());
        final EditText mEtComments = (EditText) view.findViewById(R.id.et_comments);
        if (workHour.getComments()!=null)
            mEtComments.setText(workHour.getComments());
        Button mBtSave = (Button) view.findViewById(R.id.bt_save);
        Button mBtCancel = (Button) view.findViewById(R.id.bt_cancel);
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
                else {
                    mPresenter.saveLine(mLine, mHours, position, actual, comment);
                    dialog.dismiss();
                }
            }
        });


    }

    @Override
    public void updateLine(Line line) {
        dbLine.child(line.getId()).setValue(line);
    }


    @Override
    public void onTargetClick(int position) {
        showActualsDialog(mHours.get(position),mLine,position,this);
    }

    @Override
    public void onOwnerClick(int position) {

    }
}
