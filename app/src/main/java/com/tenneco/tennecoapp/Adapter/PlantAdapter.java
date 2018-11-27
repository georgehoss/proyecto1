package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 13/09/2018.
 */
public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.LineViewHolder> {
    private ArrayList<Plant> plants;
    private ItemInteraction itemInteraction;
    private boolean date;

    public PlantAdapter(ArrayList<Plant> plants, ItemInteraction itemInteraction, boolean date) {
        if (plants !=null)
            Collections.sort(plants,Plant.NameComparator);
        this.plants = plants;
        this.itemInteraction = itemInteraction;
        this.date = date;
    }

    @NonNull
    @Override
    public LineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_row_layout,parent,false);
        return new LineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineViewHolder holder, int position) {

        final Plant plant = plants.get(position);
        if (plant!=null) {

            String name = plant.getName();
            holder.mTvName.setText(name);
            holder.mLlPlant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemInteraction.onItemClick(plant.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (plants !=null && plants.size()>0)
            return plants.size();
        else
            return 0;
    }

    class LineViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.ll_plant) LinearLayout mLlPlant;


        LineViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemInteraction{
        void onItemClick(String lineId);
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public void setPlants(ArrayList<Plant> plants) {
        this.plants = plants;
    }
}
