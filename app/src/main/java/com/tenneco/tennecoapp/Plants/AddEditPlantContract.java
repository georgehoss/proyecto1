package com.tenneco.tennecoapp.Plants;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Plant;

/**
 * Created by ghoss on 14/11/2018.
 */
public interface AddEditPlantContract {
    interface View extends BaseView<Presenter> {
        void getData();
        void setData(Plant plant);
        void showExitDialog(Context context);
        void showDeleteDialog(Context context);
        void showNameError();
        void showCodeError();
        void delete();
        void savePlant(Plant plant);
    }
    interface Presenter extends BasePresenter<View> {
        boolean validName(String name);
        boolean validCode(String code);
        void saveChanges(Plant plant);
    }
}
