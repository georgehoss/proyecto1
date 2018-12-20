package com.tenneco.tennecoapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Reject;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 14/09/2018.
 */


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.DailyViewHolder> {
    private ArrayList<Line> lines;


    public ReportAdapter(ArrayList<Line> lines) {
        this.lines = lines;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_row,parent,false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, final int position) {
        Line line = lines.get(position);


        if (line!=null) {
            String lineName = line.getCode() + " " + line.getName();
            holder.mTvLine.setText(lineName);
            int actual = 0;
            if (line.getFirst().getCumulativeActual() != null && !line.getFirst().getCumulativeActual().isEmpty())
                actual += Integer.valueOf(line.getFirst().getCumulativeActual());
            if (line.getSecond().getCumulativeActual() != null && !line.getSecond().getCumulativeActual().isEmpty())
                actual += Integer.valueOf(line.getSecond().getCumulativeActual());
            if (line.getThird().getCumulativeActual() != null && !line.getThird().getCumulativeActual().isEmpty())
                actual += Integer.valueOf(line.getThird().getCumulativeActual());

            int target = 0;
            if (line.getFirst().getCumulativePlanned() != null && !line.getFirst().getCumulativePlanned().isEmpty())
                target += Integer.valueOf(line.getFirst().getCumulativePlanned());
            if (line.getSecond().getCumulativePlanned() != null && !line.getSecond().getCumulativePlanned().isEmpty())
                target += Integer.valueOf(line.getSecond().getCumulativePlanned());
            if (line.getThird().getCumulativePlanned() != null && !line.getThird().getCumulativePlanned().isEmpty())
                target += Integer.valueOf(line.getThird().getCumulativePlanned());

            holder.mTvActual.setText(String.valueOf(actual));
            holder.mTvTarget.setText(String.valueOf(target));
            String productivity;
            if (target !=0)
                productivity= String.valueOf((actual* 100) / target) + " %";
            else
                productivity= String.valueOf(target) + " %";
            holder.mTvProductivity.setText(productivity);

            StringBuilder sb = new StringBuilder();
            if (line.getFirst().getPositions() != null && line.getFirst().getPositions().size() > 0) {
                for (EmployeePosition pos : line.getFirst().getPositions())
                    if (!pos.getOperator().equals("-Select Operator-"))
                        sb.append(pos.getOperator()).append(" ");

                sb.append("\n");
            }
            if (line.getSecond().getPositions() != null && line.getSecond().getPositions().size() > 0) {
                for (EmployeePosition pos : line.getSecond().getPositions())
                    if (!pos.getOperator().equals("-Select Operator-"))
                        sb.append(pos.getOperator()).append(" ");
                sb.append("\n");
            }
            if (line.getThird().getPositions()!=null && line.getThird().getPositions().size()>0)
                for (EmployeePosition pos : line.getThird().getPositions())
                    if (!pos.getOperator().equals("-Select Operator-"))
                    sb.append(pos.getOperator()).append(" ");
            if (!sb.toString().isEmpty())
                holder.mTvOperators.setText(sb.toString());

            sb = new StringBuilder();
            if (line.getFirst().getTeamLeaders()!=null && !line.getFirst().getTeamLeaders().isEmpty())
            sb.append(line.getFirst().getTeamLeaders()).append("\n");
            if (line.getSecond().getTeamLeaders()!=null && !line.getSecond().getTeamLeaders().isEmpty())
            sb.append(line.getSecond().getTeamLeaders()).append("\n");
            if (line.getThird().getTeamLeaders()!=null && !line.getThird().getTeamLeaders().isEmpty())
            sb.append(line.getThird().getTeamLeaders()).append("\n");
            holder.mTvTls.setText(sb.toString());

            sb = new StringBuilder();
            if (line.getFirst().getGroupLeaders()!=null && !line.getFirst().getGroupLeaders().isEmpty())
                sb.append(line.getFirst().getGroupLeaders()).append("\n");
            if (line.getSecond().getGroupLeaders()!=null && !line.getSecond().getGroupLeaders().isEmpty())
                sb.append(line.getSecond().getGroupLeaders()).append("\n");
            if (line.getThird().getGroupLeaders()!=null && !line.getThird().getGroupLeaders().isEmpty())
                sb.append(line.getThird().getGroupLeaders()).append("\n");
            holder.mTvGls.setText(sb.toString());

            sb = new StringBuilder();
            if (line.getDowntimes()!=null && line.getDowntimes().size()>0)
                for (Downtime downtime : line.getDowntimes())
                    sb.append(downtime.getStartTime()).append(" - ").append(downtime.getEndTime()).append("\n");

            holder.mTvDowntime.setText(sb.toString());

            int ftq = 0;
            if (line.getFirst().getCumulativeFTQ()!=null && !line.getFirst().getCumulativeFTQ().isEmpty())
                ftq+= Integer.valueOf(line.getFirst().getCumulativeFTQ());
            if (line.getSecond().getCumulativeFTQ()!=null && !line.getSecond().getCumulativeFTQ().isEmpty())
                ftq+= Integer.valueOf(line.getSecond().getCumulativeFTQ());
            if (line.getThird().getCumulativeFTQ()!=null && !line.getThird().getCumulativeFTQ().isEmpty())
                ftq+= Integer.valueOf(line.getThird().getCumulativeFTQ());

            holder.mTvFTQ.setText(String.valueOf(ftq));

            sb = new StringBuilder();
            StringBuilder actions = new StringBuilder();
            if (line.getFirst().getRejects()!=null && line.getFirst().getRejects().size()>0 )
                for (Reject reject : line.getFirst().getRejects()) {
                    if(reject.getReason()!=null)
                    sb.append(reject.getReason()).append("\n");
                    if(reject.getActions()!=null)
                    actions.append(reject.getActions()).append("\n");
                    else
                        actions.append("\n");
                }
            if (line.getSecond().getRejects()!=null && line.getSecond().getRejects().size()>0 )
                for (Reject reject : line.getSecond().getRejects()) {
                    if(reject.getReason()!=null)
                    sb.append(reject.getReason()).append("\n");
                    if(reject.getActions()!=null)
                        actions.append(reject.getActions()).append("\n");
                    else
                        actions.append("\n");
                }

            if (line.getThird().getRejects()!=null && line.getThird().getRejects().size()>0 )
                for (Reject reject : line.getFirst().getRejects()) {
                    if(reject.getReason()!=null)
                    sb.append(reject.getReason()).append("\n");
                    if(reject.getActions()!=null)
                        actions.append(reject.getActions()).append("\n");
                    else
                        actions.append("\n");
                }
            holder.mTvRejects.setText(sb.toString());
            holder.mTvActions.setText(actions.toString());

        }
    }

    @Override
    public int getItemCount() {
        if (lines !=null && lines.size()>0)
            return lines.size();
        return 0;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    class DailyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_line) TextView mTvLine;
        @BindView(R.id.tv_target) TextView mTvTarget;
        @BindView(R.id.tv_actual) TextView mTvActual;
        @BindView(R.id.tv_productivity) TextView mTvProductivity;
        @BindView(R.id.tv_operator) TextView mTvOperators;
        @BindView(R.id.tv_tls) TextView mTvTls;
        @BindView(R.id.tv_gls) TextView mTvGls;
        @BindView(R.id.tv_downtime) TextView mTvDowntime;
        @BindView(R.id.tv_ftq) TextView mTvFTQ;
        @BindView(R.id.tv_rejects) TextView mTvRejects;
        @BindView(R.id.tv_actions) TextView mTvActions;
        DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
