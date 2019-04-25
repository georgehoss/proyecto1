package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 13/09/2018.
 */
public class ScheduleLineAdapter extends RecyclerView.Adapter<ScheduleLineAdapter.LineViewHolder> {
    private ArrayList<Line> lines;
    private ItemInteraction itemInteraction;
    public ScheduleLineAdapter(ArrayList<Line> lines, ItemInteraction itemInteraction) {
        this.lines = lines;
        this.itemInteraction = itemInteraction;
    }

    @NonNull
    @Override
    public LineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_line_row_layout,parent,false);
        return new LineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LineViewHolder holder, int position) {

        final Line line = lines.get(position);
        if (line!=null) {

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
            holder.mTvCode.setText(name);
            holder.mTvName.setText(line.getName());
            holder.mSw.setChecked(line.isSchedule());
            holder.mSw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    line.setSchedule(holder.mSw.isChecked());
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
        @BindView(R.id.tv_code) TextView mTvCode;
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.sw) SwitchCompat mSw;


        LineViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public interface ItemInteraction{
        void onItemClick(Line line, boolean schedule);
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }
}
