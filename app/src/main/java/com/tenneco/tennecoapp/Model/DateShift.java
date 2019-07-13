package com.tenneco.tennecoapp.Model;

public class DateShift {
    public static final String DB_SHIFTS = "shifts_ended";
    String date;
    Shift shift;
    String code;

    public DateShift() {
    }

    public DateShift(String date, Shift shift, String code) {
        this.date = date;
        this.shift = shift;
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
