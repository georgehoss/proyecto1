package com.tenneco.tennecoapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by ghoss on 25/11/2018.
 */
public class SmsList  {
    public static final String DB_SMS_LIST = "smslists";
    private String name;
    private Map<String, Sms> sms_numbers;
    private String id;

    public static Comparator<SmsList> NameComparator = new Comparator<SmsList>() {

        public int compare(SmsList us1, SmsList us2) {
            String name1 = us1.getName().toUpperCase();
            String name2 = us2.getName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }
    };


    public SmsList() {
    }

    public SmsList(String id) {
        this.id = id;
    }

    public SmsList(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public SmsList(String name, Map<String,Sms> sms_numbers, String id) {
        this.name = name;
        this.sms_numbers = sms_numbers;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String,Sms> getSms_numbers() {
        return sms_numbers;
    }

    public void setSms_numbers(Map<String,Sms> sms_numbers) {
        this.sms_numbers = sms_numbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Sms> getNumbers(){
       return new ArrayList<Sms>(sms_numbers.values());
    }
}
