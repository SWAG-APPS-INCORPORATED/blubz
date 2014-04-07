package com.example.blubz;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.util.Calendar;

/**
 * Created by Nathan on 2/27/14.
 */
public class NotifyService extends Service {

    private ContentDataSource contentdatasource;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand (Intent myIntent, int flags, int startId) {


        // Get the message from the intent
        String message = "Time to enter a blub for the day!";
        String title = getString(R.string.title);




        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.icon1, message, System.currentTimeMillis());
        Intent intent = new Intent(this , AddMessage.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this, title, message, contentIntent);
        mNM.notify(1, notification);

        return 0;
    }
}
