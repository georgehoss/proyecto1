package com.tenneco.tennecoapp.Downtime;

import android.content.Context;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;

/**
 * Created by ghoss on 18/11/2018.
 */
public class EditCreateDowntimePresenter implements EditCreateDowntimeContract.Presenter {
    private EditCreateDowntimeContract.View mView;

    EditCreateDowntimePresenter(EditCreateDowntimeContract.View view) {
        this.mView = view;
    }


    @Override
    public Downtime initData(Context context, String id) {
        return null;
    }

    @Override
    public boolean validName(String name) {
        return !name.isEmpty();
    }

    @Override
    public void saveChanges(String name,Downtime downtime) {
        if (validName(name))
        {
            downtime.setName(name);
            if (downtime.getZones()!=null && downtime.getReasons()!=null)
                mView.saveData(downtime);
        }
        else
            mView.showNameEmptyError();
    }

    @Override
    public void bindView(EditCreateDowntimeContract.View view) {

    }

    @Override
    public void unbindView() {

    }
}
