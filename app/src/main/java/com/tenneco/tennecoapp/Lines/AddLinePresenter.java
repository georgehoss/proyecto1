package com.tenneco.tennecoapp.Lines;

import android.content.Context;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Email;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;
import java.util.Collections;

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
        if (viewVisibility == resultVisibility) {
            mView.hideshift1();
            mView.showAll();
        }
        else {
            mView.hideAll();
            mView.showshift1();
        }

    }

    @Override
    public void onShift2Click(int viewVisibility, int resultVisibility) {
        if (viewVisibility == resultVisibility) {
            mView.hideshift2();
            mView.showAll();
        }
        else {
            mView.hideAll();
            mView.showshift2();
        }
    }

    @Override
    public void onShift3Click(int viewVisibility, int resultVisibility) {
        if (viewVisibility == resultVisibility) {
            mView.hideshift3();
            mView.showAll();
        }
        else {
            mView.hideAll();
            mView.showshift3();
        }
    }

    @Override
    public void onPositionClick(int viewVisibility, int resultVisibility) {
        if (viewVisibility == resultVisibility) {
            mView.hidePosition();
            mView.showAll();
        }
        else {
            mView.hideAll();
            mView.showPosition();
        }
    }

    @Override
    public void onDowntimeClick(int viewVisibility, int resultVisibility) {
        if (viewVisibility == resultVisibility) {
            mView.hideDowntime();
            mView.showAll();
        }
        else {
            mView.hideAll();
            mView.showDowntime();
        }
    }

    @Override
    public void onScrapClick(int viewVisibility, int resultVisibility) {
        if (viewVisibility == resultVisibility) {
            mView.hideScrap();
            mView.showAll();
        }
        else {
            mView.hideAll();
            mView.showScrap();
        }
    }

    @Override
    public void onEmailClick(int viewVisibility, int resultVisibility) {
        if (viewVisibility == resultVisibility) {
            mView.hideEmails();
            mView.showAll();
        }
        else {
            mView.hideAll();
            mView.showEmails();
        }
    }

    @Override
    public StringBuilder getEmailList(ArrayList<Email> emails) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1st Shift:\n");
        stringBuilder.append("\nAddresses:");
        for (Email email :emails)
            if (email.isShift1()&& !email.isCc1())
                stringBuilder.append(" ").append(email.getEmail());
        stringBuilder.append("\n\nCC:");
        for (Email email : emails)
            if (email.isShift1() && email.isCc1())
                stringBuilder.append(" ").append(email.getEmail());

        stringBuilder.append("\n\n2nd Shift:\n");
        stringBuilder.append("\nAddresses:");
        for (Email email : emails)
            if (email.isShift2()&& !email.isCc2())
                stringBuilder.append(" ").append(email.getEmail());
        stringBuilder.append("\n\nCC:");
        for (Email email : emails)
            if (email.isShift2() && email.isCc2())
                stringBuilder.append(" ").append(email.getEmail());

        stringBuilder.append("\n\n3rd Shift:\n");
        stringBuilder.append("\nAddresses:");
        for (Email email : emails)
            if (email.isShift3()&& !email.isCc3())
                stringBuilder.append(" ").append(email.getEmail());
        stringBuilder.append("\n\nCC:");
        for (Email email : emails)
            if (email.isShift3() && email.isCc3())
                stringBuilder.append(" ").append(email.getEmail());
        return stringBuilder;
    }

    @Override
    public void saveChanges(String name, String id, Shift first, Shift second, Shift third, ArrayList<EmployeePosition> positions,
                            Downtime downtime, ArrayList<Reason> reasons,Line mLine, ArrayList<Employee> employees,
                            ArrayList<Email> emails, String psw) {
        Line line = new Line(name,id,first,second,third,positions);
        line.setDowntime(downtime);
        line.setScrapReasons(reasons);
        if (line.getFirst().getEmployees()==null)
            line.getFirst().setEmployees(employees);
        if (line.getSecond().getEmployees()==null)
            line.getSecond().setEmployees(employees);
        if (line.getThird().getEmployees()==null)
            line.getThird().setEmployees(employees);

        for (EmployeePosition employeePosition : line.getPositions()) {
            employeePosition.setPosition(0);
            employeePosition.setOperator("");
        }

        for (Employee employee : employees)
            for (Employee shifte : first.getEmployees())
                if (employee.getId().equals(shifte.getId())) {
                    employee.setShift(shifte.getShift());
                    employee.setAvailable(shifte.isAvailable());
                }

        for (Employee employee : employees)
            for (Employee shifte : second.getEmployees())
                if (employee.getId().equals(shifte.getId())) {
                    employee.setShift(shifte.getShift());
                    employee.setAvailable(shifte.isAvailable());
                }

        for (Employee employee : employees)
            for (Employee shifte : third.getEmployees())
                if (employee.getId().equals(shifte.getId())) {
                    employee.setShift(shifte.getShift());
                    employee.setAvailable(shifte.isAvailable());
                }

        line.setDowntimeList(mLine.getDowntimeList());
        line.setScrap1List(mLine.getScrap1List());
        line.setScrap2List(mLine.getScrap2List());
        line.setScrap3List(mLine.getScrap3List());
        if (mLine.getParentId()!=null)
            line.setParentId(mLine.getParentId());

        if (mLine.getDate()!=null)
            line.setDate(mLine.getDate());

        if (mLine.getGroupLeaders()!=null)
            line.setGroupLeaders(mLine.getGroupLeaders());

        if (mLine.getTeamLeaders()!=null)
            line.setTeamLeaders(mLine.getTeamLeaders());

        if (mLine.getScraps()!=null)
            line.setScraps(mLine.getScraps());


        if (line.getDowntimeList()==null) {
            mLine.setDowntimeList(new ArrayList<Email>());
            for (Email email : emails)
            {
                Email email1 = new Email(email);
                email1.setShift1(true);
                email1.setShift2(true);
                email1.setShift3(true);
                mLine.getDowntimeList().add(new Email(email1));
            }
        }

        if (line.getScrap1List()==null) {
            mLine.setScrap1List(new ArrayList<Email>());
            for (Email email : emails)
            {
                Email email1 = new Email(email);
                email1.setShift1(true);
                email1.setShift2(true);
                email1.setShift3(true);
                mLine.getScrap1List().add(new Email(email1));
            }
        }

        if (line.getScrap2List()==null) {
            mLine.setScrap2List(new ArrayList<Email>());
            for (Email email : emails)
            {
                Email email1 = new Email(email);
                email1.setShift1(true);
                email1.setShift2(true);
                email1.setShift3(true);
                mLine.getScrap2List().add(new Email(email1));
            }
        }


        if (line.getScrap3List()==null) {
            mLine.setScrap3List(new ArrayList<Email>());
            for (Email email : emails)
            {
                Email email1 = new Email(email);
                email1.setShift1(true);
                email1.setShift2(true);
                email1.setShift3(true);
                mLine.getScrap3List().add(new Email(email1));
            }
        }

        line.setPassword(psw);

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
    public void addPosition(String position) {
        if(validPosition(position))
            mView.addPosition(new EmployeePosition(position));
    }

    @Override
    public boolean validPosition(String position) {
        return !position.isEmpty();
    }

    @Override
    public ArrayList<Employee> verifyEmployees(ArrayList<Employee> shift, ArrayList<Employee> employees1) {

        ArrayList<Employee> employees = new ArrayList<>();
        employees.addAll(employees1);


        if (shift!=null && shift.size()>0)
        {
            for ( Employee online : employees)
                for (Employee offline : shift)
                    if (online.getFullName().equals(offline.getFullName())) {
                        online.setAvailable(offline.isAvailable());
                        online.setShift(offline.getShift());
                    }

        }
        else
        {
            for (Employee employee : employees)
                employee.setAvailable(true);

        }

        Collections.sort(employees,Employee.EmployeeNameComparator);

        return employees;

    }

    @Override
    public ArrayList<Email> verifyEmails(ArrayList<Email> list, ArrayList<Email> notifications) {
        ArrayList<Email> emails = new ArrayList<>();
        for (Email email : notifications)
            emails.add(new Email(email));


        if (list!=null && list.size()>0)
        {
            for ( Email online : emails)
                for (Email offline : list)
                    if (online.getId().equals(offline.getId())) {
                        online.setShift1(offline.isShift1());
                        online.setShift2(offline.isShift2());
                        online.setShift3(offline.isShift3());
                        online.setCc1(offline.isCc1());
                        online.setCc2(offline.isCc2());
                        online.setCc3(offline.isCc3());

                    }

        }
        else
        {
            for (Email online : emails)
            {
                online.setShift1(false);
                online.setShift2(false);
                online.setShift3(false);
                online.setCc1(false);
                online.setCc2(false);
                online.setCc3(false);
            }

        }

        Collections.sort(emails,Email.EmailNameComparator);

        return emails;

    }

    @Override
    public ArrayList<Employee> getEmployees(ArrayList<Employee> shift1, ArrayList<Employee> shift2, ArrayList<Employee> shift3) {
        ArrayList<Employee> employees = new ArrayList<>();
        if (shift1!=null)
            employees.addAll(shift1);
        if (shift2!=null)
            employees.addAll(shift2);
        if (shift3!=null)
            employees.addAll(shift3);

        Collections.sort(employees,Employee.EmployeeNameComparator);

        return employees;
    }

    @Override
    public void initData(Context context) {
        Line line = new Line();
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
        ArrayList<EmployeePosition> positions = new ArrayList<>();
        positions.add(new EmployeePosition(context.getString(R.string.add_robot_1)));
        positions.add(new EmployeePosition(context.getString(R.string.add_robot_2)));
        positions.add(new EmployeePosition(context.getString(R.string.add_manual_weld)));
        positions.add(new EmployeePosition(context.getString(R.string.add_gauge_test)));
        ArrayList<Location>locations = new ArrayList<>();
        locations.add(new Location("Side 1 group 1"));
        locations.add(new Location("Side 1 group 2"));
        locations.add(new Location("Side 1 group 3"));
        locations.add(new Location("Side 2 group 1"));
        locations.add(new Location("Side 2 group 2"));
        locations.add(new Location("Side 2 group 3"));
        locations.add(new Location("Side 2 group 4"));
        locations.add(new Location("Operator"));
        locations.add(new Location("Material"));
        locations.add(new Location("Other"));
        ArrayList<Location>locations1 = new ArrayList<>();
        locations1.add(new Location("Robot 1"));
        locations1.add(new Location("Robot 2"));
        locations1.add(new Location("Robot 3"));
        locations1.add(new Location("Robot 4"));
        locations1.add(new Location("Robot 5"));
        locations1.add(new Location("All Robots"));
        locations1.add(new Location("Tip Change Window"));
        locations1.add(new Location("Lower beam for full assembly"));
        locations1.add(new Location("Other"));
        ArrayList<Location>locations2 = new ArrayList<>();
        locations2.add(new Location("Welder"));
        locations2.add(new Location("Tip fixture"));
        locations2.add(new Location("Center rod fixture"));
        locations2.add(new Location("Curtain"));
        locations2.add(new Location("Operator"));
        locations2.add(new Location("Material"));
        locations2.add(new Location("Other"));
        ArrayList<Location>locations3 = new ArrayList<>();
        locations3.add(new Location("Welder"));
        locations3.add(new Location("Operator"));
        locations3.add(new Location("Material"));
        locations3.add(new Location("Other"));
        ArrayList<Location>locations4 = new ArrayList<>();
        locations4.add(new Location("Tip fixture"));
        locations4.add(new Location("Center rod fixture"));
        locations4.add(new Location("Resonator fixture"));
        locations4.add(new Location("Grommet loader"));
        locations4.add(new Location("Operator"));
        locations4.add(new Location("Material"));
        locations4.add(new Location("Other"));
        ArrayList<Location>locations5 = new ArrayList<>();
        locations5.add(new Location("Welder"));
        locations5.add(new Location("Operator"));
        ArrayList<Location>locations6 = new ArrayList<>();
        locations6.add(new Location("Other"));
        ArrayList<Zone>zones = new ArrayList<>();
        zones.add(new Zone("Gauge",locations));
        zones.add(new Zone("Lift assist",locations1));
        zones.add(new Zone("Load side",locations2));
        zones.add(new Zone("Repair table",locations3));
        zones.add(new Zone("Robot",locations4));
        zones.add(new Zone("Weld booth",locations5));
        zones.add(new Zone("Other",locations6));

        ArrayList<Reason> downtimeReasons = new ArrayList<>();
        downtimeReasons.add(new Reason("Air leak"));
        downtimeReasons.add(new Reason("Air Pressure Issue"));
        downtimeReasons.add(new Reason("Arc out"));
        downtimeReasons.add(new Reason("Bathroom"));
        downtimeReasons.add(new Reason("Burn Thru"));
        downtimeReasons.add(new Reason("Changeover"));
        downtimeReasons.add(new Reason("Clamp issue"));
        downtimeReasons.add(new Reason("Efficiency"));
        downtimeReasons.add(new Reason("Electrical"));
        downtimeReasons.add(new Reason("Engineering"));
        downtimeReasons.add(new Reason("Excessive repair"));
        downtimeReasons.add(new Reason("Filling out scrap tag"));
        downtimeReasons.add(new Reason("Fixture routing cleaning"));
        downtimeReasons.add(new Reason("Gas change"));
        downtimeReasons.add(new Reason("Gp12"));
        downtimeReasons.add(new Reason("Gp12 repair"));
        downtimeReasons.add(new Reason("Gripper Issue"));
        downtimeReasons.add(new Reason("Hole in pipe"));
        downtimeReasons.add(new Reason("Hourly Check"));
        downtimeReasons.add(new Reason("Incoming material quality"));
        downtimeReasons.add(new Reason("Light Curtain"));
        downtimeReasons.add(new Reason("Machine/Fixture adjustment"));
        downtimeReasons.add(new Reason("Manpower"));
        downtimeReasons.add(new Reason("Material outage"));
        downtimeReasons.add(new Reason("Mechanical"));
        downtimeReasons.add(new Reason("Meeting"));
        downtimeReasons.add(new Reason("Method"));
        downtimeReasons.add(new Reason("No build due to scrap"));
        downtimeReasons.add(new Reason("Other"));
        downtimeReasons.add(new Reason("Other quality issue"));
        downtimeReasons.add(new Reason("Pipe not fitting correctly"));
        downtimeReasons.add(new Reason("Plc/Programming"));
        downtimeReasons.add(new Reason("Ribbon/Label replacement"));
        downtimeReasons.add(new Reason("Robot out of sequence"));
        downtimeReasons.add(new Reason("Safety"));
        downtimeReasons.add(new Reason("Scrap disposal"));
        downtimeReasons.add(new Reason("Seal replacement"));
        downtimeReasons.add(new Reason("Sensor"));
        downtimeReasons.add(new Reason("Stuck in tip change"));
        downtimeReasons.add(new Reason("Tip change"));
        downtimeReasons.add(new Reason("Training"));
        downtimeReasons.add(new Reason("Waiting on process tech"));
        downtimeReasons.add(new Reason("Weld adjustment"));
        downtimeReasons.add(new Reason("Weld Issue"));
        downtimeReasons.add(new Reason("Wire change"));
        downtimeReasons.add(new Reason("Wire feed/Birdnest"));
        Downtime downtime = new Downtime();
        downtime.setZones(zones);
        downtime.setReasons(downtimeReasons);
        ArrayList<Reason> reasons = new ArrayList<>();
        reasons.add(new Reason("Burn Thru at muffler"));
        reasons.add(new Reason("Burn thru at Collector"));
        reasons.add(new Reason("Burn Thru Other"));
        reasons.add(new Reason("Rattle"));
        reasons.add(new Reason("Tip Damage"));
        reasons.add(new Reason("No Fit GAGE"));
        reasons.add(new Reason("No Fit ROBOT"));
        reasons.add(new Reason("Pipe Long/Short"));
        reasons.add(new Reason("Damaged Flex"));
        reasons.add(new Reason("Damaged EGO"));
        reasons.add(new Reason("Poor Repair"));
        reasons.add(new Reason("Damager Other"));
        line.setDowntime(downtime);
        line.setScrapReasons(reasons);
        line.setFirst(new Shift(hours1,context.getString(R.string.add_1st_shift),context.getString(R.string.add_t_273)));
        line.setSecond(new Shift(hours2,context.getString(R.string.add_2nd_shift),context.getString(R.string.add_t_273)));
        line.setThird(new Shift(hours3,context.getString(R.string.add_3rd_shift),context.getString(R.string.add_t_273)));
        line.setPositions(positions);
        ArrayList<Employee> employees = new ArrayList<>();
        line.setEmployees(employees);
        mView.setData(line);
    }

    @Override
    public boolean validName(String name) {
        return !name.isEmpty();
    }

}
