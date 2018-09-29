package com.tenneco.tennecoapp.Daily;

import android.content.Intent;

import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;

import java.util.ArrayList;

/**
 * Created by ghoss on 14/09/2018.
 */
public class DailyPresenter implements DailyContract.Presenter {

    private DailyContract.View mView;

    DailyPresenter(DailyContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void bindView(DailyContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }

    @Override
    public void saveLine(Line line, ArrayList<WorkHour> hours, int position, String actual, String comment) {


        hours.get(position).setActuals(actual);
        hours.get(position).setComments(comment);
        line.getFirst().getHours().clear();
        line.getSecond().getHours().clear();
        line.getThird().getHours().clear();
        int cA1=0;
        int cA2=0;
        int cA3=0;
        for (int j=0;j<=23;j++)
        {
            int cA=0;
            if (hours.get(j).getActuals()!=null
                    && !hours.get(j).getActuals().isEmpty()){
                if (j!=0 && j!=8 && j!=16 &&
                        hours.get(j-1).getCumulativeActual()!=null &&
                        !hours.get(j-1).getCumulativeActual().isEmpty())
                {
                    cA = Integer.valueOf(hours.get(j).getActuals()) + Integer.valueOf(hours.get(j - 1).getCumulativeActual());
                    hours.get(j).setCumulativeActual(String.valueOf(cA));
                }
                else
                if ( j==0 || j==8 ||j==16)
                {
                    hours.get(j).setCumulativeActual(hours.get(j).getActuals());
                }

                if (j<=7)
                {
                    cA1 = cA1 + Integer.valueOf(hours.get(j).getActuals());
                }
                else
                if (j<=15)
                {
                    cA2 = cA2 + Integer.valueOf(hours.get(j).getActuals());
                }
                else
                {
                    cA3 = cA3 + Integer.valueOf(hours.get(j).getActuals());
                }

            }
        }



        for (int i=0; i<=23; i++)
        {
            if (i<=7) {
                line.getFirst().getHours().add(hours.get(i));

            }
            else
            if (i<16)
                line.getSecond().getHours().add(hours.get(i));
            else
            {
                line.getThird().getHours().add(hours.get(i));
            }

        }

        if (cA1!=0)
        line.getFirst().setCumulativeActual(String.valueOf(cA1));
        else
            line.getFirst().setCumulativeActual("");
        if (cA2!=0)
            line.getSecond().setCumulativeActual(String.valueOf(cA2));
        else
            line.getSecond().setCumulativeActual("");
        if (cA3!=0)
            line.getThird().setCumulativeActual(String.valueOf(cA3));
        else
            line.getThird().setCumulativeActual("");


        mView.updateLine(line);

    }

    @Override
    public boolean validateActual(String actual) {
        return actual.isEmpty();
    }

    @Override
    public boolean validateComment(String comment, String actual, String target) {
        int act,targ;
        act = Integer.valueOf(actual);
        targ = Integer.valueOf(target);

        if (act<targ && comment.isEmpty())
            return true;
        else
            return false;
    }

    @Override
    public int reportHour(ArrayList<WorkHour> workHours) {
        for (int i=0;i<=23;i++)
        if ((workHours.get(i).getActuals()==null || workHours.get(i).getActuals().isEmpty()) && !workHours.get(i).isClosed())
            return i;

        return 24;
    }

    @Override
    public void showCount(Line line) {
        if (!line.getFirst().isClosed())
        {
            mView.setCount(line.getFirst().getLfCounter());
        }
        else
        if (!line.getSecond().isClosed())
        {
            mView.setCount(line.getSecond().getLfCounter());
        }
        else
        if (!line.getThird().isClosed())
        {
            mView.setCount(line.getThird().getLfCounter());
        }
        else
            mView.hideLeakCounter();
    }

    @Override
    public void incrementCount(Line line) {


        if ((!line.getFirst().isClosed()) || (!line.getSecond().isClosed()) || (!line.getThird().isClosed())) {
            if (!line.getFirst().isClosed())
                line.getFirst().setLfCounter(line.getFirst().getLfCounter() + 1);
                else if (!line.getSecond().isClosed())
                line.getSecond().setLfCounter(line.getSecond().getLfCounter() + 1);
                else if (!line.getThird().isClosed())
                line.getThird().setLfCounter(line.getThird().getLfCounter() + 1);


            mView.updateLine(line);
            showCount(line);

        }

    }


}
