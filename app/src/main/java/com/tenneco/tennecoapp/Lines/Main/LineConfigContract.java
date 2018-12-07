package com.tenneco.tennecoapp.Lines.Main;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;

/**
 * Created by ghoss on 06/12/2018.
 */
public interface LineConfigContract {
    interface View extends BaseView<Presenter>{
        void updateLine();
        void setData();
        void getData();
        void setTextWatchers();

    }
    interface Presenter extends BasePresenter<View>{

    }
}
