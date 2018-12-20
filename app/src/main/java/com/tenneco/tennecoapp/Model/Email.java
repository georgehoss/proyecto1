package com.tenneco.tennecoapp.Model;

import java.util.Comparator;

/**
 * Created by ghoss on 26/09/2018.
 */
public class Email {
    public static final String DB = "emails";
    private String id;
    private String email;
    private String name;
    boolean shift1;
    boolean shift2;
    boolean shift3;
    boolean cc1;
    boolean cc2;
    boolean cc3;

    public Email() {
    }

    public static Comparator<Email> NameComparator = new Comparator<Email>() {

        public int compare(Email us1, Email us2) {
            String user1 = us1.getName().toUpperCase();
            String user2 = us2.getName().toUpperCase();

            //ascending order
            return user1.compareTo(user2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

    @Override
    public String toString() {
        return email;
    }

    public Email(String id) {
        this.id = id;
    }

    public Email(Email email){
        this.id = email.getId();
        this.name = email.getName();
        this.email = email.getEmail();
        this.setShift1(true);
        this.setShift2(true);
        this.setShift3(true);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShift1() {
        return shift1;
    }

    public void setShift1(boolean shift1) {
        this.shift1 = shift1;
    }

    public boolean isShift2() {
        return shift2;
    }

    public void setShift2(boolean shift2) {
        this.shift2 = shift2;
    }

    public boolean isShift3() {
        return shift3;
    }

    public void setShift3(boolean shift3) {
        this.shift3 = shift3;
    }

    public boolean isCc1() {
        return cc1;
    }

    public void setCc1(boolean cc1) {
        this.cc1 = cc1;
    }

    public boolean isCc2() {
        return cc2;
    }

    public void setCc2(boolean cc2) {
        this.cc2 = cc2;
    }

    public boolean isCc3() {
        return cc3;
    }

    public void setCc3(boolean cc3) {
        this.cc3 = cc3;
    }
}
