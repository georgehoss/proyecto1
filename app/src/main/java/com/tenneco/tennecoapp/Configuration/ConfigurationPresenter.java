package com.tenneco.tennecoapp.Configuration;

/**
 * Created by ghoss on 13/09/2018.
 */
public class ConfigurationPresenter implements ConfigurationContract.Presenter{
    private ConfigurationContract.View mView;

    ConfigurationPresenter(ConfigurationContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void bindView(ConfigurationContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView=null;
    }
}
