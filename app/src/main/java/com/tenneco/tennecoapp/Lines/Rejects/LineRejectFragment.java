package com.tenneco.tennecoapp.Lines.Rejects;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tenneco.tennecoapp.Adapter.ScrapAdapter;
import com.tenneco.tennecoapp.Lines.ConfigLineActivity;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LineRejectFragment extends Fragment implements LineRejectContract.View, ScrapAdapter.OnItemClick {

    private ScrapAdapter mAdapterSr;
    private ConfigLineActivity main;
    @BindView(R.id.rv_scrap)
    RecyclerView mRvScrap;

    @OnClick(R.id.bt_add_scrap) void scrr () {showAddEventDialog(getContext(),0);}


    public LineRejectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_reject, container, false);
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
        mRvScrap.setLayoutManager(new LinearLayoutManager(main));
        mAdapterSr = new ScrapAdapter(null,this);
        mRvScrap.setAdapter(mAdapterSr);
    }

    @Override
    public void updateLine() {
        if (main!=null && main.mLine!=null && main.mLine.getScrapReasons()!=null) {
            mAdapterSr.setReasons(main.mLine.getScrapReasons());
            mAdapterSr.notifyDataSetChanged();
        }
    }

    @Override
    public void showAddEventDialog(Context context, int reason) {
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

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEvName.getText().toString().isEmpty()) {
                    mEvName.setError("Introduce Reason name!");
                    mEvName.requestFocus();
                }
                else {
                    main.mLine.getScrapReasons().add(new Reason(mEvName.getText().toString().trim()));
                    Collections.sort(main.mLine.getScrapReasons(),Reason.NameComparator);
                    mAdapterSr.setReasons(main.mLine.getScrapReasons());
                    mAdapterSr.notifyDataSetChanged();
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
    public void eventDeletClick(Reason reason) {

    }
}
