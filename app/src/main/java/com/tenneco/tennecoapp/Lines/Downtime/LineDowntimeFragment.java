package com.tenneco.tennecoapp.Lines.Downtime;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tenneco.tennecoapp.Adapter.DowntimeAdapter;
import com.tenneco.tennecoapp.Adapter.LocationAdapter;
import com.tenneco.tennecoapp.Adapter.ScrapAdapter;
import com.tenneco.tennecoapp.Lines.ConfigLineActivity;
import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LineDowntimeFragment extends Fragment implements  LineDowntimeContract.View, ScrapAdapter.OnItemClick, DowntimeAdapter.OnItemClick {
    LineDowntimeContract.Presenter mPresenter;
    private DowntimeAdapter mAdapterDt;
    private ScrapAdapter mAdapterSr;
    private ScrapAdapter mAdapterDr;
    @BindView(R.id.rv_downtime)
    RecyclerView mRvDowntime;
    @BindView(R.id.rv_downtime_reasons) RecyclerView mRvDowntimeReasons;
    private ConfigLineActivity main;


    @OnClick(R.id.bt_add_downtime) void dtd(){
        showAddDowntimeDialog(getContext(),null);
    }

    @OnClick (R.id.bt_add_downtime_reasons) void dtr () {showAddEventDialog(getContext(),0);}

    public LineDowntimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_line_downtime, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity()!=null)
            main = (ConfigLineActivity) getActivity();
        initAdapters();
        updateLine();
    }

    @Override
    public void initAdapters() {
        mRvDowntime.setLayoutManager(new LinearLayoutManager(main));
        mAdapterDt = new DowntimeAdapter(null,this);
        mRvDowntime.setAdapter(mAdapterDt);
        mRvDowntimeReasons.setLayoutManager(new LinearLayoutManager(main));
        mAdapterDr = new ScrapAdapter(null,this);
        mRvDowntimeReasons.setAdapter(mAdapterDr);
    }

    @Override
    public void updateLine() {
        if (main!=null && main.mLine.getDowntime()!=null)
        {
            mAdapterDt.setZones(main.mLine.getDowntime().getZones());
            mAdapterDt.notifyDataSetChanged();
            mAdapterDr.setReasons(main.mLine.getDowntime().getReasons());
            mAdapterDr.notifyDataSetChanged();
        }
    }

    @Override
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
                    main.mLine.getDowntime().getZones().add(new Zone(mEvName.getText().toString().trim(),mAdapter.getLocations()));
                    Collections.sort(main.mLine.getDowntime().getZones(),Zone.NameComparator);
                    mAdapterDt.setZones(main.mLine.getDowntime().getZones());
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
                }
            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    @Override
    public void showAddEventDialog(Context context, int reason) {
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
                    main.mLine.getDowntime().getReasons().add(new Reason(mEvName.getText().toString().trim()));
                    Collections.sort(main.mLine.getDowntime().getReasons(),Reason.NameComparator);
                    mAdapterDr.setReasons(main.mLine.getDowntime().getReasons());
                    mAdapterDr.notifyDataSetChanged();

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
    public void bindPresenter(LineDowntimeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void eventDeletClick(Reason reason) {

    }

    @Override
    public void ZoneClick(Zone zone) {

    }
}
