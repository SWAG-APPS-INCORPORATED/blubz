package com.example.blubz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Nathan on 2/27/14.
 */
public class NotifyService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand (Intent myIntent, int flags, int startId) {

        // Get the message from the intent
        String message = "Time to enter a blub for the day!";
        String title = getString(R.string.title);




        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.icon, message, System.currentTimeMillis());
        Intent intent = new Intent(this , AddMessage.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this, title, message, contentIntent);
        mNM.notify(1, notification);

        return 0;
    }
}
