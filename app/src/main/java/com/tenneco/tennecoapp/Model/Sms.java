package com.tenneco.tennecoapp.Model;

import java.util.Comparator;

/**
 * Created by ghoss on 25/11/2018.
 */
public class Sms {
    public static final String DB_SMS = "sms_numbers";
    private String name;
    private String number;
    private String id;

    public Sms() {
    }

    public static Comparator<Sms> NameComparator = new Comparator<Sms>() {

        public int compare(Sms us1, Sms us2) {
            String name1 = us1.getName().toUpperCase();
            String name2 = us2.getName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }
    };


    public Sms(String name, String number,String id) {
        this.name = name;
        this.number = number;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
