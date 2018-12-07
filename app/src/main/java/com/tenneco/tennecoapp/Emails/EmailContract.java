package com.tenneco.tennecoapp.Emails;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Email;

/**
 * Created by ghoss on 26/09/2018.
 */
public interface EmailContract {
    interface View extends BaseView<Presenter> {
        void hideProgressBar();
        void showFloatingButton();
        void getEmails();
        void addEditDialog(Email email, Context context);
        void editDeleteDialog(Email email);
        void delete(String id);
        void addEditEmployee(Email email);
        void deleteDialog(Email email);

    }
    interface Presenter extends BasePresenter<View> {

    }
}
