package com.tenneco.tennecoapp.Emails;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenneco.tennecoapp.Downtime.DowntimeFragment;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Plants.PlantsActivity;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Reasons.ReasonsFragment;
import com.tenneco.tennecoapp.SMS.SmsListFragment;
import com.tenneco.tennecoapp.Templates.TemplatesActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class EmailMainFragment extends Fragment {

    private MainActivity main;

    public EmailMainFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.tv_email) void launchEmails(){
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new EmailFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.tv_downtime) void launchDowntime(){
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new DowntimeFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.tv_sms) void launchSms(){
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new SmsListFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.tv_reason) void launchReason(){
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new ReasonsFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.tv_email_lists) void launchEmailList(){
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new EmailListFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.tv_templates) void launchTemplates(){
        startActivity(new Intent(main, TemplatesActivity.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_email_main, container, false);
        ButterKnife.bind(this,view);
       return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        if (main!=null) {
            main.restoreButtons();
            main.setEmail();
            main.showMenu();
        }
    }

}
