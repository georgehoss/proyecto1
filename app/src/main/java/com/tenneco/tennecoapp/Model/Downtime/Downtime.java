package com.tenneco.tennecoapp.Model.Downtime;

import android.widget.Adapter;

import java.util.ArrayList;

/**
 * Created by ghoss on 12/09/2018.
 */
public class Downtime {
    private ArrayList<Zone> zones;
    private ArrayList<Reason> reasons;
    private String startTime;
    private String endTime;
    private boolean set;
    private int zone;
    private int location;
    private int reason;

    public Downtime() {
    }

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
}
