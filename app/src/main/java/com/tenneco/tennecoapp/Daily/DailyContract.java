package com.tenneco.tennecoapp.Daily;

import android.app.AlertDialog;
import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.Model.WorkHour;

import java.util.ArrayList;

/**
 * Created by ghoss on 14/09/2018.
 */
public interface DailyContract {
    interface View extends BaseView<Presenter>{
        void getLine();
        void setLine();
        void getGroup();
        void getTeam();
        void showActualsDialog(WorkHour workHour, Line line,int position, Context context);
        void showTargetDialog(WorkHour workHour,Line line, int position,Context context);
        void showEndShiftDialog(Line line,int shift,Context context,boolean close);
        void updateLine(Line line);
        void hideLeakCounter();
        void setCount(int count);
        void showDowntimeDialog(Downtime downtime, Context context);
        void showScrapDialog(ArrayList<Reason> reasons, Context context);
        void showFTQ(int shift);
        void sendEmail(String[] Adresses,String[] CCs,String subject,String body);
        void showScrap();
        void hideScrap();
        void initAdapter();
        void showUserDialog(ArrayList<User> users,Context context,String title,int position);
        void showTeam();
        void hideTeam();
        void setTeam(String title);
        void showGroup();
        void hideGroup();
        void setGroup(String title);

    }
    interface Presenter extends BasePresenter<View>{
        void saveLine(Line line,ArrayList<WorkHour> hours, int position, String actual, String comment);
        void saveLine(Line line,ArrayList<WorkHour> hours, int position, String target);
        boolean validateActual(String actual);
        boolean validateComment(String comment, String actual, String target);
        int reportHour(ArrayList<WorkHour>workHours);
        void showCount(Line line);
        void incrementCount(Line line);
        void setDowntime(Line line,Downtime downtime);
        boolean setComment(WorkHour workHour, Downtime downtime, int shift);
        String convertHour(String hour, int shift);
        String downtime (String zone, String location , String reason);
        void verifyLeaks(Line line);
        String[] getEmails (ArrayList<Email> emails, Line line);
        String[] getCC (ArrayList<Email> emails, Line line);
        void setTeam(String teams);
        void setGroup(String group);
    }
}
