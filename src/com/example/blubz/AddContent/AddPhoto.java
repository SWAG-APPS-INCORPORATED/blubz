package com.example.blubz.AddContent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.blubz.Database.ContentDataSource;
import com.example.blubz.MainScreen;
import com.example.blubz.R;
import com.example.blubz.TimeHelper;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;


//A signifacnt amount of the following code was taken from the Andorid Developer website for camera implementation

public class AddPhoto extends Activity {

	private static final int ACTION_TAKE_PHOTO_B = 1;

    private ContentDataSource datasource;

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private ImageButton mImageView;
	private Bitmap mImageBitmap;

    private TextView dateText;
    private boolean imageExists;

	private String mCurrentPhotoPath;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

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

/*        Button picBtn = (Button) findViewById(R.id.btnIntend);
        setBtnListenerOrDisable(
                mImageView,
                mTakePicOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );*/

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

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		/*int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		*//* Get the size of the image *//*
//		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//		bmOptions.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		int photoW = mImageBitmap.getWidth(); //bmOptions.outWidth;
		int photoH = mImageBitmap.getHeight(); //bmOptions.outHeight;
		
		*//* Figure out which way needs to be reduced less *//*
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}*/

		/* Set bitmap options to scale the image decode target *//*
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		*//* Decode the JPEG file into a Bitmap *//*
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);*/
		
		/* Associate the Bitmap to the ImageView */
	}

//Take Photo Activity

	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

/*		switch(actionCode) {
		case ACTION_TAKE_PHOTO_B:
			//File f = null;
			
			try {
				//f = setUpPhotoFile();
				//mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				//f = null;
				//mCurrentPhotoPath = null;
			}
			break;

		default:
			break;			
		} // switch*/

		startActivityForResult(takePictureIntent, actionCode);
	}

    public void takePhoto(View view) {
        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
    }

	Button.OnClickListener mTakePicOnClickListener = 
		new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
		}
	};

//Get the thumbnail
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode == RESULT_OK) {
				//handleBigCameraPhoto();

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

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
			packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

    private void showDialogBox(String title, String message, DialogInterface.OnClickListener onClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", onClick);

        AlertDialog dialog = builder.create();

        dialog.show();

    }

	private void setBtnListenerOrDisable( 
			Button btn, 
			Button.OnClickListener onClickListener,
			String intentName
	) {
		if (isIntentAvailable(this, intentName)) {
			btn.setOnClickListener(onClickListener);        	
		} else {
			btn.setText( 
				getText(R.string.cannot).toString() + " " + btn.getText());
			btn.setClickable(false);
		}
	}

    /*	private void galleryAddPic() {
		    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
			File f = new File(mCurrentPhotoPath);
		    Uri contentUri = Uri.fromFile(f);
		    mediaScanIntent.setData(contentUri);
		    this.sendBroadcast(mediaScanIntent);
	}*/

    /*
	private void handleBigCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			setPic();
			//galleryAddPic();
			mCurrentPhotoPath = null;
		}

	}
*/

    	/* Photo album for this application */
/*	private String getAlbumName() {
		return getString(R.string.album_name);
	}*/


/*	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			storageDir = mAlbumStorage.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(getString(R.string.camera_name), "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}*/

/*	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		//File albumF = getAlbumDir();
		//File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		//return imageF;
        return null;
	}*/

/*	private File setUpPhotoFile() throws IOException {

		//File f = createImageFile();
		//mCurrentPhotoPath = f.getAbsolutePath();

		//return f;
        return null;
	}*/

}