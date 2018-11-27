package com.tenneco.tennecoapp.Model;

import java.util.Comparator;

/**
 * Created by ghoss on 26/11/2018.
 */
public class ReasonDelay {
    public static final String DB_DELAY_REASONS = "delay_reasons";
    private String name;
    private String id;

    public ReasonDelay() {
    }

    public ReasonDelay(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public static Comparator<ReasonDelay> NameComparator = new Comparator<ReasonDelay>() {

        public int compare(ReasonDelay us1, ReasonDelay us2) {
            String name1 = us1.getName().toUpperCase();
            String name2 = us2.getName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }};

    @Override
    public String toString() {
        return name;
    }

    public ReasonDelay(String name) {
        this.name = name;
    }

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
}
