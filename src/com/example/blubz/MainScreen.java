package com.example.blubz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by macalester on 2/19/14.
 */
public class MainScreen extends Activity {

    public final static String INTENT_MESSAGE = "com.example.blubz.MESSAGE";
    public final static String INTENT_DATE = "com.example.blubz.DATE";
    private CommentsDataSource commentdatasource;
    private ContentDataSource contentdatasource;
    public long NotificationTime;
    private Random random;
    private ImageButton secretButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);

        commentdatasource = new CommentsDataSource(this);
        commentdatasource.open();

        contentdatasource = new ContentDataSource(this);
        contentdatasource.open();

        secretButton = (ImageButton)findViewById(R.id.secretButton);

        if(contentdatasource.isCount(0)){
            //TO-DO: MAKE THE ABOUT BLUBZ PAGE SHOW UP
            setInitialNotificationTime();
        }

        if(!commentdatasource.isEmpty()){
            secretButtonCheck();
        }
        random = new Random();
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



        //if (allMessages == null) {
        if(0 ==1){
            // TODO: add empty string notification
        } else {

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
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTimeInMillis(currentTime);
            String date = simpleDateFormat.format(currentCalendar.getTime());

            contentdatasource.createContent(date, currentTime);

            startActivity(intent);
        }
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

        long timestampTime = 0;
        long currentTime = System.currentTimeMillis();

        if(!contentdatasource.isCount(1)){
            timestampTime = contentdatasource.getMostRecentTimestamp();
        }
        Calendar currentCalendar = Calendar.getInstance();
        Calendar timestampCalendar = Calendar.getInstance();

        currentCalendar.setTimeInMillis(currentTime);
        timestampCalendar.setTimeInMillis(timestampTime);

        if((currentCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
                && (timestampCalendar.get(Calendar.DATE) != currentCalendar.get(Calendar.DATE))) {
            secretButton.setVisibility(View.VISIBLE);
        }
    }

    private void setInitialNotificationTime(){

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);

        /**
        SettingsActivity s1 = new SettingsActivity();
        s1.setNotificationTime(calendar);
        **/

        contentdatasource.createContent("notification",calendar.getTimeInMillis());


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