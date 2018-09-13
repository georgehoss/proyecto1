package com.tenneco.tennecoapp.Model;

import java.util.ArrayList;

/**
 * Created by ghoss on 11/09/2018.
 */
public class Shift {
    private ArrayList<WorkHour> hours;
    private String cell;
    private String date;
    private String shiftName;
    private String actual;
    private String target;
    private String cumulativeActual;
    private String cumulativePlanned;
    private ArrayList<EmployeePosition> positions;

    public Shift() {
    }

    public ArrayList<WorkHour> getHours() {
        return hours;
    }

    public void setHours(ArrayList<WorkHour> hours) {
        this.hours = hours;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public ArrayList<EmployeePosition> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<EmployeePosition> positions) {
        this.positions = positions;
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
}
