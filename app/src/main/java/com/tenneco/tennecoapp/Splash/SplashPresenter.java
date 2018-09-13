package com.tenneco.tennecoapp.Splash;

/**
 * Created by ghoss on 11/09/2018.
 */
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;

    public SplashPresenter(SplashContract.View mView) {
        bindView(mView);
    }

    @Override
    public void bindView(SplashContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}
