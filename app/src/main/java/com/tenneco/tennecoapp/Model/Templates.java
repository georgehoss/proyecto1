package com.tenneco.tennecoapp.Model;

/**
 * Created by ghoss on 19/12/2018.
 */
public class Templates {
    public static final String ID = "templates";
    private String id;
    private Template downtimeStart;
    private Template downtimeEnd;
    private Template ftqs;
    private Template reject1;
    private Template reject2;
    private Template reject3;
    private Template report;
    private Template shift1;
    private Template shift2;
    private Template shift3;

    public Templates() {
    }

    public Templates(Template downtimeStart, Template downtimeEnd, Template ftqs, Template reject1, Template reject2, Template reject3, Template report, Template shift1, Template shift2, Template shift3) {
        this.downtimeStart = downtimeStart;
        this.downtimeEnd = downtimeEnd;
        this.ftqs = ftqs;
        this.reject1 = reject1;
        this.reject2 = reject2;
        this.reject3 = reject3;
        this.report = report;
        this.shift1 = shift1;
        this.shift2 = shift2;
        this.shift3 = shift3;
        this.id  = ID;
    }

    public static String getID() {
        return ID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Template getDowntimeStart() {
        return downtimeStart;
    }

    public void setDowntimeStart(Template downtimeStart) {
        this.downtimeStart = downtimeStart;
    }

    public Template getDowntimeEnd() {
        return downtimeEnd;
    }

    public void setDowntimeEnd(Template downtimeEnd) {
        this.downtimeEnd = downtimeEnd;
    }

    public Template getFtqs() {
        return ftqs;
    }

    public void setFtqs(Template ftqs) {
        this.ftqs = ftqs;
    }

    public Template getReject1() {
        return reject1;
    }

    public void setReject1(Template reject1) {
        this.reject1 = reject1;
    }

    public Template getReject2() {
        return reject2;
    }

    public void setReject2(Template reject2) {
        this.reject2 = reject2;
    }

    public Template getReject3() {
        return reject3;
    }

    public void setReject3(Template reject3) {
        this.reject3 = reject3;
    }

    public Template getReport() {
        return report;
    }

    public void setReport(Template report) {
        this.report = report;
    }

    public Template getShift1() {
        return shift1;
    }

    public void setShift1(Template shift1) {
        this.shift1 = shift1;
    }

    public Template getShift2() {
        return shift2;
    }

    public void setShift2(Template shift2) {
        this.shift2 = shift2;
    }

    public Template getShift3() {
        return shift3;
    }

    public void setShift3(Template shift3) {
        this.shift3 = shift3;
    }
}
