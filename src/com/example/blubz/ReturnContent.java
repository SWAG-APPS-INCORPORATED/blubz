package com.example.blubz;

import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.os.Bundle;
/*
import android.widget.Button;
import java.util.Calendar;
*/

/**
 * Created by Danyshi on 3/2/14.
 */

public class ReturnContent extends Activity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.returned_content);
        Button resetButton=(Button)findViewById(R.id.imageButton3);
        resetButton.setVisibility(View.INVISIBLE);
        // Alternative in xml: android:visibility="invisible"
    }

// Source: http://stackoverflow.com/questions/4127725/how-can-i-remove-a-button-or-make-it-invisible-in-android
    public void onClick(){
        View b = findViewById(R.id.button);
        b.setVisibility(View.GONE);
    }
/*
    public void buttonVisible(){
        if(weekDuration(timecheck1, System.currentTimeMillis())){
            Button resetButton=(Button)findViewById(R.id.imageButton3);
            resetButton.setVisibility(0);
        }

    }

    private boolean weekDuration(long timecheck1, long timecheck2){
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTimeInMillis(timecheck1);
        calendar2.setTimeInMillis(timecheck2);

        return(calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR + 7));
        }
*/
}

