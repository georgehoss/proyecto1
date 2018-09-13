package com.tenneco.tennecoapp;

/**
 * Created by ghoss on 11/09/2018.
 */
public interface BaseView<P extends BasePresenter> {

    void bindPresenter(P presenter);
}
