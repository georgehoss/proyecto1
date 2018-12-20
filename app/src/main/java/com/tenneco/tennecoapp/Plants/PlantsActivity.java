package com.tenneco.tennecoapp.Plants;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.tenneco.tennecoapp.Main.MainFragment;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Utils.StorageUtils;

public class PlantsActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private static int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mTabLayout = findViewById(R.id.tablayout);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        type = StorageUtils.getUserPermissions(this);
    }




    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           switch (position)
           {
               default:
                   return new PlantMainFragment();
               case 1:
                   if (type>1)
                   return new PlantConfigurationFragment();
                   else
                       return new PlantMainFragment();
           }

        }

        @Override
        public int getCount() {

            return 2;

        }

    }
}
