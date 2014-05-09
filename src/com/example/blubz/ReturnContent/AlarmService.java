package com.example.blubz.ReturnContent;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import com.example.blubz.SharedPreferencesHelper;

import java.util.Calendar;

/**
 * Created by Swag Apps on 5/4/14.
 */
public class AlarmService extends IntentService {

    private AlarmManager alarmManager;
    private SharedPreferences sharedPrefs;

    public static final int DAILY_ID = 1;
    public static final int MONDAY_ID = 2;
    public static final int FRIDAY_ID = 3;

    public AlarmService(){
        super("AlarmService");
    }

    public void onHandleIntent (Intent myIntent) {

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        String notifType;
        if(myIntent.hasExtra("notifType")){
            notifType = myIntent.getStringExtra("notifType");
        }else{
            return;
        }
        sharedPrefs = getSharedPreferences("myPrefs",0);

        if(notifType.equals("daily")){
            setAlarm("daily",SharedPreferencesHelper.getValue(sharedPrefs,"notification"), DAILY_ID, AlarmManager.INTERVAL_DAY);
        }else if(notifType.equals("monday")){
            setAlarm("secret",getNotificationTime(Calendar.MONDAY), MONDAY_ID, AlarmManager.INTERVAL_DAY*7);
        }else if(notifType.equals("friday")){
            setAlarm("secret",getNotificationTime(Calendar.FRIDAY), FRIDAY_ID, AlarmManager.INTERVAL_DAY*7);
        }else if(notifType.equals("all")){
            setAllAlarms();
        }

    }

    public IBinder onBind(Intent intent) {
        return null;
    }


    private long getNotificationTime(int calendarDayOfWeek){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK,calendarDayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);

        long notificationTime;

        if(calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
            notificationTime = calendar.getTimeInMillis();
        }else{
            notificationTime = calendar.getTimeInMillis()+(AlarmManager.INTERVAL_DAY)*7;
        }

        return(notificationTime);

    }


    private void setAllAlarms(){

        long dailyNotificationTime = SharedPreferencesHelper.getValue(sharedPrefs,"notification");
        if(dailyNotificationTime < System.currentTimeMillis()){
            dailyNotificationTime += 1000 * 60 * 60 * 24;
        }

        setAlarm("daily",dailyNotificationTime, DAILY_ID, AlarmManager.INTERVAL_DAY);
        setAlarm("secret",getNotificationTime(Calendar.MONDAY), MONDAY_ID, AlarmManager.INTERVAL_DAY*7);
        setAlarm("secret",getNotificationTime(Calendar.FRIDAY), FRIDAY_ID, AlarmManager.INTERVAL_DAY*7);


    }


    private void setAlarm(String notificationType, long notificationTime, int id, long interval){
        Intent intent = new Intent(this, NotifyService.class);
        intent.putExtra("notifType", notificationType);
        PendingIntent pendingIntent = PendingIntent.getService(this, id, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationTime, interval, pendingIntent);

    }

}
