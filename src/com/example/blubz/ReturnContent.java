package com.example.blubz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Danyshi on 3/2/14.
 */

public class ReturnContent extends Activity{

    // Button resetButton=(Button)findViewById(R.id.secretButton);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.returned_content);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainScreen.INTENT_MESSAGE);
        String date = intent.getStringExtra(MainScreen.INTENT_DATE);

        TextView textView = (TextView) findViewById(R.id.date);
        textView.setText(date);

        TextView textView2 = (TextView) findViewById(R.id.messageTextView);
        textView2.setText(message);

    }
}

