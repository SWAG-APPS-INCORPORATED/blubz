package com.example.blubz;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Danyshi on 3/2/14.
 */

public class ReturnContent extends Activity{

    // Button resetButton=(Button)findViewById(R.id.secretButton);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String message = intent.getStringExtra(AddMessage.INTENT_MESSAGE);

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(message);

        setContentView(textView);
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

