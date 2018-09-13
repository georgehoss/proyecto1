package com.tenneco.tennecoapp.Configuration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenneco.tennecoapp.R;

import butterknife.ButterKnife;


public class ConfigurationFragment extends Fragment {


    public ConfigurationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

}
