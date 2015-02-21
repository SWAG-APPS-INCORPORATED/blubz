package com.swagappsincorporated.blubz;

import android.content.*;
import com.swagappsincorporated.blubz.ReturnContent.AlarmService;

/**
 * Created by Swag Apps on 5/6/14.
 * Inspired by http://stackoverflow.com/questions/11527581/how-to-ensure-alarmmanager-survives-phone-restart-and-application-kill
 */
public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        Intent startAlarmServiceIntent = new Intent(context, AlarmService.class);
        startAlarmServiceIntent.putExtra("notifType", "all");
        context.startService(startAlarmServiceIntent);

    }



}
