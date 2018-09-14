package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 14/09/2018.
 */
public class ProductionLineAdapter extends RecyclerView.Adapter<ProductionLineAdapter.ProductionLineViewHolder> {
    private ArrayList<Line> lines;
    private ItemInteraction itemInteraction;

    public ProductionLineAdapter(ArrayList<Line> lines, ItemInteraction itemInteraction){
        this.lines = lines;
        this.itemInteraction = itemInteraction;
    }

    @NonNull
    @Override
    public ProductionLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_actual_row,parent,false);
        return new ProductionLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductionLineViewHolder holder, int position) {

        Line line = lines.get(position);
        if (line!=null) {
            holder.mTvDate.setText(line.getDate());
            holder.mTvName.setText(line.getName());
            holder.mTvNumber.setText(line.getName());
            holder.mTvActual1.setText(line.getFirst().getCumulativeActual());
            holder.mTvActual2.setText(line.getSecond().getCumulativeActual());
            holder.mTvActual3.setText(line.getThird().getCumulativeActual());
            holder.mTvTarget1.setText(line.getFirst().getCumulativePlanned());
            holder.mTvTarget2.setText(line.getSecond().getCumulativePlanned());
            holder.mTvTarget3.setText(line.getThird().getCumulativePlanned());
        }
    }

    @Override
    public int getItemCount() {
        if (lines!=null && lines.size()>0)
            return lines.size();
        else
            return 0;
    }

    class ProductionLineViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date) TextView mTvDate;
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.tv_number) TextView mTvNumber;
        @BindView(R.id.tv_a_s1) TextView mTvActual1;
        @BindView(R.id.tv_a_s2) TextView mTvActual2;
        @BindView(R.id.tv_a_s3) TextView mTvActual3;
        @BindView(R.id.tv_t_s1) TextView mTvTarget1;
        @BindView(R.id.tv_t_s2) TextView mTvTarget2;
        @BindView(R.id.tv_t_s3) TextView mTvTarget3;

        ProductionLineViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemInteraction{
        void onItemClick();
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }
}
