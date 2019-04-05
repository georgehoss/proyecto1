package com.tenneco.tennecoapp.Utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ghoss on 14/09/2018.
 */
public class Utils {
    public static String getDateString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(new Date());
    }

    public static String getDateStamp() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
        return dateFormat.format(new Date());
    }

    public static String getTomorrowDateString() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        return dateFormat.format(tomorrow);
    }

    public static boolean compareTime(String time, String endtime) {

        String pattern = "HH:mm:ss a";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if(date1.after(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }


    public static boolean compareDate(String today, String date) {

        String pattern = "MM/dd/yyyy";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(today);
            Date date2 = sdf.parse(date);

            if(date2.before(date1)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    public static String getTimeDateString(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a MM/dd/yyyy");
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


    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    public static String getTimeDiference (String start, String end){
        String date = "";
        SimpleDateFormat dateFormat;
        if (start.length()<=12)
        dateFormat= new SimpleDateFormat("hh:mm:ss a");
        else
            dateFormat = new SimpleDateFormat("hh:mm:ss a MM/dd/yyyy");

        try{
            Date startDate = dateFormat.parse(start); // Set start date
            Date endDate   = dateFormat.parse(end); // Set end date
            long duration  = startDate.getTime() -  endDate.getTime();


            long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
            long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
            if (diffInHours==0)
                date = String.format("%02d:%02d", diffInMinutes %60 ,diffInSeconds %60);
            else
                if (diffInDays==0)
                    date = String.format("%01d:%02d:%02d", diffInHours %24 , diffInMinutes %60 ,diffInSeconds %60);
                else
                    date = String.format("%01d day(s), %02d:%02d:%02d",diffInDays, diffInHours %24 , diffInMinutes %60 ,diffInSeconds %60);
        } catch (ParseException e) {
            e.printStackTrace();
        }




        return date;
    }


    @SuppressLint("DefaultLocale")
    public static String durationDt(long duration)
    {
        String date;
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        if (diffInHours==0)
            date = String.format("%02d:%02d", diffInMinutes %60 ,diffInSeconds %60);
        else
        if (diffInDays==0)
            date = String.format("%01d:%02d:%02d", diffInHours %24 , diffInMinutes %60 ,diffInSeconds %60);
        else
            date = String.format("%01d day(s), %02d:%02d:%02d",diffInDays, diffInHours %24 , diffInMinutes %60 ,diffInSeconds %60);
        return  date;
    }


    @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
    public static long getTimeDiferenceMillis (String start, String end){
        String date = "";
        SimpleDateFormat dateFormat;
        if (start.length()<=12)
            dateFormat= new SimpleDateFormat("hh:mm:ss a");
        else
            dateFormat = new SimpleDateFormat("hh:mm:ss a MM/dd/yyyy");

        try{
            Date startDate = dateFormat.parse(start); // Set start date
            Date endDate   = dateFormat.parse(end); // Set end date
            return  endDate.getTime()-startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }




        return 0;
    }


    public static String getDay(String date){
        if (date!=null && date.length()>4)
        return date.substring(3,5);
        else
            return "";
    }

    public static String getMonth(String date){
        if (date!=null && date.length()>1)
        return date.substring(0,2);
        else
            return "";
    }

    public static String getYear(String date){
        if (date!=null && date.length()>5)
        return date.substring(6);
        else
            return "";
    }
}