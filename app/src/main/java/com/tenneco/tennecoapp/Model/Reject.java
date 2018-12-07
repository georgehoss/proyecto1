package com.tenneco.tennecoapp.Model;

/**
 * Created by ghoss on 30/09/2018.
 */
public class Reject {
    private String reason;
    private String time;
    private String actions;

    public Reject() {
    }

    public Reject(String reason, String time) {
        this.reason = reason;
        this.time = time;
    }

    public Reject(String reason, String time, String actions) {
        this.reason = reason;
        this.time = time;
        this.actions = actions;
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
}
