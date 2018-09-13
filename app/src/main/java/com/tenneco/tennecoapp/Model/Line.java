package com.tenneco.tennecoapp.Model;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;

import java.util.ArrayList;

/**
 * Created by ghoss on 11/09/2018.
 */
public class Line {
    public static final String DB = "line";
    private String name;
    private String id;
    private Shift first;
    private Shift second;
    private Shift third;
    private String leakFailureCounter;
    private ArrayList<Reason> scrapReasons;
    private Downtime downtime;

    public Line() {
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

    public Shift getFirst() {
        return first;
    }

    public void setFirst(Shift first) {
        this.first = first;
    }

    public Shift getSecond() {
        return second;
    }

    public void setSecond(Shift second) {
        this.second = second;
    }

    public Shift getThird() {
        return third;
    }

    public void setThird(Shift third) {
        this.third = third;
    }

    public String getLeakFailureCounter() {
        return leakFailureCounter;
    }

    public void setLeakFailureCounter(String leakFailureCounter) {
        this.leakFailureCounter = leakFailureCounter;
    }

    public ArrayList<Reason> getScrapReasons() {
        return scrapReasons;
    }

    public void setScrapReasons(ArrayList<Reason> scrapReasons) {
        this.scrapReasons = scrapReasons;
    }

    public Downtime getDowntime() {
        return downtime;
    }

    public void setDowntime(Downtime downtime) {
        this.downtime = downtime;
    }
}
