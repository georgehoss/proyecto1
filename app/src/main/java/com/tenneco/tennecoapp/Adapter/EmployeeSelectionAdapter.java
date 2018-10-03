package com.tenneco.tennecoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ghoss on 21/09/2018.
 */
public class EmployeeSelectionAdapter extends RecyclerView.Adapter<EmployeeSelectionAdapter.EmployeeViewHolder> {
    private ArrayList<Employee> employees;
    private OnEmployee onEmployee;

    public EmployeeSelectionAdapter(ArrayList<Employee> employees, OnEmployee onEmployee) {
        if (employees!= null)
            Collections.sort(employees,Employee.EmployeeNameComparator);
        this.employees = employees;
        this.onEmployee = onEmployee;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_row_selection,parent,false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeViewHolder holder, final int position) {
        final Employee employee = employees.get(position);
        if (employee!=null)
        {
            holder.mTvName.setText(employee.getFullName());
            if (employee.getType()!=null && !employee.getType().isEmpty()) {
                holder.mTvType.setText(employee.getType());
                holder.mTvType.setVisibility(View.VISIBLE);
            }
            else
                holder.mTvType.setVisibility(View.GONE);

            switch (employee.getShift())
            {
                case 1:
                    holder.mRbs1.setChecked(true);
                    break;
                case 2:
                    holder.mRbs2.setChecked(true);
                    break;
                case 3:
                    holder.mRbs3.setChecked(true);
                    break;
                default:
                    holder.mRbs1.setChecked(false);
                    holder.mRbs3.setChecked(false);
                    holder.mRbs2.setChecked(false);
                    break;

            }

            holder.mCheckBox.setChecked(employee.isAvailable());




            holder.mRbs1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.mRbs1.isChecked())
                        employee.setShift(1);

                    if (holder.mRbs2.isChecked())
                        employee.setShift(2);

                    if (holder.mRbs3.isChecked())
                        employee.setShift(3);

                    employee.setAvailable(holder.mCheckBox.isChecked());

                    onEmployee.onEmployeeChange();

                }
            });

            holder.mRbs2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.mRbs1.isChecked())
                        employee.setShift(1);

                    if (holder.mRbs2.isChecked())
                        employee.setShift(2);

                    if (holder.mRbs3.isChecked())
                        employee.setShift(3);

                    employee.setAvailable(holder.mCheckBox.isChecked());

                    onEmployee.onEmployeeChange();

                }
            });

            holder.mRbs3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    employee.setAvailable(holder.mCheckBox.isChecked());

                    if (employee.isAvailable()){
                    if (holder.mRbs1.isChecked())
                        employee.setShift(1);

                    if (holder.mRbs2.isChecked())
                        employee.setShift(2);

                    if (holder.mRbs3.isChecked())
                        employee.setShift(3);
                    }
                    else {
                        employee.setShift(0);
                        holder.mRbs1.setChecked(false);
                        holder.mRbs3.setChecked(false);
                        holder.mRbs2.setChecked(false);
                    }


                    onEmployee.onEmployeeChange();

                }
            });

            holder.mCheckBox.setVisibility(View.VISIBLE);
            holder.mCheckBox.setChecked(employee.isAvailable());
            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    employee.setAvailable(holder.mCheckBox.isChecked());

                    if (holder.mRbs1.isChecked())
                        employee.setShift(1);

                    if (holder.mRbs2.isChecked())
                        employee.setShift(2);

                    if (holder.mRbs3.isChecked())
                        employee.setShift(3);

                    onEmployee.onEmployeeChange();
                }
            });

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
        @BindView(R.id.tv_type) TextView mTvType;
        @BindView(R.id.ll_employee) LinearLayout mLlEmployee;
        @BindView(R.id.cb_check) CheckBox mCheckBox;
        @BindView(R.id.rg_shift) RadioGroup mRbGs;
        @BindView(R.id.rb_1) RadioButton mRbs1;
        @BindView(R.id.rb_2) RadioButton mRbs2;
        @BindView(R.id.rb_3) RadioButton mRbs3;

        EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnEmployee{
        void onEmployeeChange();
    }

}
