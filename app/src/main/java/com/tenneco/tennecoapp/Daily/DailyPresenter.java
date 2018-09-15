package com.tenneco.tennecoapp.Daily;

/**
 * Created by ghoss on 14/09/2018.
 */
public class DailyPresenter implements DailyContract.Presenter {

    private DailyContract.View mView;

    DailyPresenter(DailyContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void bindView(DailyContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}
