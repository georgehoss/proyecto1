package com.tenneco.tennecoapp.Lines.Main;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tenneco.tennecoapp.Adapter.EmployeeSelectionAdapter;
import com.tenneco.tennecoapp.Adapter.PositionAdapter;
import com.tenneco.tennecoapp.Lines.ConfigLineActivity;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LineConfigFragment extends Fragment implements LineConfigContract.View, PositionAdapter.OnItemClick {
    private LineConfigContract.Presenter mPresenter;
    private PositionAdapter mAdapterPos;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    @BindView(R.id.et_psw) EditText mEtPsw;
    @BindView(R.id.rv_position) RecyclerView mRvPosition;
    private ConfigLineActivity main;


    public LineConfigFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.bt_add_position) void addPos(){
        if (main!=null)
        showPositionDialog(new EmployeePosition(""),main);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_line_config, container, false);
        ButterKnife.bind(this,view);
        mRvPosition.setLayoutManager(new LinearLayoutManager(main));
        mAdapterPos = new PositionAdapter(null,this);
        mRvPosition.setAdapter(mAdapterPos);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity()!=null)
            main = (ConfigLineActivity) getActivity();
        setData();

        setTextWatchers();
    }

    @Override
    public void updateLine() {
       setData();

    }

    @Override
    public void setData() {
        if (main!=null && main.mLine!=null)
        {
            if (main.mLine.getName()!=null)
                mEtName.setText(main.mLine.getName());
            if (main.mLine.getCellList()!=null)
                mEtCode.setText(main.mLine.getCode());
            if (main.mLine.getDescription()!=null)
                mEtDescription.setText(main.mLine.getDescription());
            if (main.mLine.getPassword()!=null)
                mEtPsw.setText(main.mLine.getPassword());
            if (main.mLine.getPositions()!=null)
            {
                mAdapterPos.setPositions(main.mLine.getPositions());
                mAdapterPos.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        getData();
    }

    @Override
    public void getData() {
        if (main!=null) {
            if (!mEtName.getText().toString().isEmpty())
                main.mLine.setName(mEtName.getText().toString().trim());
            if (!mEtCode.getText().toString().isEmpty())
                main.mLine.setCode(mEtCode.getText().toString().trim());
            if (!mEtDescription.getText().toString().isEmpty())
                main.mLine.setDescription(mEtDescription.getText().toString().trim());
            if (!mEtPsw.getText().toString().isEmpty())
                main.mLine.setPassword(mEtPsw.getText().toString().trim());
        }

    }

    @Override
    public void setTextWatchers() {
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mEtName.removeTextChangedListener(this);
                if (editable!=null && main!=null)
                    main.mLine.setName(editable.toString());
                mEtName.addTextChangedListener(this);
            }
        });

        mEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mEtCode.removeTextChangedListener(this);
                if (editable!=null && main!=null)
                    main.mLine.setCode(editable.toString());
                mEtCode.addTextChangedListener(this);
            }
        });


        mEtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mEtDescription.removeTextChangedListener(this);
                if (editable!=null && main!=null)
                    main.mLine.setDescription(editable.toString());
                mEtDescription.addTextChangedListener(this);
            }
        });

        mEtPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mEtPsw.removeTextChangedListener(this);
                if (editable!=null && main!=null)
                    main.mLine.setPassword(editable.toString());
                mEtPsw.addTextChangedListener(this);
            }
        });
    }

    @Override
    public void bindPresenter(LineConfigContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void PositionClick(EmployeePosition employeePosition) {
        if (main!=null)
        showDeletePosition(employeePosition,main);
    }

    public void showDeletePosition(final EmployeePosition employeePosition, Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete Employee Position");
        alertDialogBuilder.setMessage(getString(R.string.delete_question)+" "+employeePosition.getName()+" ?");
        alertDialogBuilder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePosition(employeePosition);
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }


    public void showPositionDialog(final EmployeePosition employeePosition,Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_position, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        if (employeePosition.getName().length()>0)
            alertDialogBuilder.setTitle(employeePosition.getName());
        final EditText mEtName = view.findViewById(R.id.et_name);
        mEtName.setText(employeePosition.getName());
        Button mBtSave = view.findViewById(R.id.bt_save);
        Button mBtCancel = view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEtName.getText().toString().length()>0) {
                    employeePosition.setName(mEtName.getText().toString());
                    addPosition(employeePosition);
                    dialog.dismiss();
                }
                else
                {
                    mEtName.setError("Introduce position name!");
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

    public void deletePosition(EmployeePosition position) {
        if (main!=null) {
            main.mLine.getPositions().remove(position);
            mAdapterPos.setPositions(main.mLine.getPositions());
            mAdapterPos.notifyDataSetChanged();
        }
    }

    public void addPosition(EmployeePosition position) {
        if (main!=null) {
            main.mLine.getPositions().add(position);
            mAdapterPos.setPositions(main.mLine.getPositions());
            mAdapterPos.notifyDataSetChanged();
        }
    }
}
