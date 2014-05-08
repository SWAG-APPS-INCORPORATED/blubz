package com.example.blubz.Preferences;

/**
 * Created by Nathan on 2/27/14.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import com.example.blubz.MainScreen;
import com.example.blubz.R;
import com.example.blubz.ReturnContent.AlarmService;
import com.example.blubz.SharedPreferencesHelper;

import java.util.Calendar;


public class SettingsActivity extends Activity {

    private SharedPreferences sharedPrefs;
    private TimePicker timePicker;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        timePicker = (TimePicker) findViewById(R.id.notifTime);
        sharedPrefs = getSharedPreferences("myPrefs", 0);

        setTimePicker();


    }

    public void setTimePicker(){
        long currentNotif = SharedPreferencesHelper.getValue(sharedPrefs, "notification");

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.setTimeInMillis(currentNotif);

        timePicker.setCurrentHour(notificationTime.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(notificationTime.get(Calendar.MINUTE));

    }

    public void setNotificationTime(View view){

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        notificationTime.set(Calendar.MINUTE, timePicker.getCurrentMinute());

        SharedPreferencesHelper.setValue(sharedPrefs,"notification", notificationTime.getTimeInMillis());

        Intent startAlarmServiceIntent = new Intent(this, AlarmService.class);
        startAlarmServiceIntent.putExtra("notifType", "daily");
        startService(startAlarmServiceIntent);

        Intent intent2 = new Intent(this, MainScreen.class);
        startActivity(intent2);

    }
}