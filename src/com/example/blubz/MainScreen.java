package com.example.blubz;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by macalester on 2/19/14.
 */
public class MainScreen extends Activity {

    public final static String INTENT_MESSAGE = "com.example.blubz.MESSAGE";
    public final static String INTENT_DATE = "com.example.blubz.DATE";
    private CommentsDataSource commentdatasource;
    private SharedPreferences sharedPrefs;

    private Random random;
    private ImageButton secretButton;
    Button button;
    ImageView backgroundImage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);
        //changeLayout();

        commentdatasource = new CommentsDataSource(this);
        commentdatasource.open();

        sharedPrefs = getSharedPreferences("myPrefs",0);

        secretButton = (ImageButton)findViewById(R.id.secretButton);

        if(SharedPreferencesHelper.getValue(sharedPrefs, "notification")==0){
            //TODO: MAKE THE ABOUT BLUBZ PAGE SHOW UP
            setInitialNotificationTime();
        }

        if(!commentdatasource.isEmpty()){
            secretButtonCheck();
        }
        random = new Random();
    }

    public void changeLayout() {

        Calendar currentTime = Calendar.getInstance();

        backgroundImage = (ImageView) findViewById(R.id.imageviewmain);

        if ((22 <= (currentTime.get(Calendar.HOUR_OF_DAY)) && (currentTime.get(Calendar.HOUR_OF_DAY)) < 0)){
            backgroundImage.setImageResource(R.drawable.mainscreenstars);
        }

        if ((0 <= (currentTime.get(Calendar.HOUR_OF_DAY)) && (currentTime.get(Calendar.HOUR_OF_DAY)) < 6)){
            backgroundImage.setImageResource(R.drawable.mainscreenstars);
        }

        if ((6 <= (currentTime.get(Calendar.HOUR_OF_DAY)) && (currentTime.get(Calendar.HOUR_OF_DAY)) < 10)){
            backgroundImage.setImageResource(R.drawable.mainscreensunrise);
        }

        if ((10 <= (currentTime.get(Calendar.HOUR_OF_DAY)) && (currentTime.get(Calendar.HOUR_OF_DAY)) < 14)){
            backgroundImage.setImageResource(R.drawable.mainscreenhills);
        }

        if ((14 <= (currentTime.get(Calendar.HOUR_OF_DAY)) && (currentTime.get(Calendar.HOUR_OF_DAY)) < 18)){
            backgroundImage.setImageResource(R.drawable.mainscreenwaves1);
        }

        if ((18 <= (currentTime.get(Calendar.HOUR_OF_DAY)) && (currentTime.get(Calendar.HOUR_OF_DAY)) < 22)){
            backgroundImage.setImageResource(R.drawable.mainscreensky);
        }

        List<Integer> backgroundList = Arrays.asList(R.drawable.mainscreenstars, R.drawable.mainscreensunrise,
                R.drawable.mainscreenhills, R.drawable.mainscreenwaves, R.drawable.mainscreensky);

        float divisor = 24/backgroundList.size();

        int sectionOfDay = (int)(currentTime.get(Calendar.HOUR_OF_DAY) / divisor);


        backgroundImage.setImageResource(backgroundList.get(sectionOfDay));

    }

    public void goToBlubChoice(View view) {

        if(!commentdatasource.isEmpty()){
            long lastTimestamp = commentdatasource.getMostRecentTimestamp();
            if(isSameDay(lastTimestamp,System.currentTimeMillis())){
                showDialogBox("You've already blubbed today!", "Sorry, but you have to wait until tomorrow to blub again.");
                return;
            }
        }


        Intent intent = new Intent(this, BlubChoiceActivity.class);

        startActivity(intent);
    }

    public void goToAboutBlubs(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }



    public void goToContent(View view) {
        Intent intent = new Intent(this, ReturnContent.class);

        List<Comment> allMessages = commentdatasource.getAllComments();





        int rand = random.nextInt(allMessages.size());

        //String message = Integer.toString(allMessages.size());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Comment randComment = allMessages.get(rand);
        String intentMessage = randComment.getComment();
        long timestamp = randComment.getTimestamp();
        Calendar timestampCalendar = Calendar.getInstance();
        timestampCalendar.setTimeInMillis(timestamp);
        String intentDate = simpleDateFormat.format(timestampCalendar.getTime());

        intent.putExtra(INTENT_MESSAGE, intentMessage);
        intent.putExtra(INTENT_DATE, intentDate);
        secretButton.setVisibility(View.GONE);

        long currentTime = System.currentTimeMillis();
        SharedPreferencesHelper.setValue(sharedPrefs, "secretButton", currentTime);

        startActivity(intent);

    }

    //Code below from android tutorial on action bar http://developer.android.com/training/basics/actionbar/setting-up.html
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainscreen_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSettings(){

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void secretButtonCheck(){


        long currentTime = System.currentTimeMillis();


        long timestampTime = SharedPreferencesHelper.getValue(sharedPrefs, "secretButton");
        Calendar currentCalendar = Calendar.getInstance();
        Calendar timestampCalendar = Calendar.getInstance();

        currentCalendar.setTimeInMillis(currentTime);
        timestampCalendar.setTimeInMillis(timestampTime);

        if((currentCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
                && (timestampCalendar.get(Calendar.DATE) != currentCalendar.get(Calendar.DATE))) {
            secretButton.setVisibility(View.VISIBLE);
        }
    }

    private void setInitialNotificationTime(){

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);

        setNotificationTime(calendar);


    }

    public void setNotificationTime(Calendar notificationTime){

        Intent intent = new Intent(this, NotifyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long dailyNotificationTime = notificationTime.getTimeInMillis();
        if(notificationTime.getTimeInMillis()-System.currentTimeMillis() < 0){
            dailyNotificationTime += 1000 * 60 * 60 * 24;
        }

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, dailyNotificationTime, 1000 * 60 * 60 * 24, pendingIntent);

        SharedPreferencesHelper.setValue(sharedPrefs, "notification", dailyNotificationTime);

    }

    private void showDialogBox(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();

    }

    private boolean isSameDay(long timestamp1, long timestamp2){
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTimeInMillis(timestamp1);
        calendar2.setTimeInMillis(timestamp2);

        return(calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR));

    }


}