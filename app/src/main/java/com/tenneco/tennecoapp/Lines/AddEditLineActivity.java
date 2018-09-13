package com.tenneco.tennecoapp.Lines;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Location;
import com.tenneco.tennecoapp.Model.Downtime.Reason;
import com.tenneco.tennecoapp.Model.Downtime.Zone;
import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.Shift;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddEditLineActivity extends AppCompatActivity {
    private DatabaseReference dbLines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_line);
        dbLines = FirebaseDatabase.getInstance().getReference(Line.DB);
        Line line = new Line();
        line.setName("402");
        line.setId(dbLines.push().getKey());
        Shift shift = new Shift();
        Shift shift1 = new Shift();
        Shift shift2 = new Shift();

        ArrayList<WorkHour> hours = new ArrayList<>();
        hours.add(new WorkHour("06:30","07:30","200","200"));
        hours.add(new WorkHour("07:30","08:30","200","400"));
        hours.add(new WorkHour("08:30","09:30","200","600"));
        hours.add(new WorkHour("09:30","10:30","200","800"));
        hours.add(new WorkHour("10:30","11:30","200","1000"));
        hours.add(new WorkHour("11:30","12:30","200","1200"));
        hours.add(new WorkHour("12:30","13:30","200","1400"));
        hours.add(new WorkHour("13:30","14:30","200","1600"));
        shift.setHours(hours);
        line.setFirst(shift);
        ArrayList<WorkHour> hours2 = new ArrayList<>();
        hours2.add(new WorkHour("14:30","15:30","200","200"));
        hours2.add(new WorkHour("15:30","16:30","200","400"));
        hours2.add(new WorkHour("16:30","17:30","200","600"));
        hours2.add(new WorkHour("17:30","18:30","200","800"));
        hours2.add(new WorkHour("18:30","19:30","200","1000"));
        hours2.add(new WorkHour("19:30","20:30","200","1200"));
        hours2.add(new WorkHour("20:30","21:30","200","1400"));
        hours2.add(new WorkHour("21:30","22:30","200","1600"));
        shift1.setHours(hours2);
        line.setSecond(shift1);
        ArrayList<WorkHour> hours1 = new ArrayList<>();
        hours1.add(new WorkHour("22:30","23:30","200","200"));
        hours1.add(new WorkHour("23:30","00:30","200","400"));
        hours1.add(new WorkHour("00:30","01:30","200","600"));
        hours1.add(new WorkHour("01:30","02:30","200","800"));
        hours1.add(new WorkHour("02:30","03:30","200","1000"));
        hours1.add(new WorkHour("03:30","04:30","200","1200"));
        hours1.add(new WorkHour("04:30","05:30","200","1400"));
        hours1.add(new WorkHour("05:30","06:30","200","1600"));
        shift2.setHours(hours1);
        line.setThird(shift2);
        ArrayList<Reason> reasons = new ArrayList<>();
        reasons.add(new Reason("Out of order"));
        reasons.add(new Reason("other reasons"));
        line.setScrapReasons(reasons);
        Downtime downtime = new Downtime();
        downtime.setReasons(reasons);
        ArrayList<Zone> zones = new ArrayList<>();
        Zone zone = new Zone();
        zone.setName("Zona 1");
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location("Location 1"));
        locations.add(new Location("Location 2"));
        locations.add(new Location("Location 3"));
        zone.setLocations(locations);
        zones.add(zone);
        downtime.setZones(zones);
        line.setDowntime(downtime);
        dbLines.child(line.getId()).setValue(line);
    }
}
