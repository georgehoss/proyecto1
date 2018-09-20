package com.tenneco.tennecoapp.Daily;

import android.app.AlertDialog;
import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.WorkHour;

import java.util.ArrayList;

/**
 * Created by ghoss on 14/09/2018.
 */
public interface DailyContract {
    interface View extends BaseView<Presenter>{
        void getLine();
        void setLine();
        void showActualsDialog(WorkHour workHour, Line line,int position, Context context);
        void updateLine(Line line);
    }
    interface Presenter extends BasePresenter<View>{
        void saveLine(Line line,ArrayList<WorkHour> hours, int position, String actual, String comment);
        boolean validateActual(String actual);
        boolean validateComment(String comment, String actual, String target);
        int reportHour(ArrayList<WorkHour>workHours);

    }
}
