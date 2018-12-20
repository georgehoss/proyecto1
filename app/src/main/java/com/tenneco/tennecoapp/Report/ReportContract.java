package com.tenneco.tennecoapp.Report;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Product;
import com.tenneco.tennecoapp.Model.ReasonDelay;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.Model.WorkHour;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ghoss on 14/09/2018.
 */
public interface ReportContract {
    interface View {
        void getLines(String date);
        void setTitle(String title);
        void setDate(String date);
        void getEmails();
        void getTemplates();
        void initAdapter();
        void hideButton();
        void showButton();
        void sendEmailDialog(File imageFile);
        void showDatePickerDialog();
    }

}
