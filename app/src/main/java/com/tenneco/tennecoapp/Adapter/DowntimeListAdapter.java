package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DowntimeListAdapter extends  RecyclerView.Adapter<DowntimeListAdapter.DowntimeViewHolder> {
    private ArrayList<Downtime> downtimes;
    private OnItemClick onItemClick;

    public DowntimeListAdapter(ArrayList<Downtime> downtimes, OnItemClick onItemClick) {
        if (downtimes !=null)
            Collections.sort(downtimes,Downtime.NameComparator);

        this.downtimes = downtimes;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public DowntimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downtime_row,parent,false);
        return new DowntimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DowntimeViewHolder holder, int position) {
        final Downtime downtime = downtimes.get(position);
        if (downtime!=null)
        {
            holder.mTvName.setText(downtime.getName());
            holder.mLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.DowntimeClick(downtime.getId());
                }
            });
            holder.mBtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onDelete(downtime);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return downtimes.size();
    }

    public ArrayList<Downtime> getDowntimes() {
        return downtimes;
    }

    public void setDowntimes(ArrayList<Downtime> downtimes) {
        this.downtimes = downtimes;
    }

    class DowntimeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.ll) CardView mLl;
        @BindView(R.id.iv_delete) ImageView mBtDelete;
        DowntimeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick{
        void DowntimeClick(String id);
        void onDelete(Downtime downtime);
    }
}
