package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 20/09/2018.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourAdapterViewHolder> {
    private ArrayList<WorkHour> hours;
    private ItemInteraction itemInteraction;
    private int id;

    public HourAdapter(ArrayList<WorkHour> hours, ItemInteraction itemInteraction, int id) {
        this.hours = hours;
        this.itemInteraction = itemInteraction;
        this.id = id;
    }

    @NonNull
    @Override
    public HourAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hour_row,parent,false);
        return new HourAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourAdapterViewHolder holder, final int position) {
        WorkHour hour = hours.get(position);
        if (hour!=null)
        {
            holder.mTvShour.setText(hour.getStartHour());
            holder.mTvEhour.setText(hour.getEndHour());
            holder.mTvTarget.setText(hour.getTarget());
            holder.mTvCumulative.setText(hour.getCumulativePlanned());
            holder.mLlHour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemInteraction.onTargetClick(id);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (hours!=null &&hours.size()>0)
            return hours.size();
        else
        return 0;
    }

    public ArrayList<WorkHour> getHours() {
        return hours;
    }

    public void setHours(ArrayList<WorkHour> hours) {
        this.hours = hours;
    }

    class HourAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_shour) TextView mTvShour;
        @BindView(R.id.tv_ehour) TextView mTvEhour;
        @BindView(R.id.tv_target) TextView mTvTarget;
        @BindView(R.id.tv_cp) TextView mTvCumulative;
        @BindView(R.id.ll_hour) LinearLayout mLlHour;

        HourAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemInteraction{
        void onTargetClick(int position);
    }
}
