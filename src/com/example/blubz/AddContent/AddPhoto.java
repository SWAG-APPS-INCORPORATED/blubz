package com.example.blubz.AddContent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.blubz.Database.ContentDataSource;
import com.example.blubz.MainScreen;
import com.example.blubz.R;
import com.example.blubz.TimeHelper;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


//A significant amount of the following code was taken from the Android Developer website for camera implementation

public class AddPhoto extends Activity {

	private static final int ACTION_TAKE_PHOTO_B = 1;

    private ContentDataSource datasource;

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private ImageButton mImageView;
	private Bitmap mImageBitmap;

    private TextView dateText;
    private boolean imageExists;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);

        mImageView = (ImageButton) findViewById(R.id.imageBtn);
        mImageView.setImageBitmap(null);
        imageExists = false;

        dateText = (TextView) findViewById(R.id.datePhoto);

        datasource = new ContentDataSource(this);
        datasource.open();

        setDateText();

        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);

    }

    public void addPhoto(View view) {
        long timestamp = System.currentTimeMillis();

        if(!datasource.isEmpty()){
            long lastTimestamp = datasource.getMostRecentTimestamp();
            if(TimeHelper.isSameDay(lastTimestamp,System.currentTimeMillis())){
                showDialogBox("You've already blubbed today!", "Sorry, but you have to wait until tomorrow to blub again.",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id){
                        Intent intent = new Intent(AddPhoto.this, MainScreen.class);
                        startActivity(intent);
                        finish();

                    }
                });
                return;
            }
        }

        if(!imageExists){
            showDialogBox("Empty blub!", "Please enter something real in your blub, bub.", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id){
                    //Do nothing
                }
            });
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        datasource.createContent(byteArray, timestamp);

        showDialogBox("Thanks for blubbing", "Don't forget to blub again tomorrow!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id){
                Intent intent = new Intent(AddPhoto.this, MainScreen.class);
                startActivity(intent);
                finish();

            }
        });
    }

	private void setPic() {

        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(View.VISIBLE);

	}

//Take Photo Activity

	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		startActivityForResult(takePictureIntent, actionCode);
	}

    public void takePhoto(View view) {
        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
    }

//Get the thumbnail
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();
                mImageBitmap = (Bitmap) bundle.get("data");
                imageExists = true;
                setPic();
			}

	}

	// Some lifecycle callbacks so that the image can survive orientation change
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
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

    private void showDialogBox(String title, String message, DialogInterface.OnClickListener onClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", onClick);

        AlertDialog dialog = builder.create();

        dialog.show();

    }
}