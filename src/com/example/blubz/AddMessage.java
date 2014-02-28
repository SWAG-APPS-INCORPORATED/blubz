package com.example.blubz;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_message);

        datasource = new CommentsDataSource(this);
        datasource.open();


        //editText = (EditText) findViewById(R.id.message);
        //button = (Button) findViewById(R.id.addButton);


        if(!datasource.isEmpty()){
            long timestamp = datasource.getMostRecentTimestamp();
            if(isSameDay(timestamp,System.currentTimeMillis())){

                button.setEnabled(false);
            }
        }


    }



    public void addMessage(View view){
        String message = editText.getText().toString();
        long timestamp = System.currentTimeMillis();
        datasource.createComment(message, timestamp);
        editText.setText(null);
        editText.setHint("Message added!");
        button.setEnabled(false);

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
}