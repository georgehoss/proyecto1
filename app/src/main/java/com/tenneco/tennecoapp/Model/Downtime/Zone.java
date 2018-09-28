package com.tenneco.tennecoapp.Model.Downtime;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by ghoss on 12/09/2018.
 */
public class Zone {
    private String name;
    private ArrayList<Location> locations;

    public Zone() {
    }

    public static Comparator<Zone> NameComparator = new Comparator<Zone>() {

        public int compare(Zone us1, Zone us2) {
            String name1 = us1.getName().toUpperCase();
            String name2 = us2.getName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }};

    public Zone(String name, ArrayList<Location> locations) {
        this.name = name;
        this.locations = locations;
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

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }
}
