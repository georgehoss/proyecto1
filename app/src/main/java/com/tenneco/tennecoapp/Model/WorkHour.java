package com.tenneco.tennecoapp.Model;

/**
 * Created by ghoss on 11/09/2018.
 */
public class WorkHour {
    private String startHour;
    private String endHour;
    private String target;
    private String actuals;
    private String cumulativeActual;
    private String cumulativePlanned;
    private String comments;
    private String user;

    public WorkHour() {
    }

    public WorkHour(String startHour, String endHour, String target, String cumulativePlanned) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.target = target;
        this.cumulativePlanned = cumulativePlanned;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
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
