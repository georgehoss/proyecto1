package com.tenneco.tennecoapp.Lines;

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
    }
    interface Presenter extends BasePresenter<View> {
        void onShift1Click(int viewVisibility, int resultVisibility);
        void onShift2Click(int viewVisibility, int resultVisibility);
        void onShift3Click(int viewVisibility, int resultVisibility);
        void saveChanges(String name,String id, Shift first,Shift second,Shift third);
        Shift getshift(String sh1,String eh1,String t1, String cp1,
                       String sh2,String eh2,String t2, String cp2,
                       String sh3,String eh3,String t3, String cp3,
                       String sh4,String eh4,String t4, String cp4,
                       String sh5,String eh5,String t5, String cp5,
                       String sh6,String eh6,String t6, String cp6,
                       String sh7,String eh7,String t7, String cp7,
                       String sh8,String eh8,String t8, String cp8);

    }
}
