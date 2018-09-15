package com.tenneco.tennecoapp.Daily;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    public void onTargetClick(int position) {

    }

    @Override
    public void onOwnerClick(int position) {

    }
}
