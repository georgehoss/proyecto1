package com.tenneco.tennecoapp.Report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.ReportAdapter;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.EmailList;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.Template;
import com.tenneco.tennecoapp.Model.Templates;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;
import com.tenneco.tennecoapp.Utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends AppCompatActivity implements ReportContract.View{
    private DatabaseReference dbLines;
    private ReportAdapter mAdapter;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector gestureDetector;
    private ArrayList<Line> mLines;
    private ProgressDialog progressDialog;
    private DatabaseReference dbEmailList;
    private ArrayList<EmailList> emailLists;
    private EmailList mEmailLists;
    private DatabaseReference dbTemplates;
    private Templates templates;
    private Query postsQuery;
    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mLines = new ArrayList<>();

            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
            {
                Line line = itemSnapshot.getValue(Line.class);
                if (line!=null)
                    mLines.add(line);
            }

            mAdapter.setLines(mLines);
            mAdapter.notifyDataSetChanged();
            if (progressDialog!=null && progressDialog.isShowing())
                progressDialog.hide();

            if (mLines.size()>0)
                showButton();
            else
                hideButton();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            if (progressDialog!=null && progressDialog.isShowing())
                progressDialog.hide();

        }
    };

    @BindView(R.id.tv_name)TextView mTvName;
    @BindView(R.id.tv_date)TextView mTvDate;
    @BindView(R.id.bt_send_report) Button mBtSend;
    @BindView(R.id.rv_lines) RecyclerView mRvLines;

    @OnClick(R.id.tv_date) void showD(){
        showDatePickerDialog();
    }

    @OnClick(R.id.bt_send_report) void shot(){
        takeScreenshot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        setGestureDetector();
        initAdapter();
        dbLines = FirebaseDatabase.getInstance().getReference(Line.DB_DATE_P_LINE).child(StorageUtils.getPlantId(this));
        dbEmailList = FirebaseDatabase.getInstance().getReference(EmailList.DB).child(StorageUtils.getPlantId(this));
        dbTemplates = FirebaseDatabase.getInstance().getReference(Template.DB_TEMPLATE).child(StorageUtils.getPlantId(this));
        showDatePickerDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Production Lines...");
        progressDialog.setCancelable(true);
        getEmails();
        getTemplates();
        setTitle("Daily Report");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showButton();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.hide();
        postsQuery.removeEventListener(valueEventListener);
    }

    @Override
    public void getLines(String date) {

        if (progressDialog!=null && !progressDialog.isShowing())
            progressDialog.show();
        postsQuery = dbLines.child(Utils.getYear(date)).child(Utils.getMonth(date)).child(Utils.getDay(date)).orderByChild("code");
        postsQuery.addValueEventListener(valueEventListener);
    }

    @Override
    public void setTitle(String title) {
        mTvName.setText(title);
    }

    @Override
    public void setDate(String date) {
        mTvDate.setText(date);
    }

    @Override
    public void getEmails() {
        dbEmailList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<EmailList> list = new ArrayList<EmailList>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    list.add(child.getValue(EmailList.class));
                }
                emailLists = new ArrayList<>();
                emailLists.addAll(list);
                Collections.sort(emailLists,EmailList.NameComparator);
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
    public void initAdapter() {
        mLines = new ArrayList<>();
        mRvLines.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReportAdapter(mLines);
        mRvLines.setAdapter(mAdapter);
    }

    @Override
    public void hideButton() {
        mBtSend.setVisibility(View.GONE);
    }

    @Override
    public void showButton() {
        mBtSend.setVisibility(View.VISIBLE);
    }

    @Override
    public void sendEmailDialog(final File imageFile) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_send_daily, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        Spinner spShift1 = view.findViewById(R.id.sp_shift_1);
        ImageView imageView = view.findViewById(R.id.iv_preview);
        imageView.setImageURI(Uri.fromFile(imageFile));
        if (emailLists!=null) {
            final ArrayList<EmailList> mReasons = new ArrayList<>(emailLists);
            ArrayAdapter<EmailList> mAdapter = new ArrayAdapter<>(this, R.layout.spinner_row, mReasons);
            spShift1.setAdapter(mAdapter);
        }


        spShift1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EmailList emailList = (EmailList) adapterView.getItemAtPosition(i);
                if (emailList!=null) {
                    mEmailLists = emailList;
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
               openScreenshot(imageFile);
                dialog.dismiss();
            }
        });


    }

    @Override
    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
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
                setDate(selectedDate);
                getLines(selectedDate);
            }
        });
        datePickerFragment.show(this.getSupportFragmentManager(), "datePicker");
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


    private void takeScreenshot() {
        hideButton();
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" +now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            sendEmailDialog(imageFile);
            //openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        if (mEmailLists!=null && mEmailLists.getEmails().size()>0)
        {
            ArrayList<Email> list = new ArrayList<>();
            ArrayList<Email> list1 = new ArrayList<>();

            for (Email email : mEmailLists.getEmails()) {
                if (email.isShift1())
                    list.add(new Email(email));
                if (email.isCc1())
                    list1.add(new Email(email));
            }

            String [] addresses = new String[list.size()];
            for (int i=0; i<list.size(); i++)
                addresses[i]= list.get(i).toString();

            String [] ccs = new String[list1.size()];
            for (int i=0; i<list1.size(); i++)
                ccs[i]= list1.get(i).toString();

            if (list.size()>0)
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            if (list1.size()>0)
            intent.putExtra(Intent.EXTRA_CC, ccs);
        }


        if (templates!=null && templates.getReport()!=null) {
            String subject = templates.getReport().getSubject()+" " + mTvDate.getText().toString();
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            String body = templates.getReport().getBody1() +" "+ mTvDate.getText().toString()+ "\n" + templates.getReport().getBody2();
            intent.putExtra(Intent.EXTRA_TEXT, body);
        }



        Uri uri = Uri.fromFile(imageFile);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(intent);
        showButton();
    }


    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener listener;

        public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            DatePickerFragment fragment = new DatePickerFragment();
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
