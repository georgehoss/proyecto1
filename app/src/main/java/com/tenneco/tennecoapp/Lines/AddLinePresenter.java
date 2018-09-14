package com.tenneco.tennecoapp.Lines;

import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;

import java.util.ArrayList;

/**
 * Created by ghoss on 13/09/2018.
 */
public class AddLinePresenter implements AddLineContract.Presenter {
    private AddLineContract.View mView;

    AddLinePresenter(AddLineContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void bindView(AddLineContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }

    @Override
    public void onShift1Click(int viewVisibility, int resultVisibility) {
        if (viewVisibility == resultVisibility)
            mView.hideshift1();
        else
            mView.showshift1();

    }

    @Override
    public void onShift2Click(int viewVisibility, int resultVisibility) {
        if (viewVisibility == resultVisibility)
            mView.hideshift2();
        else
            mView.showshift2();
    }

    @Override
    public void onShift3Click(int viewVisibility, int resultVisibility) {
        if (viewVisibility == resultVisibility)
            mView.hideshift3();
        else
            mView.showshift3();
    }

    @Override
    public void saveChanges(String name, String id, Shift first, Shift second, Shift third) {
        Line line = new Line(name,id,first,second,third);
        mView.saveLine(line);

    }

    @Override
    public Shift getshift(String sh1, String eh1, String t1, String cp1, String sh2, String eh2, String t2, String cp2, String sh3, String eh3, String t3, String cp3, String sh4, String eh4, String t4, String cp4, String sh5, String eh5, String t5, String cp5, String sh6, String eh6, String t6, String cp6, String sh7, String eh7, String t7, String cp7, String sh8, String eh8, String t8, String cp8) {
        ArrayList<WorkHour> hours = new ArrayList<>();
        hours.add(new WorkHour(sh1, eh1,t1,cp1));
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
}
