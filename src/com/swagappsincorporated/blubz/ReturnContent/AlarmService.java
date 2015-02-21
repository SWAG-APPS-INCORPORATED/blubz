package com.swagappsincorporated.blubz.ReturnContent;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import com.swagappsincorporated.blubz.SharedPreferencesHelper;

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
        sharedPrefs = getSharedPreferences("myPrefs",0);

        String notifType;
        if(myIntent.hasExtra("notifType")){
            notifType = myIntent.getStringExtra("notifType");
        }else{
            return;
        }

        if(notifType.equals("daily")){
            setAlarm("daily",SharedPreferencesHelper.getValue(sharedPrefs,"notification"), DAILY_ID, AlarmManager.INTERVAL_DAY);
        }else if(notifType.equals("monday")){
            setAlarm("secret",getSecretButtonNotificationTime(Calendar.MONDAY), MONDAY_ID, AlarmManager.INTERVAL_DAY*7);
        }else if(notifType.equals("friday")){
            setAlarm("secret",getSecretButtonNotificationTime(Calendar.FRIDAY), FRIDAY_ID, AlarmManager.INTERVAL_DAY*7);
        }else if(notifType.equals("all")){
            setAllAlarms();
        }

    }

    public IBinder onBind(Intent intent) {
        return null;
    }


    private long getSecretButtonNotificationTime(int calendarDayOfWeek){
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



    private long findNextNotificationTime(long notificationTimestamp){
        Calendar oldNotificationCal = Calendar.getInstance();
        oldNotificationCal.setTimeInMillis(notificationTimestamp);

        Calendar nextNotificationCal = Calendar.getInstance();
        nextNotificationCal.set(Calendar.HOUR_OF_DAY,oldNotificationCal.get(Calendar.HOUR_OF_DAY));
        nextNotificationCal.set(Calendar.MINUTE, oldNotificationCal.get(Calendar.MINUTE));

        notificationTimestamp = nextNotificationCal.getTimeInMillis();
        if(notificationTimestamp < System.currentTimeMillis()){
            notificationTimestamp += 1000 * 60 * 60 * 24;
        }

        return notificationTimestamp;

    }


    private void setAllAlarms(){
        setAlarm("daily",findNextNotificationTime(SharedPreferencesHelper.getValue(sharedPrefs,"notification")),
                DAILY_ID, AlarmManager.INTERVAL_DAY);
        setAlarm("secret",getSecretButtonNotificationTime(Calendar.MONDAY), MONDAY_ID, AlarmManager.INTERVAL_DAY*7);
        setAlarm("secret",getSecretButtonNotificationTime(Calendar.FRIDAY), FRIDAY_ID, AlarmManager.INTERVAL_DAY*7);
    }


    private void setAlarm(String notificationType, long notificationTime, int id, long interval){
        Intent intent = new Intent(this, NotifyService.class);
        intent.putExtra("notifType", notificationType);
        PendingIntent pendingIntent = PendingIntent.getService(this, id, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationTime, interval, pendingIntent);

    }

}
