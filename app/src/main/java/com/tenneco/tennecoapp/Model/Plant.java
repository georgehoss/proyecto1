package com.tenneco.tennecoapp.Model;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by ghoss on 14/11/2018.
 */
public class Plant {
    public static final String DB_PLANTS = "plants";
    public static final String DB_LIST_PLANTS = "list_plants";
    private String name;
    private String id;
    private String code;
    private String psw;
    private String address;


    public Plant() {
    }

    public Plant(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Plant(String name, String id, String code, String psw, String address) {
        this.name = name;
        this.id = id;
        this.code = code;
        this.psw = psw;
        this.address = address;
    }

    public static Comparator<Plant> NameComparator = new Comparator<Plant>() {

        public int compare(Plant us1, Plant us2) {
            String name1 = us1.getName().toUpperCase();
            String name2 = us2.getName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
