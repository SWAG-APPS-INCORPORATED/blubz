package com.example.blubz;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public final class FroyoAlbum extends AlbumStorage {

	@TargetApi(Build.VERSION_CODES.FROYO)
    @Override
	public File getAlbumStorageDir(String albumName) {
		// TODO Auto-generated method stub
		return new File(
		  Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES
          ),
		  albumName
		);
	}
}
