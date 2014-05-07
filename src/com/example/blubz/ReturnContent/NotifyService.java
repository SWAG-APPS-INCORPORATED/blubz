package com.example.blubz.ReturnContent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.blubz.BlubChoiceActivity;
import com.example.blubz.Database.ContentDataSource;
import com.example.blubz.MainScreen;
import com.example.blubz.R;
import com.example.blubz.TimeHelper;

/**
 * Created by Nathan on 2/27/14.
 */
public class NotifyService extends Service {

    private ContentDataSource dataSource;


    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand (Intent myIntent, int flags, int startId) {



        String notifType = myIntent.getStringExtra("notifType");

        String message;
        String title;
        int smallIcon;
        Intent intent;

        if(notifType.equals("daily")){
            dataSource = new ContentDataSource(this);
            dataSource.open();

            long lastTimestamp = dataSource.getMostRecentTimestamp();
            if(TimeHelper.isSameDay(lastTimestamp, System.currentTimeMillis())){
                return 0;
            }

            message = "Time to enter a blub for the day!";
            title = getString(R.string.title);
            smallIcon = R.drawable.blubiconsmall;
            intent = new Intent(this , BlubChoiceActivity.class);


        }else if(notifType.equals("secret")){
            message = "The portal to your past awaits";
            title = getString(R.string.title);
            smallIcon = R.drawable.secretbuttonsmall;
            intent = new Intent(this , MainScreen.class);
        }else{
            return 0;
        }




        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);




        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(smallIcon)
                .setContentIntent(contentIntent)
                .build();


        mNM.notify(1, notification);


        return 0;
    }

}
