package com.example.blubz;

import android.app.*;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;

import java.util.Calendar;

/**
 * Created by Nathan on 2/27/14.
 */
public class NotifyService extends Service {

    private CommentsDataSource commentsdatasource;


    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand (Intent myIntent, int flags, int startId) {

        commentsdatasource = new CommentsDataSource(this);
        commentsdatasource.open();


        // Get the message from the intent
        String message = "Time to enter a blub for the day!";
        String title = getString(R.string.title);


        if(!commentsdatasource.isEmpty()){
            long lastTimestamp = commentsdatasource.getMostRecentTimestamp();
            if(isSameDay(lastTimestamp,System.currentTimeMillis())){
                return 0;
            }
        }

        Intent intent = new Intent(this , AddMessage.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);




        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.icon1)
                .setContentIntent(contentIntent)
                .build();


        mNM.notify(1, notification);


        return 0;
    }

    private boolean isSameDay(long timestamp1, long timestamp2){
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTimeInMillis(timestamp1);
        calendar2.setTimeInMillis(timestamp2);

        return(calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR));

    }
}
