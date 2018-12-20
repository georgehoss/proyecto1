package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.EmailList;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EmailListAdapter extends  RecyclerView.Adapter<EmailListAdapter.DowntimeViewHolder> {
    private ArrayList<EmailList> emails;
    private OnItemClick onItemClick;
    private boolean hideDelete;

    public EmailListAdapter(ArrayList<EmailList> emails, OnItemClick onItemClick) {
        if (emails !=null)
            Collections.sort(emails,EmailList.NameComparator);

        this.emails = emails;
        this.onItemClick = onItemClick;
        this.hideDelete=false;
    }

    public EmailListAdapter(ArrayList<EmailList> emails, OnItemClick onItemClick, boolean hideDelete) {
        if (emails !=null)
            Collections.sort(emails,EmailList.NameComparator);
        this.emails = emails;
        this.onItemClick = onItemClick;
        this.hideDelete = hideDelete;
    }

    @NonNull
    @Override
    public DowntimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downtime_row,parent,false);
        return new DowntimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DowntimeViewHolder holder, int position) {
        final EmailList email = emails.get(position);
        if (email!=null)
        {
            holder.mTvName.setText(email.getName());
            holder.mLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.emailClick(email);
                }
            });

            if (hideDelete)
                holder.mBtDelete.setVisibility(View.GONE);
            else
                holder.mBtDelete.setVisibility(View.VISIBLE);

            holder.mBtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onDelete(email);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    public ArrayList<EmailList> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<EmailList> emails) {
        this.emails = emails;
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
        void emailClick(EmailList email);
        void onDelete(EmailList emial);
    }
}
