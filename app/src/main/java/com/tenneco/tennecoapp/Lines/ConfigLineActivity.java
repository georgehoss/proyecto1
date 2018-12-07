package com.tenneco.tennecoapp.Lines;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import android.view.animation.ScaleAnimation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Adapter.EmployeeSelectionAdapter;
import com.tenneco.tennecoapp.Lines.Downtime.LineDowntimeFragment;
import com.tenneco.tennecoapp.Lines.Emails.LineEmailFragment;
import com.tenneco.tennecoapp.Lines.Main.LineConfigFragment;
import com.tenneco.tennecoapp.Lines.Product.LineProductFragment;
import com.tenneco.tennecoapp.Lines.Rejects.LineRejectFragment;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;

public class ConfigLineActivity extends AppCompatActivity implements ConfigLineContract.View{
    private static final int INFO = 0;
    private static final int PRODUCTS = 1;
    private static final int DOWNTIME = 2;
    private static final int REJECTS = 3;
    private static final int EMAILS = 4;
    private DatabaseReference dbLines;
    private DatabaseReference dbEmployees;
    private DatabaseReference dbEmails;
    private EmployeeSelectionAdapter mAdapterEmployee;
    private ArrayList<EmployeePosition> mPositions;
    private ConfigLineContract.Presenter mPresenter;
    public Line mLine;
    private ProgressDialog progressDialog;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector gestureDetector;
    private String id;
    private boolean deletable =false;
    private CoordinatorLayout layout;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_line);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        layout = findViewById(R.id.main_content);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        if (getIntent().getExtras()!=null && getIntent().getExtras().getBoolean("cell"))
            dbLines = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(this)).child(Line.DB_PRODUCTION_LINE);
        else
            dbLines = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(this)).child(Line.DB_LINE);


        if (mPresenter==null)
            mPresenter = new ConfigLinePresenter(this);
        else
            mPresenter.bindView(this);

        if (getIntent().getExtras()!=null && getIntent().getExtras().getString("id")!=null)
        {
            id = getIntent().getExtras().getString("id");
            getData();
        }
        else
        if (getIntent().getExtras()==null || !getIntent().getExtras().getBoolean("cell")){
            id = dbLines.push().getKey();
            mPresenter.initData(this);

        }
        else
            finish();



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Changes");
        progressDialog.setMessage("Please Wait.");
        setGestureDetector();
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
            saveLine(mLine);
        }

        if (item.getItemId() == R.id.menu_delete)
        {
            showDeleteDialog(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveLine(Line line) {
        progressDialog.show();
        if (mPresenter.validName(line.getName()) && mPresenter.validCode(line.getCode())
                && mPresenter.validOperators(mLine.getPositions())
                && mPresenter.validDowntime(mLine.getDowntime()))
            dbLines.child(line.getId()).setValue(line).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    finish();
                    if (progressDialog!=null && progressDialog.isShowing())
                        progressDialog.hide();
                }
            });
        else
            progressDialog.hide();
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
                mLine = dataSnapshot.getValue(Line.class);
                if (mLine!=null) {
                    setData(mLine);
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
    public void setData(Line line) {
        mLine = line;
        if(mViewPager.getCurrentItem() == INFO) {
            LineConfigFragment fragment = (LineConfigFragment)mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager, mViewPager.getCurrentItem());
            fragment.updateLine();
        }
    }

    @Override
    public void showDeleteDialog(Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Delete production Line");
        alertDialogBuilder.setMessage(getString(R.string.delete_question)+ " "+mLine.getName()+" ?");
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
        alertDialogBuilder.setMessage("Do you want to save the changes of the line:"+mLine.getName()+" ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                saveLine(mLine);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.hide();
                finish();

            }
        });
    }

    @Override
    public void setPage(int page) {
        mViewPager.setCurrentItem(page,true);
    }

    @Override
    public void snackbar(String text) {
        Snackbar.make(layout, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    @Override
    public void showCodeError() {
        setPage(INFO);
        snackbar("The Code Can't be empty!");
    }

    @Override
    public void showNameError() {
        setPage(INFO);
        snackbar("The Name Can't be empty!");
    }

    @Override
    public void showPositionError() {
        setPage(INFO);
        snackbar("Add at least one Operator Position!");
    }

    @Override
    public void showDowntimeZoneError() {
        setPage(DOWNTIME);
        snackbar("Add at least one Downtime Zone!");
    }

    @Override
    public void showDowntimeReasonError() {
        setPage(DOWNTIME);
        snackbar("Add at least one Downtime Reason!");
    }

    @Override
    public void bindPresenter(ConfigLineContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                default:
                    return new LineConfigFragment();
                case 1:
                    return new LineProductFragment();
                case 2:
                    return new LineDowntimeFragment();
                case 3:
                    return new LineRejectFragment();
                case 4:
                    return new LineEmailFragment();

            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
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
}
