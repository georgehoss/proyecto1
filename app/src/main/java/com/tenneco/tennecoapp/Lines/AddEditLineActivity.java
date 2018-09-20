package com.tenneco.tennecoapp.Lines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.HourAdapter;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditLineActivity extends AppCompatActivity implements AddLineContract.View,HourAdapter.ItemInteraction {
    private DatabaseReference dbLines;
    private AddLineContract.Presenter mPresenter;
    private Shift shift1;
    private Shift shift2;
    private Shift shift3;
    private String id;
    private HourAdapter mAdapter1;
    private HourAdapter mAdapter2;
    private HourAdapter mAdapter3;
    private boolean deletable = false;
    @BindView(R.id.et_name) EditText mEtName;
    @BindView(R.id.ll_shift1) LinearLayout mLlS1;
    @BindView(R.id.ll_shift2) LinearLayout mLlS2;
    @BindView(R.id.ll_shift3) LinearLayout mLlS3;
    @BindView(R.id.rv_shift1) RecyclerView mRvS1;
    @BindView(R.id.rv_shift2) RecyclerView mRvS2;
    @BindView(R.id.rv_shift3) RecyclerView mRvS3;



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

        if (getIntent().getExtras()!=null)
        {
            id = getIntent().getExtras().getString("id");
            getData();
        }
        else {
            id = dbLines.push().getKey();
            mPresenter.initData(this);
        }


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
                mPresenter.saveChanges(mEtName.getText().toString().trim(),id,shift1,shift2,shift3);
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
    public void onBackPressed() {
        showExitDialog(this);
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
                    setData(line.getFirst(),line.getSecond(),line.getThird());
                    deletable = true;
                    invalidateOptionsMenu();
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
    public void setData(Shift s1,Shift s2 , Shift s3) {
        shift1 = new Shift(s1);
        shift2 = new Shift(s2);
        shift3 = new Shift(s3);
        mAdapter1.setHours(shift1.getHours());
        mAdapter1.notifyDataSetChanged();
        mAdapter2.setHours(shift2.getHours());
        mAdapter2.notifyDataSetChanged();
        mAdapter3.setHours(shift3.getHours());
        mAdapter3.notifyDataSetChanged();

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
        alertDialogBuilder.setMessage("Are you sure you want to delete "+mEtName.getText().toString()+" ?");
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
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
        alertDialogBuilder.setMessage("Do you want to save changes of the line:"+mEtName.getText().toString()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (mPresenter.validName(mEtName.getText().toString().trim()))
                    mPresenter.saveChanges(mEtName.getText().toString().trim(),id,shift1,shift2,shift3);
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
    public void delete() {
        dbLines.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
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
