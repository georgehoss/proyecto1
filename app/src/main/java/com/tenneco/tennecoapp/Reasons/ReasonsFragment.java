package com.tenneco.tennecoapp.Reasons;


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
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.ReasonDelayAdapter;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.ReasonDelay;
import com.tenneco.tennecoapp.Model.SmsList;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReasonsFragment extends Fragment implements ReasonsContract.View, ReasonDelayAdapter.OnItemClick {
    private ReasonsContract.Presenter mPresenter;
    private MainActivity main;
    private ReasonDelayAdapter mAdapterDt;
    private ArrayList<ReasonDelay> reasonDelays;
    private DatabaseReference dbReasons;
    private ProgressDialog progressDialog;
    @BindView(R.id.rv_reasons)
    RecyclerView mRvSmsList;
    @BindView(R.id.fb_add)
    FloatingActionButton mFbAdd;
    @BindView(R.id.pb_loading)
    ProgressBar mPb;

    public ReasonsFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.fb_add) void saveDw(){
        showAddDialog(getContext(),null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reasons, container, false);
        ButterKnife.bind(this,view);
        dbReasons = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(getContext())).child(ReasonDelay.DB_DELAY_REASONS);
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
            mPresenter = new ReasonsPresenter(this);
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
        dbReasons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPb.setVisibility(View.GONE);
                List<ReasonDelay> list = new ArrayList<ReasonDelay>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    list.add(child.getValue(ReasonDelay.class));
                }
                reasonDelays = new ArrayList<>();
                reasonDelays.addAll(list);
                Collections.sort(reasonDelays,ReasonDelay.NameComparator);
                mAdapterDt.setReasonDelays(reasonDelays);
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
    public void saveData(ReasonDelay reasonDelay) {
        dbReasons.child(reasonDelay.getId()).setValue(reasonDelay).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgessDialog();
            }
        });
    }

    @Override
    public void initAdapters() {
        reasonDelays = new ArrayList<>();
        mRvSmsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapterDt = new ReasonDelayAdapter(reasonDelays,this);
        mRvSmsList.setAdapter(mAdapterDt);
    }


    @Override
    public void showAddDialog(Context context, ReasonDelay reasonDelay) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_reason, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final EditText mEvName = view.findViewById(R.id.et_name);
        Button btSave = view.findViewById(R.id.bt_save);
        Button btCancel = view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        String id="";
        if (reasonDelay==null)
            id = dbReasons.push().getKey();
        else
        {
            id = reasonDelay.getId();
            mEvName.setText(reasonDelay.getName());
        }

        final String finalId = id;
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEvName.getText().toString().isEmpty()) {
                    mEvName.setError("Introduce Reason!");
                    mEvName.requestFocus();
                }
                else {

                    saveData(new ReasonDelay(mEvName.getText().toString().trim(), finalId));
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
    public void showDeleteDialog(Context context, final ReasonDelay reasonDelay) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete Reason");
        alertDialogBuilder.setMessage(getString(R.string.delete_question)+ " "+reasonDelay.getName()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete(reasonDelay);
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
    public void delete(ReasonDelay reasonDelay) {
        progressDialog.show();
        dbReasons.child(reasonDelay.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgessDialog();
            }
        });
    }

    @Override
    public void bindPresenter(ReasonsContract.Presenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void DowntimeClick(ReasonDelay reasonDelay) {
        showAddDialog(getContext(),reasonDelay);
    }

    @Override
    public void onDelete(ReasonDelay reasonDelay) {
        showDeleteDialog(getContext(),reasonDelay);
    }
}
