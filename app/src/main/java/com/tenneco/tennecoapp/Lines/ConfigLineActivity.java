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

import android.view.View;
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
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.EmailList;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.Product;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;

public class ConfigLineActivity extends AppCompatActivity implements ConfigLineContract.View{
    private static final int INFO = 0;
    private static final int PRODUCTS = 1;
    private static final int DOWNTIME = 2;
    private static final int REJECTS = 3;
    private static final int EMAILS = 4;
    private DatabaseReference dbLines;
    private DatabaseReference dbDowntime;
    private DatabaseReference dbProducts;
    public ArrayList<Downtime> mDowntimes;
    public ArrayList<EmailList> mEmailLists;
    public ArrayList<Product> mProducts;
    private DatabaseReference dbEmailList;
    private ConfigLineContract.Presenter mPresenter;
    public Line mLine;
    private ProgressDialog progressDialog;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    GestureDetector gestureDetector;
    private String id;
    private boolean deletable =false;
    private CoordinatorLayout layout;
    private Realm realm;
    private com.tenneco.tennecoapp.Model.realm.Line realmLine;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private Query postsQuery;
    private ValueEventListener valueEventListener = new ValueEventListener() {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_line);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        layout = findViewById(R.id.main_content);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        realm = Realm.getDefaultInstance();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        String parentId="";
        if (getIntent().getExtras()!=null && getIntent().getExtras().getString("parentId")!=null){
            parentId =getIntent().getExtras().getString("parentId");
        }

        if (getIntent().getExtras()!=null && getIntent().getExtras().getBoolean("cell")) {
            dbLines = FirebaseDatabase.getInstance().getReference(Line.DB_PRODUCTION_LINE).child(StorageUtils.getPlantId(this)).child(parentId);
            dbLines = FirebaseDatabase.getInstance().getReference(Line.DB_PRODUCTION_LINE).child(StorageUtils.getPlantId(this)).child(parentId);
            realmLine = realm.where(com.tenneco.tennecoapp.Model.realm.Line.class).equalTo("plantId",StorageUtils.getPlantId(this)).equalTo("parentId",parentId).equalTo("id",id).findFirst();
        }
        else {
            dbLines = FirebaseDatabase.getInstance().getReference(Line.DB_LINE).child(StorageUtils.getPlantId(this));
            realmLine = realm.where(com.tenneco.tennecoapp.Model.realm.Line.class).equalTo("plantId",StorageUtils.getPlantId(this)).equalTo("id",id).findFirst();
        }

        dbLines.keepSynced(false);

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
        dbEmailList = FirebaseDatabase.getInstance().getReference(EmailList.DB).child(StorageUtils.getPlantId(this));
        dbDowntime = FirebaseDatabase.getInstance().getReference(Downtime.DB_DOWNTIMES).child(StorageUtils.getPlantId(this));
        dbProducts =  FirebaseDatabase.getInstance().getReference(Product.DB).child(StorageUtils.getPlantId(this));
        getDowntimeLists();
        getEmailLists();
        getProducts();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Saving Changes");
        progressDialog.setMessage("Please Wait.");
        setGestureDetector();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (postsQuery!=null && valueEventListener!=null)
        postsQuery.removeEventListener(valueEventListener);
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
            mPresenter.saveLine(mLine);
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
        line.setId(id);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new com.tenneco.tennecoapp.Model.realm.Line(
                line.getId(),line.getName(),line.getCode(),line.getDate(),line.getProducts().get(0).getFirst().getCumulativePlanned(),
                line.getProducts().get(0).getSecond().getCumulativePlanned(),line.getProducts().get(0).getThird().getCumulativePlanned(),
                "0","0","0",false,false,StorageUtils.getPlantId(this)));
        realm.commitTransaction();
        dbLines.child(id).setValue(line).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    hideProgressBar();
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

        postsQuery = dbLines.child(id);
        postsQuery.addValueEventListener(valueEventListener);
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
        else
        if(mViewPager.getCurrentItem() == DOWNTIME) {
            LineDowntimeFragment fragment = (LineDowntimeFragment)mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager, mViewPager.getCurrentItem());
            fragment.updateLine();
        }
        else
        if(mViewPager.getCurrentItem() == REJECTS) {
            LineRejectFragment fragment = (LineRejectFragment)mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager, mViewPager.getCurrentItem());
            fragment.updateLine();
        }
        else
        if(mViewPager.getCurrentItem() == PRODUCTS) {
            LineProductFragment fragment = (LineProductFragment)mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager, mViewPager.getCurrentItem());
            fragment.updateLine();
        }
        else
        if(mViewPager.getCurrentItem() == EMAILS) {
            LineEmailFragment fragment = (LineEmailFragment)mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager, mViewPager.getCurrentItem());
            fragment.updateLine();
        }
    }

    @Override
    public void getDowntimeLists() {
        dbDowntime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Downtime> list = new ArrayList<Downtime>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    list.add(child.getValue(Downtime.class));
                }
                mDowntimes = new ArrayList<>();
                mDowntimes.addAll(list);
                Collections.sort(mDowntimes,Downtime.NameComparator);
                setData(mLine);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void getEmailLists() {
        dbEmailList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<EmailList> list = new ArrayList<EmailList>();
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
                mEmailLists = new ArrayList<>();
                mEmailLists.addAll(list);
                Collections.sort(mEmailLists,EmailList.NameComparator);

                setData(mLine);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void getProducts() {
        dbProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mProducts = new ArrayList<>();
                hideProgressBar();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren())
                {
                    Product product = itemSnapshot.getValue(Product.class);
                    if (product!=null)
                        mProducts.add(product);
                }
                Collections.sort(mProducts,Product.NameComparator);
                setData(mLine);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressBar();
            }
        });
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
                mPresenter.saveLine(mLine);
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

        try {
            realm.beginTransaction();
            realmLine.deleteFromRealm();
            realm.commitTransaction();
        }
        catch (Exception ignored){

        }

        dbLines.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                hideProgressBar();
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
        snackbar("Add at least one (1) Operator Position!");
    }

    @Override
    public void showDowntimeZoneError() {
        setPage(DOWNTIME);
        snackbar("Add at least one (1) Downtime Zone!");
    }

    @Override
    public void showDowntimeReasonError() {
        setPage(DOWNTIME);
        snackbar("Add at least one (1) Downtime Reason!");
    }

    @Override
    public void showRejectReasonError() {
        setPage(REJECTS);
        snackbar("Add at least one (1) Reject Reason!");
    }

    @Override
    public void showEmailError() {
        setPage(EMAILS);
        snackbar("You must set all emails list notifications!");
    }

    @Override
    public void showProductError() {
        setPage(PRODUCTS);
        snackbar("Add at least one (1) Product!");
    }

    @Override
    public void hideProgressBar() {
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.hide();
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
