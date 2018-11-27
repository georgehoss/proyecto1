package com.tenneco.tennecoapp.SMS;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.tenneco.tennecoapp.Adapter.SmsListAdapter;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.SmsList;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by ghoss on 25/11/2018.
 */
public class SmsListPresenter implements SmsListContract.Presenter {
    private SmsListContract.View mView;
    private MainActivity main;
    private SmsListAdapter mAdapterDt;
    private SmsList smsList;
    private ArrayList<SmsList> smsLists;
    private String id;
    private DatabaseReference dbSmsList;
    private ProgressDialog progressDialog;
    @BindView(R.id.rv_downtime) RecyclerView mRvDw;
    @BindView(R.id.et_name) EditText mEtName;

    @BindView(R.id.pb_loading)
    ProgressBar mPb;


    public SmsListPresenter(SmsListContract.View mView) {
        this.mView = mView;
    }

    @Override
    public SmsList initData(Context context, String id) {
        return new SmsList(id);
    }

    @Override
    public boolean validName(String name) {

            return !name.isEmpty();
    }

    @Override
    public void saveChanges(String name, SmsList smsList) {

        if (validName(name)) {
            smsList.setName(name);
            mView.saveData(smsList);
        }
        else mView.showNameEmptyError();

    }

    @Override
    public void bindView(SmsListContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}
