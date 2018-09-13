package com.tenneco.tennecoapp.Model.Downtime;

import java.util.ArrayList;

/**
 * Created by ghoss on 12/09/2018.
 */
public class Zone {
    private String name;
    private ArrayList<Location> locations;

    public Zone() {
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
