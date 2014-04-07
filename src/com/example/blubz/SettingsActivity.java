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

    private ContentDataSource contentdatasource;
    private TimePicker timePicker;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        contentdatasource = new ContentDataSource(this);
        contentdatasource.open();

        timePicker = (TimePicker) findViewById(R.id.notifTime);
        Content content = contentdatasource.getContent("notification");

        long currentNotif = content.getTimestamp();

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

    public void setNotificationTime(Calendar calendar){
        Intent intent = new Intent(this, NotifyService.class);
        intent.putExtra("Calendar", calendar.getTimeInMillis());


        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        if(calendar.getTimeInMillis()-System.currentTimeMillis() < 0){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+1000 * 60 * 60 * 24,
                    1000 * 60 * 60 * 24, pendingIntent);
        }else{alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
        }

        contentdatasource.createContent("notification",calendar.getTimeInMillis());



        Intent intent2 = new Intent(this, MainScreen.class);
        startActivity(intent2);

    }
}