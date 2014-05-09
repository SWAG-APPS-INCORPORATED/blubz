package com.example.blubz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.blubz.AddContent.AddMessage;
import com.example.blubz.AddContent.AddPhoto;

/**
 * Created by Swag Apps on 3/24/14.
 */
public class BlubChoiceActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blub_choice);

    }

    public void goToMessage(View view) {
        goTo(AddMessage.class);
    }

    public void goToPicture(View view) {
        goTo(AddPhoto.class);
    }

    public void goTo(Class<?> destinationClass){

        Intent intent = new Intent(this, destinationClass);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            finish();
        }
    }
}