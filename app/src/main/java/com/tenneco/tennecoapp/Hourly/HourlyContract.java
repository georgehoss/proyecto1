package com.tenneco.tennecoapp.Hourly;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

/**
 * Created by ghoss on 14/09/2018.
 */
public interface HourlyContract {
    interface View extends BaseView<Presenter>{
        void getLines();
        void getLine();
        void addNewLine();
        void addTNewLine();
        void hideProgressBar();
        void showFb();
        void hideFb();
        void showTw();
        void hideTw();
        void setLine();
        void hideShifts();
        void showShifts();
        void launchDaily(String lineId);
        void getRealmResults();
    }
    interface Presenter extends BasePresenter<View>{

    }
}
