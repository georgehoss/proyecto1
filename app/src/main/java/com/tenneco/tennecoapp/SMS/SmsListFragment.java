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
import android.telephony.SmsManager;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.SmsListAdapter;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.Sms;
import com.tenneco.tennecoapp.Model.SmsList;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SmsListFragment extends Fragment implements SmsListContract.View, SmsListAdapter.OnItemClick {
    private SmsListContract.Presenter mPresenter;
    private MainActivity main;
    private SmsListAdapter mAdapterDt;
    private ArrayList<SmsList> smsLists;
    private String id;
    private DatabaseReference dbSmsList;
    private ProgressDialog progressDialog;
    @BindView(R.id.rv_number_list)
    RecyclerView mRvSmsList;
    @BindView(R.id.fb_add)
    FloatingActionButton mFbAdd;


    @BindView(R.id.pb_loading)
    ProgressBar mPb;

    public SmsListFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.bt_send_sms) void send(){

    }

    @OnClick(R.id.fb_add) void saveDw(){
       showAddDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_sms_list, container, false);
        ButterKnife.bind(this,view);
        dbSmsList = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(getContext())).child(SmsList.DB_SMS_LIST);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        initAdapters();
        getData();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Changes");
        progressDialog.setMessage("Please Wait.");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter ==null)
            mPresenter = new SmsListPresenter(this);
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
        dbSmsList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPb.setVisibility(View.GONE);


                List<SmsList> list = new ArrayList<SmsList>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                   SmsList smsList = child.getValue(SmsList.class);
                   Log.d("Debug",smsList.getName());
                   list.add(smsList);
                }
                smsLists = new ArrayList<>();
                smsLists.addAll(list);
                Collections.sort(smsLists,SmsList.NameComparator);
                mAdapterDt.setSmsLists(smsLists);
                mAdapterDt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mPb.setVisibility(View.GONE);
            }
        });

        mFbAdd.setVisibility(View.VISIBLE);
    }

    @Override
    public void saveData(SmsList smsList) {

        dbSmsList.child(smsList.getId()).setValue(smsList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgessDialog();
            }
        });
    }

    @Override
    public void initAdapters() {
        smsLists = new ArrayList<>();
        mRvSmsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapterDt = new SmsListAdapter(smsLists,this);
        mRvSmsList.setAdapter(mAdapterDt);
    }

    @Override
    public void showNameEmptyError() {

    }

    @Override
    public void showAddDialog(Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_reason, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final EditText mEvName = view.findViewById(R.id.et_name);
        mEvName.setHint("Introduce List Name");
        Button btSave = view.findViewById(R.id.bt_save);
        Button btCancel = view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        id = dbSmsList.push().getKey();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEvName.getText().toString().isEmpty()) {
                    mEvName.setError("Introduce List Name!");
                    mEvName.requestFocus();
                }
                else {

                    mPresenter.saveChanges(mEvName.getText().toString(),new SmsList(mEvName.getText().toString().trim(),id));
                    dialog.dismiss();
                    mPb.setVisibility(View.VISIBLE);
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
    public void showDeleteDialog(Context context, final SmsList smsList) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete production Line");
        alertDialogBuilder.setMessage(getString(R.string.delete_question)+ " "+smsList.getName()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete(smsList);
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

    @Override
    public void delete(SmsList reasonDelay) {
        progressDialog.show();
        dbSmsList.child(reasonDelay.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgessDialog();
            }
        });
    }

    @Override
    public void showSendSmsDialog() {

    }

    @Override
    public void sendSms(String number, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null, null);

    }

    @Override
    public void bindPresenter(SmsListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void DowntimeClick(String id) {
        if (id!=null) {
            EditSmsListFragment smsListFragment = EditSmsListFragment.newInstance(id);
            main.getSupportFragmentManager().beginTransaction().replace(R.id.container, smsListFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onDelete(SmsList smsList) {
        showDeleteDialog(getContext(),smsList);
    }
}
