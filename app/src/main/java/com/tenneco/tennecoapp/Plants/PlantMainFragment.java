package com.tenneco.tennecoapp.Plants;


import android.content.Intent;
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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.LineAdapter;
import com.tenneco.tennecoapp.Adapter.PlantAdapter;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlantMainFragment extends Fragment implements PlantMainContract.View, PlantAdapter.ItemInteraction {
    private FirebaseUser mUser;
    private PlantsActivity main;
    private DatabaseReference dbPlants;
    private PlantAdapter mAdapter;
    private ArrayList<Plant> mPlants;
    private PlantMainContract.Presenter mPresenter;
    @BindView(R.id.rv_lines)
    RecyclerView mRvLines;
    @BindView(R.id.tv_slogan)
    TextView mTvSlogan;
    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;


    public PlantMainFragment() {
        // Required empty public constructor
    }

    public static PlantMainFragment newInstance(String param1, String param2) {
        PlantMainFragment fragment = new PlantMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_main, container, false);
        ButterKnife.bind(this,view);
        dbPlants = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS);
        mPlants = new ArrayList<>();
        mRvLines.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PlantAdapter(mPlants,this);
        mRvLines.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (PlantsActivity) getActivity();
        getPlants();
    }


    @Override
    public void getPlants() {
        dbPlants.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPlants = new ArrayList<>();
                hideProgress();


                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Plant plant = itemSnapshot.getValue(Plant.class);
                    if (plant!=null)
                        mPlants.add(plant);
                }

                mAdapter.setPlants(mPlants);
                mAdapter.notifyDataSetChanged();

                if (mPlants.size()>0)
                    showPickOne();
                else
                    showNoPlants();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgress();
                showNoPlants();
            }
        });
    }

    @Override
    public void hideProgress() {
        if (getContext()!=null)
            mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showPickOne() {
        if (getContext()!=null) {
            mTvSlogan.setText(getString(R.string.main_plants_on));
            mTvSlogan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoPlants() {
        if (getContext()!=null){
            mTvSlogan.setText(R.string.main_plants_come_back_later);
            mTvSlogan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void launchLines(String plantId) {
        if (plantId!=null) {
            StorageUtils.savePlantId(getActivity(), plantId);
            startActivity(new Intent(main, MainActivity.class));
            main.finish();
        }
    }

    @Override
    public void bindPresenter(PlantMainContract.Presenter presenter) {

    }

    @Override
    public void onItemClick(String plantId) {
        launchLines(plantId);

    }
}
