package com.swagappsincorporated.blubz.Preferences;

/**
 * Created by Swag Apps on 2/27/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import com.swagappsincorporated.blubz.Database.ContentDataSource;
import com.swagappsincorporated.blubz.MainScreen;
import com.swagappsincorporated.blubz.R;
import com.swagappsincorporated.blubz.ReturnContent.AlarmService;
import com.swagappsincorporated.blubz.SharedPreferencesHelper;

import java.util.Calendar;


public class SettingsActivity extends Activity {

    private SharedPreferences sharedPrefs;
    private TimePicker timePicker;
    private ContentDataSource dataSource;
    private TextView countsView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        timePicker = (TimePicker) findViewById(R.id.notifTime);
        sharedPrefs = getSharedPreferences("myPrefs", 0);
        countsView = (TextView) findViewById(R.id.countView);
        dataSource = new ContentDataSource(this);
        dataSource.open();


        setTimePicker();
        if(!dataSource.isMessagesEmpty() || !dataSource.isImagesEmpty()){
            setContentCounts();
        }
    }

    public void setTimePicker(){
        long currentNotif = SharedPreferencesHelper.getValue(sharedPrefs, "notification");

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.setTimeInMillis(currentNotif);

        timePicker.setCurrentHour(notificationTime.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(notificationTime.get(Calendar.MINUTE));

    }

    public void setContentCounts() {
        int messageCount, imageCount;

        messageCount = dataSource.getMessageCount();
        imageCount = dataSource.getImageCount();

        countsView.setText("you have made " + messageCount + " text blubz, and "
                    + imageCount + " photo blubz!");
    }

    public void setNotificationTime(View view){

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        notificationTime.set(Calendar.MINUTE, timePicker.getCurrentMinute());

        SharedPreferencesHelper.setValue(sharedPrefs,"notification", notificationTime.getTimeInMillis());

        Intent startAlarmServiceIntent = new Intent(this, AlarmService.class);
        startAlarmServiceIntent.putExtra("notifType", "daily");
        startService(startAlarmServiceIntent);

        Intent intent2 = new Intent(this, MainScreen.class);
        startActivity(intent2);

    }
}