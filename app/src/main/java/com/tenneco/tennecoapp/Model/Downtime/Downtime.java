package com.tenneco.tennecoapp.Model.Downtime;

import android.widget.Adapter;

import java.util.ArrayList;
import java.util.Comparator;


public class Downtime {
    public static final String DB_DOWNTIMES = "downtimes";
    private ArrayList<Zone> zones;
    private ArrayList<Reason> reasons;
    private String startTime;
    private String endTime;
    private boolean set;
    private int zone;
    private int location;
    private int reason;
    private String downtime;
    private String zoneValue;
    private String locationValue;
    private String reasonValue;
    private String name;
    private String id;


    public Downtime() {
    }

    public static Comparator<Downtime> NameComparator = new Comparator<Downtime>() {

        public int compare(Downtime us1, Downtime us2) {
            String name1 = us1.getName().toUpperCase();
            String name2 = us2.getName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }
    };


    public ArrayList<Zone> getZones() {
        return zones;
    }

    public void setZones(ArrayList<Zone> zones) {
        this.zones = zones;
    }

    public ArrayList<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(ArrayList<Reason> reasons) {
        this.reasons = reasons;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getDowntime() {
        return downtime;
    }

    public void setDowntime(String downtime) {
        this.downtime = downtime;
    }

    public String getZoneValue() {
        return zoneValue;
    }

    public void setZoneValue(String zoneValue) {
        this.zoneValue = zoneValue;
    }

    public String getLocationValue() {
        return locationValue;
    }

    public void setLocationValue(String locationValue) {
        this.locationValue = locationValue;
    }

    public String getReasonValue() {
        return reasonValue;
    }

    public void setReasonValue(String reasonValue) {
        this.reasonValue = reasonValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
