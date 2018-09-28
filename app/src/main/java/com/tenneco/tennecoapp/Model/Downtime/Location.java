package com.tenneco.tennecoapp.Model.Downtime;

import com.tenneco.tennecoapp.Model.Line;

import java.util.Comparator;

/**
 * Created by ghoss on 12/09/2018.
 */
public class Location {
    private String name;

    public Location() {
    }

    public static Comparator<Location> NameComparator = new Comparator<Location>() {

        public int compare(Location us1, Location us2) {
            String name1 = us1.getName().toUpperCase();
            String name2 = us2.getName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }};

    public Location(String name) {
        this.name = name;
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
}
