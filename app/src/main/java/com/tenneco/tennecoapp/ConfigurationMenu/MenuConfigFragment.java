package com.tenneco.tennecoapp.ConfigurationMenu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tenneco.tennecoapp.Configuration.ConfigurationFragment;
import com.tenneco.tennecoapp.Employee.EmployeeFragment;
import com.tenneco.tennecoapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuConfigFragment extends Fragment implements MenuConfigContract.View {


    @OnClick(R.id.tv_lines) void lines(){
        launchLines();
    }

    @OnClick(R.id.tv_users) void users(){
        launchUsers();
    }

   @OnClick(R.id.tv_employee) void employee(){
        launchEmployee();
   }

    @OnClick(R.id.tv_emails) void emails(){
        launchEmails();
    }



    public MenuConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu_config, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void launchUsers() {

    }

    @Override
    public void launchLines() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ConfigurationFragment()).addToBackStack(null).commit();
    }

    @Override
    public void launchEmails() {

    }

    @Override
    public void launchEmployee() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new EmployeeFragment()).addToBackStack(null).commit();
    }

    @Override
    public void bindPresenter(MenuConfigContract.Presenter presenter) {

    }
}
