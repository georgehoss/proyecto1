package com.tenneco.tennecoapp.Daily;

import android.content.Intent;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.Utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

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

        if (cA1!=0) {
            line.getFirst().setCumulativeActual(String.valueOf(cA1));
            if (line.getFirst().getLfCounter()>=(0.10*cA1)&& !line.getFirst().isClosed())
                line.getFirst().setLeakReached(true);
        }
        else {
            line.getFirst().setCumulativeActual("");
        }
        if (cA2!=0) {
            line.getSecond().setCumulativeActual(String.valueOf(cA2));
            if (line.getSecond().getLfCounter()>=(0.10*cA2) && !line.getSecond().isClosed())
                line.getSecond().setLeakReached(true);
        }
        else
            line.getSecond().setCumulativeActual("");
        if (cA3!=0) {
            line.getThird().setCumulativeActual(String.valueOf(cA3));
            if (line.getThird().getLfCounter()>=(0.10*cA3) && !line.getThird().isClosed())
                line.getThird().setLeakReached(true);
        }
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
        int counter = 0;

        if ((!line.getFirst().isClosed()) || (!line.getSecond().isClosed()) || (!line.getThird().isClosed())) {
            if (!line.getFirst().isClosed()) {
                line.getFirst().setLfCounter(line.getFirst().getLfCounter() + 1);
                if (line.getFirst().getCumulativeActual()!=null && !line.getFirst().getCumulativeActual().isEmpty()) {
                    counter = Integer.valueOf(line.getFirst().getCumulativeActual());
                    if (line.getFirst().getLfCounter() >= (0.10 * counter))
                        line.getFirst().setLeakReached(true);
                }
            }
                else if (!line.getSecond().isClosed()) {
                line.getSecond().setLfCounter(line.getSecond().getLfCounter() + 1);
                if (line.getSecond().getCumulativeActual()!=null && !line.getSecond().getCumulativeActual().isEmpty()){
                    counter = Integer.valueOf(line.getSecond().getCumulativeActual());
                    if (line.getSecond().getLfCounter()>= (0.10 * counter))
                    line.getSecond().setLeakReached(true);}
            }
                else if (!line.getThird().isClosed()) {
                line.getThird().setLfCounter(line.getThird().getLfCounter() + 1);
                if (line.getThird().getCumulativeActual()!=null && !line.getThird().getCumulativeActual().isEmpty()){
                    counter = Integer.valueOf(line.getThird().getCumulativeActual());
                    if (line.getThird().getLfCounter()>= (0.10 * counter))
                    line.getThird().setLeakReached(true);
                }
            }


            mView.updateLine(line);
            showCount(line);

        }

    }

    @Override
    public void setDowntime(Line line, Downtime downtime) {

        for (WorkHour workHour : line.getFirst().getHours())
            if (setComment(workHour,downtime,1))
                if (workHour.getComments()==null || workHour.getComments().isEmpty())                 {
                    workHour.setComments(downtime.getDowntime() +".\n  Start:"+downtime.getStartTime() + " End:"+downtime.getEndTime());
                }
                else{
                    workHour.setComments(workHour.getComments() + "\n" + downtime.getDowntime() + ".\n  Start:"+downtime.getStartTime() + " End:"+downtime.getEndTime());
                }




        for (WorkHour workHour : line.getSecond().getHours())
            if (setComment(workHour,downtime,2))
                if (workHour.getComments()==null || workHour.getComments().isEmpty())
                {
                    workHour.setComments(downtime.getDowntime() +".\n  Start:"+downtime.getStartTime() + " End:"+downtime.getEndTime());
                }
                else{
                    workHour.setComments(workHour.getComments() + "\n" + downtime.getDowntime() + ".\n  Start:"+downtime.getStartTime() + " End:"+downtime.getEndTime());
                }

        for (WorkHour workHour : line.getThird().getHours())
            if (setComment(workHour,downtime,3))
                if (workHour.getComments()==null || workHour.getComments().isEmpty())
                {
                    workHour.setComments(downtime.getDowntime() +".\n  Start:"+downtime.getStartTime() + " End:"+downtime.getEndTime());
                }
                else{
                    workHour.setComments(workHour.getComments() + "\n" + downtime.getDowntime() + ".\n  Start:"+downtime.getStartTime() + " End:"+downtime.getEndTime());
                }

        line.setDowntime(downtime);

        mView.updateLine(line);

    }

    @Override
    public boolean setComment(WorkHour workHour, Downtime downtime, int shift) {

        String startHour = convertHour(workHour.getStartHour(),shift);

        startHour = Utils.converTimeString(startHour);


        String[] parts = startHour.split(":");
        Calendar starth = Calendar.getInstance();
        starth.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        starth.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        starth.set(Calendar.SECOND, Integer.parseInt(parts[2]));

        String endHour = convertHour(workHour.getEndHour(),shift);
        endHour = Utils.converTimeString(endHour);

        String[] partsEnd = endHour.split(":");
        Calendar endh = Calendar.getInstance();
        endh.set(Calendar.HOUR_OF_DAY, Integer.parseInt(partsEnd[0]));
        endh.set(Calendar.MINUTE, Integer.parseInt(partsEnd[1]));
        endh.set(Calendar.SECOND, Integer.parseInt(partsEnd[2]));


        String startDowntime = Utils.converTimeString(downtime.getStartTime());

        String[] partsSd = startDowntime.split(":");
        Calendar starthdowntime = Calendar.getInstance();
        starthdowntime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(partsSd[0]));
        starthdowntime.set(Calendar.MINUTE, Integer.parseInt(partsSd[1]));
        starthdowntime.set(Calendar.SECOND, Integer.parseInt(partsSd[2]));

        String endDowntime = Utils.converTimeString(downtime.getEndTime());

        String[] partsEndD = endDowntime.split(":");
        Calendar endhdowntime = Calendar.getInstance();
        endhdowntime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(partsEndD[0]));
        endhdowntime.set(Calendar.MINUTE, Integer.parseInt(partsEndD[1]));
        endhdowntime.set(Calendar.SECOND, Integer.parseInt(partsEndD[2]));


        if (endhdowntime.before(starthdowntime)) {
            endhdowntime.add(Calendar.DATE, 1);
        }

        if (shift==3)
        {
            String mid = "00:00:00";
            String[] split = mid.split(":");
            Calendar middnight = Calendar.getInstance();
            middnight.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
            middnight.set(Calendar.MINUTE, Integer.parseInt(split[1]));
            middnight.set(Calendar.SECOND, Integer.parseInt(split[2]));

            if (starth.after(middnight))
                starth.add(Calendar.DATE,1);

            if (endh.after(middnight))
                endh.add(Calendar.DATE,1);
        }

        return (starthdowntime.after(starth) && starthdowntime.before(endh)) || (endhdowntime.after(starth) && endhdowntime.before(endh));


    }

    @Override
    public String convertHour(String hour, int shift) {

        if ((shift==1 && (hour.equals("12:30")||hour.equals("01:30") ||hour.equals("02:30")))
                ||(shift ==2)
                || (shift == 3 && (hour.equals("10:30")||hour.equals("11:30")))
                ) return hour + ":00 PM";

            return hour + ":00 AM";
    }

    @Override
    public String downtime(String zone, String location, String reason) {
        String zon ="";
        String loc = "";
        String reas = "";

        if (zone!=null)
            zon = zone;

        if (location !=null)
            loc = location;

        if (reason !=null)
            reas = reason;

        return "DOWNTIME due to: " + zon + ", " + loc + ", " + reas;
    }

    @Override
    public void verifyLeaks(Line line) {
        if (line.getFirst().isLeakReached() &&
                line.getFirst().getHours().get(0).getActuals()!=null &&
                !line.getFirst().getHours().get(0).getActuals().isEmpty()
                )
            mView.showFTQ(1);
        else
        if (line.getSecond().isLeakReached()&&
                line.getSecond().getHours().get(0).getActuals()!=null &&
                !line.getSecond().getHours().get(0).getActuals().isEmpty())
            mView.showFTQ(2);
        else
        if (line.getThird().isLeakReached()&&
                line.getSecond().getHours().get(0).getActuals()!=null &&
                !line.getSecond().getHours().get(0).getActuals().isEmpty())
            mView.showFTQ(3);
    }

    @Override
    public String[] getEmails(ArrayList<Email> emails, Line line) {
        ArrayList<Email> list = new ArrayList<>();
        if (!line.getFirst().isClosed())
        {
            for (Email email : emails)
                if (email.isShift1())
                list.add(new Email(email));

        }
        else
        if (!line.getSecond().isClosed())
        {
            for (Email email : emails)
                if (email.isShift2())
                    list.add(new Email(email));
        }
        else
        {
            for (Email email : emails)
                if (email.isShift3())
                    list.add(new Email(email));
        }
        String [] em = new String[list.size()];
        for (int i=0; i<list.size(); i++)
            em[i]= list.get(i).toString();
        return em ;
    }

    @Override
    public String[] getCC(ArrayList<Email> emails, Line line) {
        ArrayList<Email> list = new ArrayList<>();
        if (!line.getFirst().isClosed())
        {
            for (Email email : emails)
                if (email.isCc1())
                    list.add(new Email(email));

        }
        else
        if (!line.getSecond().isClosed())
        {
            for (Email email : emails)
                if (email.isCc2())
                    list.add(new Email(email));
        }
        else
        {
            for (Email email : emails)
                if (email.isCc3())
                    list.add(new Email(email));
        }

        String [] em = new String[list.size()];
        for (int i=0; i<list.size(); i++)
            em[i]= list.get(i).toString();
        return em ;
    }

    @Override
    public void setTeam(String teams) {
        if (teams==null || teams.isEmpty())
            mView.hideTeam();
        else
        {
            mView.showTeam();
            mView.setTeam(teams);
        }
    }

    @Override
    public void setGroup(String group) {
        if (group==null || group.isEmpty())
            mView.hideGroup();
        else
        {
            mView.showGroup();
            mView.setGroup(group);
        }
    }


}
