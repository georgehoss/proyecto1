package com.tenneco.tennecoapp.Plants;

import com.tenneco.tennecoapp.Model.Plant;

/**
 * Created by ghoss on 14/11/2018.
 */
public class AddEditPlantPresenter implements AddEditPlantContract.Presenter {
    private AddEditPlantContract.View mView;

    public AddEditPlantPresenter(AddEditPlantContract.View mView) {
        this.mView = mView;
    }

    @Override
    public boolean validName(String name) {
        return !name.isEmpty();
    }

    @Override
    public boolean validCode(String code) {
        return !code.isEmpty();
    }

    @Override
    public void saveChanges(Plant plant) {
        boolean valid=true;
        if (!validName(plant.getName())){
            mView.showNameError();
            valid = false;
        }
        else
        if (!validCode(plant.getCode())){
            mView.showCodeError();
            valid = false;
        }

        if (valid)
        {
            mView.savePlant(plant);
        }

    }

    @Override
    public void bindView(AddEditPlantContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}
