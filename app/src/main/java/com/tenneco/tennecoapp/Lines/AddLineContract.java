package com.tenneco.tennecoapp.Lines;

import android.content.Context;

import com.tenneco.tennecoapp.BasePresenter;
import com.tenneco.tennecoapp.BaseView;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;

/**
 * Created by ghoss on 13/09/2018.
 */
public interface AddLineContract {
    interface View extends BaseView<Presenter> {
        void hideshift1();
        void showshift1();
        void hideshift2();
        void showshift2();
        void hideshift3();
        void showshift3();
        void saveLine(Line line);
        void getData();
        void setData(Shift s1,Shift s2, Shift s3);
        void showShiftDialog(Shift shift,int shiftNumber, Context context);
        void showDeleteDialog(Context context);
        void showExitDialog(Context context);
        void delete();
        void showNameError();
    }
    interface Presenter extends BasePresenter<View> {
        void initData(Context context);
        boolean validName(String name);
        void onShift1Click(int viewVisibility, int resultVisibility);
        void onShift2Click(int viewVisibility, int resultVisibility);
        void onShift3Click(int viewVisibility, int resultVisibility);
        void saveChanges(String name,String id, Shift first,Shift second,Shift third);
        Shift getshift(String sh1,String eh1,String t1,
                       String sh2,String eh2,String t2,
                       String sh3,String eh3,String t3,
                       String sh4,String eh4,String t4,
                       String sh5,String eh5,String t5,
                       String sh6,String eh6,String t6,
                       String sh7,String eh7,String t7,
                       String sh8,String eh8,String t8);

    }


}
