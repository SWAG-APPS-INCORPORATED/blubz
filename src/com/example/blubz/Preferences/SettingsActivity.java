package com.example.blubz.Preferences;

/**
 * Created by Nathan on 2/27/14.
 */
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import com.example.blubz.MainScreen;
import com.example.blubz.ReturnContent.NotifyService;
import com.example.blubz.R;
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

        long currentNotif = SharedPreferencesHelper.getValue(sharedPrefs, "notification");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentNotif);

        Integer currentMinute = calendar.get(Calendar.MINUTE);
        Integer currentHour = calendar.get(Calendar.HOUR_OF_DAY);


        timePicker.setCurrentHour(currentHour);
        timePicker.setCurrentMinute(currentMinute);
    }

    public void setNotification(View view){

        TimePicker timePicker = (TimePicker) findViewById(R.id.notifTime);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        calendar.set(Calendar.AM_PM, Calendar.AM);

        setNotificationTime(calendar);

    }

    public void setNotificationTime(Calendar notificationTime){
        Intent intent = new Intent(this, NotifyService.class);
        intent.putExtra("notifType", "daily");
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long dailyNotificationTime = notificationTime.getTimeInMillis();
        if(notificationTime.getTimeInMillis()-System.currentTimeMillis() < 0){
            dailyNotificationTime += 1000 * 60 * 60 * 24;
        }

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, dailyNotificationTime, 1000 * 60 * 60 * 24, pendingIntent);

        SharedPreferencesHelper.setValue(sharedPrefs,"notification", dailyNotificationTime);

        Intent intent2 = new Intent(this, MainScreen.class);
        startActivity(intent2);

    }
}