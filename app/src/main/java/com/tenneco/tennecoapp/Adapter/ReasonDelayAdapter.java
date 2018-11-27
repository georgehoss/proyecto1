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

import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.ReasonDelay;
import com.tenneco.tennecoapp.Model.Sms;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReasonDelayAdapter extends  RecyclerView.Adapter<ReasonDelayAdapter.DowntimeViewHolder> {
    private ArrayList<ReasonDelay> reasonDelays;
    private OnItemClick onItemClick;

    public ReasonDelayAdapter(ArrayList<ReasonDelay> reasonDelays, OnItemClick onItemClick) {
        if (reasonDelays !=null)
            Collections.sort(reasonDelays,ReasonDelay.NameComparator);

        this.reasonDelays = reasonDelays;
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
        final ReasonDelay reasonDelay = reasonDelays.get(position);
        if (reasonDelay!=null)
        {
            holder.mTvName.setText(reasonDelay.getName());
            holder.mLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.DowntimeClick(reasonDelay);
                }
            });
            holder.mBtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onDelete(reasonDelay);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return reasonDelays.size();
    }

    public ArrayList<ReasonDelay> getReasonDelays() {
        return reasonDelays;
    }

    public void setReasonDelays(ArrayList<ReasonDelay> reasonDelays) {
        this.reasonDelays = reasonDelays;
    }

    class DowntimeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.ll)
        CardView mLl;
        @BindView(R.id.iv_delete) ImageView mBtDelete;
        DowntimeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick{
        void DowntimeClick(ReasonDelay reasonDelay);
        void onDelete(ReasonDelay reasonDelay);
    }
}
