package com.tenneco.tennecoapp.Model.Downtime;

import java.util.Comparator;

/**
 * Created by ghoss on 12/09/2018.
 */
public class Reason {
    private String name;
    private String reason;

    public Reason() {
    }

    public static Comparator<Reason> NameComparator = new Comparator<Reason>() {

        public int compare(Reason us1, Reason us2) {
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

    public Reason(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
