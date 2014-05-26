package com.example.blubz.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swag Apps on 3/11/14.
 */
public class ContentDataSource {

    private SQLiteDatabase database;
    private ContentSQLiteHelper dbHelper;
    private String[] imagesColumns = {ContentSQLiteHelper.COLUMN_ID, ContentSQLiteHelper.COLUMN_IMAGE,
            ContentSQLiteHelper.COLUMN_TIMESTAMP};
    private String[] messagesColumns = {ContentSQLiteHelper.COLUMN_ID,
            ContentSQLiteHelper.COLUMN_MESSAGE, ContentSQLiteHelper.COLUMN_TIMESTAMP};

    public ContentDataSource(Context context){
        dbHelper = new ContentSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Image createImageContent(byte[] imageArray, long timestamp) {
        ContentValues values = new ContentValues();
        values.put(ContentSQLiteHelper.COLUMN_IMAGE, imageArray);
        values.put(ContentSQLiteHelper.COLUMN_TIMESTAMP, timestamp);
        long insertId = database.insert(ContentSQLiteHelper.TABLE_IMAGES, null,
                values);

        Image newImage = new Image();
        return newImage;
    }

    public Image createMessageContent(String message, long timestamp) {
        ContentValues values = new ContentValues();
        values.put(ContentSQLiteHelper.COLUMN_MESSAGE, message);
        values.put(ContentSQLiteHelper.COLUMN_TIMESTAMP, timestamp);
        long insertId = database.insert(ContentSQLiteHelper.TABLE_MESSAGES, null,
                values);

        Image newImage = new Image();
        return newImage;
    }

    public int getImageCount() {
        int count = 0;
        Cursor cursor = database.query(ContentSQLiteHelper.TABLE_IMAGES,
                imagesColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            count++;
            cursor.moveToNext();
        }

        cursor.close();
        return count;
    }

    public int getMessageCount() {
        int count = 0;
        Cursor cursor = database.query(ContentSQLiteHelper.TABLE_MESSAGES,
                messagesColumns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            count++;
            cursor.moveToNext();
        }

        cursor.close();
        return count;
    }

    public List<Image> getAllImages(){
        List<Image> images = new ArrayList<Image>();

        Cursor cursor = database.query(ContentSQLiteHelper.TABLE_IMAGES,
                imagesColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Image image = cursorToImage(cursor);
            images.add(image);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();

        return images;
    }

    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<Message>();

        Cursor cursor = database.query(ContentSQLiteHelper.TABLE_MESSAGES,
                messagesColumns, null, null, null, null, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Message message = cursorToMessage(cursor);
            messages.add(message);
            cursor.moveToNext();
        }

        // Make sure to close the cursor
        cursor.close();

        return messages;
    }

    public long getMostRecentTimestamp(){
        long imageTimeStamp = 0;
        long messageTimeStamp = 0;

        Cursor cursor = database.query(ContentSQLiteHelper.TABLE_IMAGES,
                imagesColumns, null, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToLast();
            Image image = cursorToImage(cursor);
            imageTimeStamp = image.getTimestamp();
        }
        cursor = database.query(ContentSQLiteHelper.TABLE_MESSAGES,
                messagesColumns, null, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToLast();
            Message message = cursorToMessage(cursor);
            messageTimeStamp = message.getTimestamp();
        }

        if (imageTimeStamp > messageTimeStamp)
            return imageTimeStamp;
        else
            return messageTimeStamp;
    }

    private Image cursorToImage(Cursor cursor){
        Image image = new Image();
        image.setId(cursor.getLong(0));
        image.setImage(cursor.getBlob(1));
        image.setTimestamp(cursor.getLong(2));
        return image;
    }

    private Message cursorToMessage(Cursor cursor){
        Message message = new Message();
        message.setId(cursor.getLong(0));
        message.setMessage(cursor.getString(1));
        message.setTimestamp(cursor.getLong(2));
        return message;
    }

    public boolean isImagesEmpty(){
        Cursor cursor = database.query(ContentSQLiteHelper.TABLE_IMAGES,
                imagesColumns, null, null, null, null, null);
        return(cursor.getCount() == 0);
    }

    public boolean isMessagesEmpty(){
        Cursor cursor = database.query(ContentSQLiteHelper.TABLE_MESSAGES,
                messagesColumns, null, null, null, null, null);
        return(cursor.getCount() == 0);
    }

    public void clearDatabases() {
        database.delete(ContentSQLiteHelper.TABLE_MESSAGES, null, new String[0]);
        database.delete(ContentSQLiteHelper.TABLE_IMAGES, null, new String[0]);
    }
}
