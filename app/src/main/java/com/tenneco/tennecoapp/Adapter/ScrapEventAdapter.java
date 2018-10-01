package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Scrap;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 01/10/2018.
 */
public class ScrapEventAdapter extends  RecyclerView.Adapter<ScrapEventAdapter.ScrapViewHolder> {
    private ArrayList<Scrap> scraps;

    public ScrapEventAdapter(ArrayList<Scrap> scraps) {
        this.scraps = scraps;
    }

    @NonNull
    @Override
    public ScrapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scrap_row,parent,false);
        return new ScrapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrapViewHolder holder, int position) {
        Scrap scrap = scraps.get(position);
        if (scrap!=null)
        {
            holder.mTvTime.setText(scrap.getTime());
            holder.mTvEvent.setText(scrap.getReason());
        }

    }

    @Override
    public int getItemCount() {
        if (scraps!=null && scraps.size()>0)
            return scraps.size();
        return 0;
    }

    public ArrayList<Scrap> getScraps() {
        return scraps;
    }

    public void setScraps(ArrayList<Scrap> scraps) {
        this.scraps = scraps;
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
