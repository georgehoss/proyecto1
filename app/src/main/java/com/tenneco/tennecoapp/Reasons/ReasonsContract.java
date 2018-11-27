package com.tenneco.tennecoapp.Reasons;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.ReasonDelay;
import com.tenneco.tennecoapp.Model.Sms;

/**
 * Created by ghoss on 26/09/2018.
 */
public interface ReasonsContract {
    interface View extends BaseView<Presenter> {
        void hideProgressBar();
        void hideProgessDialog();
        void getData();
        void saveData(ReasonDelay reasonDelay);
        void initAdapters();
        void showAddDialog(Context context, ReasonDelay reasonDelay);
        void showDeleteDialog (Context context,ReasonDelay reasonDelay);
        void delete (ReasonDelay reasonDelay);
    }
    interface Presenter extends BasePresenter<View> {
    }
}
