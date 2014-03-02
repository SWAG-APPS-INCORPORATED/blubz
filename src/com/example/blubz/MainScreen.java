package com.example.blubz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by macalester on 2/19/14.
 */
public class MainScreen extends Activity {

    public long NotificationTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);
    }

    public void goToMessage(View view) {
        Intent intent = new Intent(this, AddMessage.class);
        startActivity(intent);
    }

    public void goToPicture(View view) {
        // Add camera stuff here
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
}