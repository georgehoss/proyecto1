package com.tenneco.tennecoapp.Plants;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

/**
 * Created by ghoss on 13/09/2018.
 */
public interface PlantMainContract {
    interface View extends BaseView<Presenter>{
        void getPlants();
        void hideProgress();
        void showPickOne();
        void showNoPlants();
        void launchLines(String plantId);
    }
    interface Presenter extends BasePresenter<View>{}
}
