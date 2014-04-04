package com.example.blubz;

import java.io.File;

abstract class AlbumStorage {
	public abstract File getAlbumStorageDir(String albumName);
}
