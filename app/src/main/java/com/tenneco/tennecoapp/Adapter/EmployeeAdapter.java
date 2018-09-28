package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 21/09/2018.
 */
public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private ArrayList<Employee> employees;
    private OnEmployeeInteraction onEmployeeInteraction;

    public EmployeeAdapter(ArrayList<Employee> employees, OnEmployeeInteraction onEmployeeInteraction) {
        if (employees!=null)
        Collections.sort(employees,Employee.EmployeeNameComparator);
        this.employees = employees;
        this.onEmployeeInteraction = onEmployeeInteraction;
    }

    public EmployeeAdapter(ArrayList<Employee> employees) {
        this.employees = employees;
        this.onEmployeeInteraction = null;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_row,parent,false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, final int position) {
        final Employee employee = employees.get(position);
        if (employee!=null)
        {
            holder.mTvName.setText(employee.getFullName());
            holder.mTvInfo.setText(employee.getInfo());
            if (employee.getType()!=null)
            holder.mTvType.setText(employee.getType());
            if (onEmployeeInteraction!=null)
                holder.mLlEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEmployeeInteraction.EditEmploye(employee);}
                    });
            /*else
            {

                holder.mCheckBox.setVisibility(View.VISIBLE);
                holder.mCheckBox.setChecked(employee.isAvailable());
                holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        employees.get(position).setAvailable(b);
                    }
                });
            }*/
        }
    }

    @Override
    public int getItemCount() {
        if (employees!=null && employees.size()>0)
            return employees.size();
        else
            return 0;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.tv_info) TextView mTvInfo;
        @BindView(R.id.tv_type) TextView mTvType;
        @BindView(R.id.ll_employee) LinearLayout mLlEmployee;
        @BindView(R.id.cb_check) CheckBox mCheckBox;
        EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnEmployeeInteraction{
        void EditEmploye(Employee employee);
    }
}
