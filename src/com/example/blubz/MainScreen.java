package com.example.blubz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by macalester on 2/19/14.
 */
public class MainScreen extends Activity {

    public final static String INTENT_MESSAGE = "com.example.DatabaseTest.MESSAGE";
    private CommentsDataSource datasource;
    public long NotificationTime;
    private Random random;
    Button secretButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);
        secretButton = (Button)findViewById(R.id.secretButton);
        secretButtonCheck();
    }

    public void goToMessage(View view) {
        Intent intent = new Intent(this, AddMessage.class);
        startActivity(intent);
    }

    public void goToPicture(View view) {
        // Add camera stuff here
    }

    public void goToContent(View view) {
        Intent intent = new Intent(this, ReturnContent.class);
        List<Comment> allMessages = datasource.getAllComments();
        if (allMessages == null) {
            // TODO: add empty string notification
        } else {
            int rand = random.nextInt(allMessages.size());
            String message = allMessages.get(rand).getComment();
            intent.putExtra(INTENT_MESSAGE, message);
            secretButton.setVisibility(View.GONE);
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
        long currentTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(currentTime);

        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
            secretButton.setVisibility(View.VISIBLE);
        }
    }
}