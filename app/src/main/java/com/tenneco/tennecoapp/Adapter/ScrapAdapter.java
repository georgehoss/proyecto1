package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 24/09/2018.
 */
public class ScrapAdapter extends RecyclerView.Adapter<ScrapAdapter.ScrapViewHolder> {

    private ArrayList<Reason> reasons;
    private OnItemClick onItemClick;

    public ScrapAdapter(ArrayList<Reason> reasons, OnItemClick onItemClick) {
        if (reasons!=null)
            Collections.sort(reasons,Reason.NameComparator);
        this.reasons = reasons;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ScrapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.position_row,parent,false);
        return new ScrapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrapViewHolder holder, final int position) {
        final Reason reason = reasons.get(position);
        if (reason!=null)
        {
            holder.mTvName.setText(reason.getName());
            holder.mBtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reasons.remove(reason);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (reasons!=null)
        return reasons.size();
        else
            return 0;
    }

    class ScrapViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.bt_delete) ImageView mBtDelete;
        public ScrapViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    public ArrayList<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(ArrayList<Reason> reasons) {
        this.reasons = reasons;
    }

    public interface OnItemClick{
        void eventDeletClick(Reason reason);
    }
}
