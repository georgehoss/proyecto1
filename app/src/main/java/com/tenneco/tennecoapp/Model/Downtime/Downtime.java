package com.tenneco.tennecoapp.Model.Downtime;

import android.widget.Adapter;

import java.util.ArrayList;

/**
 * Created by ghoss on 12/09/2018.
 */
public class Downtime {
    private ArrayList<Zone> zones;
    private ArrayList<Reason> reasons;

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
}
