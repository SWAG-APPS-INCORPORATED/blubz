package com.example.blubz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Swag Apps on 3/27/14.
 */
public class AboutActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_blubs);

    }

    public void goToMainScreen(View view) {
        Intent intent = new Intent(this, MainScreen.class);
        startActivityForResult(intent, 0);
    }
}