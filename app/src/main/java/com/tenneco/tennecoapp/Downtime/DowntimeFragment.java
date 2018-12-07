package com.tenneco.tennecoapp.Downtime;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.DowntimeListAdapter;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DowntimeFragment extends Fragment implements DowntimeListAdapter.OnItemClick {

    private MainActivity main;
    private DowntimeListAdapter mAdapter;
    private DatabaseReference dbDowntime;
    private ArrayList<Downtime> mDowntimes;
    private ProgressDialog progressDialog;

    @BindView(R.id.rv_downtime) RecyclerView mRv;
    @BindView(R.id.pb_loading) ProgressBar mPb;


    @OnClick(R.id.fb_add) void addDowntime(){
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new EditCreateDowntimeFragment()).addToBackStack(null).commit();
    }


    public DowntimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downtime, container, false);
        ButterKnife.bind(this,view);
        dbDowntime = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(getContext())).child(Downtime.DB_DOWNTIMES);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        mDowntimes = new ArrayList<>();
        initAdapters();
        getDowntimes();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Changes");
        progressDialog.setMessage("Please Wait.");
    }

    public void initAdapters(){
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DowntimeListAdapter(mDowntimes,this);
        mRv.setAdapter(mAdapter);

    }



    public void getDowntimes() {
        dbDowntime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPb.setVisibility(View.GONE);
                List<Downtime> list = new ArrayList<Downtime>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    list.add(child.getValue(Downtime.class));
                }
                mDowntimes = new ArrayList<>();
                mDowntimes.addAll(list);
                Collections.sort(mDowntimes,Downtime.NameComparator);
                mAdapter.setDowntimes(mDowntimes);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mPb.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void DowntimeClick(String id) {
        if (id!=null) {
            EditCreateDowntimeFragment editCreateDowntimeFragment = EditCreateDowntimeFragment.newInstance(id);
            main.getSupportFragmentManager().beginTransaction().replace(R.id.container, editCreateDowntimeFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onDelete(Downtime downtime) {
        showDeleteDialog(getContext(),downtime);
    }

    public void showDeleteDialog(Context context, final Downtime downtime) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete Downtime List");
        alertDialogBuilder.setMessage(getString(R.string.delete_question)+ " "+downtime.getName()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete(downtime.getId());
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

    public void delete(String id) {
        progressDialog.show();
        dbDowntime.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.hide();

            }
        });
    }
}
