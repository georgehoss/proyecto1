package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 21/09/2018.
 */
public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.PositionViewHolder> {
    private ArrayList<EmployeePosition> employeePositions;
    private OnItemClick onItemClick;

    public PositionAdapter(ArrayList<EmployeePosition> employeePositions, OnItemClick onItemClick) {
        this.employeePositions = employeePositions;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public PositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.position_row,parent,false);
        return new PositionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PositionViewHolder holder, int position) {
        final EmployeePosition employeePosition = employeePositions.get(position);
        if (employeePosition!=null)
        {
            holder.mTvName.setText(employeePosition.getName());
            holder.mBtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.PositionClick(employeePosition);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
       if (employeePositions!=null && employeePositions.size()>0)
           return employeePositions.size();
       else
           return 0;
    }

    public ArrayList<EmployeePosition> getPositions() {
        return employeePositions;
    }

    public void setPositions(ArrayList<EmployeePosition> employeePositions) {
        this.employeePositions = employeePositions;
    }

    class PositionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.ll_position) LinearLayout mLlPosition;
        @BindView(R.id.bt_delete) ImageView mBtDelete;

        PositionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClick{
        void PositionClick(EmployeePosition employeePosition);
    }
}
