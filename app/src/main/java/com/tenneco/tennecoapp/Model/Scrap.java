package com.tenneco.tennecoapp.Model;

/**
 * Created by ghoss on 30/09/2018.
 */
public class Scrap {
    private String reason;
    private String time;

    public Scrap() {
    }

    public Scrap(String reason, String time) {
        this.reason = reason;
        this.time = time;
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
}
