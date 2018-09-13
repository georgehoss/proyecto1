package com.tenneco.tennecoapp.Register;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

/**
 * Created by ghoss on 11/09/2018.
 */
public interface RegisterContract {
    interface View extends BaseView<Presenter> {
        void showFirstNameError();
        void showLastNameError();
        void showEmailError();
        void showEmailFormatError();
        void showPswLengthError();
        void showPasswordError();
        void signUpUser(String firstName, String lastName, String email, String password);
        void showEmailDuplicatedError();
        void showSingUpFailedError();
        void updateUserInfo(String name);
        void launchSignIn();
    }
    interface Presenter extends BasePresenter<View> {
        void register (String firstName, String lastName, String email, String password);
        boolean validData(String firstName, String lastName, String email, String password);
        boolean validEmail(CharSequence email);
        boolean validPsw (CharSequence psw);

    }
}

