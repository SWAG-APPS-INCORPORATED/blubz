package com.example.blubz.AddContent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.blubz.*;
import com.example.blubz.Database.Comment;
import com.example.blubz.Database.CommentsDataSource;
import com.example.blubz.ReturnContent.ReturnContent;

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
    private TextView characterView;
    private EditText characterText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_message);

        datasource = new CommentsDataSource(this);
        datasource.open();


        dateText = (TextView) findViewById(R.id.date);
        editText = (EditText) findViewById(R.id.messageEditText);
        button = (ImageButton) findViewById(R.id.addBlubButton);

        setDateText();

        characterText=(EditText)findViewById(R.id.messageEditText);
        characterView=(TextView)findViewById(R.id.characters);
        characterText.addTextChangedListener(mTextEditorWatcher);

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


        if(!datasource.isEmpty()){
            long lastTimestamp = datasource.getMostRecentTimestamp();
            if(isSameDay(lastTimestamp,System.currentTimeMillis())){
                showDialogBox("You've already blubbed today!", "Sorry, but you have to wait until tomorrow to blub again.",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id){
                        Intent intent = new Intent(AddMessage.this, MainScreen.class);
                        startActivity(intent);

                    }
                });
                editText.setHint("See you tomorrow!");
                return;

            }
        }
        if(message.isEmpty()){
            showDialogBox("Empty blub!", "Please enter something real in your blub, bub.", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id){
                //Do nothing
                }
            });
            return;
        }

        datasource.createComment(message, timestamp);

        showDialogBox("Thanks for blubbing", "Don't forget to blub again tomorrow!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id){
                Intent intent = new Intent(AddMessage.this, MainScreen.class);
                startActivity(intent);

            }
        });

        editText.setText(null);
        editText.setHint("See you tomorrow!");

            //button.setEnabled(false);

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

    public void viewMessages(View view){
        Intent intent = new Intent(this, ReturnContent.ViewMessage.class);
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

        AlertDialog dialog = builder.create();

        dialog.show();

    }
}