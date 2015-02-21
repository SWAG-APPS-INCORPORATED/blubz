package com.swagappsincorporated.blubz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.swagappsincorporated.blubz.Database.ContentDataSource;
import com.swagappsincorporated.blubz.Database.Image;
import com.swagappsincorporated.blubz.Database.Message;
import com.swagappsincorporated.blubz.Preferences.SettingsActivity;
import com.swagappsincorporated.blubz.ReturnContent.AlarmService;
import com.swagappsincorporated.blubz.ReturnContent.ReturnContent;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Swag Apps on 2/19/14.
 */
public class MainScreen extends Activity {

    public final static String INTENT_MESSAGE = "com.example.blubz.MESSAGE";
    public final static String INTENT_DATE = "com.example.blubz.DATE";
    public final static String INTENT_BOOLEAN = "com.example.blubz.BOOLEAN";
    public final static String INTENT_IMAGE = "com.example.blubz.IMAGE";
    public final static int DIALOG_TYPE_YES_NO = 1;
    public final static int DIALOG_TYPE_OK = 2;


    private ContentDataSource contentdatasource;
    private SharedPreferences sharedPrefs;

    private Random random;
    private ImageButton secretButton;
    private ImageView backgroundImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);
        changeLayout();

        random = new Random();

        contentdatasource = new ContentDataSource(this);
        contentdatasource.open();

        sharedPrefs = getSharedPreferences("myPrefs",0);

        secretButton = (ImageButton)findViewById(R.id.secretButton);


        if(SharedPreferencesHelper.getValue(sharedPrefs, "notification")==0){ //on first use
            setInitialNotifications();
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        if(!contentdatasource.isImagesEmpty() || !contentdatasource.isMessagesEmpty()){
            secretButtonCheck();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        changeLayout();
        secretButtonCheck();
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
                goToSettings();
                return true;
            case R.id.action_about:
                goToAbout();
                return true;
            case R.id.action_delete:
                deleteSavedBlubz();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToSettings(){

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void goToAbout(){

        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void goToBlubChoice(View view) {

        long lastTimestamp = contentdatasource.getMostRecentTimestamp();

        if(TimeHelper.isSameDay(lastTimestamp,System.currentTimeMillis())){
            showDialogBox("You've already blubbed today!", "Sorry, but you have to wait until tomorrow to blub again.", DIALOG_TYPE_OK, new DialogInterface.OnClickListener()  {
                public void onClick(DialogInterface dialog,int id){
                    //Do nothing
                }
            });
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

    }

    public void changeLayout() {

        Calendar currentTime = Calendar.getInstance();

        backgroundImage = (ImageView) findViewById(R.id.imageviewmain);

        List<Integer> backgroundList = Arrays.asList(R.drawable.mainscreenwhiteflower,R.drawable.mainscreenpathway,R.drawable.mainscreenflowerfields,
                R.drawable.mainscreenpurpleflower,R.drawable.mainscreenmosaic,R.drawable.mainscreencloudfield);

        double divisor = 24.0 / backgroundList.size();
        double backgroundIndex = (double)currentTime.get(Calendar.HOUR_OF_DAY) / divisor;

        backgroundImage.setImageResource(backgroundList.get((int)backgroundIndex));

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

        }else{
            secretButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setInitialNotifications(){

        Calendar notificationTime = Calendar.getInstance();

        notificationTime.set(Calendar.HOUR_OF_DAY, 18);
        notificationTime.set(Calendar.MINUTE, 0);

        long dailyNotificationTime = notificationTime.getTimeInMillis() + 1000 * 60 * 60 * 24;


        SharedPreferencesHelper.setValue(sharedPrefs, "notification", dailyNotificationTime);

        Intent startAlarmServiceIntent = new Intent(this, AlarmService.class);
        startAlarmServiceIntent.putExtra("notifType", "all");
        startService(startAlarmServiceIntent);


    }

    public void deleteSavedBlubz(){
        showDialogBox("Delete...", "Are you sure you want to delete all saved blubz?", DIALOG_TYPE_YES_NO, new DialogInterface.OnClickListener()  {
            public void onClick(DialogInterface dialog,int id){
                contentdatasource.clearDatabases();
                showDialogBox("Deleted!", "All of your blubz are deleted!", DIALOG_TYPE_OK, new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog,int id){
                        //Do nothing
                    }
                });
            }
        });
    }

    private void showDialogBox(String title, String message, int type, DialogInterface.OnClickListener onClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        if(type == DIALOG_TYPE_YES_NO){
        builder.setPositiveButton("YES", onClick);
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()  {
                public void onClick(DialogInterface dialog,int id){
                    //Do nothing
                }
            });
        }

        if(type == DIALOG_TYPE_OK){
        builder.setPositiveButton("OK", onClick);
        }

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();

            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();

    }




}