package com.tenneco.tennecoapp.Main;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

/**
 * Created by ghoss on 13/09/2018.
 */
public interface MainContract {
    interface View extends BaseView<Presenter>{
        void getUser();
        void getLines();
        void getRealmResults();
        void hideProgress();
        void showPickOne();
        void showNoLines();
        void showPlants();
        void launchPlants();
        void launchHourly(String lineId);
    }
    interface Presenter extends BasePresenter<View>{}
}
