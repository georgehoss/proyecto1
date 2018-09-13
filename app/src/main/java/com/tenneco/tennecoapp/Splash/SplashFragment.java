package com.tenneco.tennecoapp.Splash;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenneco.tennecoapp.Login.LoginFragment;
import com.tenneco.tennecoapp.R;
import com.tenneco.tennecoapp.Register.RegisterFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplashFragment extends Fragment implements SplashContract.View {


    public SplashFragment() {
        // Required empty public constructor
    }


    @OnClick(R.id.bt_sign_up) void singUp(){
        launchSignUp();
    }

    @OnClick(R.id.bt_sign_in) void singIn(){
        launchSignIn();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this,view);
        return  view;
    }

    @Override
    public void launchSignUp() {
        if (getActivity()!=null)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new RegisterFragment()).addToBackStack(null).commit();
    }

    @Override
    public void launchSignIn() {
        if (getActivity()!=null)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).addToBackStack(null).commit();
    }

    @Override
    public void bindPresenter(SplashContract.Presenter presenter) {

    }
}
