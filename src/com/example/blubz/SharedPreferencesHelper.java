package com.example.blubz;

import android.content.SharedPreferences;

/**
 * Created by Swag Apps on 4/22/14.
 */
public class SharedPreferencesHelper {


    public static void setValue(SharedPreferences sharedPrefs, String name, long value){

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong(name, value);
        editor.commit();

    }

    public static void setString(SharedPreferences sharedPrefs, String name, String string){

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(name, string);
        editor.commit();

    }

    public static long getValue(SharedPreferences sharedPrefs, String name){
        return sharedPrefs.getLong(name, 0);

    }

    public static String getString(SharedPreferences sharedPrefs, String name){
        return sharedPrefs.getString(name, null);

    }

}
