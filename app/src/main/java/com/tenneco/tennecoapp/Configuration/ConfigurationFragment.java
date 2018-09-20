package com.tenneco.tennecoapp.Configuration;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.LineAdapter;
import com.tenneco.tennecoapp.Lines.AddEditLineActivity;
import com.tenneco.tennecoapp.Lines.AddLineContract;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConfigurationFragment extends Fragment implements ConfigurationContract.View, LineAdapter.ItemInteraction {
    private MainActivity main;
    private DatabaseReference dbLines;
    private LineAdapter mAdapter;
    private ArrayList<Line> mLines;
    private ConfigurationContract.Presenter mPresenter;
    @BindView(R.id.rv_lines) RecyclerView mRvLines;
    @BindView(R.id.pb_loading) ProgressBar mPbLoading;
    @BindView(R.id.fb_add) FloatingActionButton mFbAdd;

    @OnClick(R.id.fb_add) void onAdd(){ launchAddActivity();}

    public ConfigurationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);
        ButterKnife.bind(this,view);
        dbLines = FirebaseDatabase.getInstance().getReference(Line.DB_LINE);
        mLines = new ArrayList<>();
        mRvLines.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new LineAdapter(mLines,this,false);
        mRvLines.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter==null)
            mPresenter = new ConfigurationPresenter(this);
        else
            mPresenter.bindView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        getLines();
    }

    @Override
    public void bindPresenter(ConfigurationContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onItemClick(String lineId) {
        Intent intent = new Intent(getContext(), AddEditLineActivity.class);
        intent.putExtra("id",lineId);
        startActivity(intent);
    }

    @Override
    public void getLines() {
        dbLines.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLines = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Line line = itemSnapshot.getValue(Line.class);
                    if (line!=null)
                        mLines.add(line);
                }

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
    public void launchAddActivity() {
        startActivity(new Intent(main, AddEditLineActivity.class));
    }

    @Override
    public void hideProgressBar() {
        if (getContext()!=null) {
            mPbLoading.setVisibility(View.GONE);
            mFbAdd.setVisibility(View.VISIBLE);
        }
    }
}
