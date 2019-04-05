package com.tenneco.tennecoapp.Model;

import java.util.Comparator;

/**
 * Created by ghoss on 11/09/2018.
 */
public class EmployeePosition {
    public static final String DB = "employee";
    private String id;
    private String name;
    private String operator;
    private String operatorId;
    private int position;

    public EmployeePosition() {
    }


    public static Comparator<EmployeePosition> NameComparator = new Comparator<EmployeePosition>() {

        public int compare(EmployeePosition us1, EmployeePosition us2) {
            String name1 = us1.getName().toUpperCase();
            String name2 = us2.getName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }};


    public EmployeePosition(String name) {
        this.name = name;
        }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}
