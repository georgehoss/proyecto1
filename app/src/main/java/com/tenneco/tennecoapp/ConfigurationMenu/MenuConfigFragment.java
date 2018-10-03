package com.tenneco.tennecoapp.ConfigurationMenu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenneco.tennecoapp.Employee.EmployeeFragment;
import com.tenneco.tennecoapp.MainActivity;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.User.UserFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuConfigFragment extends Fragment implements MenuConfigContract.View {


    private MainActivity main;

    @OnClick(R.id.tv_groups) void groups(){
        launchGroups();
    }

    @OnClick(R.id.tv_users) void users(){
        launchUsers();
    }

   @OnClick(R.id.tv_employee) void employee(){
        launchEmployee();
   }

    @OnClick(R.id.tv_teams) void teams(){
        launchTeams();
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
    public void onStart() {
        super.onStart();
        main = (MainActivity) getActivity();
        if (main!=null) {
            main.restoreButtons();
            main.setUsers();
            main.showMenu();
        }
    }

    @Override
    public void launchUsers() {
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new UserFragment()).addToBackStack(null).commit();
    }

    @Override
    public void launchGroups() {
        UserFragment userFragment = UserFragment.newInstance(User.DB_GROUP);
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, userFragment).addToBackStack(null).commit();
    }

    @Override
    public void launchTeams() {
        UserFragment userFragment = UserFragment.newInstance(User.DB_TEAM);
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, userFragment).addToBackStack(null).commit();
    }

    @Override
    public void launchEmployee() {
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new EmployeeFragment()).addToBackStack(null).commit();
    }

    @Override
    public void bindPresenter(MenuConfigContract.Presenter presenter) {

    }
}
