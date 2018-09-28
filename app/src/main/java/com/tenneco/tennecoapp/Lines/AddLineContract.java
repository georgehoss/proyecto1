package com.tenneco.tennecoapp.Lines;

import android.app.AlertDialog;
import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;

import java.util.ArrayList;

/**
 * Created by ghoss on 13/09/2018.
 */
public interface AddLineContract {
    interface View extends BaseView<Presenter> {
        void initAdapters();
        void hideshift1();
        void showshift1();
        void hideshift2();
        void showshift2();
        void hideshift3();
        void showshift3();
        void hidePosition();
        void showPosition();
        void hideAll();
        void showAll();
        void hideDowntime();
        void showDowntime();
        void hideScrap();
        void showScrap();
        void addPosition (EmployeePosition position);
        void deletePosition (EmployeePosition position);
        void saveLine(Line line);
        void getData();
        void setData(Line line);
        void showShiftDialog(Shift shift,int shiftNumber, Context context);
        void showDeleteDialog(Context context);
        void showExitDialog(Context context);
        void showPositionDialog(EmployeePosition employeePosition, Context context);
        void showDeletePosition(EmployeePosition employeePosition, Context context);
        void delete();
        void showNameError();
        void getEmployees();
        AlertDialog showDialogEmployee(ArrayList<Employee> employees, Context context,int shift);
        void showAddDowntimeDialog(Context context, Zone zone);
        void showAddEventDialog(Context context,int reason);


    }
    interface Presenter extends BasePresenter<View> {
        void initData(Context context);
        boolean validName(String name);
        void onShift1Click(int viewVisibility, int resultVisibility);
        void onShift2Click(int viewVisibility, int resultVisibility);
        void onShift3Click(int viewVisibility, int resultVisibility);
        void onPositionClick(int viewVisibility, int resultVisibility);
        void onDowntimeClick(int viewVisibility, int resultVisibility);
        void onScrapClick(int viewVisibility, int resultVisibility);
        void saveChanges(String name,String id, Shift first,Shift second,Shift third,ArrayList<EmployeePosition> positions,Downtime downtime, ArrayList<Reason> reasons,ArrayList<Employee> employees);
        Shift getshift(String sh1,String eh1,String t1,
                       String sh2,String eh2,String t2,
                       String sh3,String eh3,String t3,
                       String sh4,String eh4,String t4,
                       String sh5,String eh5,String t5,
                       String sh6,String eh6,String t6,
                       String sh7,String eh7,String t7,
                       String sh8,String eh8,String t8);
        void addPosition(String position);
        boolean validPosition(String position);
        ArrayList<Employee> verifyEmployees(ArrayList<Employee> shift, ArrayList<Employee> employees);
        ArrayList<Employee> getEmployees(ArrayList<Employee> shift1,ArrayList<Employee> shift2,ArrayList<Employee> shift3);
    }


}
