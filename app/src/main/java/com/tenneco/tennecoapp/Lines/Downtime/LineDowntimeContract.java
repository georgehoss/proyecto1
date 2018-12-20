package com.tenneco.tennecoapp.Lines.Downtime;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Zone;

import java.util.ArrayList;

/**
 * Created by ghoss on 07/12/2018.
 */
public interface LineDowntimeContract {
    interface View extends BaseView<Presenter>{
        void initAdapters();
        void updateLine();
        void showAddDowntimeDialog(Context context, Zone zone);
        void showAddEventDialog(Context context, final int reason);
        void showListDowntimeDialog(Context context, ArrayList<Downtime> downtimes);
        void showListButton();
        void hideListButton();

    }
    interface Presenter extends BasePresenter<View>{}
}
