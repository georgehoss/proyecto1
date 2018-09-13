package com.tenneco.tennecoapp.Login;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

/**
 * Created by ghoss on 11/09/2018.
 */
public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void showUserError();
        void showPasswordError();
        void showLoginError();
        void showEmailError();
        void showPswError();
        void signIn(String user, String psw);
        void launchRegister();
        void passwordReset();
        void showPswResetMsg(String email);
    }
    interface Presenter extends BasePresenter<View> {
        void login(String user, String password);
        boolean validData(String user,String psw);
        boolean validEmail(CharSequence email);
        boolean validPsw (CharSequence psw);
    }
}
