package com.tenneco.tennecoapp.SMS;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Sms;
import com.tenneco.tennecoapp.Model.SmsList;

/**
 * Created by ghoss on 26/09/2018.
 */
public interface EditSmsListContract {
    interface View extends BaseView<Presenter> {
        void hideProgressBar();
        void hideProgessDialog();
        void getData();
        void saveData(Sms sms);
        void initAdapters();
        void showNameEmptyError();
        void showAddDialog(Context context,Sms sms);
        void showDeleteDialog (Context context,Sms sms);
        void delete(Sms sms);
    }
    interface Presenter extends BasePresenter<View> {
    }
}
