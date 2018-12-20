package com.tenneco.tennecoapp.Templates;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Template;
import com.tenneco.tennecoapp.Model.Templates;

/**
 * Created by ghoss on 19/12/2018.
 */
public interface TemplateContract {
    interface View extends BaseView<Presenter>{
        void getTemplates();
        void saveTemplates(Templates templates);
        void showExitDialog();
        void setData(Templates templates);
        void showProgress();
        void hideProgress();
    }
    interface Presenter extends BasePresenter<View>{
        Templates setData(Templates templates);
    }
}
