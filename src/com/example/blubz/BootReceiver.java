package com.example.blubz;

import android.content.*;
import android.util.Log;
import com.example.blubz.returncontent.AlarmService;

/**
 * Created by Nathan on 5/6/14.
 * Inspired by http://stackoverflow.com/questions/11527581/how-to-ensure-alarmmanager-survives-phone-restart-and-application-kill
 */
public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        Intent startAlarmServiceIntent = new Intent(context, AlarmService.class);
        startAlarmServiceIntent.putExtra("notifType", "all");
        context.startService(startAlarmServiceIntent);

    }



}
