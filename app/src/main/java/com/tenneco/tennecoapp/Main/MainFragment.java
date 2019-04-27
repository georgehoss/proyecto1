package com.tenneco.tennecoapp.Main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.LineAdapter;
import com.tenneco.tennecoapp.Adapter.RealmLineAdapter;
import com.tenneco.tennecoapp.Hourly.HourlyFragment;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.Plants.PlantsActivity;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class MainFragment extends Fragment implements LineAdapter.ItemInteraction,MainContract.View, RealmLineAdapter.ItemInteraction {

    private FirebaseUser mUser;
    private MainActivity main;
    private DatabaseReference dbUsers;
    private DatabaseReference dbLines;
    private LineAdapter mAdapter;
    private RealmLineAdapter mRAdapter;
    private ArrayList<Line> mLines;
    private MainContract.Presenter mPresenter;
    @BindView(R.id.rv_lines) RecyclerView mRvLines;
    @BindView(R.id.tv_slogan) TextView mTvSlogan;
    @BindView(R.id.pb_loading) ProgressBar mPbLoading;
    @BindView(R.id.cv_plants) CardView mCvPlants;
    private int admin =0;
    private Query postsQuery;
    private ValueEventListener valueEventListener =new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);


            if (user==null)
            {
                if (mUser.getDisplayName()==null)
                    mUser = FirebaseAuth.getInstance().getCurrentUser();

                saveUser(mUser);
                main.setUserName(mUser.getDisplayName());
            }
            else
            {

                admin = user.getType();
                main.setUserName(user.getName());

                StorageUtils.saveUserPermissions(main,admin);

                if (user.getType()==0)
                {
                    showNoLines();
                }
                else
                    getLines();

                if (user.getType()==3) {
                    main.showMenu();
                }
                else
                    main.hideMenu();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ValueEventListener linesListener = new ValueEventListener() {
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


           // mAdapter.setLines(mLines);
           // mAdapter.notifyDataSetChanged();

            if (mLines.size()>0) {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.beginTransaction();
                    for (Line l : mLines) {
                        com.tenneco.tennecoapp.Model.realm.Line line = new com.tenneco.tennecoapp.Model.realm.Line(
                                l.getId(),l.getName(),l.getCode(),l.getDate(),l.getProducts().get(0).getFirst().getCumulativePlanned(),
                                l.getProducts().get(0).getSecond().getCumulativePlanned(),l.getProducts().get(0).getThird().getCumulativePlanned(),
                                "0","0","0",false,l.isSchedule(),StorageUtils.getPlantId(main));
                        realm.copyToRealmOrUpdate(line);
                    }

                    realm.commitTransaction();

                } catch (Exception ignored) {

                }
                finally {
                    getRealmResults();
                }

                showPickOne();
            }
            else
                showNoLines();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            hideProgress();
            showNoLines();
        }
    };




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

    @OnClick(R.id.cv_plants) void showPlantss(){
        launchPlants();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        dbUsers = FirebaseDatabase.getInstance().getReference(User.DB_USER);
        if (StorageUtils.getPlantId(getContext())!=null)
        dbLines = FirebaseDatabase.getInstance().getReference(Line.DB_LINE).child(StorageUtils.getPlantId(getContext()));
        mLines = new ArrayList<>();
        mRvLines.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRAdapter = new RealmLineAdapter(null,this,false);
        getRealmResults();
        mRvLines.setAdapter(mRAdapter);
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

        if (main!=null) {
            main.restoreButtons();
            main.setProduction();
        }






    }

    private void saveUser(FirebaseUser user){
        User mUser = new User(user.getUid(),user.getDisplayName(),user.getEmail(),0);
        if (mUser.getName()==null)
            mUser.setName(mUser.getEmail());
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

        postsQuery = dbUsers.child(mUser.getUid());
        postsQuery.addValueEventListener(valueEventListener);
    }

    @Override
    public void getLines() {

        getRealmResults();

        if (getActivity()!=null && (StorageUtils.getPlantId(getActivity())==null || StorageUtils.getPlantId(getActivity()).equals("0"))) {
            startActivity(new Intent(main, PlantsActivity.class));
            main.finish();
        }

        dbLines.addValueEventListener(linesListener);
    }

    @Override
    public void getRealmResults() {
        Realm realm = Realm.getDefaultInstance();
        String plantId="";
        if(main!=null)
            plantId=StorageUtils.getPlantId(main);
        else
            if (getActivity()!=null)
                plantId = StorageUtils.getPlantId(getActivity());

        RealmResults<com.tenneco.tennecoapp.Model.realm.Line> mLines = realm.where(com.tenneco.tennecoapp.Model.realm.Line.class)
                .equalTo("isPline",false).equalTo("plantId",plantId).sort("code", Sort.ASCENDING).findAll();
        if (mLines!=null && mLines.size()>0) {
            showPickOne();
            hideProgress();
        }
        mRAdapter.setLines(mLines);
        mRAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        postsQuery.removeEventListener(valueEventListener);
        dbLines.removeEventListener(linesListener);
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
    public void showPlants() {
        mCvPlants.setVisibility(View.VISIBLE);
    }

    @Override
    public void launchPlants() {
        StorageUtils.removePlantId(main);
        startActivity(new Intent(main,PlantsActivity.class));
        main.finish();
    }

    @Override
    public void launchHourly(String lineId) {
        HourlyFragment hourlyFragment = HourlyFragment.newInstance(lineId,admin);
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, hourlyFragment).addToBackStack(null).commit();
    }

    @Override
    public void bindPresenter(MainContract.Presenter presenter) {

    }
}
