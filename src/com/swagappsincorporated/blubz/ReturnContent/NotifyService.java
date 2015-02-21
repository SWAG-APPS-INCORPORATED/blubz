package com.swagappsincorporated.blubz.ReturnContent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import com.swagappsincorporated.blubz.Database.ContentDataSource;
import com.swagappsincorporated.blubz.MainScreen;
import com.swagappsincorporated.blubz.R;
import com.swagappsincorporated.blubz.TimeHelper;
import com.swagappsincorporated.blubz.*;
import java.util.Calendar;

/**
 * Created by Swag Apps on 2/27/14.
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
        String notifType;

        if(!myIntent.hasExtra("notifType")){
            return 0;
        }

        notifType = myIntent.getStringExtra("notifType");

        if(notifType.equals("daily")){
            notif = dailyNotification();
        }else if(notifType.equals("secret")){
            notif = secretButtonNotification();
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
        notif.intent = new Intent(this , MainScreen.class);

        return notif;

    }

    private Notif secretButtonNotification(){

        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        //Checks to make sure that there is something to show if the secret button is pressed, and that
        //the secret button is actually visible before sending notification
        if((today==Calendar.FRIDAY && !dataSource.isImagesEmpty()) ||
                (today==Calendar.MONDAY && !dataSource.isMessagesEmpty())
                        && !TimeHelper.isSameDay(SharedPreferencesHelper.getValue(sharedPrefs,"secretButton"),
                        Calendar.getInstance().getTimeInMillis())){

            Notif notif = new Notif();
            notif.message = "The portal to your past awaits.";
            notif.title = getString(R.string.title);
            notif.smallIcon = R.drawable.secretbuttonsmall;
            notif.intent = new Intent(this , MainScreen.class);

            return notif;
        }else{
            return null;
        }
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
