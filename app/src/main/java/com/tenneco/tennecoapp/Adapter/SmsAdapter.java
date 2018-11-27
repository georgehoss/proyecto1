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

import com.tenneco.tennecoapp.Model.Sms;
import com.tenneco.tennecoapp.Model.SmsList;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SmsAdapter extends  RecyclerView.Adapter<SmsAdapter.DowntimeViewHolder> {
    private ArrayList<Sms> sms;
    private OnItemClick onItemClick;

    public SmsAdapter(ArrayList<Sms> sms, OnItemClick onItemClick) {
        if (sms !=null)
            Collections.sort(sms,Sms.NameComparator);

        this.sms = sms;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public DowntimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_row,parent,false);
        return new DowntimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DowntimeViewHolder holder, int position) {
        final Sms smsList = sms.get(position);
        if (smsList!=null)
        {
            holder.mTvName.setText(smsList.getName());
            holder.mTvNumber.setText(smsList.getNumber());
            holder.mLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.DowntimeClick(smsList);
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
        return sms.size();
    }

    public ArrayList<Sms> getSms() {
        return sms;
    }

    public void setSms(ArrayList<Sms> sms) {
        this.sms = sms;
    }

    class DowntimeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.tv_number) TextView mTvNumber;
        @BindView(R.id.ll)
        CardView mLl;
        @BindView(R.id.iv_delete) ImageView mBtDelete;
        DowntimeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick{
        void DowntimeClick(Sms sms);
        void onDelete (Sms sms);
    }
}
