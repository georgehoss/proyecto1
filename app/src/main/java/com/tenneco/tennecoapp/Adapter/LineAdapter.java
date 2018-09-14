package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 13/09/2018.
 */
public class LineAdapter extends RecyclerView.Adapter<LineAdapter.LineViewHolder> {
    private ArrayList<Line> lines;
    private ItemInteraction itemInteraction;
    private boolean date;

    public LineAdapter(ArrayList<Line> lines, ItemInteraction itemInteraction, boolean date) {
        this.lines = lines;
        this.itemInteraction = itemInteraction;
        this.date = date;
    }

    @NonNull
    @Override
    public LineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_row_layout,parent,false);
        return new LineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineViewHolder holder, int position) {

        final Line line = lines.get(position);
        if (line!=null) {
            if (date) {
                holder.mTvDate.setVisibility(View.VISIBLE);
                holder.mTvDate.setText(line.getDate());
            }
            holder.mTvName.setText(line.getName());
            holder.mTvFirstShift.setText(line.getFirst().getCumulativePlanned());
            holder.mTvSecondShift.setText(line.getSecond().getCumulativePlanned());
            holder.mTvThirdShift.setText(line.getThird().getCumulativePlanned());
            holder.mLlLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemInteraction.onItemClick(line.getId());
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

    class LineViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date) TextView mTvDate;
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.tv_shift1) TextView mTvFirstShift;
        @BindView(R.id.tv_shift2) TextView mTvSecondShift;
        @BindView(R.id.tv_shift3) TextView mTvThirdShift;
        @BindView(R.id.ll_line) LinearLayout mLlLine;


        LineViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemInteraction{
        void onItemClick(String lineId);
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }
}
