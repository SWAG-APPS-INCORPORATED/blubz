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

        EditText editText = (EditText) findViewById(R.id.messageEditText);
        editText.setText(message);


        // Wait an minute until let button reappear
/*        new CountDownTimer(60000, 1) {
            public void onTick(long millisUntilFinished) {
                if(resetButton.getVisibility() == View.GONE)
                    resetButton.setVisibility(View.VISIBLE);
                else
                    resetButton.setVisibility(View.GONE);
            }

            public void onFinish() {
                start();
            }
        };*/
    }
}

