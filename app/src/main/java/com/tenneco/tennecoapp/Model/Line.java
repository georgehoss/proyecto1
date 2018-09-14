package com.tenneco.tennecoapp.Model;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;

import java.util.ArrayList;

/**
 * Created by ghoss on 11/09/2018.
 */
public class Line {
    public static final String DB_LINE = "line";
    public static final String DB_PRODUCTION_LINE = "production_line";

    private String name;
    private String id;
    private Shift first;
    private Shift second;
    private Shift third;
    private String leakFailureCounter;
    private ArrayList<Reason> scrapReasons;
    private Downtime downtime;
    private String date;
    private String parentId;

    public Line() {
    }

    public Line(String name, String id, Shift first, Shift second, Shift third) {
        this.name = name;
        this.id = id;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Line(Line line){
        this.name = line.getName();
        this.first = line.getFirst();
        this.second = line.getSecond();
        this.third = line.getThird();
        this.leakFailureCounter = line.getLeakFailureCounter();
        this.scrapReasons = line.getScrapReasons();
        this.downtime = line.getDowntime();
        this.parentId = line.getId();
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
