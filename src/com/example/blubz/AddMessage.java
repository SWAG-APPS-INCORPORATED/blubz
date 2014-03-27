package com.example.blubz;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by macalester on 2/19/14.
 */
public class AddMessage extends Activity {

    // I used the following tutorial to learn how to create Java Objects that
    // can be used to control SQL Queries.
    // http://www.vogella.com/tutorials/AndroidSQLite/article.html

    private CommentsDataSource datasource;
    public final static String INTENT_MESSAGE = "com.example.DatabaseTest.MESSAGE";
    private EditText editText;
    private ImageButton button;
    private TextView dateText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_message);

        datasource = new CommentsDataSource(this);
        datasource.open();


        dateText = (TextView) findViewById(R.id.date);
        editText = (EditText) findViewById(R.id.messageEditText);
        button = (ImageButton) findViewById(R.id.addButton);

        setDateText();

        if(!datasource.isEmpty()){
            long lastTimestamp = datasource.getMostRecentTimestamp();
            if(isSameDay(lastTimestamp,System.currentTimeMillis())){
                editText.setHint("See you tomorrow!");

            }
        }




    }



    public void addMessage(View view){
        String message = editText.getText().toString();
        long timestamp = System.currentTimeMillis();

        if(message.isEmpty()){
            showDialogBox("Empty blub!", "Please enter something real in your blub, bub.");
        }else{

            if(!datasource.isEmpty()){
                long lastTimestamp = datasource.getMostRecentTimestamp();
                if(isSameDay(lastTimestamp,System.currentTimeMillis())){
                    showDialogBox("You've already blubbed today!", "Sorry, but you have to wait until tomorrow to blub again.");
                    return;

                }
            }

            datasource.createComment(message, timestamp);

            editText.setText(null);
            editText.setHint("Your Blub has been stored! See you tomorrow!");

            //button.setEnabled(false);
        }
    }


    public void viewMessages(View view){
        Intent intent = new Intent(this, ViewMessage.class);
        List<Comment> allMessages = datasource.getAllComments();
        String fullMessage = "";
        for(Comment singleComment : allMessages){
            fullMessage = fullMessage + " " + singleComment.getComment();
        }
        intent.putExtra(INTENT_MESSAGE, fullMessage);
        startActivity(intent);
    }

    public void clearMessages(View view){
        List<Comment> allMessages = datasource.getAllComments();
        for(Comment singleComment : allMessages){
            datasource.deleteComment(singleComment);
        }
        editText.setText(null);
        editText.setHint("All messages deleted.");
    }

    private boolean isSameDay(long timestamp1, long timestamp2){
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTimeInMillis(timestamp1);
        calendar2.setTimeInMillis(timestamp2);

        return(calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR));

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