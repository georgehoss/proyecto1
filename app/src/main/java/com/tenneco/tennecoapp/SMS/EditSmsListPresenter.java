package com.tenneco.tennecoapp.SMS;

import android.content.Context;

import com.tenneco.tennecoapp.Model.SmsList;

/**
 * Created by ghoss on 25/11/2018.
 */
public class EditSmsListPresenter implements EditSmsListContract.Presenter {
    private EditSmsListContract.View mView;

    public EditSmsListPresenter(EditSmsListContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void bindView(EditSmsListContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}
