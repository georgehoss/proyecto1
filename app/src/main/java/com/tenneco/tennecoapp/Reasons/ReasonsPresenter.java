package com.tenneco.tennecoapp.Reasons;

/**
 * Created by ghoss on 26/11/2018.
 */
public class ReasonsPresenter implements ReasonsContract.Presenter {
    private ReasonsContract.View mView;

    public ReasonsPresenter(ReasonsContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void bindView(ReasonsContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}
