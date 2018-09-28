package com.tenneco.tennecoapp.Model;

import java.util.ArrayList;
import java.util.Comparator;


public class User {
    public static final String DB_USER = "users";
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private int type;
    private ArrayList<Line> productionLines;

    public User() {
    }

    @Override
    public String toString() {
        return name;
    }

    public static Comparator<User> UserNameComparator = new Comparator<User>() {

        public int compare(User us1, User us2) {
            String user1 = us1.getName().toUpperCase();
            String user2 = us2.getName().toUpperCase();

            //ascending order
            return user1.compareTo(user2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

    public User(String id, String name, String email, int type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.type = type;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Line> getProductionLines() {
        return productionLines;
    }

    public void setProductionLines(ArrayList<Line> ProductionLines) {
        this.productionLines = ProductionLines;
    }
}
