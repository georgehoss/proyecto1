package com.tenneco.tennecoapp.Lines;

import android.content.Context;

import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

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
    public void initData(Context context) {

        ArrayList<WorkHour> hours1 = new ArrayList<>();
        hours1.add(new WorkHour(context.getString(R.string.add_06_30),context.getString(R.string.add_07_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_29)));
        hours1.add(new WorkHour(context.getString(R.string.add_07_30),context.getString(R.string.add_08_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_68)));
        hours1.add(new WorkHour(context.getString(R.string.add_08_30),context.getString(R.string.add_09_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_97)));
        hours1.add(new WorkHour(context.getString(R.string.add_09_30),context.getString(R.string.add_10_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_136)));
        hours1.add(new WorkHour(context.getString(R.string.add_10_30),context.getString(R.string.add_11_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_175)));
        hours1.add(new WorkHour(context.getString(R.string.add_11_30),context.getString(R.string.add_12_30),context.getString(R.string.add_t_20),context.getString(R.string.add_t_195)));
        hours1.add(new WorkHour(context.getString(R.string.add_12_30),context.getString(R.string.add_01_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_234)));
        hours1.add(new WorkHour(context.getString(R.string.add_01_30),context.getString(R.string.add_02_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_273)));
        ArrayList<WorkHour> hours2 = new ArrayList<>();
        hours2.add(new WorkHour(context.getString(R.string.add_02_30),context.getString(R.string.add_03_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_29)));
        hours2.add(new WorkHour(context.getString(R.string.add_03_30),context.getString(R.string.add_04_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_68)));
        hours2.add(new WorkHour(context.getString(R.string.add_04_30),context.getString(R.string.add_05_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_97)));
        hours2.add(new WorkHour(context.getString(R.string.add_05_30),context.getString(R.string.add_06_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_136)));
        hours2.add(new WorkHour(context.getString(R.string.add_06_30),context.getString(R.string.add_07_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_175)));
        hours2.add(new WorkHour(context.getString(R.string.add_07_30),context.getString(R.string.add_08_30),context.getString(R.string.add_t_20),context.getString(R.string.add_t_195)));
        hours2.add(new WorkHour(context.getString(R.string.add_08_30),context.getString(R.string.add_09_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_234)));
        hours2.add(new WorkHour(context.getString(R.string.add_09_30),context.getString(R.string.add_10_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_273)));
        ArrayList<WorkHour> hours3 = new ArrayList<>();
        hours3.add(new WorkHour(context.getString(R.string.add_10_30),context.getString(R.string.add_11_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_29)));
        hours3.add(new WorkHour(context.getString(R.string.add_11_30),context.getString(R.string.add_12_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_68)));
        hours3.add(new WorkHour(context.getString(R.string.add_12_30),context.getString(R.string.add_01_30),context.getString(R.string.add_t_29),context.getString(R.string.add_t_97)));
        hours3.add(new WorkHour(context.getString(R.string.add_01_30),context.getString(R.string.add_02_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_136)));
        hours3.add(new WorkHour(context.getString(R.string.add_02_30),context.getString(R.string.add_03_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_175)));
        hours3.add(new WorkHour(context.getString(R.string.add_03_30),context.getString(R.string.add_04_30),context.getString(R.string.add_t_20),context.getString(R.string.add_t_195)));
        hours3.add(new WorkHour(context.getString(R.string.add_04_30),context.getString(R.string.add_05_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_234)));
        hours3.add(new WorkHour(context.getString(R.string.add_05_30),context.getString(R.string.add_06_30),context.getString(R.string.add_t_39),context.getString(R.string.add_t_273)));
        mView.setData(new Shift(hours1,context.getString(R.string.add_1st_shift),context.getString(R.string.add_t_273)),
                new Shift(hours2,context.getString(R.string.add_2nd_shift),context.getString(R.string.add_t_273)),
                new Shift(hours3,context.getString(R.string.add_3rd_shift),context.getString(R.string.add_t_273)));
    }

    @Override
    public boolean validName(String name) {
        return !name.isEmpty();
    }

}
