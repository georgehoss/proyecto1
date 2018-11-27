package com.tenneco.tennecoapp.Utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ghoss on 14/09/2018.
 */
public class Utils {
    public static String getDateString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(new Date());
    }

    public static String getTimeString(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        return dateFormat.format(new Date());
    }

    public static String converTimeString(String input){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date date = null;
        try {
            date = dateFormat.parse(input);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            return formatter.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}