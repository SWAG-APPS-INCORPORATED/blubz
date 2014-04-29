package com.example.blubz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.blubz.AddContent.AddMessage;
import com.example.blubz.AddContent.AddPhoto;

/**
 * Created by carinalei on 3/24/14.
 */
public class BlubChoiceActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blub_choice);
    }

    public void goToMessage(View view) {
        Intent intent = new Intent(this, AddMessage.class);
        startActivity(intent);
    }

    public void goToPicture(View view) {
        Intent intent = new Intent(this, AddPhoto.class);
        startActivity(intent);
    }
}