package com.tenneco.tennecoapp.Lines;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditLineActivity extends AppCompatActivity implements AddLineContract.View {
    private DatabaseReference dbLines;
    private AddLineContract.Presenter mPresenter;
    @BindView(R.id.et_name) EditText mEtName;
    @BindView(R.id.ll_shift1) LinearLayout mLlS1;
    @BindView(R.id.ll_shift2) LinearLayout mLlS2;
    @BindView(R.id.ll_shift3) LinearLayout mLlS3;
    @BindView(R.id.et_s1_shour1) EditText mEtS1s1;
    @BindView(R.id.et_s1_ehour1) EditText mEtS1e1;
    @BindView(R.id.et_s1_t1) EditText mEtS1t1;
    @BindView(R.id.et_s1_cp1) EditText mEtS1cp1;
    @BindView(R.id.et_s1_shour2) EditText mEtS1s2;
    @BindView(R.id.et_s1_ehour2) EditText mEtS1e2;
    @BindView(R.id.et_s1_t2) EditText mEtS1t2;
    @BindView(R.id.et_s1_cp2) EditText mEtS1cp2;
    @BindView(R.id.et_s1_shour3) EditText mEtS1s3;
    @BindView(R.id.et_s1_ehour3) EditText mEtS1e3;
    @BindView(R.id.et_s1_t3) EditText mEtS1t3;
    @BindView(R.id.et_s1_cp3) EditText mEtS1cp3;
    @BindView(R.id.et_s1_shour4) EditText mEtS1s4;
    @BindView(R.id.et_s1_ehour4) EditText mEtS1e4;
    @BindView(R.id.et_s1_t4) EditText mEtS1t4;
    @BindView(R.id.et_s1_cp4) EditText mEtS1cp4;
    @BindView(R.id.et_s1_shour5) EditText mEtS1s5;
    @BindView(R.id.et_s1_ehour5) EditText mEtS1e5;
    @BindView(R.id.et_s1_t5) EditText mEtS1t5;
    @BindView(R.id.et_s1_cp5) EditText mEtS1cp5;
    @BindView(R.id.et_s1_shour6) EditText mEtS1s6;
    @BindView(R.id.et_s1_ehour6) EditText mEtS1e6;
    @BindView(R.id.et_s1_t6) EditText mEtS1t6;
    @BindView(R.id.et_s1_cp6) EditText mEtS1cp6;
    @BindView(R.id.et_s1_shour7) EditText mEtS1s7;
    @BindView(R.id.et_s1_ehour7) EditText mEtS1e7;
    @BindView(R.id.et_s1_t7) EditText mEtS1t7;
    @BindView(R.id.et_s1_cp7) EditText mEtS1cp7;
    @BindView(R.id.et_s1_shour8) EditText mEtS1s8;
    @BindView(R.id.et_s1_ehour8) EditText mEtS1e8;
    @BindView(R.id.et_s1_t8) EditText mEtS1t8;
    @BindView(R.id.et_s1_cp8) EditText mEtS1cp8;
    @BindView(R.id.et_s2_shour1) EditText mEtS2s1;
    @BindView(R.id.et_s2_ehour1) EditText mEtS2e1;
    @BindView(R.id.et_s2_t1) EditText mEtS2t1;
    @BindView(R.id.et_s2_cp1) EditText mEtS2cp1;
    @BindView(R.id.et_s2_shour2) EditText mEtS2s2;
    @BindView(R.id.et_s2_ehour2) EditText mEtS2e2;
    @BindView(R.id.et_s2_t2) EditText mEtS2t2;
    @BindView(R.id.et_s2_cp2) EditText mEtS2cp2;
    @BindView(R.id.et_s2_shour3) EditText mEtS2s3;
    @BindView(R.id.et_s2_ehour3) EditText mEtS2e3;
    @BindView(R.id.et_s2_t3) EditText mEtS2t3;
    @BindView(R.id.et_s2_cp3) EditText mEtS2cp3;
    @BindView(R.id.et_s2_shour4) EditText mEtS2s4;
    @BindView(R.id.et_s2_ehour4) EditText mEtS2e4;
    @BindView(R.id.et_s2_t4) EditText mEtS2t4;
    @BindView(R.id.et_s2_cp4) EditText mEtS2cp4;
    @BindView(R.id.et_s2_shour5) EditText mEtS2s5;
    @BindView(R.id.et_s2_ehour5) EditText mEtS2e5;
    @BindView(R.id.et_s2_t5) EditText mEtS2t5;
    @BindView(R.id.et_s2_cp5) EditText mEtS2cp5;
    @BindView(R.id.et_s2_shour6) EditText mEtS2s6;
    @BindView(R.id.et_s2_ehour6) EditText mEtS2e6;
    @BindView(R.id.et_s2_t6) EditText mEtS2t6;
    @BindView(R.id.et_s2_cp6) EditText mEtS2cp6;
    @BindView(R.id.et_s2_shour7) EditText mEtS2s7;
    @BindView(R.id.et_s2_ehour7) EditText mEtS2e7;
    @BindView(R.id.et_s2_t7) EditText mEtS2t7;
    @BindView(R.id.et_s2_cp7) EditText mEtS2cp7;
    @BindView(R.id.et_s2_shour8) EditText mEtS2s8;
    @BindView(R.id.et_s2_ehour8) EditText mEtS2e8;
    @BindView(R.id.et_s2_t8) EditText mEtS2t8;
    @BindView(R.id.et_s2_cp8) EditText mEtS2cp8;
    @BindView(R.id.et_s3_shour1) EditText mEtS3s1;
    @BindView(R.id.et_s3_ehour1) EditText mEtS3e1;
    @BindView(R.id.et_s3_t1) EditText mEtS3t1;
    @BindView(R.id.et_s3_cp1) EditText mEtS3cp1;
    @BindView(R.id.et_s3_shour2) EditText mEtS3s2;
    @BindView(R.id.et_s3_ehour2) EditText mEtS3e2;
    @BindView(R.id.et_s3_t2) EditText mEtS3t2;
    @BindView(R.id.et_s3_cp2) EditText mEtS3cp2;
    @BindView(R.id.et_s3_shour3) EditText mEtS3s3;
    @BindView(R.id.et_s3_ehour3) EditText mEtS3e3;
    @BindView(R.id.et_s3_t3) EditText mEtS3t3;
    @BindView(R.id.et_s3_cp3) EditText mEtS3cp3;
    @BindView(R.id.et_s3_shour4) EditText mEtS3s4;
    @BindView(R.id.et_s3_ehour4) EditText mEtS3e4;
    @BindView(R.id.et_s3_t4) EditText mEtS3t4;
    @BindView(R.id.et_s3_cp4) EditText mEtS3cp4;
    @BindView(R.id.et_s3_shour5) EditText mEtS3s5;
    @BindView(R.id.et_s3_ehour5) EditText mEtS3e5;
    @BindView(R.id.et_s3_t5) EditText mEtS3t5;
    @BindView(R.id.et_s3_cp5) EditText mEtS3cp5;
    @BindView(R.id.et_s3_shour6) EditText mEtS3s6;
    @BindView(R.id.et_s3_ehour6) EditText mEtS3e6;
    @BindView(R.id.et_s3_t6) EditText mEtS3t6;
    @BindView(R.id.et_s3_cp6) EditText mEtS3cp6;
    @BindView(R.id.et_s3_shour7) EditText mEtS3s7;
    @BindView(R.id.et_s3_ehour7) EditText mEtS3e7;
    @BindView(R.id.et_s3_t7) EditText mEtS3t7;
    @BindView(R.id.et_s3_cp7) EditText mEtS3cp7;
    @BindView(R.id.et_s3_shour8) EditText mEtS3s8;
    @BindView(R.id.et_s3_ehour8) EditText mEtS3e8;
    @BindView(R.id.et_s3_t8) EditText mEtS3t8;
    @BindView(R.id.et_s3_cp8) EditText mEtS3cp8;


    @OnClick(R.id.tv_shift1) void show1st(){
        mPresenter.onShift1Click(mLlS1.getVisibility(),View.VISIBLE);
    }
    @OnClick(R.id.tv_shift2) void show2nd(){
        mPresenter.onShift2Click(mLlS2.getVisibility(),View.VISIBLE);
    }
    @OnClick(R.id.tv_shift3) void show3rd(){
        mPresenter.onShift3Click(mLlS3.getVisibility(),View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_line);
        ButterKnife.bind(this);
        dbLines = FirebaseDatabase.getInstance().getReference(Line.DB_LINE);
        if (mPresenter == null)
            mPresenter = new AddLinePresenter(this);
        else
            mPresenter.bindView(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save)
        {
            mPresenter.saveChanges(mEtName.getText().toString().trim(),dbLines.push().getKey(),
                    mPresenter.getshift(mEtS1s1.getText().toString().trim(), mEtS1e1.getText().toString().trim(),
                            mEtS1t1.getText().toString().trim(),mEtS1cp1.getText().toString().trim(),
                            mEtS1s2.getText().toString().trim(), mEtS1e2.getText().toString().trim(),
                            mEtS1t2.getText().toString().trim(),mEtS1cp2.getText().toString().trim(),
                            mEtS1s3.getText().toString().trim(), mEtS1e3.getText().toString().trim(),
                            mEtS1t3.getText().toString().trim(),mEtS1cp3.getText().toString().trim(),
                            mEtS1s4.getText().toString().trim(), mEtS1e4.getText().toString().trim(),
                            mEtS1t4.getText().toString().trim(),mEtS1cp4.getText().toString().trim(),
                            mEtS1s5.getText().toString().trim(), mEtS1e5.getText().toString().trim(),
                            mEtS1t5.getText().toString().trim(),mEtS1cp5.getText().toString().trim(),
                            mEtS1s6.getText().toString().trim(), mEtS1e6.getText().toString().trim(),
                            mEtS1t6.getText().toString().trim(),mEtS1cp6.getText().toString().trim(),
                            mEtS1s7.getText().toString().trim(), mEtS1e7.getText().toString().trim(),
                            mEtS1t7.getText().toString().trim(),mEtS1cp7.getText().toString().trim(),
                            mEtS1s8.getText().toString().trim(), mEtS1e8.getText().toString().trim(),
                            mEtS1t8.getText().toString().trim(),mEtS1cp8.getText().toString().trim()),
                    mPresenter.getshift(mEtS2s1.getText().toString().trim(), mEtS2e1.toString().trim(),
                            mEtS2t1.getText().toString().trim(),mEtS2cp1.getText().toString().trim(),
                            mEtS1s2.getText().toString().trim(), mEtS1e2.getText().toString().trim(),
                            mEtS2t2.getText().toString().trim(),mEtS2cp2.getText().toString().trim(),
                            mEtS2s3.getText().toString().trim(), mEtS2e3.getText().toString().trim(),
                            mEtS2t3.getText().toString().trim(),mEtS2cp3.getText().toString().trim(),
                            mEtS2s4.getText().toString().trim(), mEtS2e4.getText().toString().trim(),
                            mEtS2t4.getText().toString().trim(),mEtS2cp4.getText().toString().trim(),
                            mEtS2s5.getText().toString().trim(), mEtS2e5.getText().toString().trim(),
                            mEtS2t5.getText().toString().trim(),mEtS2cp5.getText().toString().trim(),
                            mEtS2s6.getText().toString().trim(), mEtS2e6.getText().toString().trim(),
                            mEtS2t6.getText().toString().trim(),mEtS2cp6.getText().toString().trim(),
                            mEtS2s7.getText().toString().trim(), mEtS2e7.getText().toString().trim(),
                            mEtS2t7.getText().toString().trim(),mEtS2cp7.getText().toString().trim(),
                            mEtS2s8.getText().toString().trim(), mEtS2e8.getText().toString().trim(),
                            mEtS2t8.getText().toString().trim(),mEtS2cp8.getText().toString().trim()),
                    mPresenter.getshift(mEtS3s1.getText().toString().trim(), mEtS3e1.toString().trim(),
                            mEtS3t1.getText().toString().trim(),mEtS3cp1.getText().toString().trim(),
                            mEtS3s2.getText().toString().trim(), mEtS3e2.getText().toString().trim(),
                            mEtS3t2.getText().toString().trim(),mEtS3cp2.getText().toString().trim(),
                            mEtS3s3.getText().toString().trim(), mEtS3e3.getText().toString().trim(),
                            mEtS3t3.getText().toString().trim(),mEtS3cp3.getText().toString().trim(),
                            mEtS3s4.getText().toString().trim(), mEtS3e4.getText().toString().trim(),
                            mEtS3t4.getText().toString().trim(),mEtS3cp4.getText().toString().trim(),
                            mEtS3s5.getText().toString().trim(), mEtS3e5.getText().toString().trim(),
                            mEtS3t5.getText().toString().trim(),mEtS3cp5.getText().toString().trim(),
                            mEtS3s6.getText().toString().trim(), mEtS3e6.getText().toString().trim(),
                            mEtS3t6.getText().toString().trim(),mEtS3cp6.getText().toString().trim(),
                            mEtS3s7.getText().toString().trim(), mEtS3e7.getText().toString().trim(),
                            mEtS3t7.getText().toString().trim(),mEtS3cp7.getText().toString().trim(),
                            mEtS3s8.getText().toString().trim(), mEtS3e8.getText().toString().trim(),
                            mEtS3t8.getText().toString().trim(),mEtS3cp8.getText().toString().trim()));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void hideshift1() {
        mLlS1.setVisibility(View.GONE);
    }

    @Override
    public void showshift1() {
        mLlS1.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideshift2() {
        mLlS2.setVisibility(View.GONE);
    }

    @Override
    public void showshift2() {
        mLlS2.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideshift3() {
        mLlS3.setVisibility(View.GONE);
    }

    @Override
    public void showshift3() {
        mLlS3.setVisibility(View.VISIBLE);
    }

    @Override
    public void saveLine(Line line) {
        dbLines.child(line.getId()).setValue(line).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
            }
        });
    }

    @Override
    public void bindPresenter(AddLineContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
