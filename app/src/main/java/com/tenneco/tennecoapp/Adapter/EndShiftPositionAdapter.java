package com.tenneco.tennecoapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EndShiftPositionAdapter extends RecyclerView.Adapter<EndShiftPositionAdapter.EndShiftPositionViewHolder>  {
    private ArrayList<EmployeePosition> employeePositions;
    private ArrayList<Employee> employees;
    private Context context;
    private boolean ended;

    public EndShiftPositionAdapter(Context context,ArrayList<EmployeePosition> employeePositions,ArrayList<Employee> employees, boolean ended) {
        if (employees!=null)
            Collections.sort(employees,Employee.EmployeeNameComparator);

        this.employeePositions = employeePositions;
        this.context = context;
        this.employees = employees;
        this.ended = ended;
    }


    @NonNull
    @Override
    public EndShiftPositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.end_shift_row,parent,false);
        return new EndShiftPositionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EndShiftPositionViewHolder holder, final int position) {
        final EmployeePosition employeePosition = employeePositions.get(position);
        if (employeePosition!=null)
        {
            holder.mTvName.setText(employeePosition.getName());
            ArrayList<Employee> mList = new ArrayList<>();
            mList.add(new Employee("-Select Operator-",null));
            if (employees!=null)
            mList.addAll(employees);
            ArrayAdapter<Employee> mAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,mList);
            holder.mSpPosition.setAdapter(mAdapter);
            if (employeePosition.getPosition()>0)
                holder.mSpPosition.setSelection(employeePosition.getPosition());

            holder.mSpPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Employee employee = (Employee) adapterView.getItemAtPosition(i);
                    if (employee!=null) {
                        employeePosition.setOperator(employee.getFullName());
                        employeePosition.setOperatorId(employee.getId());
                        employeePosition.setPosition(i);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            holder.mSpPosition.setEnabled(!ended);
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

    class EndShiftPositionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.sp_position) Spinner mSpPosition;


        EndShiftPositionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
