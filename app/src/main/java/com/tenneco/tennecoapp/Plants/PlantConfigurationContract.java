package com.tenneco.tennecoapp.Plants;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

public interface PlantConfigurationContract {
    interface View extends BaseView<Presenter>{
        void getPlants();
        void launchAddActivity();
        void hideProgressBar();
    }
    interface Presenter extends BasePresenter<View>{}
}
