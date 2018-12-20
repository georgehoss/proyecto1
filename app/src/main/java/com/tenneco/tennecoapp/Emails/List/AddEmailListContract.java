package com.tenneco.tennecoapp.Emails.List;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.EmailList;

import java.util.ArrayList;

/**
 * Created by ghoss on 26/09/2018.
 */
public interface AddEmailListContract {
    interface View extends BaseView<Presenter> {
        void hideProgressBar();
        void getData();
        void saveData(EmailList emailList);
        void initAdapters();
        void showAddEmailDialog(Context context, ArrayList<Email> emails);
        void showNameEmptyError();
        void showExitDialog(Context context);
        void getEmails();
        void showEmailsError();
        void showSelectionError();
        void launchEmails();
    }
    interface Presenter extends BasePresenter<View> {
        EmailList initData(Context context, String id);
        boolean validName(String name);
        void saveChanges(String name, EmailList emailList);
        ArrayList<Email> setSelected(ArrayList<Email> eDefault,ArrayList<Email> eSaved);
        boolean reviewSelected(EmailList emailList);
    }
}
