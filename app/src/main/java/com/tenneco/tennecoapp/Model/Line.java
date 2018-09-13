package com.tenneco.tennecoapp.Model;

import java.util.ArrayList;

/**
 * Created by ghoss on 11/09/2018.
 */
public class Line {
    private String name;
    private Shift first;
    private Shift second;
    private Shift third;
    private String leakFailureCounter;
    private ArrayList<ScrapReason> scrapReasons;
}
