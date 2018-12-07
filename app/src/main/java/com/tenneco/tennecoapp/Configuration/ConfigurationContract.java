package com.tenneco.tennecoapp.Configuration;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

/**
 * Created by ghoss on 13/09/2018.
 */
public interface ConfigurationContract {
    interface View extends BaseView<Presenter>{
        void getLines();
        void launchAddActivity();
        void hideProgressBar();
        void launchProducts();
    }
    interface Presenter extends BasePresenter<View>{}
}
