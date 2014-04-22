package com.example.blubz;

import android.content.SharedPreferences;

/**
 * Created by Nathan on 4/22/14.
 */
public class SharedPreferencesHelper {


    public SharedPreferencesHelper(SharedPreferences sharedPrefs){

    }

    public static void setValue(SharedPreferences sharedPrefs, String name, long value){

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong(name, value);
        editor.commit();

    }

    public static long getValue(SharedPreferences sharedPrefs, String name){
        return sharedPrefs.getLong(name, 0);

    }

}
