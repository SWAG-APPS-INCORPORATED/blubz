package com.example.blubz.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Swag Apps on 3/11/14.
 */
public class ContentSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_IMAGES = "images";
    public static final String TABLE_MESSAGES = "messages";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_ID = "id";

    private static final String DATABASE_NAME = "content.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_IMAGES_CREATE = "create table "
            + TABLE_IMAGES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_IMAGE
            + " text, " + COLUMN_TIMESTAMP
            + " time)";

    private static final String TABLE_MESSAGES_CREATE = "create table "
            + TABLE_MESSAGES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_MESSAGE
            + " text not null, " + COLUMN_TIMESTAMP
            + " blob)";

    public ContentSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_IMAGES_CREATE);
        database.execSQL(TABLE_MESSAGES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ContentSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }
}
