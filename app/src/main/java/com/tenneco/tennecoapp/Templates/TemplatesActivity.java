package com.tenneco.tennecoapp.Templates;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tenneco.tennecoapp.Model.Plant;
import com.tenneco.tennecoapp.Model.Template;
import com.tenneco.tennecoapp.Model.Templates;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

public class TemplatesActivity extends AppCompatActivity implements TemplateContract.View {
    private static final int DOWNTIME = 0;
    private static final int FTQ = 1;
    private static final int REJECTS = 2;
    private static final int REPORT = 3;
    private static final int SHIFT = 4;
    private TemplateContract.Presenter mPresenter;
    private DatabaseReference dbTemplates;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public Templates mTemplates;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        dbTemplates = FirebaseDatabase.getInstance().getReference(Plant.DB_PLANTS).child(StorageUtils.getPlantId(this)).child(Template.DB_TEMPLATE);

        if (mPresenter==null)
            mPresenter= new TemplatePresenter(this,this);
        else
            mPresenter.bindView(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait.");
        progressDialog.setCancelable(true);
        setData(mPresenter.setData(mTemplates));
        getTemplates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_templates, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save) {
            saveTemplates(mTemplates);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getTemplates() {
        showProgress();
        Query postsQuery;
        postsQuery = dbTemplates.child(Templates.ID);
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTemplates = dataSnapshot.getValue(Templates.class);
                setData(mPresenter.setData(mTemplates));
                hideProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
    }


    @Override
    public void saveTemplates(Templates templates) {
        showProgress();
        templates.setId(Templates.ID);
        dbTemplates.child(templates.getId()).setValue(templates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgress();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    @Override
    public void showExitDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Save Changes");
        alertDialogBuilder.setMessage("Do you want to save the changes of the Templates ?");
        alertDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                saveTemplates(mTemplates);
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
    public void setData(Templates templates) {
        mTemplates = templates;

            DowntimeTemplateFragment fragment = (DowntimeTemplateFragment)mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager, DOWNTIME);
            fragment.updateTemplate();


            FTQTemplateFragment fragment1 = (FTQTemplateFragment)mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager, FTQ);
            fragment1.updateTemplate();

            RejectsTemplateFragment fragment2 = (RejectsTemplateFragment)mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager,REJECTS);
            fragment2.updateTemplate();


            ReportTemplateFragment fragment3 = (ReportTemplateFragment)mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager, REPORT);
            fragment3.updateTemplate();

        if(mViewPager.getCurrentItem() == SHIFT) {
            ShiftTemplateFragment fragment4 = (ShiftTemplateFragment)mViewPager
                    .getAdapter()
                    .instantiateItem(mViewPager, mViewPager.getCurrentItem());
            //fragment.updateLine();
        }
    }

    @Override
    public void showProgress() {
        if (progressDialog!=null && !progressDialog.isShowing())
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.hide();
    }

    @Override
    public void bindPresenter(TemplateContract.Presenter presenter) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                default:
                    return new DowntimeTemplateFragment();
                case FTQ:
                    return new FTQTemplateFragment();
                case REJECTS:
                    return new RejectsTemplateFragment();
                case REPORT:
                    return new ReportTemplateFragment();
                case SHIFT:
                    return new ShiftTemplateFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
