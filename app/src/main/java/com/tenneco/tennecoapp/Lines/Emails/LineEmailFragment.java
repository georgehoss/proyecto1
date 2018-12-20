package com.tenneco.tennecoapp.Lines.Emails;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tenneco.tennecoapp.Lines.ConfigLineActivity;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.EmailList;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LineEmailFragment extends Fragment implements LineEmailContract.View {
    private LineEmailContract.Presenter mPresenter;
    public static final int DOWNTIME = 0;
    public static final int REJECT_1 = 1;
    public static final int REJECT_2 = 2;
    public static final int REJECT_3 = 3;
    public static final int FQT = 4;
    public static final int LINE =5;
    private ConfigLineActivity main;

    @BindView(R.id.tv_addresses_dwt)
    TextView mTvDwEmailList;
    @BindView(R.id.tv_addresses_sc1) TextView mTvSc1EmailList;
    @BindView(R.id.tv_addresses_sc2) TextView mTvSc2EmailList;
    @BindView(R.id.tv_addresses_sc3) TextView mTvSc3EmailList;
    @BindView(R.id.tv_addresses_leak) TextView mTvLeakEmailList;
    @BindView(R.id.tv_addresses_cell) TextView mTvCellEmailList;

    @OnClick(R.id.tv_cell) void onCell(){
        if (mTvCellEmailList.getVisibility()==View.VISIBLE)
            hideLine();
        else
            showLine();
    }

    @OnClick(R.id.tv_dw_emails) void onDw(){
        if (mTvDwEmailList.getVisibility()==View.VISIBLE)
            hideDw();
        else
            showDw();
    }

    @OnClick(R.id.tv_dw_scrap1) void onS1(){
        if (mTvSc1EmailList.getVisibility()==View.VISIBLE)
            hideR1();
        else
            showR1();
    }


    @OnClick(R.id.tv_dw_scrap2) void onS2(){
        if (mTvSc2EmailList.getVisibility()==View.VISIBLE)
            hideR2();
        else
            showR2();
    }

    @OnClick(R.id.tv_dw_scrap3) void onS3(){
        if (mTvSc3EmailList.getVisibility()==View.VISIBLE)
            hideR3();
        else
            showR3();
    }

    @OnClick(R.id.tv_leak_email) void onLeak(){
        if (mTvLeakEmailList.getVisibility()==View.VISIBLE)
            hideLeak();
        else
            showLeak();
    }

    @OnClick(R.id.bt_add_dw) void dwlist(){
        showSelectListDialog(DOWNTIME);
    }

    @OnClick (R.id.bt_add_dw_sc1) void sc1list(){
        showSelectListDialog(REJECT_1);
    }

    @OnClick (R.id.bt_add_dw_sc2) void sc2list(){
        showSelectListDialog(REJECT_2);
    }

    @OnClick (R.id.bt_add_dw_sc3) void sc3list(){
        showSelectListDialog(REJECT_3);
    }

    @OnClick (R.id.bt_add_leak) void leaklist(){
        showSelectListDialog(FQT);
    }

    @OnClick (R.id.bt_add_cell) void emaillist(){
        showSelectListDialog(LINE);
    }


    public LineEmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_email, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity()!=null)
            main = (ConfigLineActivity) getActivity();
        updateLine();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mPresenter==null)
            mPresenter = new LineEmailPresenter(this);
        else
            mPresenter.bindView(this);
    }

    @Override
    public void updateLine() {
        if(main!=null && main.mLine!=null && main.mLine.getFirst()!=null && main.mLine.getSecond()!=null && main.mLine.getThird()!=null)
        {

            if (main.mLine.getFirst().getDowntimeList()!=null &&
                    main.mLine.getSecond().getDowntimeList()!=null && main.mLine.getThird().getDowntimeList()!=null) {
                setAddressDw(mPresenter.getAddresses(main.mLine.getFirst().getDowntimeList(),
                        main.mLine.getSecond().getDowntimeList(), main.mLine.getThird().getDowntimeList()));
                showDw();
            }


            if (main.mLine.getFirst().getScrap1List()!=null &&
                    main.mLine.getSecond().getScrap1List()!=null && main.mLine.getThird().getScrap1List()!=null) {
                setAddressR1(mPresenter.getAddresses(main.mLine.getFirst().getScrap1List(),
                        main.mLine.getSecond().getScrap1List(), main.mLine.getThird().getScrap1List()));
                showR1();
            }

            if (main.mLine.getFirst().getScrap2List()!=null &&
                    main.mLine.getSecond().getScrap2List()!=null && main.mLine.getThird().getScrap2List()!=null) {
                setAddressR2(mPresenter.getAddresses(main.mLine.getFirst().getScrap2List(),
                        main.mLine.getSecond().getScrap2List(), main.mLine.getThird().getScrap2List()));
                showR2();
            }

            if (main.mLine.getFirst().getScrap3List()!=null &&
                    main.mLine.getSecond().getScrap3List()!=null && main.mLine.getThird().getScrap3List()!=null) {
                setAddressR3(mPresenter.getAddresses(main.mLine.getFirst().getScrap3List(),
                        main.mLine.getSecond().getScrap3List(), main.mLine.getThird().getScrap3List()));
                showR3();
            }

            if (main.mLine.getFirst().getLeakList()!=null &&
                    main.mLine.getSecond().getLeakList()!=null && main.mLine.getThird().getLeakList()!=null) {
                setAddressFQT(mPresenter.getAddresses(main.mLine.getFirst().getLeakList(),
                        main.mLine.getSecond().getLeakList(), main.mLine.getThird().getLeakList()));
                showLeak();
            }

            if (main.mLine.getFirst().getLineList()!=null &&
                    main.mLine.getSecond().getLineList()!=null && main.mLine.getThird().getLineList()!=null){
            setAddressLine(mPresenter.getAddresses(main.mLine.getFirst().getLineList(),
                    main.mLine.getSecond().getLineList(), main.mLine.getThird().getLineList()));
            showLine();
            }

        }
    }


    @Override
    public void showSelectListDialog(final int position) {

        if (main!=null && main.mEmailLists!=null && main.mEmailLists.size()>0)
        {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(main);
            LayoutInflater inflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_email_list, null);
            alertDialogBuilder.setView(view);
            alertDialogBuilder.setCancelable(false);
            Spinner spShift1 = view.findViewById(R.id.sp_shift_1);
            final Spinner spShift2 = view.findViewById(R.id.sp_shift_2);
            Spinner spShift3 = view.findViewById(R.id.sp_shift_3);
            final ArrayList<EmailList> mReasons = new ArrayList<>(main.mEmailLists);
            ArrayAdapter<EmailList> mAdapter = new ArrayAdapter<>(main,R.layout.spinner_row,mReasons);
            spShift1.setAdapter(mAdapter);
            spShift2.setAdapter(mAdapter);
            spShift3.setAdapter(mAdapter);
            final EmailList shift1= new EmailList();
            final EmailList shift2= new EmailList();
            final EmailList shift3= new EmailList();


            spShift1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    EmailList emailList = (EmailList) adapterView.getItemAtPosition(i);
                    if (emailList!=null) {
                        shift1.setName(emailList.getName());
                        shift1.setEmails(emailList.getEmails());
                        shift1.setId(emailList.getId());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spShift2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    EmailList emailList = (EmailList) adapterView.getItemAtPosition(i);
                    if (emailList!=null) {
                        shift2.setName(emailList.getName());
                        shift2.setEmails(emailList.getEmails());
                        shift2.setId(emailList.getId());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spShift3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    EmailList emailList = (EmailList) adapterView.getItemAtPosition(i);
                    if (emailList!=null) {
                        shift3.setName(emailList.getName());
                        shift3.setEmails(emailList.getEmails());
                        shift3.setId(emailList.getId());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            Button mBtStart = view.findViewById(R.id.bt_save);
            Button mBtEnd = view.findViewById(R.id.bt_cancel);

            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();


            mBtEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            mBtStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position==DOWNTIME) {
                        main.mLine.getFirst().setDowntimeList(shift1);
                        main.mLine.getSecond().setDowntimeList(shift2);
                        main.mLine.getThird().setDowntimeList(shift3);
                    }
                    else
                    if (position==REJECT_1) {
                        main.mLine.getFirst().setScrap1List(shift1);
                        main.mLine.getSecond().setScrap1List(shift2);
                        main.mLine.getThird().setScrap1List(shift3);
                    }
                    else
                    if (position==REJECT_2) {
                        main.mLine.getFirst().setScrap2List(shift1);
                        main.mLine.getSecond().setScrap2List(shift2);
                        main.mLine.getThird().setScrap2List(shift3);
                    }
                    else
                    if (position==REJECT_3) {
                        main.mLine.getFirst().setScrap3List(shift1);
                        main.mLine.getSecond().setScrap3List(shift2);
                        main.mLine.getThird().setScrap3List(shift3);
                    }
                    else
                    if (position==FQT) {
                        main.mLine.getFirst().setLeakList(shift1);
                        main.mLine.getSecond().setLeakList(shift2);
                        main.mLine.getThird().setLeakList(shift3);
                    }
                    else
                    {
                        main.mLine.getFirst().setLineList(shift1);
                        main.mLine.getSecond().setLineList(shift2);
                        main.mLine.getThird().setLineList(shift3);
                    }

                    updateLine();
                    dialog.dismiss();
                }
            });

        }
        else
            if (main!=null && main.mEmailLists!=null)
                showEmailListError();
    }

    @Override
    public void setAddressDw(String addressDw) {
        mTvDwEmailList.setText(addressDw);
    }

    @Override
    public void setAddressR1(String addressR1) {
        mTvSc1EmailList.setText(addressR1);
    }

    @Override
    public void setAddressR2(String addressR2) {
        mTvSc2EmailList.setText(addressR2);
    }

    @Override
    public void setAddressR3(String addressR3) {
        mTvSc3EmailList.setText(addressR3);
    }

    @Override
    public void setAddressFQT(String addressFQT) {
        mTvLeakEmailList.setText(addressFQT);
    }

    @Override
    public void setAddressLine(String addressLine) {
        mTvCellEmailList.setText(addressLine);
    }

    @Override
    public void showEmailListError() {
        Toast.makeText(main, "You must create a Email List First. \n Go back to Main menu - Notifications - Email List.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void launchEmailList() {

    }

    @Override
    public void hideLine() {
        mTvCellEmailList.setVisibility(View.GONE);
    }

    @Override
    public void showLine() {
        mTvCellEmailList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDw() {
        mTvDwEmailList.setVisibility(View.GONE);
    }

    @Override
    public void showDw() {
        mTvDwEmailList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideR1() {
        mTvSc1EmailList.setVisibility(View.GONE);
    }

    @Override
    public void showR1() {
        mTvSc1EmailList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideR2() {
        mTvSc2EmailList.setVisibility(View.GONE);
    }

    @Override
    public void showR2() {
        mTvSc2EmailList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideR3() {
        mTvSc3EmailList.setVisibility(View.GONE);
    }

    @Override
    public void showR3() {
        mTvSc3EmailList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLeak() {
        mTvLeakEmailList.setVisibility(View.GONE);
    }

    @Override
    public void showLeak() {
        mTvLeakEmailList.setVisibility(View.VISIBLE);
    }

    @Override
    public void bindPresenter(LineEmailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
