package com.tenneco.tennecoapp.Lines.Product;

import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;

import java.util.ArrayList;

/**
 * Created by ghoss on 09/12/2018.
 */
public class LineProductPresenter implements LineProductContract.Presenter {
    private LineProductContract.View mView;

    public LineProductPresenter(LineProductContract.View mView) {
        this.mView = mView;
    }

    @Override
    public Shift getshift(String sh1, String eh1, String t1, String sh2, String eh2, String t2, String sh3, String eh3, String t3, String sh4, String eh4, String t4, String sh5, String eh5, String t5, String sh6, String eh6, String t6, String sh7, String eh7, String t7, String sh8, String eh8, String t8) {
        ArrayList<WorkHour> hours = new ArrayList<>();
        String cp2 = String.valueOf(Integer.valueOf(t2)+Integer.valueOf(t1));
        String cp3 = String.valueOf(Integer.valueOf(t3)+Integer.valueOf(cp2));
        String cp4 = String.valueOf(Integer.valueOf(t4)+Integer.valueOf(cp3));
        String cp5 = String.valueOf(Integer.valueOf(t5)+Integer.valueOf(cp4));
        String cp6 = String.valueOf(Integer.valueOf(t6)+Integer.valueOf(cp5));
        String cp7 = String.valueOf(Integer.valueOf(t7)+Integer.valueOf(cp6));
        String cp8 = String.valueOf(Integer.valueOf(t8)+Integer.valueOf(cp7));
        hours.add(new WorkHour(sh1, eh1,t1,t1));
        hours.add(new WorkHour(sh2, eh2,t2,cp2));
        hours.add(new WorkHour(sh3, eh3,t3,cp3));
        hours.add(new WorkHour(sh4, eh4,t4,cp4));
        hours.add(new WorkHour(sh5, eh5,t5,cp5));
        hours.add(new WorkHour(sh6, eh6,t6,cp6));
        hours.add(new WorkHour(sh7, eh7,t7,cp7));
        hours.add(new WorkHour(sh8, eh8,t8,cp8));
        Shift shift = new Shift();
        shift.setHours(hours);
        shift.setCumulativePlanned(cp8);
        return shift;
    }

    @Override
    public void bindView(LineProductContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}
