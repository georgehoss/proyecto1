package com.tenneco.tennecoapp.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 14/09/2018.
 */


public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {
    private ArrayList<WorkHour> hours;
    private ItemInteraction itemInteraction;
    private Context context;

    public DailyAdapter(ArrayList<WorkHour> hours, Context context) {
        this.hours = hours;
        this.itemInteraction = (ItemInteraction) context;
        this.context = context;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_row,parent,false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, final int position) {
        WorkHour hour = hours.get(position);
        if (hour!=null)
        {
            String date = hour.getStartHour() + " - "+ hour.getEndHour();

            int actual=0,target=0;

            if (hour.getActuals()!=null &&  !hour.getActuals().isEmpty())
                try{
                    actual = Integer.valueOf(hour.getActuals());
                    holder.mTvActual.setText(hour.getActuals());
                }catch (NumberFormatException ignored){
                    holder.mTvActual.setText("");
                    holder.mTvVariance.setText("");
                }
                else {
                holder.mTvActual.setText("");
                holder.mTvVariance.setText("");
            }

            if (hour.getTarget()!=null &&  !hour.getTarget().isEmpty())
                try{
                    target = Integer.valueOf(hour.getTarget());
                    holder.mTvTarget.setText(hour.getTarget());
                }catch (NumberFormatException ignored){
                    holder.mTvActual.setText("");
                    holder.mTvVariance.setText("");
                }
            else {
                holder.mTvActual.setText("");
                holder.mTvVariance.setText("");
            }


            if (actual!=0 && target !=0)
            {
                if (actual==target || actual>target) {
                    holder.mTvVariance.setText(String.valueOf(actual - target));
                    holder.mTvVariance.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    holder.mTvVariance.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    holder.mTvActual.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    holder.mTvActual.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                else {
                    holder.mTvVariance.setText(String.valueOf(target - actual));
                    holder.mTvVariance.setTextColor(context.getResources().getColor(R.color.colorRed));
                    holder.mTvVariance.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    holder.mTvActual.setTextColor(context.getResources().getColor(R.color.colorRed));
                    holder.mTvActual.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            }



            int cactual=0,cplanned=0;

            if (hour.getCumulativeActual()!=null &&  !hour.getCumulativeActual().isEmpty())
                try{
                    cactual = Integer.valueOf(hour.getCumulativeActual());
                    holder.mTvCActual.setText(hour.getCumulativeActual());
                }catch (NumberFormatException ignored){
                    holder.mTvCActual.setText("");
                }
            else {
                holder.mTvCActual.setText("");
            }

            if (hour.getCumulativePlanned()!=null &&  !hour.getCumulativePlanned().isEmpty())
                try{
                    cplanned = Integer.valueOf(hour.getCumulativePlanned());
                    holder.mTvCPlanned.setText(hour.getCumulativePlanned());
                }catch (NumberFormatException ignored){
                    holder.mTvCPlanned.setText("");
                }
            else {
                holder.mTvCPlanned.setText("");
            }


            if (cactual!=0 && cplanned !=0)
            {
                if (cactual==cplanned || cactual>cplanned) {
                    holder.mTvCActual.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    holder.mTvCActual.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                else {
                    holder.mTvCActual.setTextColor(context.getResources().getColor(R.color.colorRed));
                    holder.mTvCActual.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            }

            holder.mTvDate.setText(date);
            holder.mTvTarget.setText(hour.getTarget());
            holder.mTvCActual.setText(hour.getCumulativeActual());
            holder.mTvComments.setText(hour.getComments());

            //holder.mTvOwner.setText(hour.getComments());
            holder.mTvActual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemInteraction.onTargetClick(position);
                }
            });

            holder.mTvComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemInteraction.onTargetClick(position);
                }
            });

            holder.mTvOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemInteraction.onOwnerClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (hours!=null && hours.size()>0)
            return hours.size();
        return 0;
    }

    public ArrayList<WorkHour> getHours() {
        return hours;
    }

    public void setHours(ArrayList<WorkHour> hours) {
        this.hours = hours;
    }

    class DailyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date) TextView mTvDate;
        @BindView(R.id.tv_target) TextView mTvTarget;
        @BindView(R.id.tv_actual) TextView mTvActual;
        @BindView(R.id.tv_variance) TextView mTvVariance;
        @BindView(R.id.tv_comments) TextView mTvComments;
        @BindView(R.id.tv_owner) TextView mTvOwner;
        @BindView(R.id.tv_cActual) TextView mTvCActual;
        @BindView(R.id.tv_cPlanned) TextView mTvCPlanned;

        DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemInteraction{
        void onTargetClick(int position);
        void onOwnerClick (int position);

    }
}
