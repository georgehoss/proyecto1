package com.tenneco.tennecoapp.Daily;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Scrap;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.Utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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

        if (line==null) {
            for (Email email : emails)
                if (email.isShift1())
                    list.add(new Email(email));
        }
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
        }
        String [] em = new String[list.size()];
        for (int i=0; i<list.size(); i++)
            em[i]= list.get(i).toString();
        return em ;
    }

    @Override
    public String[] getCC(ArrayList<Email> emails, Line line) {
        ArrayList<Email> list = new ArrayList<>();

        if (line==null) {
            for (Email email : emails)
                if (email.isCc1())
                    list.add(new Email(email));
        }
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
        }

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

            if (line.getFirst().getTeamLeaders()!=null)
                teams.append("1st Shift\n").append(line.getFirst().getTeamLeaders());

            if (line.getSecond().getTeamLeaders()!=null)
                teams.append("\n\n").append("2nd Shift\n").append(line.getSecond().getTeamLeaders());

            if (line.getThird().getTeamLeaders()!=null)
                teams.append("\n\n").append("3rd Shift\n").append(line.getThird().getTeamLeaders());


            mView.showTeam();
            mView.setTeam(teams.toString());
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

            if (line.getFirst().getGroupLeaders()!=null)
                group.append("1st Shift\n").append(line.getFirst().getGroupLeaders());

            if (line.getSecond().getGroupLeaders()!=null)
                group.append("\n\n").append("2nd Shift\n").append(line.getSecond().getGroupLeaders());

            if (line.getThird().getGroupLeaders()!=null)
                group.append("\n\n").append("3rd Shift\n").append(line.getThird().getGroupLeaders());


            mView.showGroup();
            mView.setGroup(group.toString());
        }
    }

    @Override
    public boolean getGroups(Line line) {


        if (!line.getFirst().isClosed()) {
            return !(line.getFirst().getGroupLeaders() == null || line.getFirst().getGroupLeaders().isEmpty());
        }

        if (!line.getSecond().isClosed()) {
            return !(line.getSecond().getGroupLeaders() == null || line.getSecond().getGroupLeaders().isEmpty());
        }

        return line.getThird().isClosed() || !(line.getThird().getGroupLeaders() == null || line.getThird().getGroupLeaders().isEmpty());


    }

    @Override
    public boolean getTeam(Line line) {
        if (!line.getFirst().isClosed()) {
            return !(line.getFirst().getTeamLeaders() == null || line.getFirst().getTeamLeaders().isEmpty());
        }

        if (!line.getSecond().isClosed()) {
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
        if (line.getScraps()!=null && line.getScraps().size()>0) {
            body.append("\n\nScrap Events:");
            for (Scrap scrap : line.getScraps())
                body.append("\n - ").append(scrap.getTime()).append(" ").append(scrap.getReason());
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
        if (line.getScraps()!=null && line.getScraps().size()>0) {
            body.append("\n\nScrap Events:");
            for (Scrap scrap : line.getScraps())
                body.append("\n - ").append(scrap.getTime()).append(" ").append(scrap.getReason());
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


        if (line.getScraps()!=null && line.getScraps().size()>0) {
            bw.write("\n\n;Time;Scrap Events");
            for (Scrap scrap : line.getScraps())
                bw.write("\n ;"+scrap.getTime()+";"+scrap.getReason());
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
            bw.write(actualShift.getCumulativePlanned());

            bw.write(";Actual;");
            bw.write(actualShift.getCumulativeActual());

            bw.write(";Hour Start;");
            bw.write(actualShift.getTimeStart());

            bw.write(";Hour End;");
            bw.write(actualShift.getTimeEnd());

            bw.write(";Team Leaders;");
            bw.write(actualShift.getTeamLeaders());

            bw.write(";Group Leaders;");
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
