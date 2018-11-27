package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 24/09/2018.
 */
public class DowntimeAdapter extends  RecyclerView.Adapter<DowntimeAdapter.DowntimeViewHolder> {
    private ArrayList<Zone> zones;
    private OnItemClick onItemClick;

    public DowntimeAdapter(ArrayList<Zone> zones, OnItemClick onItemClick) {
        if (zones!=null)
            Collections.sort(zones,Zone.NameComparator);

        this.zones = zones;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public DowntimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.position_row,parent,false);
        return new DowntimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DowntimeViewHolder holder, final int position) {
        final Zone zone = zones.get(position);
        if (zone!=null)
        {
            holder.mTvName.setText(zone.getName());
            ArrayList<Location> locationss = zone.getLocations();
            Collections.sort(locationss,Location.NameComparator);
            StringBuilder locations= new StringBuilder();
            locations.append("Locations:\n");
            for (Location location : locationss)
                if (location.getName()!=null && !location.getName().isEmpty())
                    locations.append("- ").append(location.getName()).append("\n");

            if (locations.length() > 0)
            {
                holder.mTvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.mTvInfo.getVisibility()==View.VISIBLE)
                            holder.mTvInfo.setVisibility(View.GONE);
                        else
                            holder.mTvInfo.setVisibility(View.VISIBLE);
                    }
                });

                holder.mTvInfo.setText(locations.toString());
            }

            holder.mBtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zones.remove(zone);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return zones.size();
    }

    public ArrayList<Zone> getZones() {
        return zones;
    }

    public void setZones(ArrayList<Zone> zones) {
        this.zones = zones;
    }

    class DowntimeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.bt_delete) ImageView mBtDelete;
        @BindView(R.id.tv_info) TextView mTvInfo;
        public DowntimeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick{
        void ZoneClick(Zone zone);
    }
}
