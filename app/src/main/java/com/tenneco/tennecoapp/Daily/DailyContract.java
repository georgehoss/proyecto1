package com.tenneco.tennecoapp.Daily;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

/**
 * Created by ghoss on 14/09/2018.
 */
public interface DailyContract {
    interface View extends BaseView<Presenter>{}
    interface Presenter extends BasePresenter<View>{}
}
