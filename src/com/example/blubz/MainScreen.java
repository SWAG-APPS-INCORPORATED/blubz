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
import com.example.blubz.Database.ContentDataSource;
import com.example.blubz.Database.Image;
import com.example.blubz.Database.Message;
import com.example.blubz.Preferences.SettingsActivity;
import com.example.blubz.ReturnContent.NotifyService;
import com.example.blubz.ReturnContent.ReturnContent;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by macalester on 2/19/14.
 */
public class MainScreen extends Activity {

    public final static String INTENT_MESSAGE = "com.example.blubz.MESSAGE";
    public final static String INTENT_DATE = "com.example.blubz.DATE";
    public final static String INTENT_BOOLEAN = "com.example.blubz.BOOLEAN";
    public final static String INTENT_IMAGE = "com.example.blubz.IMAGE";
    private ContentDataSource contentdatasource;
    private SharedPreferences sharedPrefs;

    private Random random;
    private ImageButton secretButton;
    Button button;
    ImageView backgroundImage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);
        changeLayout();

        contentdatasource = new ContentDataSource(this);
        contentdatasource.open();

        sharedPrefs = getSharedPreferences("myPrefs",0);

        secretButton = (ImageButton)findViewById(R.id.secretButton);

        if(SharedPreferencesHelper.getValue(sharedPrefs, "notification")==0){
            setInitialNotificationTime();
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        }

        if(!contentdatasource.isMessagesEmpty() || !contentdatasource.isImagesEmpty()){
            secretButtonCheck();
        }
        random = new Random();
    }

    public void changeLayout() {

        Calendar currentTime = Calendar.getInstance();

        backgroundImage = (ImageView) findViewById(R.id.imageviewmain);

        List<Integer> backgroundList = Arrays.asList(R.drawable.whiteflower,R.drawable.pathway,R.drawable.flowerfields,
                R.drawable.purpleflower,R.drawable.mosaic,R.drawable.cloudfield);

        double divisor = 24.0 / backgroundList.size();
        double backgroundIndex = (double)currentTime.get(Calendar.HOUR_OF_DAY) / divisor;

        backgroundImage.setImageResource(backgroundList.get((int)backgroundIndex));

    }


    public void goToBlubChoice(View view) {

        long lastTimestamp = contentdatasource.getMostRecentTimestamp();

        if(TimeHelper.isSameDay(lastTimestamp,System.currentTimeMillis())){
            showDialogBox("You've already blubbed today!", "Sorry, but you have to wait until tomorrow to blub again.");
            return;
        }



        Intent intent = new Intent(this, BlubChoiceActivity.class);

        startActivity(intent);
    }

    public void goToContent(View view) {
        Intent intent = new Intent(this, ReturnContent.class);

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        if(currentCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            List<Message> allMessages = contentdatasource.getAllMessages();
            boolean isImage = false;

            int rand = random.nextInt(allMessages.size());
            Message randMessage = allMessages.get(rand);
            String intentMessage = randMessage.getMessage();
            long timestamp = randMessage.getTimestamp();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Calendar timestampCalendar = Calendar.getInstance();
            timestampCalendar.setTimeInMillis(timestamp);
            String intentDate = simpleDateFormat.format(timestampCalendar.getTime());

            intent.putExtra(INTENT_BOOLEAN, isImage);
            intent.putExtra(INTENT_MESSAGE, intentMessage);
            intent.putExtra(INTENT_DATE, intentDate);
            secretButton.setVisibility(View.GONE);

            long currentTime = System.currentTimeMillis();
            SharedPreferencesHelper.setValue(sharedPrefs, "secretButton", currentTime);

            startActivity(intent);
        } else {
            List<Image> allImages = contentdatasource.getAllImages();
            boolean isImage = true;

            int rand = random.nextInt(allImages.size());
            Image randImage = allImages.get(rand);
            byte[] intentImage = randImage.getImage();
            long timestamp = randImage.getTimestamp();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Calendar timestampCalendar = Calendar.getInstance();
            timestampCalendar.setTimeInMillis(timestamp);
            String intentDate = simpleDateFormat.format(timestampCalendar.getTime());

            intent.putExtra(INTENT_BOOLEAN, isImage);
            intent.putExtra(INTENT_IMAGE, intentImage);
            intent.putExtra(INTENT_DATE, intentDate);
            secretButton.setVisibility(View.GONE);

            long currentTime = System.currentTimeMillis();
            SharedPreferencesHelper.setValue(sharedPrefs, "secretButton", currentTime);

            startActivity(intent);
        }


//        int rand = random.nextInt(allMessages.size());
//        Message randComment = allMessages.get(rand);
//        String intentMessage = randComment.getMessage();
//        long timestamp = randComment.getTimestamp();

        //String message = Integer.toString(allMessages.size());

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
//        Calendar timestampCalendar = Calendar.getInstance();
//        timestampCalendar.setTimeInMillis(timestamp);
//        String intentDate = simpleDateFormat.format(timestampCalendar.getTime());
//
//        intent.putExtra(INTENT_MESSAGE, intentMessage);
//        intent.putExtra(INTENT_DATE, intentDate);
//        secretButton.setVisibility(View.GONE);
//
//        long currentTime = System.currentTimeMillis();
//        SharedPreferencesHelper.setValue(sharedPrefs, "secretButton", currentTime);
//
//        startActivity(intent);

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
            case R.id.action_about:
                openAbout();
                return true;
            case R.id.action_delete:
                deleteSavedBlubz();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSettings(){

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openAbout(){

        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void deleteSavedBlubz(){

        Intent intent = new Intent(this, DeleteSavedBlubz.class);
        startActivity(intent);
    }

    private void secretButtonCheck(){


        long currentTime = System.currentTimeMillis();
        long timestampTime = SharedPreferencesHelper.getValue(sharedPrefs, "secretButton");

        Calendar currentCalendar = Calendar.getInstance();
        Calendar timestampCalendar = Calendar.getInstance();

        currentCalendar.setTimeInMillis(currentTime);
        timestampCalendar.setTimeInMillis(timestampTime);

        if(((currentCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && !contentdatasource.isMessagesEmpty())
                || (currentCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && !contentdatasource.isImagesEmpty()))
                && (timestampCalendar.get(Calendar.DATE) != currentCalendar.get(Calendar.DATE))) {
            secretButton.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, NotifyService.class);
            intent.putExtra("notifType", "secret");
            startService(intent); 
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
        intent.putExtra("notifType", "daily");
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


}