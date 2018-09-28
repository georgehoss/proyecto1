package com.tenneco.tennecoapp.Model;

import java.util.ArrayList;
import java.util.Comparator;

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
    private ArrayList<Employee> employees;
    private boolean closed;
    private int lfCounter;

    public Shift() {
    }


    public static Comparator<Shift> NameComparator = new Comparator<Shift>() {

        public int compare(Shift us1, Shift us2) {
            String name1 = us1.getShiftName().toUpperCase();
            String name2 = us2.getShiftName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }};

    public Shift(ArrayList<WorkHour> hours, String shiftName, String cumulativePlanned) {
        this.hours = hours;
        this.shiftName = shiftName;
        this.cumulativePlanned = cumulativePlanned;
        this.closed = false;
    }

    public Shift(Shift shift) {
        this.hours = shift.getHours();
        this.shiftName = shift.getShiftName();
        this.cumulativePlanned = shift.getCumulativePlanned();
        this.employees = shift.getEmployees();
        this.lfCounter = shift.getLfCounter();
    }

    public int getLfCounter() {
        return lfCounter;
    }

    public void setLfCounter(int lfCounter) {
        this.lfCounter = lfCounter;
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

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
