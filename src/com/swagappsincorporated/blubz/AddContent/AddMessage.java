package com.swagappsincorporated.blubz.AddContent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.swagappsincorporated.blubz.Database.ContentDataSource;
import com.swagappsincorporated.blubz.MainScreen;
import com.swagappsincorporated.blubz.R;
import com.swagappsincorporated.blubz.TimeHelper;

import com.swagappsincorporated.blubz.*;

import java.util.Calendar;

/**
 * Created by Swag Apps Incorporated on 2/19/14.
 */
public class AddMessage extends Activity {

/*
     We used the following tutorial to learn how to create Java Objects that can be used to control SQL Queries.
     Source: http://www.vogella.com/tutorials/AndroidSQLite/article.html
*/

    private ContentDataSource datasource;
    public final static String INTENT_MESSAGE = "com.example.DatabaseTest.MESSAGE";
    private EditText editText;
    private ImageButton button;
    private TextView dateText;
    private TextView characterView;
    private EditText characterText;
    private SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_message);

        datasource = new ContentDataSource(this);
        datasource.open();

        dateText = (TextView) findViewById(R.id.date);
        editText = (EditText) findViewById(R.id.messageEditText);
        button = (ImageButton) findViewById(R.id.addBlubButton);

        setDateText();

        characterText=(EditText)findViewById(R.id.messageEditText);
        characterView=(TextView)findViewById(R.id.characters);
        characterText.addTextChangedListener(mTextEditorWatcher);

        long lastTimestamp = datasource.getMostRecentTimestamp();

        sharedPrefs = getSharedPreferences("myPrefs",0);

        String draftText = SharedPreferencesHelper.getString(sharedPrefs,"draft");
        if(draftText !=  null){
            editText.setText(draftText);
        }

        if(TimeHelper.isSameDay(lastTimestamp,System.currentTimeMillis())){
            editText.setHint("See you tomorrow!");
        }
    }

    @Override
    public void onPause(){
        String draftText = editText.getText().toString();
        SharedPreferencesHelper.setString(sharedPrefs, "draft", draftText);
        super.onPause();
    }

    public void addMessage(View view){
        String message = editText.getText().toString();
        long timestamp = System.currentTimeMillis();

        long lastTimestamp = datasource.getMostRecentTimestamp();
        if(TimeHelper.isSameDay(lastTimestamp,System.currentTimeMillis())){
            showDialogBox("You've already blubbed today!", "Sorry, but you have to wait until tomorrow to blub again.",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id){
                        Intent intent = new Intent(AddMessage.this, MainScreen.class);
                        startActivity(intent);
                    }
                });
            editText.setHint("See you tomorrow!");
            return;

        }

        if(message.isEmpty()){
            showDialogBox("Empty blub!", "Please enter something real in your blub, bub.", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id){
                //Do nothing
                }
            });
            return;
        }

        datasource.createMessageContent(message, timestamp);

        showDialogBox("Thanks for blubbing", "Don't forget to blub again tomorrow!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id){
                Intent intent = new Intent(AddMessage.this, MainScreen.class);
                startActivity(intent);
                finish();

            }
        });

        editText.setText(null);
        editText.setHint("See you tomorrow!");

    }

//Source: http://akashkubavat.wordpress.com/2012/06/08/hide-virtual-keyboard-when-touch-out-side-edittext-in-android/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }


    private void setDateText(){
        long dateInMillis = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);

        String day = Integer.toString(calendar.get(Calendar.DATE));
        String month = Integer.toString(calendar.get(Calendar.MONTH)+1);
        String year = Integer.toString(calendar.get(Calendar.YEAR));

        String date = month.concat(".").concat(day).concat(".").concat(year);

        dateText.setText(date);
    }


//Source: http://stackoverflow.com/questions/3013791/live-character-count-for-edittext-android

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            characterView.setText(String.valueOf(140 - s.length()));
        }
        public void afterTextChanged(Editable s) {
        }
    };

    private void showDialogBox(String title, String message, DialogInterface.OnClickListener onClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", onClick);
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