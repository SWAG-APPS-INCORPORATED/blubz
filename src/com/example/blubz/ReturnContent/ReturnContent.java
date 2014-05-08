package com.example.blubz.ReturnContent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.blubz.MainScreen;
import com.example.blubz.R;

/**
 * Created by Danyshi on 3/2/14.
 */

public class ReturnContent extends Activity{

    // Button resetButton=(Button)findViewById(R.id.secretButton);

    ImageView image;
    TextView textView2;
    private ImageView backgroundImage;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.returned_content);

        Intent intent = getIntent();
        boolean isImage = intent.getBooleanExtra(MainScreen.INTENT_BOOLEAN, false);

        String date = intent.getStringExtra(MainScreen.INTENT_DATE);

        TextView textView = (TextView) findViewById(R.id.date);
        textView.setText(date);

        image = (ImageView) findViewById(R.id.messageImageView);
        textView2 = (TextView) findViewById(R.id.messageTextView);

        if (isImage) {
            byte[] imageArray = intent.getByteArrayExtra(MainScreen.INTENT_IMAGE);
            backgroundImage = (ImageView) findViewById(R.id.camerareturnimage);

            Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);

            image.setImageBitmap(bmp);

        } else {
            String message = intent.getStringExtra(MainScreen.INTENT_MESSAGE);
            backgroundImage = (ImageView) findViewById(R.id.returnimage);
            textView2.setText(message);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}

