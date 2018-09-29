package com.tenneco.tennecoapp.Daily;

import android.app.AlertDialog;
import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
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
        void showEndShiftDialog(Line line,int shift,Context context,boolean close);
        void updateLine(Line line);
        void hideLeakCounter();
        void setCount(int count);
        void showDowntimeDialog(Downtime downtime, Context context);
        void showScrapDialog(ArrayList<Reason> reasons, Context context);

    }
    interface Presenter extends BasePresenter<View>{
        void saveLine(Line line,ArrayList<WorkHour> hours, int position, String actual, String comment);
        boolean validateActual(String actual);
        boolean validateComment(String comment, String actual, String target);
        int reportHour(ArrayList<WorkHour>workHours);
        void showCount(Line line);
        void incrementCount(Line line);


    }
}
