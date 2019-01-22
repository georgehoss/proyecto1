package com.tenneco.tennecoapp.Model;

/**
 * Created by ghoss on 30/09/2018.
 */
public class Reject {
    private String reason;
    private String time;
    private String actions;
    private int shift;

    public Reject() {
    }

    public Reject(String reason, String time,int shift) {
        this.reason = reason;
        this.time = time;
        this.shift = shift;
    }

    public Reject(String reason, String time, String actions,int shift) {
        this.reason = reason;
        this.time = time;
        this.actions = actions;
        this.shift = shift;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }
}
