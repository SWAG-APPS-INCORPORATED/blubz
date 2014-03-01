package com.example.blubz;

/**
 * Created by Nathan on 2/27/14.
 */
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;


public class SettingsActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setNotification(View view){

        TimePicker timePicker = (TimePicker) findViewById(R.id.notifTime);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        calendar.set(Calendar.AM_PM, Calendar.AM);


        //editText.setText(Integer.toString(calendar.get(Calendar.AM_PM)));

        Intent intent = new Intent(this, NotifyService.class);


        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);



        Intent intent2 = new Intent(this, MainScreen.class);
        startActivity(intent2);




    }
}