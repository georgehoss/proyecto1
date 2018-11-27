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
import com.tenneco.tennecoapp.Model.SmsList;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SmsListAdapter extends  RecyclerView.Adapter<SmsListAdapter.DowntimeViewHolder> {
    private ArrayList<SmsList> smsLists;
    private OnItemClick onItemClick;

    public SmsListAdapter(ArrayList<SmsList> smsLists, OnItemClick onItemClick) {
        if (smsLists !=null)
            Collections.sort(smsLists,SmsList.NameComparator);

        this.smsLists = smsLists;
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
        final SmsList smsList = smsLists.get(position);
        if (smsList!=null)
        {
            holder.mTvName.setText(smsList.getName());
            holder.mLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.DowntimeClick(smsList.getId());
                }
            });

            holder.mBtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onDelete(smsList);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return smsLists.size();
    }

    public ArrayList<SmsList> getSmsLists() {
        return smsLists;
    }

    public void setSmsLists(ArrayList<SmsList> smsLists) {
        this.smsLists = smsLists;
    }

    class DowntimeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.ll)
        CardView mLl;
        @BindView(R.id.iv_delete)
        ImageView mBtDelete;
        DowntimeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick{
        void DowntimeClick(String id);
        void onDelete(SmsList smsList);
    }
}
