package com.tenneco.tennecoapp.SMS;


import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.ScrapAdapter;
import com.tenneco.tennecoapp.Adapter.SmsAdapter;
import com.tenneco.tennecoapp.Adapter.SmsListAdapter;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.Sms;
import com.tenneco.tennecoapp.Model.SmsList;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditSmsListFragment extends Fragment  implements  EditSmsListContract.View, SmsAdapter.OnItemClick {
    private EditSmsListContract.Presenter mPresenter;
    private MainActivity main;
    private SmsAdapter mAdapterDt;
    private SmsList smsList;
    private ArrayList<Sms> smsLists;
    private String id;
    private DatabaseReference dbSms;
    private ProgressDialog progressDialog;
    @BindView(R.id.rv_sms_numbers) RecyclerView mRvDw;
    @BindView(R.id.pb_loading) ProgressBar mPb;
    @BindView(R.id.fb_add) FloatingActionButton mFbAdd;

    public EditSmsListFragment() {
        // Required empty public constructor
    }

    public static EditSmsListFragment newInstance(String id) {

        Bundle args = new Bundle();
        EditSmsListFragment fragment = new EditSmsListFragment();
        args.putString("id",id);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.fb_add) void saveDw(){
        showAddDialog(getContext(),null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_sms_list, container, false);
        ButterKnife.bind(this,view);
        //dbSms = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(getContext())).child(SmsList.DB_SMS_LIST);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        if (getArguments()!=null && getArguments().getString("id")!=null)
        {
            initAdapters();
            id = getArguments().getString("id");
            dbSms = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(getContext())).child(SmsList.DB_SMS_LIST).child(id).child(Sms.DB_SMS);
            getData();
        }
        else
        {
            if (getFragmentManager()!=null)
                getFragmentManager().popBackStack();
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Changes");
        progressDialog.setMessage("Please Wait.");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter ==null)
            mPresenter = new EditSmsListPresenter(this);
        else
            mPresenter.bindView(this);
    }

    @Override
    public void hideProgressBar() {
        mPb.setVisibility(View.GONE);

    }

    @Override
    public void hideProgessDialog() {
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.hide();
    }

    @Override
    public void getData() {
        dbSms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPb.setVisibility(View.GONE);
                List<Sms> list = new ArrayList<Sms>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    list.add(child.getValue(Sms.class));
                }
                smsLists = new ArrayList<>();
                smsLists.addAll(list);
                Collections.sort(smsLists,Sms.NameComparator);
                if (smsLists!=null) {
                    mAdapterDt.setSms(smsLists);
                    mAdapterDt.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mPb.setVisibility(View.GONE);
            }
        });

        mFbAdd.setVisibility(View.VISIBLE);
    }

    @Override
    public void saveData(Sms smsList) {

        dbSms.child(smsList.getId()).setValue(smsList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgessDialog();
            }
        });
    }

    @Override
    public void initAdapters() {
        smsLists =new ArrayList<>();
        mRvDw.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapterDt = new SmsAdapter(smsLists,this);
        mRvDw.setAdapter(mAdapterDt);
    }

    @Override
    public void showNameEmptyError() {

    }

    @Override
    public void showAddDialog(Context context, Sms sms) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_number, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final EditText mEvName = view.findViewById(R.id.et_name);
        final EditText mEvNumber = view.findViewById(R.id.et_number);
        Button btSave = view.findViewById(R.id.bt_save);
        Button btCancel = view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        String id;
        if(sms == null)
            id = dbSms.push().getKey();
        else{
            id = sms.getId();
            mEvName.setText(sms.getName());
            mEvNumber.setText(sms.getNumber());
        }

        final String finalSmsId = id;
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEvName.getText().toString().isEmpty()) {
                    mEvName.setError("Introduce List Name!");
                    mEvName.requestFocus();
                }
                else
                if (mEvNumber.getText().toString().isEmpty()) {
                    mEvNumber.setError("Introduce Number!");
                    mEvNumber.requestFocus();
                }
                else {

                    saveData(new Sms(mEvName.getText().toString().trim(),mEvNumber.getText().toString().trim(), finalSmsId));
                    mPb.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void showDeleteDialog(Context context, final Sms sms) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete production Line");
        alertDialogBuilder.setMessage(getString(R.string.delete_question)+ " "+sms.getName()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete(sms);
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    @Override
    public void delete(Sms sms) {
        progressDialog.show();
        dbSms.child(sms.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgessDialog();
            }
        });
    }

    @Override
    public void bindPresenter(EditSmsListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void DowntimeClick(Sms sms) {
        showAddDialog(getContext(),sms);
    }

    @Override
    public void onDelete(Sms sms) {
        showDeleteDialog(getContext(),sms);
    }
}
