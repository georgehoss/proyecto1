package com.tenneco.tennecoapp;

/**
 * Created by ghoss on 11/09/2018.
 */

public interface BasePresenter<V extends BaseView> {

    void bindView(V view);
    void unbindView();
}