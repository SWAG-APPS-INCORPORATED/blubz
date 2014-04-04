package com.example.blubz;

import android.os.Environment;

import java.io.File;

public final class BaseAlbum extends AlbumStorage {

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/dcim/";

	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName
		);
	}
}
