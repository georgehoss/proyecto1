package com.tenneco.tennecoapp.Schedule;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.ScheduleLineAdapter;
import com.tenneco.tennecoapp.Model.realm.Line;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Report.ReportActivity;
import com.tenneco.tennecoapp.Utils.StorageUtils;
import com.tenneco.tennecoapp.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class ScheduleActivity extends AppCompatActivity implements ScheduleContract, ScheduleLineAdapter.ItemInteraction {
    private Realm realm;
    private RealmResults<Line> mRlines;
    private ArrayList<com.tenneco.tennecoapp.Model.Line> mLines;
    private ScheduleLineAdapter mAdapter;
    private DatabaseReference dbLines;
    private DatabaseReference dbPLine;
    private ValueEventListener linesListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mLines = new ArrayList<>();
            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                com.tenneco.tennecoapp.Model.Line line = itemSnapshot.getValue(com.tenneco.tennecoapp.Model.Line.class);
                if (line != null)
                    mLines.add(line);
            }
            if (mLines.size()>0) {
                updateLines();
                mBtSchedule.setVisibility(View.VISIBLE);
                mPb.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            finish();
        }
    };
    @BindView(R.id.bt_schedule) Button mBtSchedule;
    @BindView(R.id.rv_lines) RecyclerView mRvLines;
    @BindView(R.id.pb_loading) ProgressBar mPb;
    @BindView(R.id.tv_date) TextView mTvDate;

    @OnClick(R.id.bt_schedule) void onSchedule() {
        dialogCreate(mAdapter.getLines());
    }

    @OnClick(R.id.tv_date) void onDate(){
        showDatePickerDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        dbLines = FirebaseDatabase.getInstance().getReference(com.tenneco.tennecoapp.Model.Line.DB_LINE).child(StorageUtils.getPlantId(this));
        dbPLine = FirebaseDatabase.getInstance().getReference(com.tenneco.tennecoapp.Model.Line.DB_PRODUCTION_LINE).child(StorageUtils.getPlantId(this));
        mRvLines.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ScheduleLineAdapter(null,this);
        getLines();
        mRvLines.setAdapter(mAdapter);
        mTvDate.setText(Utils.getDateString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            realm.close();
        }
        catch (Exception ignored){

        }
    }

    @Override
    public void getLines() {
        dbLines.addValueEventListener(linesListener);
    }

    @Override
    public void updateLines() {
        mAdapter.setLines(mLines);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void dialogCreate(final ArrayList<com.tenneco.tennecoapp.Model.Line> lines) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_schedulel, null);
        alertDialogBuilder.setView(view);
        TextView tvDate = view.findViewById(R.id.tv_date);
        TextView tvLines = view.findViewById(R.id.tv_lines);
        Button btCancel = view.findViewById(R.id.bt_cancel);
        Button btAccept = view.findViewById(R.id.bt_save);

        StringBuilder sb = new StringBuilder();
        tvDate.setText(mTvDate.getText());
        int sch=0;
        for (com.tenneco.tennecoapp.Model.Line line : lines)
            if (line.isSchedule())
                sch++;
        if (sch>0) {
            for (com.tenneco.tennecoapp.Model.Line line : lines)
                if (line.isSchedule())
                    sb.append("-").append(" ").append(line.getCode()).append(" ").append(line.getName()).append("\n\n");
            tvLines.setText(sb.toString());

            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (com.tenneco.tennecoapp.Model.Line line : lines)
                        if (line.isSchedule())
                            createLine(line);
                    dialog.dismiss();
                    Toast.makeText(ScheduleActivity.this, "Lines have been scheduled successfully for date "+ mTvDate.getText().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else
            Toast.makeText(this, "You must schedule at least one (1) line", Toast.LENGTH_SHORT).show();


    }


    @Override
    public void createLine(com.tenneco.tennecoapp.Model.Line line) {
        String date = mTvDate.getText().toString();
        String id = line.getId();
        line.setParentId(id);
        line.setDate(date);
        line.setId(id+line.getCode()+date.replaceAll("/",""));
        dbPLine.child(id).child(line.getId()).setValue(line);
        DatabaseReference  dbTPLines = FirebaseDatabase.getInstance().getReference(com.tenneco.tennecoapp.Model.Line.DB_DATE_P_LINE).child(StorageUtils.getPlantId(this)).child(Utils.getYear(line.getDate()))
                .child(Utils.getMonth(line.getDate())).child(Utils.getDay(line.getDate()));
        dbTPLines.child(line.getId()).setValue(line);
    }


    @Override
    public void onItemClick(com.tenneco.tennecoapp.Model.Line line, boolean schedule) {

    }

    public void showDatePickerDialog() {
        ReportActivity.DatePickerFragment datePickerFragment = ReportActivity.DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                String mes = String.valueOf(month+1);
                String dia = String.valueOf(day);
                String ano = String.valueOf(year);
                if (mes.length()==1)
                    mes = "0"+mes;

                if (dia.length()==1)
                    dia = "0"+dia;

                final String selectedDate = mes + "/" + dia + "/" + ano;
                if (Utils.compareDate(Utils.getDateString(),selectedDate))
                    Toast.makeText(ScheduleActivity.this, "Sorry, dates before today are not allowed", Toast.LENGTH_SHORT).show();
                else
                    mTvDate.setText(selectedDate);

            }
        });
        datePickerFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener listener;

        public static ReportActivity.DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            ReportActivity.DatePickerFragment fragment = new ReportActivity.DatePickerFragment();
            fragment.setListener(listener);
            return fragment;
        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }
    }
}
