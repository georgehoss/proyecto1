package com.tenneco.tennecoapp.Daily;

import android.app.AlertDialog;
import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Product;
import com.tenneco.tennecoapp.Model.ReasonDelay;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.Model.WorkHour;

import java.io.IOException;
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
        void getNumbers();
        void getOperators();
        void getReasons();
        void getTemplates();
        void showActualsDialog(WorkHour workHour, Line line,int position, Context context);
        void showTargetDialog(WorkHour workHour,Line line, int position,Context context);
        void showOwnerDialog(WorkHour workHour, Line line,int position, Context context);
        void showEndShiftDialog(Line line,int shift,Context context,boolean close);
        void updateLine(Line line);
        void hideLeakCounter();
        void setCount(int count);
        void showDowntimeDialog(Downtime downtime, Context context);
        void showRejectDialog(ArrayList<Reason> reasons, Context context);
        void showFTQ(int shift);
        void sendEmail(String[] Adresses,String[] CCs,String subject,String body,int shift);
        void showReject();
        void hideReject();
        void initAdapter();
        void showUserDialog(ArrayList<User> users,Context context,int position);
        void showTeam();
        void hideTeam();
        void setTeam(String title);
        void showGroup();
        void hideGroup();
        void setGroup(String title);
        void showCellEmailDialog();
        void sendCellEmail();
        void longClicks();
        void showDialogClose(int position);
        void showDialogEndShift(int position);
        void sendShiftEmail(int position);
        void getPLine();
        void saveTl(String tl,int shift);
        void saveGl(String gl,int shift);
        void saveProduct(Product lastProduct);
        void sendSms(String number,String message);
        void showSendMsgButton();
        void showSendMsgDialog(Context context);
        void showProductListDialog(ArrayList<Product> products,Context context);
        void setLeakReached(int shift);


    }
    interface Presenter extends BasePresenter<View>{
        void saveLine(Line line,ArrayList<WorkHour> hours, int position, String actual, String comment,ReasonDelay reasonDelay,String owner,String ftq);
        void saveLine(Line line,ArrayList<WorkHour> hours, int position, String target);
        void setProduct(Line line,Product product);
        boolean validateActual(String actual);
        boolean validateComment(String comment, String actual, String target);
        boolean validateReason(ReasonDelay reasonDelay, String actual, String target);
        boolean validateReasonSelection(String actual,String target,String reason, String detail);
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
        void setTeam(Line line);
        void setGroup(Line line);
        boolean getGroups(Line line);
        boolean getTeam(Line line);
        String lineInformation(Line line);
        String shiftInformation(Line line,int position);
        void createCVS(Context context,Line line) throws IOException;
        void createCSVShift(Context context, Line line, int shift, Shift actualShift);
        boolean validateUser(FirebaseUser user, ArrayList<User>Team, ArrayList<User>Group);
        void validateFQT(Line line);
    }
}
