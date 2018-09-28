package com.tenneco.tennecoapp.ConfigurationMenu;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

/**
 * Created by ghoss on 21/09/2018.
 */
public interface MenuConfigContract {
    interface View extends BaseView<Presenter> {
        void launchUsers();
        void launchLines();
        void launchEmails();
        void launchEmployee();
    }
    interface Presenter extends BasePresenter<View> {}
}
