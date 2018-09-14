package com.tenneco.tennecoapp.Main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.LineAdapter;
import com.tenneco.tennecoapp.Hourly.HourlyFragment;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainFragment extends Fragment implements LineAdapter.ItemInteraction,MainContract.View {

    private FirebaseUser mUser;
    private MainActivity main;
    private DatabaseReference dbUsers;
    private DatabaseReference dbLines;
    private LineAdapter mAdapter;
    private ArrayList<Line> mLines;
    private MainContract.Presenter mPresenter;
    @BindView(R.id.rv_lines) RecyclerView mRvLines;
    @BindView(R.id.tv_slogan) TextView mTvSlogan;
    @BindView(R.id.pb_loading) ProgressBar mPbLoading;




   /* @OnClick (R.id.tv_email) void onVerify(){
        if (!mUser.isEmailVerified())
            mUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Correo de verificación enviado", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
    }*/


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        dbUsers = FirebaseDatabase.getInstance().getReference(User.DB_USER);
        dbLines = FirebaseDatabase.getInstance().getReference(Line.DB_LINE);
        mLines = new ArrayList<>();
        mRvLines.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new LineAdapter(mLines,this,false);
        mRvLines.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        if (main!=null) {
            mUser = main.mUser;
            if (mUser != null) {
                getUser();
            }
        }

        getLines();



    }

    private void saveUser(FirebaseUser user){
        User mUser = new User(user.getUid(),user.getDisplayName(),user.getEmail(),0);
        dbUsers.child(mUser.getId()).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    public void onItemClick(String lineId) {
        launchHourly(lineId);

    }

    @Override
    public void getUser() {
        Query postsQuery;
        postsQuery = dbUsers.child(mUser.getUid());
        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user==null)
                {
                    saveUser(mUser);
                }
                else
                {
                    if (user.getType()==3)
                        main.showMenu();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getLines() {
        dbLines.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLines = new ArrayList<>();
                hideProgress();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Line line = itemSnapshot.getValue(Line.class);
                    if (line!=null)
                        mLines.add(line);
                }

                mAdapter.setLines(mLines);
                mAdapter.notifyDataSetChanged();

                if (mLines.size()>0)
                    showPickOne();
                else
                    showNoLines();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgress();
                showNoLines();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }

    @Override
    public void hideProgress() {
        if (getContext()!=null)
            mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showPickOne() {
        if (getContext()!=null) {
            mTvSlogan.setText(getString(R.string.main_select_one_production_line));
            mTvSlogan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoLines() {
        if (getContext()!=null){
            mTvSlogan.setText(R.string.main_come_back_later);
            mTvSlogan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void launchHourly(String lineId) {
        HourlyFragment hourlyFragment = HourlyFragment.newInstance(lineId);
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, hourlyFragment).addToBackStack(null).commit();
    }

    @Override
    public void bindPresenter(MainContract.Presenter presenter) {

    }
}
