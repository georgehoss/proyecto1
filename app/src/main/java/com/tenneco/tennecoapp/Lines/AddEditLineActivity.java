package com.tenneco.tennecoapp.Lines;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.DowntimeAdapter;
import com.tenneco.tennecoapp.Adapter.EmailSelectionAdapter;
import com.tenneco.tennecoapp.Adapter.EmployeeAdapter;
import com.tenneco.tennecoapp.Adapter.EmployeeSelectionAdapter;
import com.tenneco.tennecoapp.Adapter.HourAdapter;
import com.tenneco.tennecoapp.Adapter.LocationAdapter;
import com.tenneco.tennecoapp.Adapter.PositionAdapter;
import com.tenneco.tennecoapp.Adapter.ScrapAdapter;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditLineActivity extends AppCompatActivity implements AddLineContract.View,HourAdapter.ItemInteraction, PositionAdapter.OnItemClick, DowntimeAdapter.OnItemClick, ScrapAdapter.OnItemClick, EmployeeAdapter.OnEmployeeInteraction, EmployeeSelectionAdapter.OnEmployee {
    private static final int DOWNTIME = 0;
    private static final int EVENT = 1;
    private DatabaseReference dbLines;
    private DatabaseReference dbEmployees;
    private DatabaseReference dbEmails;
    private AddLineContract.Presenter mPresenter;
    private Line mLine;
    private Shift shift1;
    private Shift shift2;
    private Shift shift3;
    private String id;
    private HourAdapter mAdapter1;
    private HourAdapter mAdapter2;
    private HourAdapter mAdapter3;
    private EmployeeSelectionAdapter mAdapterEmployee;
    private PositionAdapter mAdapterPos;
    private DowntimeAdapter mAdapterDt;
    private ScrapAdapter mAdapterSr;
    private ScrapAdapter mAdapterDr;
    private ArrayList<EmployeePosition> mPositions;
    private ArrayList<Employee> mEmployees;
    private ArrayList<Email> mEmails;
    private ArrayList<Zone> mZones;
    private ArrayList<Reason> mReasons;
    private ArrayList<Reason> mDReasons;
    private ArrayList<Email> mDwtList;
    private ArrayList<Email> mScr1List;
    private boolean deletable = false;
    private Downtime downtime;
    private ProgressDialog progressDialog;
    @BindView(R.id.et_name) EditText mEtName;
    @BindView(R.id.et_psw) EditText mEtPsw;
    @BindView(R.id.ll_shift1) LinearLayout mLlS1;
    @BindView(R.id.ll_shift2) LinearLayout mLlS2;
    @BindView(R.id.ll_shift3) LinearLayout mLlS3;
    @BindView(R.id.ll_position) LinearLayout mLlPosition;
    @BindView(R.id.ll_downtime) LinearLayout mLlDowntime;
    @BindView(R.id.ll_scrap) LinearLayout mLlScrap;
    @BindView(R.id.ll_email) LinearLayout mLlEmails;
    @BindView(R.id.rv_shift1) RecyclerView mRvS1;
    @BindView(R.id.rv_shift2) RecyclerView mRvS2;
    @BindView(R.id.rv_shift3) RecyclerView mRvS3;
    @BindView(R.id.rv_employee) RecyclerView mRvEmployee;
    @BindView(R.id.rv_position) RecyclerView mRvPosition;
    @BindView(R.id.rv_downtime) RecyclerView mRvDowntime;
    @BindView(R.id.rv_downtime_reasons) RecyclerView mRvDowntimeReasons;
    @BindView(R.id.rv_scrap) RecyclerView mRvScrap;
    @BindView(R.id.cv_s1) CardView mCvS1;
    @BindView(R.id.cv_s2) CardView mCvS2;
    @BindView(R.id.cv_s3) CardView mCvS3;
    @BindView(R.id.cv_sr) CardView mCvSr;
    @BindView(R.id.cv_dt) CardView mCvDt;
    @BindView(R.id.cv_ss) CardView mCvSs;
    @BindView(R.id.cv_psw) CardView mCvPsw;
    @BindView(R.id.cv_emails) CardView mCvEmail;
    @BindView(R.id.tv_addresses_dwt) TextView mTvDwEmailList;
    @BindView(R.id.tv_addresses_sc1) TextView mTvSc1EmailList;
    @BindView(R.id.tv_addresses_sc2) TextView mTvSc2EmailList;
    @BindView(R.id.tv_addresses_sc3) TextView mTvSc3EmailList;




    @OnClick(R.id.tv_shift1) void show1st(){
        mPresenter.onShift1Click(mLlS1.getVisibility(),View.VISIBLE);
    }
    @OnClick(R.id.tv_shift2) void show2nd(){
        mPresenter.onShift2Click(mLlS2.getVisibility(),View.VISIBLE);
    }
    @OnClick(R.id.tv_shift3) void show3rd(){
        mPresenter.onShift3Click(mLlS3.getVisibility(),View.VISIBLE);
    }

    @OnClick(R.id.tv_position) void position(){
        mPresenter.onPositionClick(mLlPosition.getVisibility(), View.VISIBLE);
    }

    @OnClick(R.id.bt_add_position) void addPos(){
        showPositionDialog(new EmployeePosition(""),this);
    }

    @OnClick (R.id.tv_downtime) void dt(){
        mPresenter.onDowntimeClick(mLlDowntime.getVisibility(),View.VISIBLE);
    }

    @OnClick (R.id.tv_scrap) void scr(){
        mPresenter.onScrapClick(mLlScrap.getVisibility(),View.VISIBLE);
    }

    @OnClick (R.id.tv_email) void email(){
        mPresenter.onEmailClick(mLlEmails.getVisibility(),View.VISIBLE);
    }

    @OnClick (R.id.bt_list_shift1) void listS1(){
        showDialogEmployee(shift1.getEmployees(),this,1).show();
    }

    @OnClick (R.id.bt_add_downtime) void dtd(){
        showAddDowntimeDialog(this,null);
    }

    @OnClick (R.id.bt_add_downtime_reasons) void dtr () {showAddEventDialog(this,DOWNTIME);}

    @OnClick (R.id.bt_add_scrap) void scrr () {showAddEventDialog(this,EVENT);}

    @OnClick (R.id.bt_add_dw_email) void dwlist(){
        showEmailList(this,mLine.getDowntimeList(),0);
    }

    @OnClick (R.id.bt_add_dw_sc1) void sc1list(){
        showEmailList(this,mLine.getScrap1List(),1);
    }

    @OnClick (R.id.bt_add_dw_sc2) void sc2list(){
        showEmailList(this,mLine.getScrap2List(),2);
    }

    @OnClick (R.id.bt_add_dw_sc3) void sc3list(){
        showEmailList(this,mLine.getScrap3List(),3);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_line);
        ButterKnife.bind(this);
        if (getIntent().getExtras()!=null && getIntent().getExtras().getBoolean("cell"))
            dbLines = FirebaseDatabase.getInstance().getReference(Line.DB_PRODUCTION_LINE);
        else
            dbLines = FirebaseDatabase.getInstance().getReference(Line.DB_LINE);

        dbEmployees = FirebaseDatabase.getInstance().getReference(Employee.DB);
        dbEmails = FirebaseDatabase.getInstance().getReference(Email.DB);
        if (mPresenter == null)
            mPresenter = new AddLinePresenter(this);
        else
            mPresenter.bindView(this);

        initAdapters();

        if (getIntent().getExtras()!=null && getIntent().getExtras().getString("id")!=null)
        {
            id = getIntent().getExtras().getString("id");
            getData();
        }
        else
        if (getIntent().getExtras()==null || !getIntent().getExtras().getBoolean("cell")){
            id = dbLines.push().getKey();
            mPresenter.initData(this);
            getEmployees();
            getEmails();
        }
        else
            finish();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Changes");
        progressDialog.setMessage("Please Wait.");


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        MenuItem item = menu.findItem(R.id.menu_delete);

        if (deletable){
            item.setVisible(true);
        }
        else
        {
            item.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save)
        {
            if (mPresenter.validName(mEtName.getText().toString().trim()))
                mPresenter.saveChanges(mEtName.getText().toString().trim(),id,shift1,shift2,shift3,
                        mPositions,downtime,mReasons,mLine,mEmployees,mEmails, mEtPsw.getText().toString().trim());
            else
                showNameError();
        }

        if (item.getItemId() == R.id.menu_delete)
        {
            showDeleteDialog(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initAdapters() {
        shift1 = new Shift();
        mRvS1.setLayoutManager(new LinearLayoutManager(this));
        mAdapter1 = new HourAdapter(new ArrayList<WorkHour>(),this,1);
        mRvS1.setAdapter(mAdapter1);
        shift2 = new Shift();
        mRvS2.setLayoutManager(new LinearLayoutManager(this));
        mAdapter2 = new HourAdapter(new ArrayList<WorkHour>(),this,2);
        mRvS2.setAdapter(mAdapter2);
        shift3 = new Shift();
        mRvS3.setLayoutManager(new LinearLayoutManager(this));
        mAdapter3 = new HourAdapter(new ArrayList<WorkHour>(),this,3);
        mRvS3.setAdapter(mAdapter3);
        mPositions = new ArrayList<>();
        mRvPosition.setLayoutManager(new LinearLayoutManager(this));
        mAdapterPos = new PositionAdapter(mPositions,this);
        mRvPosition.setAdapter(mAdapterPos);
        mZones = new ArrayList<>();
        mRvDowntime.setLayoutManager(new LinearLayoutManager(this));
        mAdapterDt = new DowntimeAdapter(mZones,this);
        mRvDowntime.setAdapter(mAdapterDt);
        mReasons = new ArrayList<>();
        mRvScrap.setLayoutManager(new LinearLayoutManager(this));
        mAdapterSr = new ScrapAdapter(mReasons,this);
        mRvScrap.setAdapter(mAdapterSr);
        mDReasons = new ArrayList<>();
        mRvDowntimeReasons.setLayoutManager(new LinearLayoutManager(this));
        mAdapterDr = new ScrapAdapter(mDReasons,this);
        mRvDowntimeReasons.setAdapter(mAdapterDr);
        mRvEmployee.setLayoutManager(new LinearLayoutManager(this));
        mAdapterEmployee = new EmployeeSelectionAdapter(null,this);
        mRvEmployee.setAdapter(mAdapterEmployee);

    }

    @Override
    public void hideshift1() {
        mLlS1.setVisibility(View.GONE);
    }

    @Override
    public void showshift1() {
        mCvS1.setVisibility(View.VISIBLE);
        mLlS1.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideshift2() {
        mLlS2.setVisibility(View.GONE);
    }

    @Override
    public void showshift2() {
        mCvS2.setVisibility(View.VISIBLE);
        mLlS2.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideshift3() {
        mLlS3.setVisibility(View.GONE);
    }

    @Override
    public void showshift3()
    {
        mCvS3.setVisibility(View.VISIBLE);
        mLlS3.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePosition() {
        mLlPosition.setVisibility(View.GONE);
    }

    @Override
    public void showPosition() {
        mCvSr.setVisibility(View.VISIBLE);
        mLlPosition.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAll() {
        mCvS1.setVisibility(View.GONE);
        mCvS2.setVisibility(View.GONE);
        mCvS3.setVisibility(View.GONE);
        mCvSr.setVisibility(View.GONE);
        mCvDt.setVisibility(View.GONE);
        mCvSs.setVisibility(View.GONE);
        mCvEmail.setVisibility(View.GONE);
        hidePsw();

    }

    @Override
    public void showAll() {
        mCvS1.setVisibility(View.VISIBLE);
        mCvS2.setVisibility(View.VISIBLE);
        mCvS3.setVisibility(View.VISIBLE);
        mCvSr.setVisibility(View.VISIBLE);
        mCvDt.setVisibility(View.VISIBLE);
        mCvSs.setVisibility(View.VISIBLE);
        mCvEmail.setVisibility(View.VISIBLE);
        showPsw();
    }

    @Override
    public void hideDowntime() {
        mLlDowntime.setVisibility(View.GONE);
    }

    @Override
    public void showDowntime() {
        mCvDt.setVisibility(View.VISIBLE);
        mLlDowntime.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideScrap() {
        mLlScrap.setVisibility(View.GONE);
    }

    @Override
    public void showScrap() {
        mCvSs.setVisibility(View.VISIBLE);
        mLlScrap.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmails() {
        mLlEmails.setVisibility(View.GONE);

    }

    @Override
    public void showEmails() {
        mCvEmail.setVisibility(View.VISIBLE);
        mLlEmails.setVisibility(View.VISIBLE);

    }

    @Override
    public void addPosition(EmployeePosition position) {
        mPositions.add(position);
        mAdapterPos.setPositions(mPositions);
        mAdapterPos.notifyDataSetChanged();
    }


    @Override
    public void deletePosition(EmployeePosition position) {
        mPositions.remove(position);
        mAdapterPos.setPositions(mPositions);
        mAdapterPos.notifyDataSetChanged();
    }

    @Override
    public void saveLine(Line line) {
        progressDialog.show();
        dbLines.child(line.getId()).setValue(line).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mCvS1.getVisibility()==View.VISIBLE && mCvS2.getVisibility()==View.VISIBLE  && mCvS3.getVisibility()==View.VISIBLE &&
        mCvSr.getVisibility()==View.VISIBLE && mCvDt.getVisibility()==View.VISIBLE && mCvSs.getVisibility()==View.VISIBLE)
            showExitDialog(this);
        else
            showAll();
    }

    @Override
    public void getData() {
        Query postsQuery;
        postsQuery = dbLines.child(id);
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Line line = dataSnapshot.getValue(Line.class);
                if (line!=null) {
                    mEtName.setText(line.getName());
                    if (line.getPassword()!=null)
                    mEtPsw.setText(line.getPassword());
                    setData(line);
                    deletable = true;
                    invalidateOptionsMenu();
                    getEmployees();
                    getEmails();
                }
                else
                    finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
    }

    @Override
    public void setData(Line line) {
        mLine = line;
        shift1 = line.getFirst();
        shift2 = line.getSecond();
        shift3 = line.getThird();
        mPositions = new ArrayList<>();
        if (line.getPositions()!=null)
        mPositions.addAll(line.getPositions());
        mAdapter1.setHours(shift1.getHours());
        mAdapter1.notifyDataSetChanged();
        mAdapter2.setHours(shift2.getHours());
        mAdapter2.notifyDataSetChanged();
        mAdapter3.setHours(shift3.getHours());
        mAdapter3.notifyDataSetChanged();
        mAdapterPos.setPositions(line.getPositions());
        mAdapterPos.notifyDataSetChanged();
        mZones = new ArrayList<>();
        if (line.getDowntime()!=null) {
            downtime = line.getDowntime();
            if (line.getDowntime().getZones()!=null){
            mAdapterDt.setZones(line.getDowntime().getZones());
            mAdapterDt.notifyDataSetChanged();}
            if (line.getDowntime().getReasons()!=null) {
                mAdapterDr.setReasons(line.getDowntime().getReasons());
                mAdapterDr.notifyDataSetChanged();
                mDReasons = new ArrayList<>();
                mDReasons.addAll(line.getDowntime().getReasons());
            }
        }
        else
            downtime = new Downtime();

        if (line.getScrapReasons()!=null) {
            mAdapterSr.setReasons(line.getScrapReasons());
            mAdapterSr.notifyDataSetChanged();
            mReasons = new ArrayList<>();
            mReasons.addAll(line.getScrapReasons());
        }

        if(mLine.getDowntimeList()!=null && mLine.getDowntimeList().size()>0)
        {
            mTvDwEmailList.setText(mPresenter.getEmailList(mLine.getDowntimeList()).toString());
        }

        if(line.getScrap1List()!=null && line.getScrap1List().size()>0)
        {

            mTvSc1EmailList.setText(mPresenter.getEmailList(line.getScrap1List()).toString());
        }

        if(line.getScrap2List()!=null && line.getScrap2List().size()>0)
        {
            mTvSc2EmailList.setText(mPresenter.getEmailList(line.getScrap2List()).toString());
        }

        if(line.getScrap3List()!=null && line.getScrap3List().size()>0)
        {
            mTvSc3EmailList.setText(mPresenter.getEmailList(line.getScrap3List()).toString());
        }




        mLine.setEmployees(mPresenter.getEmployees(shift1.getEmployees(),shift2.getEmployees(),shift3.getEmployees()));


    }



    @Override
    public void showShiftDialog(final Shift shift, final int shiftNumber, Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.dialog_shift, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        //alertDialogBuilder.setTitle(Window.FEATURE_NO_TITLE);
        TextView mTvName = view.findViewById(R.id.tv_shift1);
        if (shiftNumber==1)
        mTvName.setText(getString(R.string.add_1st_shift));
        else
        if (shiftNumber==2)
            mTvName.setText(getString(R.string.add_2nd_shift));
        else
            mTvName.setText(getString(R.string.add_3rd_shift));

        final EditText mEtS1s1 = view.findViewById(R.id.et_s1_shour1);
        final EditText mEtS1e1 = view.findViewById(R.id.et_s1_ehour1);
        final EditText mEtS1t1 = view.findViewById(R.id.et_s1_t1);
        if (shift.getHours().get(0)!=null) {
            mEtS1s1.setText(shift.getHours().get(0).getStartHour());
            mEtS1e1.setText(shift.getHours().get(0).getEndHour());
            mEtS1t1.setText(shift.getHours().get(0).getTarget());
        }
        final EditText mEtS1s2 = view.findViewById(R.id.et_s1_shour2);
        final EditText mEtS1e2 = view.findViewById(R.id.et_s1_ehour2);
        final EditText mEtS1t2 = view.findViewById(R.id.et_s1_t2);
        if (shift.getHours().get(1)!=null) {
            mEtS1s2.setText(shift.getHours().get(1).getStartHour());
            mEtS1e2.setText(shift.getHours().get(1).getEndHour());
            mEtS1t2.setText(shift.getHours().get(1).getTarget());
        }
        final EditText mEtS1s3 = view.findViewById(R.id.et_s1_shour3);
        final EditText mEtS1e3 = view.findViewById(R.id.et_s1_ehour3);
        final EditText mEtS1t3 = view.findViewById(R.id.et_s1_t3);
        if (shift.getHours().get(2)!=null) {
            mEtS1s3.setText(shift.getHours().get(2).getStartHour());
            mEtS1e3.setText(shift.getHours().get(2).getEndHour());
            mEtS1t3.setText(shift.getHours().get(2).getTarget());
        }
        final EditText mEtS1s4 = view.findViewById(R.id.et_s1_shour4);
        final EditText mEtS1e4 = view.findViewById(R.id.et_s1_ehour4);
        final EditText mEtS1t4 = view.findViewById(R.id.et_s1_t4);
        if (shift.getHours().get(3)!=null) {
            mEtS1s4.setText(shift.getHours().get(3).getStartHour());
            mEtS1e4.setText(shift.getHours().get(3).getEndHour());
            mEtS1t4.setText(shift.getHours().get(3).getTarget());
        }
        final EditText mEtS1s5 = view.findViewById(R.id.et_s1_shour5);
        final EditText mEtS1e5 = view.findViewById(R.id.et_s1_ehour5);
        final EditText mEtS1t5 = view.findViewById(R.id.et_s1_t5);
        if (shift.getHours().get(4)!=null) {
            mEtS1s5.setText(shift.getHours().get(4).getStartHour());
            mEtS1e5.setText(shift.getHours().get(4).getEndHour());
            mEtS1t5.setText(shift.getHours().get(4).getTarget());
        }
        final EditText mEtS1s6 = view.findViewById(R.id.et_s1_shour6);
        final EditText mEtS1e6 = view.findViewById(R.id.et_s1_ehour6);
        final EditText mEtS1t6 = view.findViewById(R.id.et_s1_t6);
        if (shift.getHours().get(5)!=null) {
            mEtS1s6.setText(shift.getHours().get(5).getStartHour());
            mEtS1e6.setText(shift.getHours().get(5).getEndHour());
            mEtS1t6.setText(shift.getHours().get(5).getTarget());
        }
        final EditText mEtS1s7 = view.findViewById(R.id.et_s1_shour7);
        final EditText mEtS1e7 = view.findViewById(R.id.et_s1_ehour7);
        final EditText mEtS1t7 = view.findViewById(R.id.et_s1_t7);
        if (shift.getHours().get(6)!=null) {
            mEtS1s7.setText(shift.getHours().get(6).getStartHour());
            mEtS1e7.setText(shift.getHours().get(6).getEndHour());
            mEtS1t7.setText(shift.getHours().get(6).getTarget());
        }
        final EditText mEtS1s8 = view.findViewById(R.id.et_s1_shour8);
        final EditText mEtS1e8 = view.findViewById(R.id.et_s1_ehour8);
        final EditText mEtS1t8 = view.findViewById(R.id.et_s1_t8);
        if (shift.getHours().get(7)!=null) {
            mEtS1s8.setText(shift.getHours().get(7).getStartHour());
            mEtS1e8.setText(shift.getHours().get(7).getEndHour());
            mEtS1t8.setText(shift.getHours().get(7).getTarget());
        }

        Button mBtSave = view.findViewById(R.id.bt_save);
        Button mBtCancel = view.findViewById(R.id.bt_cancel);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s1 = mEtS1s1.getText().toString().trim();
                String e1 = mEtS1e1.getText().toString().trim();
                String t1 = mEtS1t1.getText().toString().trim();
                String s2 = mEtS1s2.getText().toString().trim();
                String e2 = mEtS1e2.getText().toString().trim();
                String t2 = mEtS1t2.getText().toString().trim();
                String s3 = mEtS1s3.getText().toString().trim();
                String e3 = mEtS1e3.getText().toString().trim();
                String t3 = mEtS1t3.getText().toString().trim();
                String s4 = mEtS1s4.getText().toString().trim();
                String e4 = mEtS1e4.getText().toString().trim();
                String t4 = mEtS1t4.getText().toString().trim();
                String s5 = mEtS1s5.getText().toString().trim();
                String e5 = mEtS1e5.getText().toString().trim();
                String t5 = mEtS1t5.getText().toString().trim();
                String s6 = mEtS1s6.getText().toString().trim();
                String e6 = mEtS1e6.getText().toString().trim();
                String t6 = mEtS1t6.getText().toString().trim();
                String s7 = mEtS1s7.getText().toString().trim();
                String e7 = mEtS1e7.getText().toString().trim();
                String t7 = mEtS1t7.getText().toString().trim();
                String s8 = mEtS1s8.getText().toString().trim();
                String e8 = mEtS1e8.getText().toString().trim();
                String t8 = mEtS1t8.getText().toString().trim();

                if (s1.isEmpty())
                {
                    mEtS1s1.setError(getString(R.string.star_hour));
                    mEtS1s1.requestFocus();
                }
                else
                if (e1.isEmpty())
                {
                    mEtS1e1.setError(getString(R.string.end_hour));
                    mEtS1e1.requestFocus();
                }
                else
                if (t1.isEmpty())
                {
                    mEtS1t1.setError(getString(R.string.error_target));
                    mEtS1t1.requestFocus();
                }
                else
                if (s2.isEmpty())
                {
                    mEtS1s2.setError(getString(R.string.star_hour));
                    mEtS1s2.requestFocus();
                }
                else
                if (e2.isEmpty())
                {
                    mEtS1e2.setError(getString(R.string.end_hour));
                    mEtS1e2.requestFocus();
                }
                else
                if (t2.isEmpty())
                {
                    mEtS1t2.setError(getString(R.string.error_target));
                    mEtS1t2.requestFocus();
                }
                else
                if (s3.isEmpty())
                {
                    mEtS1s3.setError(getString(R.string.star_hour));
                    mEtS1s3.requestFocus();
                }
                else
                if (e3.isEmpty())
                {
                    mEtS1e3.setError(getString(R.string.end_hour));
                    mEtS1e3.requestFocus();
                }
                else
                if (t3.isEmpty())
                {
                    mEtS1t3.setError(getString(R.string.error_target));
                    mEtS1t3.requestFocus();
                }
                else
                if (s4.isEmpty())
                {
                    mEtS1s4.setError(getString(R.string.star_hour));
                    mEtS1s4.requestFocus();
                }
                else
                if (e4.isEmpty())
                {
                    mEtS1e4.setError(getString(R.string.end_hour));
                    mEtS1e4.requestFocus();
                }
                else
                if (t4.isEmpty())
                {
                    mEtS1t4.setError(getString(R.string.error_target));
                    mEtS1t4.requestFocus();
                }
                else
                if (s5.isEmpty())
                {
                    mEtS1s5.setError(getString(R.string.star_hour));
                    mEtS1s5.requestFocus();
                }
                else
                if (e5.isEmpty())
                {
                    mEtS1e5.setError(getString(R.string.end_hour));
                    mEtS1e5.requestFocus();
                }
                else
                if (t5.isEmpty())
                {
                    mEtS1t5.setError(getString(R.string.error_target));
                    mEtS1t5.requestFocus();
                }
                else
                if (s6.isEmpty())
                {
                    mEtS1s6.setError(getString(R.string.star_hour));
                    mEtS1s6.requestFocus();
                }
                else
                if (e6.isEmpty())
                {
                    mEtS1e6.setError(getString(R.string.end_hour));
                    mEtS1e6.requestFocus();
                }
                else
                if (t6.isEmpty())
                {
                    mEtS1t6.setError(getString(R.string.error_target));
                    mEtS1t6.requestFocus();
                }
                else
                if (s7.isEmpty())
                {
                    mEtS1s7.setError(getString(R.string.star_hour));
                    mEtS1s7.requestFocus();
                }
                else
                if (e7.isEmpty())
                {
                    mEtS1e7.setError(getString(R.string.end_hour));
                    mEtS1e7.requestFocus();
                }
                else
                if (t7.isEmpty())
                {
                    mEtS1t7.setError(getString(R.string.error_target));
                    mEtS1t7.requestFocus();
                }
                else
                if (s8.isEmpty())
                {
                    mEtS1s8.setError(getString(R.string.star_hour));
                    mEtS1s8.requestFocus();
                }
                else
                if (e8.isEmpty())
                {
                    mEtS1e8.setError(getString(R.string.end_hour));
                    mEtS1e8.requestFocus();
                }
                else
                if (t8.isEmpty())
                {
                    mEtS1t8.setError(getString(R.string.error_target));
                    mEtS1t8.requestFocus();
                }
                else
                if (shiftNumber==1)
                {
                    shift1=mPresenter.getshift(
                            s1,e1,t1,
                            s2,e2,t2,
                            s3,e3,t3,
                            s4,e4,t4,
                            s5,e5,t5,
                            s6,e6,t6,
                            s7,e7,t7,
                            s8,e8,t8);
                    mAdapter1.setHours(shift1.getHours());
                    mAdapter1.notifyDataSetChanged();
                    dialog.dismiss();
                }
                else
                if (shiftNumber==2)
                {
                    shift2=mPresenter.getshift(
                            s1,e1,t1,
                            s2,e2,t2,
                            s3,e3,t3,
                            s4,e4,t4,
                            s5,e5,t5,
                            s6,e6,t6,
                            s7,e7,t7,
                            s8,e8,t8);
                    mAdapter2.setHours(shift2.getHours());
                    mAdapter2.notifyDataSetChanged();
                    dialog.dismiss();
                }
                else
                if (shiftNumber==3)
                {
                    shift3=mPresenter.getshift(
                            s1,e1,t1,
                            s2,e2,t2,
                            s3,e3,t3,
                            s4,e4,t4,
                            s5,e5,t5,
                            s6,e6,t6,
                            s7,e7,t7,
                            s8,e8,t8);
                    mAdapter3.setHours(shift3.getHours());
                    mAdapter3.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });

    }

    @Override
    public void showDeleteDialog(Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete production Line");
        alertDialogBuilder.setMessage(getString(R.string.delete_question)+ " "+mEtName.getText().toString()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
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
    public void showExitDialog(Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Save Changes");
        alertDialogBuilder.setMessage("Do you want to leave without saving the changes of the line:"+mEtName.getText().toString()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (mPresenter.validName(mEtName.getText().toString().trim()))
                    mPresenter.saveChanges(mEtName.getText().toString().trim(),id,shift1,shift2,shift3, mPositions,downtime,mReasons,mLine,mEmployees,mEmails
                            , mEtPsw.getText().toString().trim());
                else
                    showNameError();
            }
        });
        alertDialogBuilder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
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

    @Override
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

    @Override
    public void delete() {
        progressDialog.show();
        dbLines.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.hide();
                finish();

            }
        });
    }

    @Override
    public void showNameError() {
        mEtName.setError("Introduce Line Name!");
        mEtName.requestFocus();
    }

    @Override
    public void getEmployees() {
        dbEmployees.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mEmployees = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Employee employee = itemSnapshot.getValue(Employee.class);
                    if (employee!=null)
                        mEmployees.add(employee);


                }

                if (mLine.getEmployees()!=null)
                    mLine.setEmployees(mPresenter.verifyEmployees(mLine.getEmployees(),mEmployees));
                else {
                    mLine.setEmployees(new ArrayList<Employee>());
                    for (Employee employee : mEmployees)
                    mLine.getEmployees().add(new Employee(employee.getFullName(),employee.getInfo(),employee.getType()));
                }

                mAdapterEmployee.setEmployees(mLine.getEmployees());
                mAdapterEmployee.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public AlertDialog showDialogEmployee(final ArrayList<Employee> employees, Context context, final int shift) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_list_operator_shift, null);
        EmployeeAdapter adapter1 = new EmployeeAdapter(mLine.getFirst().getEmployees());
        EmployeeAdapter adapter2 = new EmployeeAdapter(mLine.getSecond().getEmployees());
        EmployeeAdapter adapter3 = new EmployeeAdapter(mLine.getThird().getEmployees());
        RecyclerView recyclerView1 = view.findViewById(R.id.rv_eshift1);
        RecyclerView recyclerView2= view.findViewById(R.id.rv_eshift2);
        RecyclerView recyclerView3 = view.findViewById(R.id.rv_eshift3);
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
        recyclerView1.setAdapter(adapter1);
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setLayoutManager(new LinearLayoutManager(context));
        recyclerView3.setAdapter(adapter3);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return alertDialogBuilder.create();

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
                }
            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    @Override
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
                    if (reason==DOWNTIME)
                    {
                        downtime.getReasons().add(new Reason(mEvName.getText().toString().trim()));
                        Collections.sort(downtime.getReasons(),Reason.NameComparator);
                        mAdapterDr.setReasons(downtime.getReasons());
                        mAdapterDr.notifyDataSetChanged();
                    }
                    else {
                        mReasons.add(new Reason(mEvName.getText().toString().trim()));
                        Collections.sort(mReasons,Reason.NameComparator);
                        mAdapterSr.setReasons(mReasons);
                        mAdapterSr.notifyDataSetChanged();
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
    public void getEmails() {
        dbEmails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Email> emails = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Email email = itemSnapshot.getValue(Email.class);
                    if (email!=null)
                        emails.add(email);
                }

                setEmails(emails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    void setEmails(ArrayList<Email> emails)
    {

        mEmails = new ArrayList<>(emails);

        if (mLine.getDowntimeList()==null) {
            mLine.setDowntimeList(new ArrayList<Email>());
           for (Email email: emails)
               mLine.getDowntimeList().add(new Email(email));
        }
        else
            mLine.setDowntimeList(mPresenter.verifyEmails(mLine.getDowntimeList(),emails));

        if (mLine.getScrap1List()==null) {
            mLine.setScrap1List(new ArrayList<Email>());
            for (Email email: emails) {
                mLine.getScrap1List().add(new Email(email));

            }
        }
        else
            mLine.setScrap1List(mPresenter.verifyEmails(mLine.getScrap1List(),emails));

        if (mLine.getScrap2List()==null){
            mLine.setScrap2List(new ArrayList<Email>());
            for (Email email: emails)
                mLine.getScrap2List().add(new Email(email));
        }
        else
            mLine.setScrap2List(mPresenter.verifyEmails(mLine.getScrap2List(),emails));

        if (mLine.getScrap3List()==null)
        {
            mLine.setScrap3List(new ArrayList<Email>());
            for (Email email: emails)
                mLine.getScrap3List().add(new Email(email));
        }
        else
            mLine.setScrap3List(mPresenter.verifyEmails(mLine.getScrap3List(),emails));

        setData(mLine);
    }

    @Override
    public void showEmailList(Context context, ArrayList<Email> list, final int position) {
        if (list!=null && list.size()>0) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            final EmailSelectionAdapter adapter = new EmailSelectionAdapter(list);
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            alertDialogBuilder.setView(recyclerView);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (position) {
                        case 1:
                            mLine.setScrap1List(adapter.getEmails());
                            break;
                        case 2:
                            mLine.setScrap2List(adapter.getEmails());
                            break;
                        case 3:
                            mLine.setScrap3List(adapter.getEmails());
                            break;
                        default:
                            mLine.setDowntimeList(adapter.getEmails());
                            break;
                    }
                    setData(mLine);
                    dialogInterface.dismiss();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
        }
    }

    @Override
    public void showPsw() {
        mCvPsw.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePsw() {
        mCvPsw.setVisibility(View.GONE);
    }


    @Override
    public void PositionClick(EmployeePosition employeePosition) {
        showDeletePosition(employeePosition,this);
    }

    @Override
    public void ZoneClick(Zone zone) {

    }

    @Override
    public void eventDeletClick(Reason reason) {

    }

    @Override
    public void EditEmploye(Employee employee) {
        showDialogEmployee(shift1.getEmployees(),this,1).show();
    }

    @Override
    public void onEmployeeChange() {

        mLine.setEmployees(mAdapterEmployee.getEmployees());
        shift1.setEmployees(new ArrayList<Employee>());
        shift2.setEmployees(new ArrayList<Employee>());
        shift3.setEmployees(new ArrayList<Employee>());

        for (Employee employee : mLine.getEmployees())
        {
            if (employee.isAvailable())
            {
                switch (employee.getShift()){
                    case 1:
                        shift1.getEmployees().add(employee);
                        break;
                    case 2:
                        shift2.getEmployees().add(employee);
                        break;
                    case 3:
                        shift3.getEmployees().add(employee);
                        break;
                }
            }
        }

        mLine.getFirst().setEmployees(shift1.getEmployees());
        mLine.getSecond().setEmployees(shift2.getEmployees());
        mLine.getThird().setEmployees(shift3.getEmployees());


    }

    @Override
    public void bindPresenter(AddLineContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onTargetClick(int position) {
        if (position==1)
            showShiftDialog(shift1,position,this);
        else
        if (position==2)
            showShiftDialog(shift2,position,this);
        else
        if (position==3)
            showShiftDialog(shift3,position,this);

    }
}
