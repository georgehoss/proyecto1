package com.tenneco.tennecoapp.Employee;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.EmployeeAdapter;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeFragment extends Fragment implements EmployeeContract.View,EmployeeAdapter.OnEmployeeInteraction {
    private DatabaseReference dbEmployee;
    private EmployeeContract.Preseneter mPresenter;
    private ArrayList<Employee> mEmployees;
    private EmployeeAdapter mAdapter;
    @BindView(R.id.pb_loading) ProgressBar mPbLoading;
    @BindView(R.id.fb_add) FloatingActionButton mFbAdd;
    @BindView(R.id.rv_employee) RecyclerView mRvEmployee;

    public EmployeeFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.fb_add) void add(){
        addEditDialog(new Employee(dbEmployee.push().getKey()),getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_employee, container, false);
        ButterKnife.bind(this,view);
        dbEmployee = FirebaseDatabase.getInstance().getReference(Employee.DB);
        mEmployees = new ArrayList<>();
        mRvEmployee.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new EmployeeAdapter(mEmployees,this);
        mRvEmployee.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getEmployees();
        showFloatingButton();
    }

    @Override
    public void hideProgressBar() {
        mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showFloatingButton() {
        mFbAdd.setVisibility(View.VISIBLE);
    }

    @Override
    public void getEmployees() {
        dbEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mEmployees = new ArrayList<>();
                hideProgressBar();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Employee employee = itemSnapshot.getValue(Employee.class);
                    if (employee!=null)
                        mEmployees.add(employee);
                }

                Collections.sort(mEmployees,Employee.EmployeeNameComparator);
                mAdapter.setEmployees(mEmployees);
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressBar();
            }
        });
    }

    @Override
    public void addEditDialog(final Employee employee, Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_employee, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(employee.getFullName());
        final EditText mEtName = view.findViewById(R.id.et_name);
        mEtName.setText(employee.getFullName());
        final EditText mEtInfo = view.findViewById(R.id.et_info);
        mEtInfo.setText(employee.getInfo());
        Button mBtSave = (Button) view.findViewById(R.id.bt_save);
        Button mBtCancel = (Button) view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        final Spinner spinner = view.findViewById(R.id.sp_employee);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.edit_operator, R.layout.spinner_row);
        spinner.setAdapter(adapter);
        if (employee.getType()!=null && employee.getType().equals("Welder"))
            spinner.setSelection(1);

        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEtName.getText().toString().length()>0) {
                    employee.setFullName(mEtName.getText().toString());
                    employee.setInfo(mEtInfo.getText().toString());
                    employee.setType(spinner.getSelectedItem().toString());
                    addEditEmployee(employee);
                    dialog.dismiss();
                }
                else
                {
                    mEtName.setError("Introduce full Name!");
                    mEtName.requestFocus();
                }

            }
        });

        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void editDeleteDialog(final Employee employee) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setItems(R.array.edit_employee, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if (position!=0) {
                    dialogInterface.dismiss();
                    deleteDialog(employee);
                }
                else{
                    dialogInterface.dismiss();
                    addEditDialog(employee,getContext());
                }
            }
        });
        alertDialogBuilder.create().show();

    }

    @Override
    public void delete(String id) {
        dbEmployee.child(id).removeValue();
    }

    @Override
    public void addEditEmployee(Employee employee) {
        dbEmployee.child(employee.getId()).setValue(employee);
    }

    @Override
    public void deleteDialog(final Employee employee) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Delete Operator");
        alertDialogBuilder.setMessage("Do you want to delete:"+employee.getFullName()+" ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                delete(employee.getId());
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    @Override
    public void bindPresenter(EmployeeContract.Preseneter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void EditEmploye(Employee employee) {
        editDeleteDialog(employee);
    }
}
