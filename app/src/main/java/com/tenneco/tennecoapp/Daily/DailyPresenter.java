package com.tenneco.tennecoapp.Daily;

import android.content.Context;
import android.os.Environment;

import com.google.firebase.auth.FirebaseUser;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Product;
import com.tenneco.tennecoapp.Model.Production;
import com.tenneco.tennecoapp.Model.ReasonDelay;
import com.tenneco.tennecoapp.Model.Reject;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.User;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.Utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public void saveLine(Line line, ArrayList<WorkHour> hours, int position, String actual, String comment,ReasonDelay reasonDelay,String owner,String ftq) {


        hours.get(position).setActuals(actual);
        hours.get(position).setComments(comment);
        if (reasonDelay!=null)
            hours.get(position).setReasonDelay(reasonDelay);
        if (owner!=null)
            hours.get(position).setOwner(owner);
        if (ftq!=null)
            hours.get(position).setLeak(ftq);

        line.getFirst().getHours().clear();
        line.getSecond().getHours().clear();
        line.getThird().getHours().clear();
        int cA1=0;
        int cA2=0;
        int cA3=0;
        int cL1=0;
        int cL2=0;
        int cL3=0;
        int shift;
        int pos;

        if (position<=7) {
            shift = 1;
            pos = position;
        }
        else
        if (position<=15) {
            shift = 2;
            pos = position-8;
        }
        else {
            shift = 3;
            pos = position-16;
        }


        for (int j=0;j<=23;j++)
        {
            int cA=0;
            if (hours.get(j).getActuals()!=null
                    && !hours.get(j).getActuals().isEmpty()){

                if (hours.get(j).getLeak()!=null && hours.get(j).getLeak().isEmpty())
                    hours.get(j).setLeak("0");
                else
                if (hours.get(j).getLeak()==null)
                    hours.get(j).setLeak("0");

                if (j!=0 && j!=8 && j!=16 &&
                        hours.get(j-1).getCumulativeActual()!=null &&
                        !hours.get(j-1).getCumulativeActual().isEmpty())
                {
                    cA = Integer.valueOf(hours.get(j).getActuals().trim()) + Integer.valueOf(hours.get(j - 1).getCumulativeActual().trim());
                    hours.get(j).setCumulativeActual(String.valueOf(cA));
                }
                else
                if ( j==0 || j==8 ||j==16)
                {
                    hours.get(j).setCumulativeActual(hours.get(j).getActuals().trim());
                }

                if (j<=7)
                {
                    cA1 = cA1 + Integer.valueOf(hours.get(j).getActuals().trim());
                    cL1 = cL1 + Integer.valueOf(hours.get(j).getLeak().trim());
                }
                else
                if (j<=15)
                {
                    cA2 = cA2 + Integer.valueOf(hours.get(j).getActuals().trim());
                    cL2 = cL2 + Integer.valueOf(hours.get(j).getLeak().trim());
                }
                else
                {
                    cA3 = cA3 + Integer.valueOf(hours.get(j).getActuals().trim());
                    cL3 = cL3 + Integer.valueOf(hours.get(j).getLeak().trim());
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

        line.getFirst().setCumulativeFTQ(String.valueOf(cL1));
        line.getSecond().setCumulativeFTQ(String.valueOf(cL2));
        line.getThird().setCumulativeFTQ(String.valueOf(cL3));
        line.getFirst().setLfCounter(cL1);
        line.getFirst().setLfCounter(cL2);
        line.getFirst().setLfCounter(cL3);

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


        if (line.getOperators()==null)
            line.setOperators(new ArrayList<Employee>());

        Shift shift1 = new Shift();
        if (shift==1)
            shift1 = line.getFirst();
        if (shift==2)
            shift1 = line.getSecond();
        if (shift==3)
            shift1 = line.getThird();

        ArrayList<Employee> operatos = new ArrayList<>();

        ArrayList<Production> productions = new ArrayList<>();
        String productid="0",productName="";
        if (line.getLastProduct()!=null) {
            productid = line.getLastProduct().getId();
            productName = line.getLastProduct().getName();
        }

        productions.add(new Production(productid,productName,Integer.valueOf(actual)));
        for (EmployeePosition e : shift1.getPositions())
            if (e.getOperatorId() != null)
                operatos.add(new Employee(e.getOperatorId(),e.getOperator(),shift,productions,e.getName()));


        ArrayList<Employee> operators = new ArrayList<>(line.getOperators());

        if (operators.size()>0) {

            for (Employee act : operatos) {
                boolean exist=false;
                for (Employee old : operators) {
                    if (old.getId().equals(act.getId())) {
                        exist = true;
                        boolean isp = false;
                        for (Production np : act.getProductions()) {
                            for (Production op : old.getProductions()) {
                                if (np.getProductId().equals(op.getProductId())) {
                                    op.setTotal(op.getTotal() + np.getTotal());
                                    isp = true;
                                }
                            }
                            if (!isp)
                                old.getProductions().add(np);
                        }

                    }
                }
                if (!exist)
                    operators.add(act);
            }

            line.setOperators(operators);
        }
        else
            line.setOperators(operatos);


        mView.updateLine(line);

    }

    @Override
    public void saveLine(Line line, ArrayList<WorkHour> hours, int position, String target) {
        hours.get(position).setTarget(target);

        line.getFirst().getHours().clear();
        line.getSecond().getHours().clear();
        line.getThird().getHours().clear();
        int cA1=0;
        int cA2=0;
        int cA3=0;
        for (int j=0;j<=23;j++)
        {
            int cA=0;
            if (hours.get(j).getTarget()!=null
                    && !hours.get(j).getTarget().isEmpty()){
                if (j!=0 && j!=8 && j!=16 &&
                        hours.get(j-1).getCumulativePlanned()!=null &&
                        !hours.get(j-1).getCumulativePlanned().isEmpty())
                {
                    cA = Integer.valueOf(hours.get(j).getTarget()) + Integer.valueOf(hours.get(j - 1).getCumulativePlanned());
                    hours.get(j).setCumulativePlanned(String.valueOf(cA));
                }
                else
                if ( j==0 || j==8 ||j==16)
                {
                    hours.get(j).setCumulativePlanned(hours.get(j).getTarget());
                }

                if (j<=7)
                {
                    cA1 = cA1 + Integer.valueOf(hours.get(j).getTarget());
                }
                else
                if (j<=15)
                {
                    cA2 = cA2 + Integer.valueOf(hours.get(j).getTarget());
                }
                else
                {
                    cA3 = cA3 + Integer.valueOf(hours.get(j).getTarget());
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
            line.getFirst().setCumulativePlanned(String.valueOf(cA1));
        }
        else {
            line.getFirst().setCumulativePlanned("");
        }

        if (cA2!=0) {
            line.getSecond().setCumulativePlanned(String.valueOf(cA2));
        }
        else
            line.getSecond().setCumulativePlanned("");

        if (cA3!=0) {
            line.getThird().setCumulativePlanned(String.valueOf(cA3));
        }
        else
            line.getThird().setCumulativePlanned("");


        mView.updateLine(line);

    }

    @Override
    public void setProduct(Line line, Product product) {

        Product newP = new Product();
        newP.setCode(product.getCode());
        newP.setName(product.getName());
        newP.setId(product.getId());

        int cA=0;
        int cA2=0;
        int cA3=0;
        for (int i=0;i<=7;i++) {
            if (!line.getFirst().getHours().get(i).isClosed() &&(
                    line.getFirst().getHours().get(i).getActuals()==null
                            ||line.getFirst().getHours().get(i).getActuals().isEmpty())) {
                line.getFirst().getHours().get(i).setTarget(product.getFirst().getHours().get(i).getTarget());
                line.getFirst().getHours().get(i).setStartHour(product.getFirst().getHours().get(i).getStartHour());
                line.getFirst().getHours().get(i).setEndHour(product.getFirst().getHours().get(i).getEndHour());
                line.getFirst().getHours().get(i).setProduct(newP);

            }

            if (i!=0 &&
                    line.getFirst().getHours().get(i-1).getCumulativePlanned()!=null &&
                    !line.getFirst().getHours().get(i-1).getCumulativePlanned().isEmpty())
            {
                cA = Integer.valueOf(line.getFirst().getHours().get(i).getTarget()) + Integer.valueOf(line.getFirst().getHours().get(i-1).getCumulativePlanned());
                line.getFirst().getHours().get(i).setCumulativePlanned(String.valueOf(cA));
            }
            else
            if (i==0)
            {
                line.getFirst().getHours().get(i).setCumulativePlanned(line.getFirst().getHours().get(i).getTarget());
            }

            if (i!=0 &&
                    line.getSecond().getHours().get(i-1).getCumulativePlanned()!=null &&
                    !line.getSecond().getHours().get(i-1).getCumulativePlanned().isEmpty())
            {
                cA2 = Integer.valueOf(line.getSecond().getHours().get(i).getTarget()) + Integer.valueOf(line.getSecond().getHours().get(i-1).getCumulativePlanned());
                line.getSecond().getHours().get(i).setCumulativePlanned(String.valueOf(cA2));
            }
            else
            if (i==0)
            {
                line.getSecond().getHours().get(i).setCumulativePlanned(line.getSecond().getHours().get(i).getTarget());
            }

            if (i!=0 &&
                    line.getThird().getHours().get(i-1).getCumulativePlanned()!=null &&
                    !line.getThird().getHours().get(i-1).getCumulativePlanned().isEmpty())
            {
                cA3 = Integer.valueOf(line.getThird().getHours().get(i).getTarget()) + Integer.valueOf(line.getThird().getHours().get(i-1).getCumulativePlanned());
                line.getThird().getHours().get(i).setCumulativePlanned(String.valueOf(cA3));
            }
            else
            if (i==0)
            {
                line.getThird().getHours().get(i).setCumulativePlanned(line.getThird().getHours().get(i).getTarget());
            }



            if (!line.getSecond().getHours().get(i).isClosed() &&(
                    line.getSecond().getHours().get(i).getActuals()==null
                            ||line.getSecond().getHours().get(i).getActuals().isEmpty())) {
                line.getSecond().getHours().get(i).setTarget(product.getSecond().getHours().get(i).getTarget());
                line.getSecond().getHours().get(i).setCumulativePlanned(product.getSecond().getHours().get(i).getCumulativePlanned());
                line.getSecond().getHours().get(i).setStartHour(product.getSecond().getHours().get(i).getStartHour());
                line.getSecond().getHours().get(i).setEndHour(product.getSecond().getHours().get(i).getEndHour());
                line.getSecond().getHours().get(i).setProduct(newP);
            }

            if (!line.getThird().getHours().get(i).isClosed() &&(
                    line.getThird().getHours().get(i).getActuals()==null
                            ||line.getThird().getHours().get(i).getActuals().isEmpty())) {
                line.getThird().getHours().get(i).setTarget(product.getThird().getHours().get(i).getTarget());
                line.getThird().getHours().get(i).setCumulativePlanned(product.getThird().getHours().get(i).getCumulativePlanned());
                line.getThird().getHours().get(i).setStartHour(product.getThird().getHours().get(i).getStartHour());
                line.getThird().getHours().get(i).setEndHour(product.getThird().getHours().get(i).getEndHour());
                line.getThird().getHours().get(i).setProduct(newP);
            }
        }

        line.getFirst().setCumulativePlanned(String.valueOf(cA));
        line.getSecond().setCumulativePlanned(String.valueOf(cA2));
        line.getThird().setCumulativePlanned(String.valueOf(cA3));

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
    public boolean validateReason(ReasonDelay reasonDelay, String actual, String target) {
        int act,targ;
        act = Integer.valueOf(actual);
        targ = Integer.valueOf(target);
        String id = reasonDelay.getId();

        if (act<targ && (id==null || id.equals("null")))
            return true;
        else
            return false;
    }

    @Override
    public boolean validateReasonSelection(String actual, String target, String reason, String detail) {
        int act,targ;
        act = Integer.valueOf(actual);
        targ = Integer.valueOf(target);

        if (act<targ && reason.equals("Other") && detail.isEmpty())
            return true;
        else
            return false;
    }

    @Override
    public int reportHour(ArrayList<WorkHour> workHours) {
        for (int i=0;i<=23;i++)
            if (i<workHours.size())
                if ((workHours.get(i).getActuals()==null || workHours.get(i).getActuals().isEmpty()) && !workHours.get(i).isClosed())
                    return i;

        return 24;
    }

    @Override
    public int reportHour(ArrayList<WorkHour> workHours,int turn) {

        int low;
        int high;

        if (turn==1) {
            low =0;
            high =7;
        }
        else
        if (turn==2) {
            low =8;
            high =15;
        }
        else
            {
                low =16;
                high =23;
            }

            for (int i = low; i <= high; i++)
                if (i < workHours.size())
                    if ((workHours.get(i).getActuals() == null || workHours.get(i).getActuals().isEmpty()) && !workHours.get(i).isClosed())
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
        else
            mView.showCellEmailDialog();

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
        ArrayList<Downtime> downtimes;
        if (line.getDowntimes()==null)
            downtimes = new ArrayList<>();
        else
            downtimes = new ArrayList<>(line.getDowntimes());

        downtimes.add(downtime);

        line.setDowntimes(downtimes);

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
        /*if (line.getFirst().isLeakReached() &&
                line.getFirst().getLines().get(0).getActuals()!=null &&
                !line.getFirst().getLines().get(0).getActuals().isEmpty()
                )
            mView.showFTQ(1);
        else
        if (line.getSecond().isLeakReached()&&
                line.getSecond().getLines().get(0).getActuals()!=null &&
                !line.getSecond().getLines().get(0).getActuals().isEmpty())
            mView.showFTQ(2);
        else
        if (line.getThird().isLeakReached()&&
                line.getSecond().getLines().get(0).getActuals()!=null &&
                !line.getSecond().getLines().get(0).getActuals().isEmpty())
            mView.showFTQ(3);*/
    }

    @Override
    public String[] getEmails(ArrayList<Email> emails, Line line) {
        ArrayList<Email> list = new ArrayList<>();

        //if (line==null) {
        for (Email email : emails)
            if (email.isShift1())
                list.add(new Email(email));
        /*}
        else
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
        }*/
        String [] em = new String[list.size()];
        for (int i=0; i<list.size(); i++)
            em[i]= list.get(i).toString();
        return em ;
    }

    @Override
    public String[] getCC(ArrayList<Email> emails, Line line) {
        ArrayList<Email> list = new ArrayList<>();

        //if (line==null) {
        for (Email email : emails)
            if (email.isCc1())
                list.add(new Email(email));

        /*}
        else
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
        }*/

        String [] em = new String[list.size()];
        for (int i=0; i<list.size(); i++)
            em[i]= list.get(i).toString();
        return em ;
    }

    @Override
    public void setTeam(Line line) {


        if ((line.getFirst().getTeamLeaders()==null || line.getFirst().getTeamLeaders().isEmpty())&&
                (line.getSecond().getTeamLeaders()==null || line.getSecond().getTeamLeaders().isEmpty()) &&
                (line.getThird().getTeamLeaders()==null || line.getThird().getTeamLeaders().isEmpty()))
            mView.hideTeam();
        else
        {
            StringBuilder teams = new StringBuilder();
            StringBuilder teams2 = new StringBuilder();
            StringBuilder teams3 = new StringBuilder();

            teams.append("1st Shift\n");
            if (line.getFirst().getTeamLeaders()!=null)
                teams.append(line.getFirst().getTeamLeaders());

            teams2.append("2nd Shift\n");
            if (line.getSecond().getTeamLeaders()!=null)
                teams2.append(line.getSecond().getTeamLeaders());

            teams3.append("3rd Shift\n");
            if (line.getThird().getTeamLeaders()!=null)
                teams3.append(line.getThird().getTeamLeaders());


            mView.showTeam();
            mView.setTeam1(teams.toString());
            mView.setTeam2(teams2.toString());
            mView.setTeam3(teams3.toString());
        }
    }

    @Override
    public void setGroup(Line line) {
        if ((line.getFirst().getGroupLeaders()==null || line.getFirst().getGroupLeaders().isEmpty())&&
                (line.getSecond().getGroupLeaders()==null || line.getSecond().getGroupLeaders().isEmpty()) &&
                (line.getThird().getGroupLeaders()==null || line.getThird().getGroupLeaders().isEmpty()))
            mView.hideGroup();
        else
        {

            StringBuilder group = new StringBuilder();
            StringBuilder group2 = new StringBuilder();
            StringBuilder group3 = new StringBuilder();

            group.append("1st Shift\n");
            if (line.getFirst().getGroupLeaders()!=null)
                group.append(line.getFirst().getGroupLeaders());

            group2.append("2nd Shift\n");
            if (line.getSecond().getGroupLeaders()!=null)
                group2.append(line.getSecond().getGroupLeaders());

            group3.append("3rd Shift\n");
            if (line.getThird().getGroupLeaders()!=null)
                group3.append(line.getThird().getGroupLeaders());


            mView.showGroup();
            mView.setGroup1(group.toString());
            mView.setGroup2(group2.toString());
            mView.setGroup3(group3.toString());
        }
    }

    @Override
    public void setOperators(Line line) {
        StringBuilder sb = new StringBuilder();
        if (line.getOperators()!=null && line.getOperators().size()>0)
        {
            mView.showOperatorsl();
            for (Employee e : line.getOperators()) {
                sb.append("- ").append(e.getFullName()).append(": ");
                int tot = 0;
                for (Production p : e.getProductions()) {
                    sb.append(" ").append(p.getProductName()).append(" (").append(p.getTotal()).append(") ");
                    tot += p.getTotal();
                }
                sb.append("Total: ").append(String.valueOf(tot));
                sb.append(" Shift: ").append(e.getShift());
                sb.append(" Station: ").append(e.getStation());
                sb.append("\n\n");
            }

            mView.setOperators(sb.toString());

        }
        else
            mView.hideOperators();
    }


    @Override
    public boolean getGroups(Line line, int shift) {


        if (shift == 1) {
            return !(line.getFirst().getGroupLeaders() == null || line.getFirst().getGroupLeaders().isEmpty());
        }

        if (shift==2) {
            return !(line.getSecond().getGroupLeaders() == null || line.getSecond().getGroupLeaders().isEmpty());
        }

        return line.getThird().isClosed() || !(line.getThird().getGroupLeaders() == null || line.getThird().getGroupLeaders().isEmpty());


    }

    @Override
    public boolean getTeam(Line line, int shift) {
        if (shift == 1) {
            return !(line.getFirst().getTeamLeaders() == null || line.getFirst().getTeamLeaders().isEmpty());
        }

        if (shift==2) {
            return !(line.getSecond().getTeamLeaders() == null || line.getSecond().getTeamLeaders().isEmpty());
        }

        return line.getThird().isClosed() || !(line.getThird().getTeamLeaders() == null || line.getThird().getTeamLeaders().isEmpty());


    }
    @Override
    public String lineInformation(Line line) {
        StringBuilder body = new StringBuilder();

        body.append("Cell: ").append(line.getName()).append(" Date: ").append(line.getDate()).append("\n\n");
        body.append("1st Shift");
        body.append(getShift(line.getFirst()));
        body.append("\n\n2nd Shift");
        body.append(getShift(line.getSecond()));
        body.append("\n\n3rd Shift");
        body.append(getShift(line.getThird()));
        if (line.getRejects()!=null && line.getRejects().size()>0) {
            body.append("\n\nReject Events:");
            for (Reject reject : line.getRejects())
                body.append("\n - ").append(reject.getTime()).append(" ").append(reject.getReason());
        }





        return body.toString();
    }

    @Override
    public String shiftInformation(Line line, int position) {
        StringBuilder body = new StringBuilder();

        body.append("Cell: ").append(line.getName()).append(" Date: ").append(line.getDate()).append("\n\n");
        if (position==1) {
            body.append("1st Shift");
            body.append(getShift(line.getFirst()));
        }
        if (position==2) {
            body.append("2nd Shift");
            body.append(getShift(line.getSecond()));
        }
        if (position==3) {
            body.append("3rd Shift");
            body.append(getShift(line.getThird()));
        }
        if (line.getRejects()!=null && line.getRejects().size()>0) {
            body.append("\n\nReject Events:");
            for (Reject reject : line.getRejects())
                body.append("\n - ").append(reject.getTime()).append(" ").append(reject.getReason());
        }





        return body.toString();
    }

    @Override
    public void createCVS(Context context, Line line) throws IOException {
        String filename = line.getName().trim()+" "+line.getDate().trim()+".csv";

        filename = filename.replaceAll("/","-");

        File directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File logDir = new File (directoryDownload, "Tenneco Report"); //Creates a new folder in DOWNLOAD directory
        logDir.mkdirs();
        File file = new File(logDir, filename);

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("Cell:;"+line.getName()+";;"+"Date:;"+line.getDate());
        bw.newLine();
        bw.write("\n;Date;Target;Actual;Cumulative Total;Cumulative Actual; Comments/Problems");
        if (line.getFirst().isClosed())
        {
            for (WorkHour workHour : line.getFirst().getHours())
                bw.write("\n;"+workHour.getStartHour()+" - "+workHour.getEndHour()+";"+
                        workHour.getTarget()+";"+workHour.getActuals()+";"+workHour.getCumulativePlanned()+";"+workHour.getCumulativeActual()+";"+workHour.getComments().replace("\n",""));
        }

        if (line.getSecond().isClosed())
        {
            for (WorkHour workHour : line.getSecond().getHours())
                bw.write("\n;"+workHour.getStartHour()+" - "+workHour.getEndHour()+";"+
                        workHour.getTarget()+";"+workHour.getActuals()+";"+workHour.getCumulativePlanned()+";"+workHour.getCumulativeActual()+";"+workHour.getComments().replace("\n",""));
        }

        if (line.getThird().isClosed())
        {
            for (WorkHour workHour : line.getThird().getHours())
                bw.write("\n;"+workHour.getStartHour()+" - "+workHour.getEndHour()+";"+
                        workHour.getTarget()+";"+workHour.getActuals()+";"+workHour.getCumulativePlanned()+";"+workHour.getCumulativeActual()+";"+workHour.getComments().replace("\n",""));
        }


        if (line.getRejects()!=null && line.getRejects().size()>0) {
            bw.write("\n\n;Time;Reject Events");
            for (Reject reject : line.getRejects())
                bw.write("\n ;"+ reject.getTime()+";"+ reject.getReason());
        }


        bw.write("\n\n;Shif;Leak FTQ");
        bw.write("\n;1st Shift;"+line.getFirst().getLfCounter());
        bw.write("\n;2nd Shift;"+line.getSecond().getLfCounter());
        bw.write("\n;3rd Shift;"+line.getThird().getLfCounter());


        bw.write("\n\n;Shift;Team Leaders");
        if (line.getFirst().getTeamLeaders()!=null)
            bw.write("\n;1st Shift;"+line.getFirst().getTeamLeaders());
        if (line.getSecond().getTeamLeaders()!=null)
            bw.write("\n;2nd Shift;"+line.getSecond().getTeamLeaders());
        if (line.getThird().getTeamLeaders()!=null)
            bw.write("\n;3rd Shift;"+line.getThird().getTeamLeaders());

        bw.write("\n\n;Shift;Group Leaders");
        if (line.getFirst().getGroupLeaders()!=null)
            bw.write("\n;1st Shift;"+line.getFirst().getGroupLeaders());
        if (line.getSecond().getGroupLeaders()!=null)
            bw.write("\n;2nd Shift;"+line.getSecond().getGroupLeaders());
        if (line.getThird().getGroupLeaders()!=null)
            bw.write("\n;3rd Shift;"+line.getThird().getGroupLeaders());


        bw.write("\n\n;Shift");
        for (EmployeePosition position : line.getFirst().getPositions())
            bw.write(";"+position.getName());
        bw.write("\n;1st Shift");
        for (EmployeePosition position : line.getFirst().getPositions())
            if (!position.getOperator().equals("-Select Operator-"))
                bw.write(";"+position.getOperator());
            else
                bw.write(";");
        bw.write("\n;2nd Shift");
        for (EmployeePosition position : line.getSecond().getPositions())
            if (!position.getOperator().equals("-Select Operator-"))
                bw.write(";"+position.getOperator());
            else
                bw.write(";");
        bw.write("\n;3rd Shift");
        for (EmployeePosition position : line.getThird().getPositions())
            if (!position.getOperator().equals("-Select Operator-"))
                bw.write(";"+position.getOperator());
            else
                bw.write(";");




        bw.close();
        fw.close();
    }

    @Override
    public void createCSVShift(Context context, Line line, int shift, Shift actualShift) {
        String filename = "";
        String shiftname="";
        switch (shift){
            default:
                shiftname = "1st Shift";
                filename = line.getName().trim()+" "+line.getDate().trim()+" "+shiftname+".csv";
                break;
            case 2:
                shiftname = "2nd Shift";
                filename = line.getName().trim()+" "+line.getDate().trim()+" "+shiftname+".csv";
                break;
            case 3:
                shiftname = "3rd Shift";
                filename = line.getName().trim()+" "+line.getDate().trim()+" "+shiftname+".csv";
                break;
        }

        filename = filename.replaceAll("/","-");

        File directoryDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File logDir = new File (directoryDownload, "Tenneco Report"); //Creates a new folder in DOWNLOAD directory
        logDir.mkdirs();
        File file = new File(logDir, filename);

        FileWriter fw = null;
        try {
            fw = new FileWriter(file);

            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Cell;"+line.getName()+";;"+"Date;"+line.getDate());
            bw.write(";"+shiftname);


            bw.write(";Target;");
            if (actualShift.getCumulativePlanned()!=null)
            bw.write(actualShift.getCumulativePlanned());

            bw.write(";Actual;");
            if (actualShift.getCumulativeActual()!=null)
            bw.write(actualShift.getCumulativeActual());

            bw.write(";Hour Start;");
            if (actualShift.getTimeStart()!=null)
            bw.write(actualShift.getTimeStart());

            bw.write(";Hour End;");
            if (actualShift.getTimeEnd()!=null)
            bw.write(actualShift.getTimeEnd());

            bw.write(";Team Leaders;");
            if (actualShift.getTeamLeaders()!=null)
                bw.write(actualShift.getTeamLeaders());

            bw.write(";Group Leaders;");
            if (actualShift.getGroupLeaders()!=null)
                bw.write(actualShift.getGroupLeaders());

            bw.write(";Leak Failure Counter;");
            bw.write(String.valueOf(actualShift.getLfCounter()));

            for (EmployeePosition position : actualShift.getPositions()) {
                bw.write(";" + position.getName());
                if (!position.getOperator().equals("-Select Operator-"))
                    bw.write(";" + position.getOperator());
                else
                    bw.write(";");
            }




            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validateUser(FirebaseUser user, ArrayList<User> team, ArrayList<User> group,String psw) {
        boolean validate = false;

        if (user!=null && user.getEmail()!=null) {

            for (User us : team)
                if (user.getEmail().equals(us.getEmail())&& psw.equals(us.getPwd()))
                    validate = true;

            for (User us : group)
                if (user.getEmail().equals(us.getEmail()) && psw.equals(us.getPwd()))
                    validate = true;
        }

        return validate;
    }

    @Override
    public String getSignature(FirebaseUser user, ArrayList<User> team, ArrayList<User> group) {
        if (user!=null && user.getEmail()!=null) {

            for (User us : team)
                if (user.getEmail().equals(us.getEmail()) && us.getSignature()!=null)
                    return us.getSignature();

            for (User us : group)
                if (user.getEmail().equals(us.getEmail()) && us.getSignature()!=null)
                    return us.getSignature();
        }

        return "-";
    }

    @Override
    public void validateFQT(Line line) {

        int cA1 = 0;
        int cA2 = 0;
        int cA3 = 0;

        if (line.getFirst().getCumulativeActual()!=null && !line.getFirst().getCumulativeActual().isEmpty())
            cA1 = Integer.valueOf(line.getFirst().getCumulativeActual());

        if (line.getSecond().getCumulativeActual()!=null && !line.getSecond().getCumulativeActual().isEmpty())
            cA2 = Integer.valueOf(line.getSecond().getCumulativeActual());

        if (line.getThird().getCumulativeActual()!=null && !line.getThird().getCumulativeActual().isEmpty())
            cA3 = Integer.valueOf(line.getThird().getCumulativeActual());


        if (cA1>0 && Integer.valueOf(line.getFirst().getCumulativeFTQ())>=(0.10*cA1)&& !line.getFirst().isClosed()) {
            mView.setLeakReached(1);
            mView.showFTQ(1);
        }
        else
        if (cA2>0 && Integer.valueOf(line.getSecond().getCumulativeFTQ())>=(0.10*cA2)&& !line.getSecond().isClosed()) {
            mView.setLeakReached(2);
            mView.showFTQ(2);
        }
        else
        if (cA3>0 && Integer.valueOf(line.getThird().getCumulativeFTQ())>=(0.10*cA3)&& !line.getThird().isClosed()) {
            mView.setLeakReached(3);
            mView.showFTQ(3);
        }
    }

    @Override
    public void setDowntimes(Line line) {


        if (line.getDowntimes()!=null && line.getDowntimes().size()>0)
        {
            ArrayList<Downtime> s1 = new ArrayList<>();
            ArrayList<Downtime> s2 = new ArrayList<>();
            ArrayList<Downtime> s3 = new ArrayList<>();
            int ls1=0;
            int ls2=0;
            int ls3=0;

            for (Downtime dw : line.getDowntimes()) {
                if (dw.getShift() == 1)
                    s1.add(dw);
                else if (dw.getShift() == 2)
                    s2.add(dw);
                else if (dw.getShift() == 3)
                    s3.add(dw);
            }

            if (s1.size()>0)
            {
                for (Downtime dt : s1)
                   ls1+= Utils.getTimeDiferenceMillis(dt.getStartTime(),dt.getEndTime());

                    mView.setDtS1(Utils.durationDt(ls1));

            }

            if (s2.size()>0)
            {
                for (Downtime dt : s2)
                    ls2+= Utils.getTimeDiferenceMillis(dt.getStartTime(),dt.getEndTime());

                if (ls2>0)
                {
                    mView.setDtS2(Utils.durationDt(ls2));
                }
            }


            if (s3.size()>0)
            {
                for (Downtime dt : s3)
                    ls3+= Utils.getTimeDiferenceMillis(dt.getStartTime(),dt.getEndTime());

                if (ls3>0)
                {
                    mView.setDtS3(Utils.durationDt(ls3));
                }
            }


        }
    }

    private String getShift(Shift shift){
        StringBuilder body = new StringBuilder();
        body.append("\nTarget:").append(shift.getCumulativePlanned()).append(" Actual:").append(shift.getCumulativeActual());
        body.append(". Hour Start:").append(shift.getTimeStart()).append(". Hour End:").append(shift.getTimeEnd()).append("\n");
        body.append("\nTeam Leaders:").append(shift.getTeamLeaders());
        body.append("\nGroup Leaders:").append(shift.getGroupLeaders()).append("\n\n");
        for (WorkHour hour : shift.getHours())
            body.append("  -  ").append(hour.getStartHour()).append(" -> ").append(hour.getEndHour()).
                    append(" Target: ").append(hour.getTarget()).append(" Actual: ").append(hour.getActuals()).
                    append("\nCumulative Planned: ").append(hour.getCumulativePlanned()).append(" Cumulative Actual: ").append(hour.getCumulativeActual())
                    .append("\nComments:  ").append(hour.getComments()).append("\n");
        body.append("\nLeak Failure Counter:").append(String.valueOf(shift.getLfCounter())).append("\n\nPositions: ");
        for (EmployeePosition position : shift.getPositions())
            if (!position.getOperator().equals("-Select Operator-"))
                body.append("\n").append(position.getName()).append(": ").append(position.getOperator());

        return body.toString();
    }


}
