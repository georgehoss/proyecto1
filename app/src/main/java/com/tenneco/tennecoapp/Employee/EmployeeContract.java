package com.tenneco.tennecoapp.Employee;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Employee;

/**
 * Created by ghoss on 21/09/2018.
 */
public interface EmployeeContract {
    interface View extends BaseView<Preseneter>{
        void hideProgressBar();
        void showFloatingButton();
        void getEmployees();
        void addEditDialog(Employee employee, Context context);
        void editDeleteDialog(Employee employee);
        void delete(String id);
        void addEditEmployee(Employee employee);
        void deleteDialog(Employee employee);

    }
    interface Preseneter extends BasePresenter<View>{

    }
}
