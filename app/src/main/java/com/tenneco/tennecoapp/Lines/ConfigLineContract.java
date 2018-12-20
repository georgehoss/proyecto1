package com.tenneco.tennecoapp.Lines;

import android.app.AlertDialog;
import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;

import java.util.ArrayList;

/**
 * Created by ghoss on 13/09/2018.
 */
public interface ConfigLineContract {
    interface View extends BaseView<Presenter> {
        void saveLine(Line line);
        void getData();
        void setData(Line line);
        void getDowntimeLists();
        void getEmailLists();
        void getProducts();
        void showDeleteDialog(Context context);
        void showExitDialog(Context context);
        void delete();
        void setPage(int page);
        void snackbar(String text);
        void showCodeError();
        void showNameError();
        void showPositionError();
        void showDowntimeZoneError();
        void showDowntimeReasonError();
        void showRejectReasonError();
        void showEmailError();
        void showProductError();
        void hideProgressBar();
    }
    interface Presenter extends BasePresenter<View> {
        void initData(Context context);
        void saveLine(Line line);
        boolean validCode(String code);
        boolean validName (String name);
        boolean validOperators(ArrayList<EmployeePosition> positions);
        boolean validDowntime(Downtime downtime);
        boolean validReasons(ArrayList<Reason> scrapReasons);
        boolean validEmails(Line line);
        boolean validProduct(Line line);

    }


}
