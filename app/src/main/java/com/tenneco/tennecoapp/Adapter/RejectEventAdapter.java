package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Reject;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 01/10/2018.
 */
public class RejectEventAdapter extends  RecyclerView.Adapter<RejectEventAdapter.ScrapViewHolder> {
    private ArrayList<Reject> rejects;

    public RejectEventAdapter(ArrayList<Reject> rejects) {
        this.rejects = rejects;
    }

    @NonNull
    @Override
    public ScrapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scrap_row,parent,false);
        return new ScrapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrapViewHolder holder, int position) {
        Reject reject = rejects.get(position);
        if (reject !=null)
        {
            holder.mTvTime.setText(reject.getTime());
            if (reject.getActions()==null  || reject.getActions().isEmpty())
                holder.mTvEvent.setText(reject.getReason());
            else {
                String actions = reject.getReason() + " Actions: " + reject.getActions();
                holder.mTvEvent.setText(actions);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (rejects !=null && rejects.size()>0)
            return rejects.size();
        return 0;
    }

    public ArrayList<Reject> getRejects() {
        return rejects;
    }

    public void setRejects(ArrayList<Reject> rejects) {
        this.rejects = rejects;
    }

    class ScrapViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time) TextView mTvTime;
        @BindView(R.id.tv_event) TextView mTvEvent;
        public ScrapViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
