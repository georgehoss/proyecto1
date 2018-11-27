package com.tenneco.tennecoapp.Downtime;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Email;

/**
 * Created by ghoss on 26/09/2018.
 */
public interface EditCreateDowntimeContract {
    interface View extends BaseView<Presenter> {
        void hideProgressBar();
        void getData();
        void saveData(Downtime downtime);
        void initAdapters();
        void showAddDowntimeDialog(Context context, Zone zone);
        void showAddEventDialog(Context context, final int reason);
        void showNameEmptyError();
        void showExitDialog(Context context);


    }
    interface Presenter extends BasePresenter<View> {
        Downtime initData(Context context, String id);
        boolean validName(String name);
        void saveChanges(String name,Downtime downtime);
    }
}
