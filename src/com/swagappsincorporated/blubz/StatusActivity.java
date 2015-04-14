package com.swagappsincorporated.blubz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.swagappsincorporated.blubz.Database.ContentDataSource;

/**
 * Created by Swag Apps on 3/27/14.
 */
public class StatusActivity extends Activity {
    private TextView countsView;
    private ContentDataSource dataSource;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_blubz);

        countsView = (TextView) findViewById(R.id.countView);
        dataSource = new ContentDataSource(this);
        dataSource.open();

        if(!dataSource.isMessagesEmpty() || !dataSource.isImagesEmpty()){
            setContentCounts();
        }
    }

    public void setContentCounts() {
        int messageCount, imageCount;

        messageCount = dataSource.getMessageCount();
        imageCount = dataSource.getImageCount();

        countsView.setText("you have made " + messageCount + " text blubz, and "
                + imageCount + " photo blubz!");
    }


    public void goToMainScreen(View view) {
        Intent intent = new Intent(this, MainScreen.class);
        startActivityForResult(intent, 0);
    }
}