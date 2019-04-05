package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.realm.Line;
import com.tenneco.tennecoapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by ghoss on 14/09/2018.
 */
public class RealmProductionLineAdapter extends RecyclerView.Adapter<RealmProductionLineAdapter.ProductionLineViewHolder> {
    private RealmResults<Line> lines;
    private ItemInteraction itemInteraction;

    public RealmProductionLineAdapter(RealmResults<Line> lines, ItemInteraction itemInteraction){
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

        final Line line = lines.get(position);
        if (line!=null) {
            holder.mTvDate.setText(line.getDate());
            holder.mTvName.setText(line.getName());
            if (line.getCode()!=null)
                holder.mTvNumber.setText(line.getCode());

            holder.mTvActual1.setText(line.getA1());
            holder.mTvActual2.setText(line.getA2());
            holder.mTvActual3.setText(line.getA3());
            holder.mTvTarget1.setText(line.getP1());
            holder.mTvTarget2.setText(line.getP2());
            holder.mTvTarget3.setText(line.getP3());
            holder.mCvLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemInteraction.onItemClick(line.getId());
                }
            });
            holder.mTvNumber.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    itemInteraction.onLongClick(line.getId());
                }
            });
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
        @BindView(R.id.cv_line) CardView mCvLine;

        ProductionLineViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemInteraction{
        void onItemClick(String lineId);
        void onLongClick(String lineId);
    }

    public RealmResults<Line> getLines() {
        return lines;
    }

    public void setLines(RealmResults<Line> lines) {
        this.lines = lines;
    }
}
