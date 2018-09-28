package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 24/09/2018.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private ArrayList<Location> locations;
    private OnItemClick onItemClick;

    public LocationAdapter(ArrayList<Location> locations, OnItemClick onItemClick) {
        if (locations!=null)
            Collections.sort(locations,Location.NameComparator);
        this.locations = locations;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.position_row,parent,false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, final int position) {
        final Location location = locations.get(position);
        if (location!=null){
            holder.mTvName.setText(location.getName());
            holder.mBtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locations.remove(location);
                    notifyItemRemoved(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (locations!=null)
        return locations.size();
        else
            return 0;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.bt_delete) ImageView mBtDelete;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick{
        void LocationDeleteClick(Location location);
    }
}
