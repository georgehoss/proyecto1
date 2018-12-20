package com.tenneco.tennecoapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ghoss on 14/11/2018.
 */
public class StorageUtils {

    private static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences("pref", Context.MODE_PRIVATE);
    }
    public static void savePlantId(Context context, String plantId) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("plantId", plantId);
        editor.commit();
    }

    public static String getPlantId(Context context) {
        SharedPreferences pref = getSharedPref(context);
        String id = pref.getString("plantId", "0");
        return id;
    }

    public static void removePlantId(Context context) {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static void saveUserPermissions(Context context, int type)
    {
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("user_permissions", type);
        editor.commit();
    }

    public static int getUserPermissions(Context context) {
        SharedPreferences pref = getSharedPref(context);
        return pref.getInt("user_permissions", 0);
    }
}
