package com.tenneco.tennecoapp.Plants;


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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.PlantAdapter;
import com.tenneco.tennecoapp.Configuration.ConfigurationPresenter;
import com.tenneco.tennecoapp.Lines.AddEditLineActivity;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PlantConfigurationFragment extends Fragment implements PlantConfigurationContract.View, PlantAdapter.ItemInteraction {
    private PlantConfigurationContract.Presenter presenter;
    private PlantsActivity main;
    private DatabaseReference dbPlants;
    private PlantAdapter mAdapter;
    private ArrayList<Plant> mPlants;
    @BindView(R.id.rv_lines)
    RecyclerView mRvLines;
    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;
    @BindView(R.id.fb_add)
    FloatingActionButton mFbAdd;

    @OnClick(R.id.fb_add) void onAdd(){ launchAddActivity();}

    public PlantConfigurationFragment() {
        // Required empty public constructor
    }


    public static PlantConfigurationFragment newInstance(String param1, String param2) {
        PlantConfigurationFragment fragment = new PlantConfigurationFragment();
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
        View view = inflater.inflate(R.layout.fragment_plant_configuration, container, false);
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
                hideProgressBar();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Plant plant = itemSnapshot.getValue(Plant.class);
                    if (plant!=null)
                        mPlants.add(plant);
                }

                mAdapter.setPlants(mPlants);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressBar();
            }
        });
    }

    @Override
    public void launchAddActivity() {
        startActivity(new Intent(main, AddEditPlantActivity.class));
    }

    @Override
    public void hideProgressBar() {
        if (getContext()!=null) {
            mPbLoading.setVisibility(View.GONE);
            mFbAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void bindPresenter(PlantConfigurationContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onItemClick(String lineId) {
        Intent intent = new Intent(getContext(), AddEditPlantActivity.class);
        intent.putExtra("id",lineId);
        startActivity(intent);
    }
}
