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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.DowntimeAdapter;
import com.tenneco.tennecoapp.Adapter.LocationAdapter;
import com.tenneco.tennecoapp.Adapter.ScrapAdapter;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCreateDowntimeFragment extends Fragment implements DowntimeAdapter.OnItemClick, ScrapAdapter.OnItemClick, EditCreateDowntimeContract.View{
    private EditCreateDowntimeContract.Presenter mPresenter;
    private MainActivity main;
    private DowntimeAdapter mAdapterDt;
    private Downtime downtime;
    private ScrapAdapter mAdapterDr;
    private String id;
    private DatabaseReference dbDowntime;
    private ProgressDialog progressDialog;
    @BindView(R.id.rv_downtime) RecyclerView mRvDw;
    @BindView(R.id.rv_downtime_reasons) RecyclerView mRvReasons;
    @BindView(R.id.et_name) EditText mEtName;


    public static EditCreateDowntimeFragment newInstance(String id) {

        Bundle args = new Bundle();
        EditCreateDowntimeFragment fragment = new EditCreateDowntimeFragment();
        args.putString("id",id);
        fragment.setArguments(args);
        return fragment;
    }

    public EditCreateDowntimeFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.bt_add_downtime) void dtd(){
        showAddDowntimeDialog(getActivity(),null);
    }

    @OnClick (R.id.bt_add_downtime_reasons) void dtr () {showAddEventDialog(getActivity(),0);}

    @OnClick (R.id.fb_add) void saveDw(){
        mPresenter.saveChanges(mEtName.getText().toString(),downtime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_create_downtime, container, false);
        ButterKnife.bind(this,view);
        dbDowntime =FirebaseDatabase.getInstance().getReference(Downtime.DB_DOWNTIMES).child(StorageUtils.getPlantId(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        if (getArguments()!=null && getArguments().getString("id")!=null)
        {
            id = getArguments().getString("id");
            getData();
        }
        else {
            downtime = new Downtime();
            downtime.setZones(new ArrayList<Zone>());
            downtime.setReasons(new ArrayList<Reason>());
            id = dbDowntime.push().getKey();
            downtime.setId(id);
            initAdapters();
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving Changes");
        progressDialog.setMessage("Please Wait.");

    }

    public void saveData(Downtime downtime){
        progressDialog.show();
        dbDowntime.child(downtime.getId()).setValue(downtime).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (getFragmentManager()!=null)
                    getFragmentManager().popBackStack();
                hideProgressBar();
            }
        });
    }

    @Override
    public void hideProgressBar() {
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.hide();
    }





    public void getData() {
        Query postsQuery;
        postsQuery = dbDowntime.child(id);
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                downtime = dataSnapshot.getValue(Downtime.class);
                 if (downtime!=null) {

                    mEtName.setText(downtime.getName());
                    initAdapters();
                }
                else
                    if (getFragmentManager()!=null)
                        getFragmentManager().popBackStack();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (getFragmentManager()!=null)
                    getFragmentManager().popBackStack();
            }
        });
    }

    public void initAdapters(){
        mRvDw.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapterDt = new DowntimeAdapter(downtime.getZones(),this);
        mRvDw.setAdapter(mAdapterDt);
        mRvReasons.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapterDr = new ScrapAdapter(downtime.getReasons(),this);
        mRvReasons.setAdapter(mAdapterDr);
    }

    public void showAddDowntimeDialog(Context context, Zone zone) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_downtime, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final EditText mEvName = view.findViewById(R.id.et_name);
        final EditText mEvInfo = view.findViewById(R.id.et_info);
        final ArrayList<Location> locations = new ArrayList<>();
        if (zone!=null)
        {
            mEvName.setText(zone.getName());
            if (zone.getLocations()!=null)
                locations.addAll(zone.getLocations());
        }

        final LocationAdapter mAdapter = new LocationAdapter(locations,null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_location);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        alertDialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mEvName.getText().toString().isEmpty()) {
                    mEvName.setError("Introduce Zone name");
                    mEvName.requestFocus();
                }
                else {
                    if (downtime.getZones()==null)
                        downtime.setZones(new ArrayList<Zone>());
                    downtime.getZones().add(new Zone(mEvName.getText().toString().trim(),mAdapter.getLocations()));
                    Collections.sort(downtime.getZones(),Zone.NameComparator);
                    mAdapterDt.setZones(downtime.getZones());
                    mAdapterDt.notifyDataSetChanged();
                    dialogInterface.dismiss();
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        Button mBtAdd = view.findViewById(R.id.bt_add);

        mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEvInfo.getText().toString().isEmpty())
                {
                    mEvInfo.setError("Introduce new Location Name!");
                    mEvInfo.requestFocus();
                }
                else
                {
                    locations.add(new Location(mEvInfo.getText().toString().trim()));
                    Collections.sort(locations,Location.NameComparator);
                    mAdapter.setLocations(locations);
                    mAdapter.notifyDataSetChanged();
                    mEvInfo.setText("");
                }
            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }


    public void showAddEventDialog(Context context, final int reason) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_reason, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final EditText mEvName = view.findViewById(R.id.et_name);
        final ArrayList<Location> locations = new ArrayList<>();
        Button btSave = view.findViewById(R.id.bt_save);
        Button btCancel = view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEvName.getText().toString().isEmpty()) {
                    mEvName.setError("Introduce Reason name!");
                    mEvName.requestFocus();
                }
                else {
                    if (reason==0)
                    {
                        if (downtime.getReasons()==null)
                            downtime.setReasons(new ArrayList<Reason>());

                        downtime.getReasons().add(new Reason(mEvName.getText().toString().trim()));
                        Collections.sort(downtime.getReasons(),Reason.NameComparator);
                        mAdapterDr.setReasons(downtime.getReasons());
                        mAdapterDr.notifyDataSetChanged();
                    }

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter == null)
            mPresenter = new EditCreateDowntimePresenter(this);
        else
            mPresenter.bindView(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showNameEmptyError() {
        mEtName.setError("Please introduce List name!");
        mEtName.requestFocus();
    }


    @Override
    public void ZoneClick(Zone zone) {

    }

    @Override
    public void eventDeletClick(Reason reason) {

    }

    @Override
    public void bindPresenter(EditCreateDowntimeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void showExitDialog(Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Save Changes");
        alertDialogBuilder.setMessage("Do you want to save the changes of the list "+mEtName.getText().toString()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                mPresenter.saveChanges(mEtName.getText().toString(),downtime);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getFragmentManager().popBackStack();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.create().show();


    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView()!=null)
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        showExitDialog(getContext());
                        return true;
                    }
                }
                return false;
            }
        });

        main.hideMenu();
    }

    @Override
    public void onPause() {
        super.onPause();
        main.showMenu();
    }
}
