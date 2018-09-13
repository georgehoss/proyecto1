package com.tenneco.tennecoapp.Model;

/**
 * Created by ghoss on 11/09/2018.
 */
public class WorkHour {
    private String hourStart;
    private String hourEnd;
    private String target;
    private String actuals;
    private String cumulativeActual;
    private String cumulativePlanned;
    private String comments;
    private String user;

    public WorkHour() {
    }

    public WorkHour(String hourStart, String hourEnd, String target, String cumulativePlanned) {
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        this.target = target;
        this.cumulativePlanned = cumulativePlanned;
    }

    public String getHourStart() {
        return hourStart;
    }

    public void setHourStart(String hourStart) {
        this.hourStart = hourStart;
    }

    public String getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(String hourEnd) {
        this.hourEnd = hourEnd;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getActuals() {
        return actuals;
    }

    public void setActuals(String actuals) {
        this.actuals = actuals;
    }

    public String getCumulativeActual() {
        return cumulativeActual;
    }

    public void setCumulativeActual(String cumulativeActual) {
        this.cumulativeActual = cumulativeActual;
    }

    public String getCumulativePlanned() {
        return cumulativePlanned;
    }

    public void setCumulativePlanned(String cumulativePlanned) {
        this.cumulativePlanned = cumulativePlanned;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
