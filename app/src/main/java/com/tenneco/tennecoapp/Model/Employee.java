package com.tenneco.tennecoapp.Model;

import java.util.Comparator;

/**
 * Created by ghoss on 21/09/2018.
 */
public class Employee {
    public static final String DB = "employee";
    private String id;
    private String fullName;
    private String photo;
    private boolean available;
    private String info;
    private String type;
    private int shift;

    @Override
    public String toString() {
        return fullName;
    }

    public Employee() {
    }

    public static Comparator<Employee> EmployeeNameComparator = new Comparator<Employee>() {

        public int compare(Employee us1, Employee us2) {
            String user1 = us1.getFullName().toUpperCase();
            String user2 = us2.getFullName().toUpperCase();

            //ascending order
            return user1.compareTo(user2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};


    public Employee(String name, String info) {
        this.fullName = name;
        this.info = info;
    }

    public Employee(String name, String info, String type) {
        this.fullName = name;
        this.info = info;
        this.type = type;
    }

    public Employee(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }



    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }
}
