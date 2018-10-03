package com.tenneco.tennecoapp.Hourly;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.LineAdapter;
import com.tenneco.tennecoapp.Adapter.ProductionLineAdapter;
import com.tenneco.tennecoapp.Daily.DailyActivity;
import com.tenneco.tennecoapp.Lines.AddEditLineActivity;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HourlyFragment extends Fragment implements HourlyContract.View,ProductionLineAdapter.ItemInteraction {
    private MainActivity main;
    private DatabaseReference dbPLines;
    private DatabaseReference dbLine;
    private ProductionLineAdapter mAdapter;
    private ArrayList<Line> mLines;
    private HourlyContract.Presenter mPresenter;
    private String lineId;
    private Line mLine;
    private int admin=0;
    @BindView(R.id.rv_lines) RecyclerView mRvLines;
    @BindView(R.id.pb_loading) ProgressBar mPbLoading;
    @BindView(R.id.fb_add) FloatingActionButton mFbAdd;
    @BindView(R.id.tv_name) TextView mTvName;
    @BindView(R.id.tv_shift1) TextView mTvFirstShift;
    @BindView(R.id.tv_shift2) TextView mTvSecondShift;
    @BindView(R.id.tv_shift3) TextView mTvThirdShift;
    @BindView(R.id.line)CardView mCvLine;
    @BindView(R.id.ll_shift) LinearLayout mLlShifts;


    @OnClick(R.id.fb_add) void onAdd(){
        addNewLine();
    }

    @OnClick(R.id.tv_name) void onTouch(){
        if (mLlShifts.getVisibility()==View.VISIBLE)
            hideShifts();
        else
            showShifts();
    }


    public HourlyFragment() {
        // Required empty public constructor
    }

    public static HourlyFragment newInstance(String lineId,int admin) {

        Bundle args = new Bundle();

        HourlyFragment fragment = new HourlyFragment();
        args.putString("id",lineId);
        args.putInt("admin",admin);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hourly, container, false);
        ButterKnife.bind(this,view);
        dbPLines = FirebaseDatabase.getInstance().getReference(Line.DB_PRODUCTION_LINE);
        dbLine =  FirebaseDatabase.getInstance().getReference(Line.DB_LINE);
        mLines = new ArrayList<>();
        mRvLines.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProductionLineAdapter(mLines,this);
        mRvLines.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onItemClick(String lineId) {
        launchDaily(lineId);
    }

    @Override
    public void onLongClick(String lineId) {
        if (admin==3) {
            Intent intent = new Intent(main, AddEditLineActivity.class);
            intent.putExtra("cell", true);
            intent.putExtra("id", lineId);
            startActivity(intent);
        }

    }

    @Override
    public void getLines() {
        Query postsQuery;
        if (admin>1)
            postsQuery = dbPLines.orderByChild("parentId").equalTo(lineId);
        else
            postsQuery = dbPLines.orderByChild("parentId").equalTo(lineId).limitToLast(1);
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Line> lines = new ArrayList<>();
                boolean today = false;
                String date = Utils.getDateString();


                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Line line = itemSnapshot.getValue(Line.class);
                    if (line!=null) {
                        lines.add(line);
                        if (line.getDate().equals(date))
                            today=true;
                    }
                }

                mLines = new ArrayList<>();
                for (int size = lines.size()-1; size>=0 ; size--)
                    mLines.add(lines.get(size));

                if(!today)
                    showFb();
                else
                    hideFb();
                mAdapter.setLines(mLines);
                mAdapter.notifyDataSetChanged();
                hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressBar();
            }
        });
    }

    @Override
    public void getLine() {
        Query postsQuery;
        postsQuery = dbLine.child(lineId);
        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void onStart() {
        super.onStart();
        if (getArguments()!=null) {
            lineId = getArguments().getString("id");
            admin  =getArguments().getInt("admin");
        }
        main = (MainActivity) getActivity();
        if (main != null) {
            main.hideMenu();
        }
        getLine();
    }

    @Override
    public void addNewLine() {
        Line line = new Line(mLine);
        line.setId(dbPLines.push().getKey());
        line.setDate(Utils.getDateString());
        line.setParentId(lineId);
        line.setDowntimeList(mLine.getDowntimeList());
        line.setScrap1List(mLine.getScrap1List());
        line.setScrap2List(mLine.getScrap2List());
        line.setScrap3List(mLine.getScrap3List());
        line.setPassword(mLine.getPassword());
        line.setLeakList(mLine.getLeakList());
        dbPLines.child(line.getId()).setValue(line);

    }

    @Override
    public void hideProgressBar() {
        if (getContext()!=null) {
            mPbLoading.setVisibility(View.GONE);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void showFb() {
        if (getContext()!=null)
        mFbAdd.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void hideFb() {
        if (getContext()!=null)
            mFbAdd.setVisibility(View.GONE);
    }

    @Override
    public void setLine() {
        mTvName.setText(mLine.getName());
        mTvFirstShift.setText(mLine.getFirst().getCumulativePlanned());
        mTvSecondShift.setText(mLine.getSecond().getCumulativePlanned());
        mTvThirdShift.setText(mLine.getThird().getCumulativePlanned());
        mCvLine.setVisibility(View.VISIBLE);
        getLines();
    }

    @Override
    public void hideShifts() {
        mLlShifts.setVisibility(View.GONE);
    }

    @Override
    public void showShifts() {
        mLlShifts.setVisibility(View.VISIBLE);
    }

    @Override
    public void launchDaily(String lineId) {
        Intent intent = new Intent(main, DailyActivity.class);
        intent.putExtra("id",lineId);
        startActivity(intent);
    }

    @Override
    public void bindPresenter(HourlyContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
