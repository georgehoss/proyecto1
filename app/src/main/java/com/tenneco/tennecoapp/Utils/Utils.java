package com.tenneco.tennecoapp.Utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ghoss on 14/09/2018.
 */
public class Utils {
    public static String getDateString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    public static String getTimeString(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        return dateFormat.format(new Date());
    }
}