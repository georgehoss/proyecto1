package com.tenneco.tennecoapp.SMS;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.SmsList;

/**
 * Created by ghoss on 26/09/2018.
 */
public interface SmsListContract {
    interface View extends BaseView<Presenter> {
        void hideProgressBar();
        void hideProgessDialog();
        void getData();
        void saveData(SmsList smsList);
        void initAdapters();
        void showNameEmptyError();
        void showAddDialog(Context context);
        void showDeleteDialog (Context context,SmsList smsList);
        void delete(SmsList reasonDelay);
        void showSendSmsDialog();
        void sendSms(String number, String message);

    }
    interface Presenter extends BasePresenter<View> {
        SmsList initData(Context context, String id);
        boolean validName(String name);
        void saveChanges(String name, SmsList smsList);
    }
}
