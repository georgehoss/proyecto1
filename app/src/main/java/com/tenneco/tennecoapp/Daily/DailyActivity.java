package com.tenneco.tennecoapp.Daily;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.DailyAdapter;
import com.tenneco.tennecoapp.Adapter.EndShiftPositionAdapter;
import com.tenneco.tennecoapp.Adapter.ProductAdapter;
import com.tenneco.tennecoapp.Adapter.RejectEventAdapter;
import com.tenneco.tennecoapp.Adapter.UserSelectorAdapter;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.EmailList;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.Product;
import com.tenneco.tennecoapp.Model.Production;
import com.tenneco.tennecoapp.Model.ReasonDelay;
import com.tenneco.tennecoapp.Model.Reject;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.Sms;
import com.tenneco.tennecoapp.Model.SmsList;
import com.tenneco.tennecoapp.Model.Template;
import com.tenneco.tennecoapp.Model.Templates;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;
import com.tenneco.tennecoapp.Utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyActivity extends AppCompatActivity implements DailyContract.View,DailyAdapter.ItemInteraction, ProductAdapter.OnItemClick {
    private static final int TEAM =0;
    private static final int GROUP =1;
    private DailyContract.Presenter mPresenter;
    private DatabaseReference dbLine;
    private DatabaseReference dbPLine;
    private DatabaseReference dbTeamLd;
    private DatabaseReference dbGroupLd;
    private DatabaseReference dbOperators;
   // private DatabaseReference dbNumbers;
    private DatabaseReference dbReasons;
    private DatabaseReference dbTemplates;
    private Templates templates;
    private Query postsQuery;
    private Context context;
    private int turno=0;
    private DatabaseReference dbEmailList;

    private ValueEventListener emailEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<EmailList> list = new ArrayList<>();
            for (DataSnapshot child: dataSnapshot.getChildren()) {
                EmailList emailList =child.getValue(EmailList.class);
                if (emailList!=null) {
                    list.add(emailList);
                    if (mLine.getFirst().getDowntimeList().getId().equals(emailList.getId()))
                        mLine.getFirst().getDowntimeList().setEmails(emailList.getEmails());
                    if (mLine.getFirst().getLineList().getId().equals(emailList.getId()))
                        mLine.getFirst().getLineList().setEmails(emailList.getEmails());
                    if (mLine.getFirst().getLeakList().getId().equals(emailList.getId()))
                        mLine.getFirst().getLeakList().setEmails(emailList.getEmails());
                    if (mLine.getFirst().getScrap1List().getId().equals(emailList.getId()))
                        mLine.getFirst().getScrap1List().setEmails(emailList.getEmails());
                    if (mLine.getFirst().getScrap2List().getId().equals(emailList.getId()))
                        mLine.getFirst().getScrap2List().setEmails(emailList.getEmails());
                    if (mLine.getFirst().getScrap3List().getId().equals(emailList.getId()))
                        mLine.getFirst().getScrap3List().setEmails(emailList.getEmails());


                    if (mLine.getSecond().getDowntimeList().getId().equals(emailList.getId()))
                        mLine.getSecond().getDowntimeList().setEmails(emailList.getEmails());
                    if (mLine.getSecond().getLineList().getId().equals(emailList.getId()))
                        mLine.getSecond().getLineList().setEmails(emailList.getEmails());
                    if (mLine.getSecond().getLeakList().getId().equals(emailList.getId()))
                        mLine.getSecond().getLeakList().setEmails(emailList.getEmails());
                    if (mLine.getSecond().getScrap1List().getId().equals(emailList.getId()))
                        mLine.getSecond().getScrap1List().setEmails(emailList.getEmails());
                    if (mLine.getSecond().getScrap2List().getId().equals(emailList.getId()))
                        mLine.getSecond().getScrap2List().setEmails(emailList.getEmails());
                    if (mLine.getSecond().getScrap3List().getId().equals(emailList.getId()))
                        mLine.getSecond().getScrap3List().setEmails(emailList.getEmails());


                    if (mLine.getThird().getDowntimeList().getId().equals(emailList.getId()))
                        mLine.getThird().getDowntimeList().setEmails(emailList.getEmails());
                    if (mLine.getThird().getLineList().getId().equals(emailList.getId()))
                        mLine.getThird().getLineList().setEmails(emailList.getEmails());
                    if (mLine.getThird().getLeakList().getId().equals(emailList.getId()))
                        mLine.getThird().getLeakList().setEmails(emailList.getEmails());
                    if (mLine.getThird().getScrap1List().getId().equals(emailList.getId()))
                        mLine.getThird().getScrap1List().setEmails(emailList.getEmails());
                    if (mLine.getThird().getScrap2List().getId().equals(emailList.getId()))
                        mLine.getThird().getScrap2List().setEmails(emailList.getEmails());
                    if (mLine.getThird().getScrap3List().getId().equals(emailList.getId()))
                        mLine.getThird().getScrap3List().setEmails(emailList.getEmails());

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };


    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mLine = dataSnapshot.getValue(Line.class);
            if (mLine!=null) {
                turno = mLine.getTurno();
                setLine();
                getPLine();
                getEmails();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ArrayList<User> mTeam;
    private ArrayList<User> mGroup;
    private ArrayList<Employee> mOperators;
    private ArrayList<SmsList> mSmsLists;
    private ArrayList<ReasonDelay> mReasons;
    private Line mLine;
    private Line mPline;
    private DailyAdapter mAdapter;
    private RejectEventAdapter mAdapterScr;
    private String lineId;
    private String parentId;
    private ArrayList<WorkHour> mHours;
    private ProgressDialog progressDialog;
    private boolean isShowingLeak = false;
    private boolean sendReport = false;
    private boolean sendReport1 = false;
    private boolean sendReport2 = false;
    private AlertDialog dialog;
    private AlertDialog productDialog;
    private AlertDialog ftqDialog;
    private AlertDialog downtimeDialog;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    private Product lastProduct;
    GestureDetector gestureDetector;


    @BindView(R.id.tv_name)TextView mTvName;
    @BindView(R.id.tv_date)TextView mTvDate;
    @BindView(R.id.rv_lines) RecyclerView mRvLine;
    @BindView(R.id.tv_a_s1) TextView mTvActS1;
    @BindView(R.id.tv_t_s1) TextView mTvTS1;
    @BindView(R.id.tv_a_s2) TextView mTvActS2;
    @BindView(R.id.tv_t_s2) TextView mTvTS2;
    @BindView(R.id.tv_a_s3) TextView mTvActS3;
    @BindView(R.id.tv_t_s3) TextView mTvTS3;
    @BindView(R.id.tv_start_s1) TextView mTvStartS1;
    @BindView(R.id.tv_start_s2) TextView mTvStartS2;
    @BindView(R.id.tv_start_s3) TextView mTvStartS3;
    @BindView(R.id.tv_dt_s1) TextView mTvDtS1;
    @BindView(R.id.tv_dt_s2) TextView mTvDtS2;
    @BindView(R.id.tv_dt_s3) TextView mTvDtS3;
    @BindView(R.id.tv_end_s1) TextView mTvEndS1;
    @BindView(R.id.tv_end_s2) TextView mTvEndS2;
    @BindView(R.id.tv_end_s3) TextView mTvEndS3;
    @BindView(R.id.tv_leak_s1) TextView mTvLeakS1;
    @BindView(R.id.tv_leak_s2) TextView mTvLeakS2;
    @BindView(R.id.tv_leak_s3) TextView mTvLeakS3;
    @BindView(R.id.tv_tls_1) TextView mTvTls1;
    @BindView(R.id.tv_tls_2) TextView mTvTls2;
    @BindView(R.id.tv_tls_3) TextView mTvTls3;
    @BindView(R.id.tv_gls_1) TextView mTvGls1;
    @BindView(R.id.tv_gls_2) TextView mTvGls2;
    @BindView(R.id.tv_gls_3) TextView mTvGls3;
    @BindView(R.id.tv_operators) TextView mTvOperators;
    @BindView(R.id.ll_operators) LinearLayout mLlOperators;
    @BindView(R.id.ll_tlstx) LinearLayout mLlTls;
    @BindView(R.id.ll_glstx) LinearLayout mLlGls;
    @BindView(R.id.bt_hour) Button mBtHour;
    @BindView(R.id.bt_downtime) Button mBtDowntime;
    @BindView(R.id.bt_event) Button mBtScrap;
    @BindView(R.id.bt_counter) Button mBtCounter;
    @BindView(R.id.bt_end_s1) Button mBtS1;
    @BindView(R.id.bt_end_s2) Button mBtS2;
    @BindView(R.id.bt_end_s3) Button mBtS3;
    @BindView(R.id.ll_scrap) LinearLayout mLlScrap;
    @BindView(R.id.rv_scrap) RecyclerView mRvScrap;



    @OnClick(R.id.bt_hour) void repor(){

        if (turno!=0) {
            if (mPresenter.reportHour(mHours,turno) != 24)
                showActualsDialog(mHours.get(mPresenter.reportHour(mHours,turno)), mLine, mPresenter.reportHour(mHours,turno), this);
        }
        else
            Toast.makeText(this, "You must start a shift first", Toast.LENGTH_SHORT).show();


    }

    @OnClick(R.id.bt_end_s1) void endS1(){
        if (mLine!=null && mLine.getFirst()!=null) {
            if (!mLine.getFirst().isClosed() && turno==0)
                showEndShiftDialog(mLine, 1, this, true);
            else
                if (turno==1)
                showFinishFirst();
                else
                    Toast.makeText(this, "You must end the actual shift", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.bt_end_s2) void endS2(){
        if (mLine!=null && mLine.getSecond()!=null) {
            if (!mLine.getSecond().isClosed()&& turno==0)
                showEndShiftDialog(mLine, 2, this, true);
            else
                if (turno ==2)
                    showFinishSecond();
                else
                    Toast.makeText(this, "You must end the actual shift", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.bt_end_s3) void endS3(){
        if (mLine!=null && mLine.getThird()!=null) {
            if (!mLine.getThird().isClosed() && turno==0)
                showEndShiftDialog(mLine, 3, this, true);
            else
                if (turno==3)
                    showFinishThird();
                else
                    Toast.makeText(this, "You must end the actual shift", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.bt_counter) void count(){
        // mPresenter.incrementCount(mLine);
        //progressDialog.show();
        showProductListDialog(mLine.getProducts(),this);
    }

    @OnClick(R.id.bt_downtime) void down(){
        if (turno!=0)
            showDowntimeDialog(mLine.getDowntime(),this);
        else
            Toast.makeText(this, "You must start a shift first", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bt_event) void scrap(){
        if (turno!=0)
        showRejectDialog(mLine.getScrapReasons(),this);
        else
            Toast.makeText(this, "You must start a shift first", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tv_shift1) void shf1(){
        showEndShiftDialog(mLine, 1, this, false);
        }

    @OnClick(R.id.tv_shift2) void shf2(){
        showEndShiftDialog(mLine,2,this,false);
    }

    @OnClick(R.id.tv_shift3) void shf3(){
        showEndShiftDialog(mLine,3,this,false);
    }

    @OnClick(R.id.tv_gls_1) void group1(){
        showUserDialog(mGroup,this,1,1);
    }

    @OnClick(R.id.tv_gls_2) void group2(){
        showUserDialog(mGroup,this,1,2);
    }

    @OnClick(R.id.tv_gls_3) void group3(){
        showUserDialog(mGroup,this,1,3);
    }

    @OnClick(R.id.tv_tls_1) void team1(){
        showUserDialog(mTeam,this,0,1);
    }

    @OnClick(R.id.tv_tls_2) void team2(){
        showUserDialog(mTeam,this,0,2);
    }

    @OnClick(R.id.tv_tls_3) void team3(){
        showUserDialog(mTeam,this,0,3);
    }

    @OnClick(R.id.bt_send_sms) void send(){

        //showSendMsgDialog(this);
        if (turno!=0) {
            showEndShiftDialog(mLine, turno, this, false);
        }
        else
            Toast.makeText(this, "You must start a shift first", Toast.LENGTH_SHORT).show();

    }

  /*  @OnClick(R.id.tv_end_s1) void fe1(){
        if (!mLine.getFirst().isClosed())
            showDialogClose(1);
    }

    @OnClick(R.id.tv_end_s2) void fe2(){
        if (!mLine.getSecond().isClosed())
            showDialogClose(2);
    }


    @OnClick(R.id.tv_end_s3) void fe3(){
        if (!mLine.getThird().isClosed())
            showDialogClose(3);
    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        ButterKnife.bind(this);

        if (getIntent().getExtras()!=null && getIntent().getExtras().getString("parentId")!=null)
            parentId = getIntent().getExtras().getString("parentId");
        else
            finish();

        dbLine = FirebaseDatabase.getInstance().getReference(Line.DB_PRODUCTION_LINE).child(StorageUtils.getPlantId(this)).child(parentId);
        dbPLine= FirebaseDatabase.getInstance().getReference(Line.DB_LINE).child(StorageUtils.getPlantId(this));
        dbGroupLd = FirebaseDatabase.getInstance().getReference(User.DB_GROUP).child(StorageUtils.getPlantId(this));
        dbTeamLd = FirebaseDatabase.getInstance().getReference(User.DB_TEAM).child(StorageUtils.getPlantId(this));
        dbOperators = FirebaseDatabase.getInstance().getReference(Employee.DB).child(StorageUtils.getPlantId(this));
        dbReasons= FirebaseDatabase.getInstance().getReference(ReasonDelay.DB_DELAY_REASONS).child(StorageUtils.getPlantId(this));
        //dbNumbers = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(this)).child(SmsList.DB_SMS_LIST);
        //dbNumbers.keepSynced(false);
        dbTemplates = FirebaseDatabase.getInstance().getReference(Template.DB_TEMPLATE).child(StorageUtils.getPlantId(this));
        dbEmailList = FirebaseDatabase.getInstance().getReference(EmailList.DB).child(StorageUtils.getPlantId(this));
        if (mPresenter == null)
            mPresenter = new DailyPresenter(this);
        else
            mPresenter.bindView(this);

        initAdapter();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Changes...");

        permissions();
        setGestureDetector();

        findViewById(R.id.bt_send_sms).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showOperators();
                return false;
            }
        });
    }


    private void showOperators(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_schedulel, null);
        alertDialogBuilder.setView(view);
        TextView tvDate = view.findViewById(R.id.tv_date);
        view.findViewById(R.id.tv_name).setVisibility(View.GONE);
        TextView tvLines = view.findViewById(R.id.tv_lines);
        Button btCancel = view.findViewById(R.id.bt_cancel);
        Button btAccept = view.findViewById(R.id.bt_save);
        btCancel.setText("OK");
        btAccept.setVisibility(View.GONE);

        StringBuilder sb = new StringBuilder();
        tvDate.setText(mTvDate.getText());
        if (mLine.getOperators()!=null && mLine.getOperators().size()>0) {
            for (Employee e : mLine.getOperators()) {
                sb.append("- ").append(e.getFullName()).append("\n");
                int tot = 0;
                for (Production p : e.getProductions()) {
                    sb.append(" ").append(p.getProductName()).append(": ").append(p.getTotal());
                    tot += p.getTotal();
                }
                sb.append("\nTotal: ").append(String.valueOf(tot));
                sb.append("\n\n");
            }
        }
        else
            sb.append("The are no records");
        tvLines.setText(sb.toString());
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void hideOperators() {
        mLlOperators.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setOperators(String operators) {
        mTvOperators.setText(operators);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getExtras()!=null && getIntent().getExtras().getString("id")!=null)
            lineId = getIntent().getExtras().getString("id");

        getLine();
        getGroup();
        getTeam();
        getOperators();
        getNumbers();
        getReasons();
        getTemplates();

    }

    @Override
    public void bindPresenter(DailyContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void getLine() {

        postsQuery = dbLine.child(lineId);
        postsQuery.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        postsQuery.removeEventListener(valueEventListener);
        dbEmailList.removeEventListener(emailEventListener);
        context = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLine();
        context = this;
    }

    @Override
    public void setLine() {


        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.hide();

        lastProduct = mLine.getLastProduct();

        if (lastProduct==null)
            showProductListDialog(mLine.getProducts(),this);
        else {
            if (productDialog!=null && productDialog.isShowing() && !DailyActivity.this.isFinishing())
                productDialog.dismiss();

            if (!mTvName.getText().toString().equals(mLine.getName()))
                mTvName.setText(mLine.getName());

            if (!mTvDate.getText().toString().equals(mLine.getDate()))
                mTvDate.setText(mLine.getDate());


            mTvActS1.setText(mLine.getFirst().getCumulativeActual());
            mTvTS1.setText(mLine.getFirst().getHours().get(7).getCumulativePlanned());
            mTvActS2.setText(mLine.getSecond().getCumulativeActual());
            mTvTS2.setText(mLine.getSecond().getHours().get(7).getCumulativePlanned());
            mTvActS3.setText(mLine.getThird().getCumulativeActual());
            mTvTS3.setText(mLine.getThird().getHours().get(7).getCumulativePlanned());


            if (mLine.getFirst().getTimeStart() != null && !mLine.getFirst().getTimeStart().isEmpty()) {
                mTvStartS1.setText(mLine.getFirst().getTimeStart());
                if (mLine.getFirst().getTimeEnd() == null || mLine.getFirst().getTimeEnd().isEmpty())
                    mBtS1.setText(R.string.daily_end_1st_shift);
                else
                    mBtS1.setText(R.string.add_1st_shift);
            } else
                mBtS1.setText(R.string.start_1st_shift);

            if (mLine.getSecond().getTimeStart() != null && !mLine.getSecond().getTimeStart().isEmpty()) {
                mTvStartS2.setText(mLine.getSecond().getTimeStart());
                if (mLine.getSecond().getTimeEnd() == null || mLine.getSecond().getTimeEnd().isEmpty())
                    mBtS2.setText(R.string.dailly_end_2nd_shift);
                else
                    mBtS2.setText(R.string.add_2nd_shift);

            } else
                mBtS2.setText(R.string.star_2nd_shift);

            if (mLine.getThird().getTimeStart() != null && !mLine.getThird().getTimeStart().isEmpty()) {
                mTvStartS3.setText(mLine.getThird().getTimeStart());
                if (mLine.getThird().getTimeEnd() == null || mLine.getThird().getTimeEnd().isEmpty())
                    mBtS3.setText(R.string.daily_end_3rd_shift);
                else
                    mBtS3.setText(R.string.add_3rd_shift);

            } else
                mBtS3.setText(R.string.start_3rd_shift);


            mPresenter.setDowntimes(mLine);


            if (mLine.getFirst().getTimeEnd() != null && !mTvEndS1.getText().toString().equals(mLine.getFirst().getTimeEnd()))
                mTvEndS1.setText(mLine.getFirst().getTimeEnd());

            if (mLine.getSecond().getTimeEnd() != null && !mTvEndS2.getText().toString().equals(mLine.getSecond().getTimeEnd()))
                mTvEndS2.setText(mLine.getSecond().getTimeEnd());

            if (mLine.getThird().getTimeEnd() != null && !mTvEndS3.getText().toString().equals(mLine.getThird().getTimeEnd()))
                mTvEndS3.setText(mLine.getThird().getTimeEnd());

            if (!mTvLeakS1.getText().toString().equals(mLine.getFirst().getCumulativeFTQ()))
                mTvLeakS1.setText(mLine.getFirst().getCumulativeFTQ());
            if (!mTvLeakS3.getText().toString().equals(mLine.getFirst().getCumulativeFTQ()))
                mTvLeakS2.setText(mLine.getSecond().getCumulativeFTQ());
            if (!mTvLeakS3.getText().toString().equals(mLine.getFirst().getCumulativeFTQ()))
                mTvLeakS3.setText(mLine.getThird().getCumulativeFTQ());

            mHours = new ArrayList<>();
            mHours.addAll(mLine.getFirst().getHours());
            mHours.addAll(mLine.getSecond().getHours());
            mHours.addAll(mLine.getThird().getHours());
            mAdapter.setHours(mHours);
            mAdapter.notifyDataSetChanged();
            mPresenter.showCount(mLine);
            mPresenter.verifyLeaks(mLine);
            ArrayList<Reject> rejects = new ArrayList<>();
            if (mLine.getFirst().getRejects() != null)
                rejects.addAll(mLine.getFirst().getRejects());
            if (mLine.getSecond().getRejects() != null)
                rejects.addAll(mLine.getSecond().getRejects());
            if (mLine.getThird().getRejects() != null)
                rejects.addAll(mLine.getThird().getRejects());

         /*   if (mLine.getRejects() != null)
                rejects.addAll(mLine.getRejects());*/


            if (mLine.getDowntime().isSet()) {
                showDowntimeDialog(mLine.getDowntime(), this);
            }


            if (rejects.size() > 0) {
                showReject();
                mAdapterScr.setRejects(rejects);
                mAdapterScr.notifyDataSetChanged();
            } else {
                hideReject();
            }

            if (!isShowingLeak && sendReport1) {
                showDialogEndShift(1);
                sendReport1 = false;
            }

            if (!isShowingLeak && sendReport2) {
                showDialogEndShift(2);
                sendReport2 = false;
            }

            if (!isShowingLeak && sendReport) {
                showDialogEndShift(3);
                sendReport = false;
            }

            mPresenter.setTeam(mLine);
            mPresenter.setGroup(mLine);
            mPresenter.setOperators(mLine);


            if (mLine.getTurno()==1)
            {
                mLine.setDowntimeEmailList(mLine.getFirst().getDowntimeList());
                mLine.setLineList(mLine.getFirst().getLineList());
                mLine.setScrap1EmailList(mLine.getFirst().getScrap1List());
                mLine.setScrap2EmailList(mLine.getFirst().getScrap2List());
                mLine.setScrap3EmailList(mLine.getFirst().getScrap3List());
                mLine.setLeakEmailList(mLine.getFirst().getLeakList());
                turno=1;
            }
            else
            if (mLine.getTurno()==2)
            {
                mLine.setDowntimeEmailList(mLine.getSecond().getDowntimeList());
                mLine.setLineList(mLine.getSecond().getLineList());
                mLine.setScrap1EmailList(mLine.getSecond().getScrap1List());
                mLine.setScrap2EmailList(mLine.getSecond().getScrap2List());
                mLine.setScrap3EmailList(mLine.getSecond().getScrap3List());
                mLine.setLeakEmailList(mLine.getSecond().getLeakList());
                turno = 2;
            }
            else
            if (mLine.getTurno()==3)
            {
                mLine.setDowntimeEmailList(mLine.getThird().getDowntimeList());
                mLine.setLineList(mLine.getThird().getLineList());
                mLine.setScrap1EmailList(mLine.getThird().getScrap1List());
                mLine.setScrap2EmailList(mLine.getThird().getScrap2List());
                mLine.setScrap3EmailList(mLine.getThird().getScrap3List());
                mLine.setLeakEmailList(mLine.getThird().getLeakList());
                turno = 3;
            }
            else
            {
                if (mLine.getFirst().getTimeStart()!=null && !mLine.getFirst().getTimeStart().isEmpty() && (mLine.getFirst().getTimeEnd()==null||mLine.getFirst().getTimeEnd().isEmpty()) )
                    turno =1;
                if (mLine.getSecond().getTimeStart()!=null && !mLine.getSecond().getTimeStart().isEmpty() && (mLine.getSecond().getTimeEnd()==null||mLine.getSecond().getTimeEnd().isEmpty()) )
                    turno =2;
                if (mLine.getThird().getTimeStart()!=null && !mLine.getThird().getTimeStart().isEmpty() && (mLine.getThird().getTimeEnd()==null||mLine.getThird().getTimeEnd().isEmpty()) )
                    turno =3;
            }


            /*

            if (mLine.getFirst().isClosed())
                mPresenter.createCSVShift(this, mLine, 1, mLine.getFirst());

            if (mLine.getSecond().isClosed())
                mPresenter.createCSVShift(this, mLine, 2, mLine.getSecond());

            if (mLine.getThird().isClosed())
                mPresenter.createCSVShift(this, mLine, 3, mLine.getThird());

            if (mLine.getFirst().isClosed() && mLine.getSecond().isClosed() && mLine.getThird().isClosed())
                try {
                    mPresenter.createCVS(this, mLine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */

        }
    }


    @Override
    public void getGroup() {

        dbGroupLd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroup = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    User user = itemSnapshot.getValue(User.class);
                    if (user!=null)
                        mGroup.add(user);
                }
                Collections.sort(mGroup,User.UserNameComparator);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getTeam() {
        dbTeamLd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTeam = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    User user = itemSnapshot.getValue(User.class);
                    if (user!=null)
                        mTeam.add(user);
                }
                Collections.sort(mTeam,User.UserNameComparator);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getNumbers() {
        /*dbNumbers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSmsLists = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    SmsList smsList = itemSnapshot.getValue(SmsList.class);
                    if (smsList!=null)
                        mSmsLists.add(smsList);
                }
                Collections.sort(mSmsLists,SmsList.NameComparator);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
    }

    @Override
    public void getOperators() {
        dbOperators.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mOperators = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Employee employee = itemSnapshot.getValue(Employee.class);
                    if (employee!=null)
                        mOperators.add(employee);
                }
                Collections.sort(mOperators,Employee.EmployeeNameComparator);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getReasons() {
        dbReasons.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mReasons = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    ReasonDelay reason = itemSnapshot.getValue(ReasonDelay.class);
                    if (reason!=null)
                        mReasons.add(reason);
                }
                Collections.sort(mReasons,ReasonDelay.NameComparator);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getTemplates() {
        Query postsQuery;
        postsQuery = dbTemplates.child(Templates.ID);
        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                templates = dataSnapshot.getValue(Templates.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
    }

    @Override
    public void getEmails() {
        dbEmailList.addValueEventListener(emailEventListener);
    }

    @Override
    public void showActualsDialog(final WorkHour workHour, Line line, final int position, Context context) {

        if (!mLine.getDowntime().isSet())
        {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_actual, null);
            alertDialogBuilder.setView(view);
            alertDialogBuilder.setCancelable(false);
            //alertDialogBuilder.setTitle(Window.FEATURE_NO_TITLE);
            TextView mTvName = view.findViewById(R.id.tv_name);
            mTvName.setText(line.getName());
            TextView mTvDate = view.findViewById(R.id.tv_date);
            mTvDate.setText(line.getDate());
            TextView mTvShift = view.findViewById(R.id.tv_shift);
            if (position<=7)
                mTvShift.setText(getString(R.string.add_1st_shift));
            if (position>=8 && position<=15)
                mTvShift.setText(getString(R.string.add_2nd_shift));
            if (position>15)
                mTvShift.setText(getString(R.string.add_3rd_shift));
            TextView mTvTime = view.findViewById(R.id.tv_time);
            String time = workHour.getStartHour() + " - " + workHour.getEndHour();
            mTvTime.setText(time);
            final TextView mTvTarget = view.findViewById(R.id.tv_target);
            mTvTarget.setText(workHour.getTarget());
            final EditText mEtActual = view.findViewById(R.id.et_actual);
            final EditText mEtFtq = view.findViewById(R.id.et_ftq);
            mEtActual.requestFocus();
            if (workHour.getLeak()!=null)
                mEtFtq.setText(workHour.getLeak());
            if (workHour.getActuals()!=null)
                mEtActual.setText(workHour.getActuals());
            if (workHour.getLeak()!=null && !workHour.getLeak().isEmpty())
                mEtFtq.setText(workHour.getLeak());
            else
                mEtFtq.setText("0");
            final EditText mEtComments = view.findViewById(R.id.et_comments);
            String comments ="";
            if (workHour.getComments()!=null) {
                mEtComments.setText(workHour.getComments());
                comments = workHour.getComments();
            }
            Button mBtSave = view.findViewById(R.id.bt_save);
            Button mBtCancel = view.findViewById(R.id.bt_cancel);
            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();

            final LinearLayout mLlReason = view.findViewById(R.id.ll_reason);
            final Spinner spReason = view.findViewById(R.id.sp_reason);
            final EditText etReason = view.findViewById(R.id.et_reason);
            final ArrayList<ReasonDelay> mList = new ArrayList<>();
            mList.add(new ReasonDelay("- Select a Reason -","null"));
            if (mReasons!=null)
                mList.addAll(mReasons);
            mList.add(new ReasonDelay("Other","0"));
            ArrayAdapter<ReasonDelay> mAdapter = new ArrayAdapter<>(context,R.layout.spinner_row,mList);
            spReason.setAdapter(mAdapter);
            final ReasonDelay reasonDelay = new ReasonDelay();


            mEtActual.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty())
                    {
                        String actual = editable.toString().trim();
                        if (mPresenter.validateReason(  reasonDelay,actual,workHour.getTarget())) {
                            mLlReason.setVisibility(View.VISIBLE);
                            mEtActual.setTextColor(getResources().getColor(R.color.colorRed));
                        }
                        else {
                            mLlReason.setVisibility(View.GONE);
                            mEtActual.setTextColor(getResources().getColor(R.color.colorGreen));
                            spReason.setSelection(0);
                            reasonDelay.setId("null");
                        }
                    }
                }
            });

            final String finalComments = comments;
            spReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ReasonDelay reas = (ReasonDelay) adapterView.getItemAtPosition(i);
                    if (reas!=null) {
                        String commentt="";
                        reasonDelay.setName(reas.getName());
                        reasonDelay.setId(reas.getId());
                        if (!reas.getName().equals("Other") && !reas.getName().equals("- Select a Reason -")) {
                            if (finalComments.isEmpty())
                                commentt = reas.getName();
                            else
                                commentt = finalComments + "\n" + reas.getName();
                        }
                        else
                        if (reas.getName().equals("Other"))
                            if (!finalComments.isEmpty())
                                commentt=finalComments +"\n";


                            if (!reas.getName().equals("- Select a Reason -"))
                                mEtComments.setText(commentt);
                            else
                                mEtComments.setText(finalComments);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            mBtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            mBtSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String actual = mEtActual.getText().toString().trim();
                    String comment = mEtComments.getText().toString().trim();
                    String leak = mEtFtq.getText().toString().trim();

                    if (position==0 && (mLine.getFirst().getTimeStart()==null || mLine.getFirst().getTimeStart().isEmpty()))
                        mLine.getFirst().setTimeStart(Utils.getTimeString());
                    else
                    if (position==8 && (mLine.getSecond().getTimeStart()==null || mLine.getSecond().getTimeStart().isEmpty()))
                        mLine.getSecond().setTimeStart(Utils.getTimeString());
                    else
                    if (position==16 && (mLine.getThird().getTimeStart()==null || mLine.getThird().getTimeStart().isEmpty()))
                        mLine.getThird().setTimeStart(Utils.getTimeString());

                    if (mPresenter.validateActual(actual))
                    {
                        mEtActual.setError("Please, introduce the actual value!");
                        mEtActual.requestFocus();
                    }
                    else
                    if (mPresenter.validateActual(leak))
                    {
                        mEtFtq.setError("Please, introduce a leak value!");
                        mEtFtq.requestFocus();
                    }
                    else
                    if (mPresenter.validateComment(comment,actual,workHour.getTarget()))
                    {
                        mEtComments.setError("You must introduce a comment!");
                        mEtComments.requestFocus();
                        mEtActual.setTextColor(getResources().getColor(R.color.colorRed));
                        Toast.makeText(DailyActivity.this, "Actual value is below target!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    if (mPresenter.validateReason(reasonDelay,actual,workHour.getTarget()))
                    {
                        mLlReason.setVisibility(View.VISIBLE);
                        Toast.makeText(DailyActivity.this, "Please select a Reason!", Toast.LENGTH_SHORT).show();

                    }
                    else
                    if(mPresenter.validateReasonSelection(actual,workHour.getTarget(),reasonDelay.getName(), mEtComments.getText().toString())){
                        mEtComments.setError("You must describe the reason!");
                        mEtComments.requestFocus();
                    }
                    else {
                        if (mLlReason.getVisibility()==View.VISIBLE && reasonDelay.getName().equals("Other"))
                            reasonDelay.setReason(mEtComments.getText().toString());
                        mPresenter.saveLine(mLine, mHours, position, actual, comment,reasonDelay,workHour.getOwner(),leak);
                        dialog.dismiss();
                    }
                }
            });
        }
        else
            showDowntimeDialog(mLine.getDowntime(),this);


    }

    @Override
    public void showTargetDialog(final WorkHour workHour, Line line, final int position, Context context) {
        if (!mLine.getDowntime().isSet())
        {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_actual, null);
            alertDialogBuilder.setView(view);
            alertDialogBuilder.setCancelable(false);
            //alertDialogBuilder.setTitle(Window.FEATURE_NO_TITLE);
            TextView mTvName = view.findViewById(R.id.tv_name);
            mTvName.setText(line.getName());
            TextView mTvDate = view.findViewById(R.id.tv_date);
            mTvDate.setText(line.getDate());
            TextView mTvShift = view.findViewById(R.id.tv_shift);
            if (position<=7)
                mTvShift.setText(getString(R.string.add_1st_shift));
            if (position>=8 && position<=15)
                mTvShift.setText(getString(R.string.add_2nd_shift));
            if (position>15)
                mTvShift.setText(getString(R.string.add_3rd_shift));

            final EditText mEtFtq = view.findViewById(R.id.et_ftq);
            mEtFtq.setEnabled(false);

            TextView mTvTime = view.findViewById(R.id.tv_time);
            String time = workHour.getStartHour() + " - " + workHour.getEndHour();
            mTvTime.setText(time);
            TextView mTvTarget = view.findViewById(R.id.tv_target);
            TextView mTvTargetLabel = view.findViewById(R.id.tv_target_label);
            mTvTargetLabel.setText(R.string.target);
            mTvTarget.setText(workHour.getTarget());
            final EditText mEtActual = view.findViewById(R.id.et_actual);
            mEtActual.setHint("Introduce new Target Value");
            if (workHour.getTarget()!=null)
                mEtActual.setText(workHour.getTarget());

            final EditText mEtComments = view.findViewById(R.id.et_comments);
            mEtComments.setHint("Introduce cell password");
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
                    String target = mEtActual.getText().toString().trim();
                    String psw = mEtComments.getText().toString().trim();

                    if (mPresenter.validateActual(target))
                    {
                        mEtActual.setError("Please, introduce the target value!");
                        mEtActual.requestFocus();
                    }
                    else
                    if (psw.equals(mLine.getPassword())){
                        mPresenter.saveLine(mLine, mHours, position, target);
                        dialog.dismiss();
                    }
                    else
                    {
                        mEtComments.setError("The Password is incorrect!");
                        mEtComments.requestFocus();
                    }
                }
            });
        }
        else
            showDowntimeDialog(mLine.getDowntime(),this);
    }

    @Override
    public void showOwnerDialog(final WorkHour workHour, Line line, final int position, Context context) {
        if (!mLine.getDowntime().isSet())
        {
            int lq = 0;
            int ac = 0;
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_actual, null);
            alertDialogBuilder.setView(view);
            alertDialogBuilder.setCancelable(false);
            //alertDialogBuilder.setTitle(Window.FEATURE_NO_TITLE);
            TextView mTvName = view.findViewById(R.id.tv_name);
            mTvName.setText(line.getName());
            TextView mTvDate = view.findViewById(R.id.tv_date);
            mTvDate.setText(line.getDate());
            TextView mTvShift = view.findViewById(R.id.tv_shift);
            final EditText mEtPsw = view.findViewById(R.id.et_psw);
            mEtPsw.setVisibility(View.VISIBLE);
            final EditText mEtFtq = view.findViewById(R.id.et_ftq);
            if (workHour.getLeak()!=null) {
                mEtFtq.setText(workHour.getLeak());
                lq = Integer.valueOf(workHour.getLeak());
            }
            mEtFtq.setEnabled(false);
            if (position<=7)
                mTvShift.setText(getString(R.string.add_1st_shift));
            if (position>=8 && position<=15)
                mTvShift.setText(getString(R.string.add_2nd_shift));
            if (position>15)
                mTvShift.setText(getString(R.string.add_3rd_shift));
            TextView mTvTime = view.findViewById(R.id.tv_time);
            final String time = workHour.getStartHour() + " - " + workHour.getEndHour();
            mTvTime.setText(time);
            TextView mTvTarget = view.findViewById(R.id.tv_target);
            mTvTarget.setText(workHour.getTarget());
            final EditText mEtActual = view.findViewById(R.id.et_actual);
            if (workHour.getActuals()!=null) {
                mEtActual.setText(workHour.getActuals());
                ac = Integer.valueOf(workHour.getActuals());
            }
            final EditText mEtComments = view.findViewById(R.id.et_comments);
            if (workHour.getComments()!=null)
                mEtComments.setText(workHour.getComments());
            Button mBtSave = view.findViewById(R.id.bt_save);
            mBtSave.setText("Validate");
            Button mBtCancel = view.findViewById(R.id.bt_cancel);
            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
            final int actual = ac;
            final int leak = lq;

            mEtActual.setEnabled(false);
            mEtComments.setEnabled(false);


            final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();



            mBtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            mBtSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mEtPsw.getText().toString().length()>0) {
                        if (mPresenter.validateUser(mUser, mTeam, mGroup, mEtPsw.getText().toString())) {

                            String owner = mPresenter.getSignature(mUser,mTeam,mGroup);
                            //mPresenter.validateFQT(mLine);
                            dialog.dismiss();
                            if (leak != 0 && leak >= (0.10 * actual)) {
                                setLeakReached(turno);
                                showFTQ(turno);
                            }
                            mPresenter.saveLine(mLine, mHours, position, workHour.getActuals(), workHour.getComments(), workHour.getReasonDelay(), owner, null);
                        }
                        else
                            Toast.makeText(DailyActivity.this, "The password or the user are incorrect", Toast.LENGTH_SHORT).show();
                    }
                    else
                        mEtPsw.setError("Please introduce the password");

                }
            });
        }
        else
            showDowntimeDialog(mLine.getDowntime(),this);

    }

    @Override
    public void showEndShiftDialog(final Line line, final int shift, final Context context, final boolean close) {
        if (!mLine.getDowntime().isSet())
        {
            if (mLine.getLastProduct()!=null) {
                if (mTeam == null || mGroup == null || (mPresenter.getGroups(mLine, shift) && mPresenter.getTeam(mLine, shift))
                        || (mLine.getFirst().isClosed() && mLine.getSecond().isClosed() && mLine.getThird().isClosed())) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.dialog_end_shift, null);
                    alertDialogBuilder.setView(view);
                    alertDialogBuilder.setCancelable(false);
                    TextView mTvName = view.findViewById(R.id.tv_name);
                    mTvName.setText(line.getName());
                    TextView mTvDate = view.findViewById(R.id.tv_date);
                    mTvDate.setText(line.getDate());
                    TextView mTvShift = view.findViewById(R.id.tv_shift);
                    final Spinner spProduct = view.findViewById(R.id.sp_product);
                    ArrayAdapter<Product> mAdapter;
                    mAdapter = new ArrayAdapter<>(this, R.layout.spinner_row, mLine.getProducts());
                    spProduct.setAdapter(mAdapter);
                    for (int i=0;i<mLine.getProducts().size();i++)
                        if (lastProduct.getCode().equals(mLine.getProducts().get(i).getCode()))
                            spProduct.setSelection(i);

                    Shift eShitf = new Shift();
                    String title = "";
                    boolean start = false;
                    int tempturn = 0;
                    final Button mBtSave = view.findViewById(R.id.bt_save);
                    Button mBtCancel = view.findViewById(R.id.bt_cancel);

                    if (shift == 1) {

                        if (line.getFirst().getTimeStart() == null || line.getFirst().getTimeStart().isEmpty()) {
                            mBtSave.setText(R.string.start_shift);
                            start = true;
                            tempturn = 1;
                        }
                        if (start)
                            title = "Start of " + getString(R.string.add_1st_shift) + " Transfer";
                        else
                            title = "End of " + getString(R.string.add_1st_shift) + " Transfer";

                        eShitf = line.getFirst();
                        if (eShitf.getPositions() == null) {
                            if (mPline!=null && mPline.getFirst()!=null && mPline.getFirst().getPositions()!=null)
                                eShitf.setPositions(mPline.getFirst().getPositions());
                            else
                                eShitf.setPositions(mLine.getPositions());
                            for (EmployeePosition employeePosition : eShitf.getPositions())
                                employeePosition.setPosition(0);
                        }

                    }
                    if (shift == 2) {
                        if (line.getSecond().getTimeStart() == null || line.getSecond().getTimeStart().isEmpty()) {
                            mBtSave.setText(R.string.start_shift);
                            start = true;
                            tempturn = 2;
                        }
                        if (start)
                            title = "Start of " + getString(R.string.add_2nd_shift) + " Transfer";
                        else
                            title = "End of " + getString(R.string.add_2nd_shift) + " Transfer";
                        eShitf = line.getSecond();

                        if (eShitf.getPositions() == null) {
                            if (mPline!=null && mPline.getSecond()!=null && mPline.getSecond().getPositions()!=null)
                                eShitf.setPositions(mPline.getSecond().getPositions());
                            else
                                eShitf.setPositions(mLine.getPositions());
                            for (EmployeePosition employeePosition : eShitf.getPositions())
                                employeePosition.setPosition(0);
                        }

                    }
                    if (shift == 3) {
                        if (line.getThird().getTimeStart() == null || line.getThird().getTimeStart().isEmpty()) {
                            mBtSave.setText(R.string.start_shift);
                            start = true;
                            tempturn = 3;
                        }
                        if (start)
                            title = "Start of " + getString(R.string.add_3rd_shift) + " Transfer";
                        else
                            title = "End of " + getString(R.string.add_3rd_shift) + " Transfer";
                        eShitf = line.getThird();
                        if (eShitf.getPositions() == null) {
                            if (mPline!=null && mPline.getThird()!=null && mPline.getThird().getPositions()!=null)
                                eShitf.setPositions(mPline.getThird().getPositions());
                            else
                                eShitf.setPositions(mLine.getPositions());

                            for (EmployeePosition employeePosition : eShitf.getPositions())
                                employeePosition.setPosition(0);
                        }
                    }


                    mTvShift.setText(title);

                    TextView mTvActual = view.findViewById(R.id.tv_actual);
                    mTvActual.setText(eShitf.getCumulativeActual());
                    TextView mTvTarget = view.findViewById(R.id.tv_target);
                    mTvTarget.setText(eShitf.getCumulativePlanned());

                    RecyclerView recyclerView = view.findViewById(R.id.rv_position);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    final EndShiftPositionAdapter adapter;
                    if (mOperators == null || mOperators.size() == 0)
                        adapter = new EndShiftPositionAdapter(this, eShitf.getPositions(), eShitf.getEmployees(), eShitf.isClosed());
                    else
                        adapter = new EndShiftPositionAdapter(this, eShitf.getPositions(), mOperators, eShitf.isClosed());

                    recyclerView.setAdapter(adapter);


                    if (!close)
                        mBtSave.setText("Save");

                    if (eShitf.isClosed()) {
                        mBtSave.setVisibility(View.GONE);
                        alertDialogBuilder.setCancelable(true);
                        mBtCancel.setText("Send by Email");
                    }

                    final AlertDialog dialog = alertDialogBuilder.create();
                    dialog.show();

                    mBtCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            if (mBtSave.getVisibility() == View.GONE)
                                showDialogEndShift(shift);
                        }
                    });

                    final boolean finalStart = start;
                    final int finalTempturn = tempturn;
                    mBtSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean error = false;
                            int valid = 0;
                            ArrayList<EmployeePosition> employeePositions = adapter.getPositions();
                            for (EmployeePosition position : employeePositions) {
                                if ((position.getOperator() == null || position.getOperator().isEmpty() || position.getOperator().equals("-Select Operator-")) && !error) {
                                    if (valid == 0)
                                        Toast.makeText(context, "Select an operator for position: " + position.getName(), Toast.LENGTH_LONG).show();
                                    error = true;
                                } else if (position.getOperator() != null && !position.getOperator().isEmpty() && !position.getOperator().equals("-Select Operator-"))
                                    valid++;
                            }


                            if (valid > 0) {
                                Shift finalShift = new Shift();
                                switch (shift) {
                                    case 1:
                                        finalShift = line.getFirst();
                                        break;
                                    case 2:
                                        finalShift = line.getSecond();
                                        break;
                                    case 3:
                                        finalShift = line.getThird();
                                        break;
                                }

                                Product mProduct = (Product) spProduct.getSelectedItem();
                                if (mProduct != null) {
                                    productClick(mProduct);
                                    line.setLastProduct(mProduct);
                                }


                                if (close && !finalStart) {

                                    turno = 0;

                                    ArrayList<WorkHour> hours = finalShift.getHours();
                                    for (WorkHour hour : hours) {
                                        hour.setClosed(true);
                                        if (hour.getActuals() == null || hour.getActuals().isEmpty()) {
                                            hour.setActuals("0");
                                            if (finalShift.getCumulativeActual() != null)
                                                hour.setCumulativeActual(finalShift.getCumulativeActual());
                                            else {
                                                hour.setCumulativeActual("0");
                                                finalShift.setCumulativeActual("0");
                                            }
                                            if (hour.getComments() == null || hour.getComments().isEmpty())
                                                hour.setComments("Shift ended");
                                        }
                                    }
                                    finalShift.setClosed(true);
                                    finalShift.setHours(hours);
                                    finalShift.setRejects(mLine.getRejects());
                                    line.setRejects(new ArrayList<Reject>());
                                }

                                if (finalStart && !mBtSave.getText().toString().equals("Save")) {
                                    finalShift.setTimeStart(Utils.getTimeString());
                                    turno = finalTempturn;
                                    line.setTurno(finalTempturn);
                                }

                                if (close && !finalStart)
                                    finalShift.setTimeEnd(Utils.getTimeString());

                                switch (shift) {
                                    case 1:
                                        line.setFirst(finalShift);
                                        mPline.getFirst().setPositions(line.getFirst().getPositions());
                                        if (finalShift.isClosed())
                                            sendReport1 = true;
                                        break;
                                    case 2:
                                        line.setSecond(finalShift);
                                        mPline.getSecond().setPositions(line.getSecond().getPositions());
                                        if (finalShift.isClosed())
                                            sendReport2 = true;
                                        break;
                                    case 3:
                                        line.setThird(finalShift);
                                        mPline.getThird().setPositions(line.getThird().getPositions());
                                        break;
                                }


                                if (line.getFirst().isClosed() && line.getSecond().isClosed() && line.getThird().isClosed())
                                    sendReport = true;
                                dbPLine.child(mPline.getId()).setValue(mPline);
                                updateLine(line);
                                dialog.dismiss();


                            }
                        }
                    });

                } else if (!(mPresenter.getGroups(mLine, shift)) && mGroup != null) {
                    Toast.makeText(this, "Please select group leaders!", Toast.LENGTH_LONG).show();
                    showUserDialog(mGroup, this, 1, shift);
                } else if (!(mPresenter.getTeam(mLine, shift)) && mTeam != null) {
                    Toast.makeText(this, "Please select team leaders!", Toast.LENGTH_LONG).show();
                    showUserDialog(mTeam, this, 0, shift);
                }
            }
            else
                if (mLine.getProducts()!=null && mLine.getProducts().size()>0)
                {
                showProductListDialog(mLine.getProducts(), this);
                Toast.makeText(this, "Please select a Product!", Toast.LENGTH_LONG).show();
            }
        }
        else
            showDowntimeDialog(mLine.getDowntime(),this);
    }

    @Override
    public void updateLine(Line line) {

        line.setTurno(turno);
        if (progressDialog!=null)
            progressDialog.show();
        dbLine.child(line.getId()).setValue(line).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.hide();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.hide();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.hide();
            }
        });

        DatabaseReference  dbTPLines = FirebaseDatabase.getInstance().getReference(Line.DB_DATE_P_LINE).child(StorageUtils.getPlantId(this)).child(Utils.getYear(line.getDate()))
                .child(Utils.getMonth(line.getDate())).child(Utils.getDay(line.getDate()));
        dbTPLines.child(line.getId()).setValue(line);
    }

    @Override
    public void hideLeakCounter() {
        mBtDowntime.setVisibility(View.GONE);
        mBtScrap.setVisibility(View.GONE);
        mBtHour.setVisibility(View.GONE);
    }

    @Override
    public void setCount(int count) {

        String title = getString(R.string.daily_leak_failure_counter);

        if(count>0)
            title = title + " " + count;

        //mBtCounter.setText(title);
    }

    @Override
    public void showDowntimeDialog(final Downtime downtime, final Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int zone = 0, location = 0, reason = 0;
        if (downtime.getZone() > 0)
            zone = downtime.getZone();
        if (downtime.getLocation() > 0)
            location = downtime.getLocation();
        if (downtime.getReason() > 0)
            reason = downtime.getReason();
        View view = inflater.inflate(R.layout.dialog_downtime, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        final TextView tvTotal = view.findViewById(R.id.tv_total);
        LinearLayout llTotal = view.findViewById(R.id.ll_total);
        Spinner spZone = view.findViewById(R.id.sp_zone);
        final Spinner spLocation = view.findViewById(R.id.sp_location);
        Spinner spReason = view.findViewById(R.id.sp_reason);
        final ArrayList<Reason> mReasons = new ArrayList<>();
        mReasons.add(new Reason("- Select a Reason -"));
        mReasons.addAll(downtime.getReasons());
        ArrayAdapter<Reason> mAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mReasons);
        spReason.setAdapter(mAdapter);
        spReason.setSelection(reason);
        ArrayList<Zone> mZones = new ArrayList<>();
        mZones.add(new Zone("- Select a Zone -", null));
        mZones.addAll(downtime.getZones());
        ArrayAdapter<Zone> mAdapterZone = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mZones);
        spZone.setAdapter(mAdapterZone);
        spZone.setSelection(zone);


        final int finalLocation = location;
        spZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Zone zone = (Zone) adapterView.getItemAtPosition(i);
                if (zone != null && zone.getLocations() != null) {
                    ArrayList<Location> mLocations = new ArrayList<>();
                    mLocations.add(new Location("- Select a Location -"));
                    mLocations.addAll(zone.getLocations());
                    ArrayAdapter<Location> mAdapterLocat = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mLocations);
                    spLocation.setAdapter(mAdapterLocat);
                    spLocation.setSelection(finalLocation);
                    mLine.getDowntime().setZone(i);
                    mLine.getDowntime().setZoneValue(zone.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Location location = (Location) adapterView.getItemAtPosition(i);
                if (location != null) {
                    mLine.getDowntime().setLocation(i);
                    mLine.getDowntime().setLocationValue(location.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Reason reason = (Reason) adapterView.getItemAtPosition(i);
                if (reason != null) {
                    mLine.getDowntime().setReason(i);
                    mLine.getDowntime().setReasonValue(reason.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button mBtStart = view.findViewById(R.id.bt_start);
        Button mBtEnd = view.findViewById(R.id.bt_end);
        final EditText mTvStart = view.findViewById(R.id.et_start);
        final EditText mTvEnd = view.findViewById(R.id.et_end);
        ImageView btClose = view.findViewById(R.id.bt_close);
        if ((downtimeDialog == null || !downtimeDialog.isShowing())&& !DailyActivity.this.isFinishing()){
            downtimeDialog = alertDialogBuilder.create();
            downtimeDialog.show();
        }
        final String time = Utils.getTimeDateString();

        final Timer T=new Timer();

        if (mLine.getDowntime().isSet())
        {
            spZone.setEnabled(false);
            spLocation.setEnabled(false);
            spReason.setEnabled(false);
            mTvEnd.setText(time);
        }
        else
            llTotal.setVisibility(View.GONE);


        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        String time = Utils.getTimeDateString();
                        if (mLine.getDowntime().isSet()) {
                            mTvEnd.setText(time);
                            try {
                                tvTotal.setText(Utils.getTimeDiference(time, mTvStart.getText().toString()));
                            }
                            catch (Exception ignored)
                            {}
                        }
                        else
                            mTvStart.setText(time);


                    }
                });
            }
        }, 1000, 1000);




        if (downtime.getStartTime()!=null && downtime.isSet()) {
            mTvStart.setText(downtime.getStartTime());
            mTvStart.setEnabled(false);


        }
        else
            mTvStart.setText(time);

        mTvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLine.getDowntime().isSet())
                    T.cancel();
            }
        });

        mTvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mLine.getDowntime().isSet())
                    T.cancel();
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T.cancel();
                downtimeDialog.dismiss();
            }
        });

        mBtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                T.cancel();

                if (mLine.getDowntime().getZone()<=0)
                    Toast.makeText(DailyActivity.this,"You must select a Zone",Toast.LENGTH_LONG).show();
                else
                if (mLine.getDowntime().getLocation()<=0)
                    Toast.makeText(DailyActivity.this,"You must select a Location",Toast.LENGTH_LONG).show();
                else
                if (mLine.getDowntime().getReason()<=0)
                    Toast.makeText(DailyActivity.this,"You must select a Reason",Toast.LENGTH_LONG).show();
                else {
                    String time = mTvStart.getText().toString();
                    /*if (time.contains("a"))
                        time = time.substring(0,8)+" AM";
                    if (time.contains("p"))
                        time = time.substring(0,8)+" PM";
                     */

                    mLine.getDowntime().setStartTime(time);
                    mLine.getDowntime().setSet(true);
                    mLine.getDowntime().setShift(turno);
                    mLine.getDowntime().setDowntime(mPresenter.downtime(mLine.getDowntime().getZoneValue(),
                            mLine.getDowntime().getLocationValue(), mLine.getDowntime().getReasonValue()));
                    updateLine(mLine);
                    downtimeDialog.dismiss();
                    String subject,body;
                    if (templates==null || templates.getDowntimeStart()==null) {
                        subject = " Cell " + mLine.getName() + " is in DOWNTIME due to: " +
                                mLine.getDowntime().getZoneValue() +
                                ", " + mLine.getDowntime().getLocationValue() +
                                ", " + mLine.getDowntime().getReasonValue();

                        body = "Hi \n \n" +
                                "This is a notification that Cell " + mLine.getName() + " is in Downtime at " + mLine.getDowntime().getStartTime() +
                                "\nZone:  " + mLine.getDowntime().getZoneValue() +
                                "\nLocation: " + mLine.getDowntime().getLocationValue() +
                                "\nReason: " + mLine.getDowntime().getReasonValue() +
                                "\n\nPlease take immediate action";
                    }
                    else
                    {
                        subject = "Cell "+mLine.getName() + " " + templates.getDowntimeStart().getSubject() +" "+
                                mLine.getDowntime().getZoneValue() +
                                ", " + mLine.getDowntime().getLocationValue() +
                                ", " + mLine.getDowntime().getReasonValue();

                        body = templates.getDowntimeStart().getBody1()+ " " + mLine.getName() + " is in Downtime at " + mLine.getDowntime().getStartTime() +
                                "\nZone:  " + mLine.getDowntime().getZoneValue() +
                                "\nLocation: " + mLine.getDowntime().getLocationValue() +
                                "\nReason: " + mLine.getDowntime().getReasonValue() +  "\n\n" + templates.getDowntimeStart().getBody2();

                    }
                    if (mLine.getDowntimeEmailList()!=null)
                        sendEmail(mPresenter.getEmails(mLine.getDowntimeEmailList().getEmails(),mLine),mPresenter.getCC(mLine.getDowntimeEmailList().getEmails(),mLine),subject,body,0);
                }
            }
        });


        mBtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T.cancel();
                if (mLine.getDowntime().isSet()){

                    String time = mTvEnd.getText().toString();
                    /*if (time.contains("a"))
                        time = time.substring(0,8)+" AM";
                    if (time.contains("p"))
                        time = time.substring(0,8)+" PM";

                     */

                    mLine.getDowntime().setEndTime(time);
                    mLine.getDowntime().setSet(false);
                    mLine.getDowntime().setZone(0);
                    mLine.getDowntime().setLocation(0);
                    mLine.getDowntime().setReason(0);
                    mPresenter.setDowntime(mLine,mLine.getDowntime());
                    if (downtimeDialog!=null)
                        downtimeDialog.dismiss();
                    String subject,body;
                    if (templates== null || templates.getDowntimeEnd()==null) {
                        subject = "Cell  " + mLine.getName() + "  is back to production at " + mTvEnd.getText().toString();
                        body = "Hi \n \n" +
                                "This is a notification that Cell " + mLine.getName() + " is back to production at " +  mTvEnd.getText().toString() +
                                "\nThank you";
                    }
                    else
                    {
                        subject = "Cell "+mLine.getName() + " " + templates.getDowntimeEnd().getSubject() +" at "+  mTvEnd.getText().toString();

                        body = templates.getDowntimeEnd().getBody1()+ " " + mLine.getName() + " is back to production at " +  mTvEnd.getText().toString() +
                                "\n\n" + templates.getDowntimeEnd().getBody2();

                    }
                    if (mLine.getDowntimeEmailList()!=null)
                        sendEmail(mPresenter.getEmails(mLine.getDowntimeEmailList().getEmails(),mLine),mPresenter.getCC(mLine.getDowntimeEmailList().getEmails(),mLine),subject,body,0);

                }
                else
                    Toast.makeText(DailyActivity.this,"You must start the downtime first",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void showRejectDialog(ArrayList<Reason> reasons, final Context context) {
        if (!mLine.getFirst().isClosed() || !mLine.getSecond().isClosed() || !mLine.getThird().isClosed()) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_scrapreason, null);
            alertDialogBuilder.setView(view);
            final Spinner spinner = view.findViewById(R.id.sp_reason);
            ArrayList<Reason> mReasons = new ArrayList<>();
            mReasons.add(new Reason("- Select a Reason -"));
            mReasons.addAll(reasons);
            ArrayAdapter<Reason> mAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mReasons);
            spinner.setAdapter(mAdapter);
            Button mBtReport = view.findViewById(R.id.bt_report);
            final EditText editText = view.findViewById(R.id.et_actions);
            if (mLine.getRejects() != null && mLine.getRejects().size() >= 2)
                editText.setVisibility(View.VISIBLE);

            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();


            mBtReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Reason reason = (Reason) spinner.getSelectedItem();


                    if (reason != null && reason.getName().equals("- Select a Reason -"))
                        Toast.makeText(context, "You must select a reason to make the Report!", Toast.LENGTH_LONG).show();
                    else if (mLine.getRejects() != null && mLine.getRejects().size() >= 2 && editText.getText().toString().isEmpty()) {
                        editText.setError("You must write some actions!");
                        editText.requestFocus();
                        editText.setVisibility(View.VISIBLE);
                    } else if (reason != null) {

                        if (mLine.getRejects() == null) {
                            mLine.setRejects(new ArrayList<Reject>());
                        }
                        if (mLine.getRejects().size() >= 2)
                            mLine.getRejects().add(new Reject(reason.getName(), Utils.getTimeString(),editText.getText().toString(),turno));
                        else
                            mLine.getRejects().add(new Reject(reason.getName(), Utils.getTimeString(),turno));

                        dialog.dismiss();

                        if (turno==1)
                            mLine.getFirst().setRejects(mLine.getRejects());
                        else
                        if (turno==2)
                            mLine.getSecond().setRejects(mLine.getRejects());
                        else
                        if (turno==3)
                            mLine.getThird().setRejects(mLine.getRejects());

                        updateLine(mLine);

                        String subject = "";
                        String body = "";

                        if (mLine.getRejects().size() == 1) {
                            if (templates==null || templates.getReject1()==null) {
                                subject = "Cell " + mLine.getName() + " Produced a First Rejected Piece due to: " + reason.getName();
                                body = "Hi \n\n" +
                                        "This is a notification that Cell " + mLine.getName() + " Produced a First Reject Piece at " + Utils.getTimeString()
                                        + " due to: " + reason.getName() +
                                        "\n\nFirst Occurrence";
                            }
                            else
                            {
                                subject = "Cell " + mLine.getName() + " "+ templates.getReject1().getSubject()+ " "+ reason.getName();
                                body = templates.getReject1().getBody1() + " "+ mLine.getName() + " Produced a First Reject Piece at " + Utils.getTimeString()
                                        + " due to: " + reason.getName() +
                                        "\n\n" + templates.getReject1().getBody2();
                            }

                            sendEmail(mPresenter.getEmails(mLine.getScrap1EmailList().getEmails(), mLine), mPresenter.getCC(mLine.getScrap1EmailList().getEmails(), mLine), subject, body,0);
                        } else if (mLine.getRejects().size() == 2) {
                            if (templates==null || templates.getReject2()==null) {
                                subject = "Cell " + mLine.getName() + " Produced a Second Rejected Piece due to: " + reason.getName();
                                body = "Hi \n\n" +
                                        "This is a notification that Cell " + mLine.getName() + " Produced a Second Rejected Piece at " + Utils.getTimeString()
                                        + " due to: " + reason.getName() +
                                        "\n\nSecond Occurrence...   Next notification will be send to Group Leaders";
                            }
                            else
                            {
                                subject = "Cell " + mLine.getName() + " "+ templates.getReject2().getSubject()+ " "+ reason.getName();
                                body = templates.getReject2().getBody1() + " "+ mLine.getName() + " Produced a Second Rejected Piece at " + Utils.getTimeString()
                                        + " due to: " + reason.getName() +
                                        "\n\n" + templates.getReject2().getBody2();
                            }

                            sendEmail(mPresenter.getEmails(mLine.getScrap2EmailList().getEmails(), mLine), mPresenter.getCC(mLine.getScrap2EmailList().getEmails(), mLine), subject, body,0);
                        } else {

                            if (templates==null || templates.getReject3()==null) {
                                subject = "Cell " + mLine.getName() + " Produced Rejected Piece #" + String.valueOf(mLine.getRejects().size())
                                        + " due to: " + reason.getName();
                                body = "Hi \n\n" +
                                        "This is a notification that Cell " + mLine.getName() + " Produced Rejected Piece #" + String.valueOf(mLine.getRejects().size())
                                        + " at " + Utils.getTimeString()
                                        + " due to: " + reason.getName() +
                                        "\n\nActions: " + editText.getText().toString() +
                                        "\n\nRejected piece # " + String.valueOf(mLine.getRejects().size()) + " Next notification will be send to the Management Team";
                            }
                            else
                            {
                                subject = "Cell " + mLine.getName() + " "+ templates.getReject3().getSubject()+ " #"+ String.valueOf(mLine.getRejects().size())
                                        + " due to: " + reason.getName();
                                body = templates.getReject3().getBody1() + " "+ mLine.getName() + " Produced Rejected Piece #" + String.valueOf(mLine.getRejects().size())
                                        + " at " + Utils.getTimeString()
                                        + " due to: " + reason.getName() +
                                        "\n\nActions: " + editText.getText().toString() +
                                        "\n\nRejected piece # " + String.valueOf(mLine.getRejects().size()) +
                                        "\n\n" + templates.getReject3().getBody2();
                            }
                            sendEmail(mPresenter.getEmails(mLine.getScrap3EmailList().getEmails(), mLine), mPresenter.getCC(mLine.getScrap3EmailList().getEmails(), mLine), subject, body,0);
                        }


                    }

                }
            });
        }

    }

    @Override
    public void showFTQ(final int shift) {

        Context context = DailyActivity.this;

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_leak, null);
            alertDialogBuilder.setView(view);
            Button btSave = view.findViewById(R.id.bt_save);
            Button btCancel = view.findViewById(R.id.bt_cancel);
            TextView mTvName = view.findViewById(R.id.tv_name);
            mTvName.setText(mLine.getName());
            TextView mTvDate = view.findViewById(R.id.tv_date);
            TextView mTvActual = view.findViewById(R.id.tv_actual);
            TextView mTvLeak = view.findViewById(R.id.tv_leak);
            String date = mLine.getDate() + " " + Utils.getTimeString();
            mTvDate.setText(date);
            TextView mTvShift = view.findViewById(R.id.tv_shift);
            final EditText eTActions = view.findViewById(R.id.et_actions);
            String stilte = "";
            Shift tshift;
            if (shift == 1) {
                stilte = getString(R.string.add_1st_shift);
                tshift = mLine.getFirst();
            } else if (shift == 2) {
                stilte = getString(R.string.add_2nd_shift);
                tshift = mLine.getSecond();
            } else {
                stilte = getString(R.string.add_3rd_shift);
                tshift = mLine.getThird();
            }

            mTvShift.setText(stilte);
            mTvActual.setText(tshift.getCumulativeActual());
            mTvLeak.setText(tshift.getCumulativeFTQ());

            if (ftqDialog==null )
                ftqDialog = alertDialogBuilder.create();

            if (!ftqDialog.isShowing())
                ftqDialog.show();


            if (!tshift.isLeakReached()) {
                ftqDialog.dismiss();
                isShowingLeak = false;
            }

            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!eTActions.getText().toString().isEmpty()) {

                        switch (shift) {
                            case 1:
                                mLine.getFirst().setLeakReached(false);
                                break;
                            case 2:
                                mLine.getSecond().setLeakReached(false);
                                break;
                            case 3:
                                mLine.getThird().setLeakReached(false);
                                break;
                        }
                        updateLine(mLine);
                        ftqDialog.dismiss();
                        String subject,body;
                        if (templates== null || templates.getFtqs()==null) {
                            subject = "Cell " + mLine.getName() + "  FTQ over 10% Escalation and Actions";
                            body = "Hi\n" +
                                    " \n" +
                                    "Please see below the Actions Taken to solve FTQ Leak issues faced on current shift on Cell " +
                                    mLine.getName() + "\n" +
                                    " \n" +
                                    eTActions.getText().toString();
                        }
                        else
                        {
                            subject = "Cell " + mLine.getName() +" "+ templates.getFtqs().getSubject();
                            body = templates.getFtqs().getBody1()+ mLine.getName() + "\n" +
                                    " \n" +
                                    eTActions.getText().toString();
                        }

                        sendEmail(mPresenter.getEmails(mLine.getLeakEmailList().getEmails(),mLine), mPresenter.getCC(mLine.getLeakEmailList().getEmails(),mLine), subject, body,0);
                    } else {
                        eTActions.setError("You must introduce the actions taken!");
                        eTActions.requestFocus();
                    }


                }
            });

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ftqDialog.dismiss();

                }
            });

    }

    @Override
    public void sendEmail(String[] addresses, String[] ccs, String subject, String body, int shift) {

        String filename=mLine.getName()+" "+mLine.getDate().replace("/","-")+".csv";
        String fileshift1=mLine.getName()+" "+mLine.getDate().replace("/","-")+" 1st Shift.csv";
        String fileshift2=mLine.getName()+" "+mLine.getDate().replace("/","-")+" 2nd Shift.csv";
        String fileshift3=mLine.getName()+" "+mLine.getDate().replace("/","-")+" 3rd Shift.csv";
        File filelocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Tenneco Report");
        File cvslocation = new File(filelocation,filename);
        File cvslocations1 = new File(filelocation,fileshift1);
        File cvslocations2 = new File(filelocation,fileshift2);
        File cvslocations3 = new File(filelocation,fileshift3);
        Uri path = Uri.fromFile(cvslocation);
        Uri paths1 = Uri.fromFile(cvslocations1);
        Uri paths2 = Uri.fromFile(cvslocations2);
        Uri paths3 = Uri.fromFile(cvslocations3);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_CC, ccs);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        switch (shift)
        {
            default:
                if (cvslocation.exists())
                    intent.putExtra(Intent.EXTRA_STREAM, path);
                break;
            case  1:
                if (cvslocations1.exists())
                    intent.putExtra(Intent.EXTRA_STREAM, paths1);
                break;
            case 2:
                if (cvslocations2.exists())
                    intent.putExtra(Intent.EXTRA_STREAM, paths2);
                break;
            case 3:
                if (cvslocations3.exists())
                    intent.putExtra(Intent.EXTRA_STREAM, paths3);
                break;

        }






        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void showReject() {
        mLlScrap.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideReject() {
        mLlScrap.setVisibility(View.GONE);
    }

    @Override
    public void initAdapter() {
        mHours = new ArrayList<>();
        mRvLine.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DailyAdapter(mHours,this);
        mRvLine.setAdapter(mAdapter);
        mRvScrap.setLayoutManager(new LinearLayoutManager(this));
        mAdapterScr = new RejectEventAdapter(null);
        mRvScrap.setAdapter(mAdapterScr);
    }


    @Override
    public void showUserDialog(ArrayList<User> users, Context context, final int position, final int shiftt) {
        if (users!=null && users.size()>0 && (!mLine.getFirst().isClosed()|| !mLine.getSecond().isClosed() || !mLine.getThird().isClosed())) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            final UserSelectorAdapter adapter = new UserSelectorAdapter(users,context);
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            alertDialogBuilder.setView(recyclerView);
            String title = "";
            if (position==GROUP)
                title = "Group Leaders Shift: " +shiftt;
            else
                title = "Team Leaders Shift: " +shiftt;
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    String value="";
                    switch (position) {
                        case GROUP:
                            for (User user : adapter.getUsers())
                                if (user.isSelected())
                                    stringBuilder.append(user.getName().trim()).append(", ");

                            if (!stringBuilder.toString().isEmpty())
                                value=stringBuilder.toString().substring(0, stringBuilder.toString().length() - 2);

                            if (shiftt==1)
                            {
                                mLine.getFirst().setGroupLeaders(value);
                                saveGl(value,1);
                            }
                            else
                            if (shiftt==2)
                            {
                                mLine.getSecond().setGroupLeaders(value);
                                saveGl(value,2);
                            }
                            else if (shiftt==3)
                            {
                                mLine.getThird().setGroupLeaders(value);
                                saveGl(value,3);
                            }


                            break;
                        default:
                            for (User user : adapter.getUsers())
                                if (user.isSelected())
                                    stringBuilder.append(user.getName().trim()).append(", ");
                            if (!stringBuilder.toString().isEmpty())
                                value=stringBuilder.toString().substring(0, stringBuilder.toString().length() - 2);
                            if (shiftt==1)
                            {
                                mLine.getFirst().setTeamLeaders(value);
                                saveTl(value,1);
                            }
                            else
                            if (shiftt==2)
                            {
                                mLine.getSecond().setTeamLeaders(value);
                                saveTl(value,2);
                            }
                            else if (shiftt==3)
                            {
                                mLine.getThird().setTeamLeaders(value);
                                saveTl(value,3);
                            }

                            break;
                    }
                    updateLine(mLine);
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
    public void showTeam() {
        mLlTls.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTeam() {
        mLlTls.setVisibility(View.GONE);
    }

    @Override
    public void setDtS1(String time) {
        mTvDtS1.setText(time);
    }

    @Override
    public void setDtS2(String time) {
        mTvDtS2.setText(time);
    }

    @Override
    public void setDtS3(String time) {
        mTvDtS3.setText(time);
    }

    @Override
    public void setTeam1(String title) {
        mTvTls1.setText(title);
    }
    @Override
    public void setTeam2(String title) {
        mTvTls2.setText(title);
    }
    @Override
    public void setTeam3(String title) {
        mTvTls3.setText(title);
    }

    @Override
    public void showGroup() {
        mLlGls.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGroup() {
        mLlGls.setVisibility(View.GONE);
    }

    @Override
    public void setGroup1(String title) {
        mTvGls1.setText(title);
    }
    @Override
    public void setGroup2(String title) {
        mTvGls2.setText(title);
    }
    @Override
    public void setGroup3(String title) {
        mTvGls3.setText(title);
    }

    @Override
    public void showOperatorsl() {
        mLlOperators.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCellEmailDialog() {
        sendReport = false;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle("Send Cell Report").
                setMessage("Would you like to send the report of the cell: "+ mLine.getName()+ "?");
        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendCellEmail();
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.create().show();
    }

    @Override
    public void sendCellEmail() {

        try {
            mPresenter.createCVS(this,mLine);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendEmail(mPresenter.getEmails(mLine.getLineList().getEmails(),null),mPresenter.getCC(mLine.getLineList().getEmails(),null),
                "Cell Report: "+mLine.getName()+ " "+mLine.getDate(),mPresenter.lineInformation(mLine),0);


    }

    @Override
    public void longClicks() {
        mBtS1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialogClose(1);
                return false;
            }
        });

        mBtS2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialogClose(2);
                return false;
            }
        });

        mBtS3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialogClose(3);
                return false;
            }
        });
    }

    @Override
    public void showDialogClose(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        String title = "";
        String body = "";
        switch (position) {
            default:
                title = "End 1st Shift";
                body = "Would you like to force the closure of the first shift?";
                break;
            case 2:
                title = "End 2nd Shift";
                body = "Would you like to force the closure of the second shift?";
                break;
            case 3:
                title = "End 3rd Shift";
                body = "Would you like to force the closure of the third shift?";
                break;
        }
        alertDialog.setTitle(title).setMessage(body);
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (position) {
                    default:
                        mLine.getFirst().setClosed(true);
                        for (WorkHour w : mLine.getFirst().getHours())
                            w.setClosed(true);
                        break;
                    case 2:
                        mLine.getSecond().setClosed(true);
                        for (WorkHour w : mLine.getSecond().getHours())
                            w.setClosed(true);
                        break;
                    case 3:
                        mLine.getThird().setClosed(true);
                        for (WorkHour w : mLine.getThird().getHours())
                            w.setClosed(true);
                        break;
                }
                dialogInterface.dismiss();
                updateLine(mLine);
            }
        });

        alertDialog.create().show();
    }

    @Override
    public void showDialogEndShift(final int position) {
        sendReport = false;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String title = "";
        String msg = "";
        if (position==1) {
            title = "End 1st Shift Report";
            msg = " 1st Shift ";
        }
        else
        if (position==2) {
            title = "End 2nd Shift Report";
            msg = " 2nd Shift ";
        }
        else {
            title = "End 3rd Shift Report";
            msg = " 3rd Shift ";
        }

        alert.setCancelable(false);
        alert.setTitle(title).
                setMessage("Would you like to send the report of the cell: "+ mLine.getName()+msg + "?");
        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendShiftEmail(position);
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.create().show();
    }

    @Override
    public void sendShiftEmail(int position) {
        String title ="";
        if (position==1) {
            title = "End 1st Shift Report";
            mPresenter.createCSVShift(this,mLine,1,mLine.getFirst());
        }
        else
        if (position==2) {
            title = "End 2nd Shift Report";
            mPresenter.createCSVShift(this,mLine,2,mLine.getSecond());
        }
        else {
            title = "End 3rd Shift Report";
            mPresenter.createCSVShift(this,mLine,3,mLine.getThird());
        }


        sendEmail(mPresenter.getEmails(mLine.getLineList().getEmails(),null),mPresenter.getCC(mLine.getLineList().getEmails(),null),
                title + " Cell: "+mLine.getName()+ " "+mLine.getDate(),mPresenter.shiftInformation(mLine,position),position);
    }

    @Override
    public void getPLine() {
        Query postsQuery;
        postsQuery = dbPLine.child(mLine.getParentId());
        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPline = dataSnapshot.getValue(Line.class);
                if (mPline!=null)
                {
                    if (mPline.getFirst().getGroupLeaders()!=null && (mLine.getFirst().getGroupLeaders()==null || mLine.getFirst().getGroupLeaders().isEmpty()))
                        mLine.getFirst().setGroupLeaders(mPline.getFirst().getGroupLeaders());
                    if (mPline.getSecond().getGroupLeaders()!=null && (mLine.getSecond().getGroupLeaders()==null || mLine.getSecond().getGroupLeaders().isEmpty()))
                        mLine.getSecond().setGroupLeaders(mPline.getSecond().getGroupLeaders());
                    if (mPline.getThird().getGroupLeaders()!=null && (mLine.getThird().getGroupLeaders()==null || mLine.getThird().getGroupLeaders().isEmpty()))
                        mLine.getThird().setGroupLeaders(mPline.getThird().getGroupLeaders());

                    if (mPline.getFirst().getTeamLeaders()!=null && (mLine.getFirst().getTeamLeaders()==null || mLine.getFirst().getTeamLeaders().isEmpty()))
                        mLine.getFirst().setTeamLeaders(mPline.getFirst().getTeamLeaders());
                    if (mPline.getSecond().getTeamLeaders()!=null && (mLine.getSecond().getTeamLeaders()==null || mLine.getSecond().getTeamLeaders().isEmpty()))
                        mLine.getSecond().setTeamLeaders(mPline.getSecond().getTeamLeaders());
                    if (mPline.getThird().getTeamLeaders()!=null && (mLine.getThird().getTeamLeaders()==null || mLine.getThird().getTeamLeaders().isEmpty()))
                        mLine.getThird().setTeamLeaders(mPline.getThird().getTeamLeaders());

                    lastProduct = mPline.getLastProduct();

                    setLine();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void saveTl(String tl, int shift) {
        if (mPline!=null)
        {
            switch (shift){
                case 1:
                    mPline.getFirst().setTeamLeaders(tl);
                    break;
                case 2:
                    mPline.getSecond().setTeamLeaders(tl);
                    break;
                case 3:
                    mPline.getThird().setTeamLeaders(tl);
                    break;
            }

            dbPLine.child(mPline.getId()).setValue(mPline);
        }
    }

    @Override
    public void saveGl(String gl, int shift) {
        if (mPline!=null)
        {
            switch (shift){
                case 1:
                    mPline.getFirst().setGroupLeaders(gl);
                    break;
                case 2:
                    mPline.getSecond().setGroupLeaders(gl);
                    break;
                case 3:
                    mPline.getThird().setGroupLeaders(gl);
                    break;
            }

            dbPLine.child(mPline.getId()).setValue(mPline);
        }
    }

    @Override
    public void saveProduct(Product lastProduct) {
        if (mPline!=null)
        {
            mPline.setLastProduct(lastProduct);
            dbPLine.child(mPline.getId()).setValue(mPline);
        }
    }

    @Override
    public void sendSms(String number, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null, null);

    }

    @Override
    public void showSendMsgButton() {

    }

    @Override
    public void showSendMsgDialog(Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_sms, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        Spinner spList = view.findViewById(R.id.sp_numbers);
        final ArrayList<SmsList> mList = new ArrayList<>();
        mList.add(new SmsList("- Select a List -","null"));
        mList.addAll(mSmsLists);
        ArrayAdapter<SmsList> mAdapter = new ArrayAdapter<>(context,R.layout.spinner_row,mList);
        spList.setAdapter(mAdapter);
        Button mBtSend = view.findViewById(R.id.bt_send);
        final EditText mEtMsg = view.findViewById(R.id.et_msg);
        ImageView btClose = view.findViewById(R.id.bt_close);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        String time = Utils.getTimeString();
        final SmsList mSmsList = new SmsList();


        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        spList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SmsList smsList = (SmsList) adapterView.getItemAtPosition(i);
                if (!smsList.getName().equals("- Select a List -"))
                    mSmsList.setSms_numbers(smsList.getSms_numbers());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mBtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEtMsg.getText().toString().isEmpty())
                {
                    mEtMsg.setError("Introduce a message!");
                    mEtMsg.requestFocus();
                }
                else
                if (mSmsList.getSms_numbers()!=null ) {
                    for (Sms sms : mSmsList.getNumbers())
                        sendSms(sms.getNumber(), mEtMsg.getText().toString());
                    dialog.dismiss();
                }
                else
                    Toast.makeText(DailyActivity.this, "You must Select a List", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showProductListDialog(ArrayList<Product> products, Context context) {
        if (context!=null && products!=null && products.size()>=0  ) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            final ProductAdapter adapter = new ProductAdapter(products, this);
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            alertDialogBuilder.setView(recyclerView);
            String title = "Select a Product";
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            if (productDialog==null)
                productDialog = alertDialogBuilder.create();
            if (!productDialog.isShowing()&& !DailyActivity.this.isFinishing())
                productDialog.show();
        }



    }

    @Override
    public void setLeakReached(int shift) {
        if (shift==2)
            mLine.getSecond().setLeakReached(true);
        else
        if (shift==3)
            mLine.getThird().setLeakReached(true);
        else
            mLine.getFirst().setLeakReached(true);

    }

    @Override
    public void productClick(Product product) {
        if (productDialog!=null && productDialog.isShowing())
            productDialog.dismiss();

        lastProduct = product;
        mLine.setLastProduct(product);
        saveProduct(lastProduct);
        mPresenter.setProduct(mLine,lastProduct);
    }



    @Override
    public void onTargetClick(int position) {
        showActualsDialog(mHours.get(position),mLine,position,this);
    }

    @Override
    public void onTargetEditClick(int position) {
        showTargetDialog(mHours.get(position),mLine,position,this);
    }

    @Override
    public void onOwnerClick(int position) {
        showOwnerDialog(mHours.get(position),mLine,position,this);
    }

    @Override
    public void showFinishFirst() {
        showEndShiftDialog(mLine,1,this,true);
    }

    @Override
    public void showFinishSecond() {
        showEndShiftDialog(mLine,2,this,true);
    }

    @Override
    public void showFinishThird() {
        showEndShiftDialog(mLine,3,this,true);
    }


    private void permissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(DailyActivity.this,
                    Arrays.toString(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,
                            Manifest.permission.SEND_SMS}))
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(DailyActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.SEND_SMS},
                        101);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            for (int in = 0; in < grantResults.length - 1; in++)
                if (!(grantResults.length > 0
                        && grantResults[in] == PackageManager.PERMISSION_GRANTED)) {

                }
        }
    }

    private void setGestureDetector(){
        gestureDetector = new GestureDetector(this, new GestureListener());

        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener()
        {

            @Override
            public boolean onScale(ScaleGestureDetector detector)
            {
                float scale = 1 - detector.getScaleFactor();

                float prevScale = mScale;
                mScale += scale;

                if (mScale < 0.1f) // Minimum scale condition:
                    mScale = 0.1f;

                if (mScale > 1) // Maximum scale condition:
                    mScale = 1;
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, detector.getFocusX(), detector.getFocusY());
                scaleAnimation.setDuration(0);
                scaleAnimation.setFillAfter(true);
                ScrollView layout = findViewById(R.id.sv);
                layout.startAnimation(scaleAnimation);
                return true;
            }
        });
    }

    // step 3: override dispatchTouchEvent()
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }


    @Override
    public void onDelete(Product product) {

    }

//step 4: add private class GestureListener

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // double tap fired.
            return true;
        }
    }
}
