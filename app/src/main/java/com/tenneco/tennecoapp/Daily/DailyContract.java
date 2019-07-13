package com.tenneco.tennecoapp.Daily;

import android.app.AlertDialog;
import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.DateShift;
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
        void saveShift(int shiftId, DateShift dateShift);
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
        void showUserDialog(ArrayList<User> users,Context context,int position,int shiftt);
        void showTeam();
        void hideTeam();
        void setDtS1(String time);
        void setDtS2(String time);
        void setDtS3(String time);
        void setTeam1(String title);
        void setTeam2(String title);
        void setTeam3(String title);
        void showGroup();
        void hideGroup();
        void setGroup1(String title);
        void setGroup2(String title);
        void setGroup3(String title);
        void showOperatorsl();
        void hideOperators();
        void setOperators(String operators);
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
        int reportHour(ArrayList<WorkHour>workHours,int turn);
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
        void setOperators(Line line);
        boolean getGroups(Line line, int shift);
        boolean getTeam(Line line, int shift);
        String lineInformation(Line line);
        String shiftInformation(Line line,int position);
        void createCVS(Context context,Line line) throws IOException;
        void createCSVShift(Context context, Line line, int shift, Shift actualShift);
        boolean validateUser(FirebaseUser user, ArrayList<User>Team, ArrayList<User>Group, String psw);
        String getSignature(FirebaseUser user, ArrayList<User>Team, ArrayList<User>Group);
        void validateFQT(Line line);
        void setDowntimes(Line line);
        void validateShift (String dateLine, String datePLine, Shift shift, int shiftId, String code);
    }
}
