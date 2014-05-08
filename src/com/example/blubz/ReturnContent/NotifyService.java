package com.example.blubz.ReturnContent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import com.example.blubz.BlubChoiceActivity;
import com.example.blubz.Database.ContentDataSource;
import com.example.blubz.MainScreen;
import com.example.blubz.R;
import com.example.blubz.TimeHelper;
import com.example.blubz.*;
import java.util.Calendar;

/**
 * Created by Nathan on 2/27/14.
 */
public class NotifyService extends Service {

    private ContentDataSource dataSource;
    private SharedPreferences sharedPrefs;

    private class Notif{
        String message;
        String title;
        int smallIcon;
        Intent intent;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand (Intent myIntent, int flags, int startId) {


        dataSource = new ContentDataSource(this);
        dataSource.open();

        sharedPrefs = getSharedPreferences("myPrefs",0);

        Notif notif = null;
        String notifType = null;

        if(!myIntent.hasExtra("notifType")){
            return 0;

        }
        notifType = myIntent.getStringExtra("notifType");

        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        if(notifType.equals("daily")){

            dataSource = new ContentDataSource(this);
            dataSource.open();

            long lastTimestamp = dataSource.getMostRecentTimestamp();
            if(TimeHelper.isSameDay(lastTimestamp, System.currentTimeMillis())){
                return 0;
            }

            notif = dailyNotification();

        }else if(notifType.equals("secret")){
            if((today==Calendar.FRIDAY && !dataSource.isImagesEmpty()) ||
                    (today==Calendar.MONDAY && !dataSource.isMessagesEmpty())
                        && !TimeHelper.isSameDay(SharedPreferencesHelper.getValue(sharedPrefs,"secret"),Calendar.getInstance().getTimeInMillis())){
                notif = secretButtonNotification();

            }
        }

        notifyNow(notif);

        return 0;
    }

    private Notif dailyNotification(){

        Intent startAlarmServiceIntent = new Intent(this, AlarmService.class);
        startAlarmServiceIntent.putExtra("notifType", "daily");


        long lastTimestamp = dataSource.getMostRecentTimestamp();
        if(TimeHelper.isSameDay(lastTimestamp, System.currentTimeMillis())){
            return null;
        }


        Notif notif = new Notif();
        notif.message = "Time to enter a blub for the day!";
        notif.title = getString(R.string.title);
        notif.smallIcon = R.drawable.blubiconsmall;
        notif.intent = new Intent(this , BlubChoiceActivity.class);



        return notif;

    }

    private Notif secretButtonNotification(){
        Notif notif = new Notif();
        notif.message = "The portal to your past awaits.";
        notif.title = getString(R.string.title);
        notif.smallIcon = R.drawable.secretbuttonsmall;
        notif.intent = new Intent(this , MainScreen.class);

        Intent startAlarmServiceIntent = new Intent(this, AlarmService.class);

        Calendar today = Calendar.getInstance();
        String secretButtonDay = "";

        if(today.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){
            secretButtonDay = "monday";
        }else if(today.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
            secretButtonDay = "friday";
        }

        startAlarmServiceIntent.putExtra("notifType", secretButtonDay);
        //startService(startAlarmServiceIntent);

        return notif;
    }

    private void notifyNow(Notif notif){

        if(notif != null){

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notif.intent, 0);

            NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(notif.title)
                    .setContentText(notif.message)
                    .setSmallIcon(notif.smallIcon)
                    .setContentIntent(contentIntent)
                    .build();

            mNM.notify(1, notification);
        }

        this.stopSelf();
    }




}
