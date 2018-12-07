package com.tenneco.tennecoapp.Lines;

import android.content.Context;

import com.tenneco.tennecoapp.Lines.ConfigLineContract;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Employee;
import com.tenneco.tennecoapp.Model.EmployeePosition;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

import java.util.ArrayList;

/**
 * Created by ghoss on 06/12/2018.
 */
public class ConfigLinePresenter implements ConfigLineContract.Presenter{
    private ConfigLineContract.View mView;

    public ConfigLinePresenter(ConfigLineContract.View mView) {
        this.mView = mView;
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
        downtimeReasons.add(new Reason("Reject disposal"));
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
    public boolean validCode(String code) {
        if (code == null ||code.isEmpty())
            mView.showCodeError();
        return !(code==null || code.isEmpty());
    }

    @Override
    public boolean validName(String name) {
        if ( name == null || name.isEmpty())
            mView.showNameError();
        return !(name==null ||name.isEmpty());
    }

    @Override
    public boolean validOperators(ArrayList<EmployeePosition> positions) {
        if (positions==null || positions.size()==0)
            mView.showPositionError();
        return !(positions==null || positions.size()==0);
    }

    @Override
    public boolean validDowntime(Downtime downtime) {
        if (downtime.getZones()==null || downtime.getZones().size()==0)
            mView.showDowntimeZoneError();
        else
            if (downtime.getReasons()==null || downtime.getReasons().size()==0)
                mView.showDowntimeReasonError();

        return (!(downtime.getZones()==null || downtime.getZones().size()==0) &&
                !(downtime.getReasons()==null || downtime.getReasons().size()==0));
    }

    @Override
    public void bindView(ConfigLineContract.View view) {
        this.mView = view;
    }

    @Override
    public void unbindView() {
        this.mView = null;
    }
}
