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
import java.util.Collections;

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
        if (lines!=null)
            Collections.sort(lines,Line.NameComparator);
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
            String name = "";

            if (line.getCode()!=null)
                name = line.getCode();
            else {

                name = line.getName();

                if (name.length() > 3) {
                    name = name.substring(0, 4);
                    name = name.trim();
                }

            }
            holder.mTvName.setText(name);
            holder.mTvNames.setText(line.getName());
            if (line.getProducts().size()>0) {
                holder.mTvShift1.setText(line.getProducts().get(0).getFirst().getCumulativePlanned());
                holder.mTvShift2.setText(line.getProducts().get(0).getSecond().getCumulativePlanned());
                holder.mTvShift3.setText(line.getProducts().get(0).getThird().getCumulativePlanned());
            }
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
        @BindView(R.id.tv_names) TextView mTvNames;
        @BindView(R.id.tv_shift1) TextView mTvShift1;
        @BindView(R.id.tv_shift2) TextView mTvShift2;
        @BindView(R.id.tv_shift3) TextView mTvShift3;
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
