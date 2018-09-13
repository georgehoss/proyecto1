package com.tenneco.tennecoapp.Splash;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

/**
 * Created by ghoss on 11/09/2018.
 */
public interface SplashContract {
    interface View extends BaseView<Presenter> {
        void launchSignUp();
        void launchSignIn();
    }
    interface Presenter extends BasePresenter<View> {}
}
