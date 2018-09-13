package com.tenneco.tennecoapp.Model;

/**
 * Created by ghoss on 11/09/2018.
 */
public class EmployeePosition {
    private String name;
    private String operator;

    public EmployeePosition() {
    }

    public EmployeePosition(String name, String operator) {
        this.name = name;
        this.operator = operator;
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
}
