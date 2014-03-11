package com.example.blubz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veritoff on 3/11/14.
 */
public class ContentDataSource {

    private SQLiteDatabase database;
    private ContentSQLiteHelper dbHelper;
    private String[] allColumns = { ContentSQLiteHelper.COLUMN_NAME,
            ContentSQLiteHelper.COLUMN_TIMESTAMP, ContentSQLiteHelper.COLUMN_ID};

    public ContentDataSource(Context context){
        dbHelper = new ContentSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Content createContent(String name, long timestamp) {
        ContentValues values = new ContentValues();
        values.put(ContentSQLiteHelper.COLUMN_NAME, name);
        values.put(ContentSQLiteHelper.COLUMN_TIMESTAMP, timestamp);
        long insertId = database.insert(CommentSQLiteHelper.TABLE_COMMENTS, null,
                values);

        /*Cursor cursor = database.query(CommentSQLiteHelper.TABLE_COMMENTS,
                allColumns, CommentSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);*/

        Content newContent = new Content();// = cursorToComment(cursor);
        //cursor.close();
        return newContent;
    }

    public void deleteContent(Content content){
        long id = content.getId();
        System.out.println("Content deleted with id: " + id);
        database.delete(ContentSQLiteHelper.TABLE_CONTENT, CommentSQLiteHelper.COLUMN_ID +
                " = " + id, null);
    }

    public List<Content> getAllContents(){
        List<Content> contents = new ArrayList<Content>();


        Cursor cursor = database.query(ContentSQLiteHelper.TABLE_CONTENT,
                allColumns, null, null, null, null, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Content content = cursorToContent(cursor);
            contents.add(content);
            cursor.moveToNext();
        }

        // Make sure to close the cursor
        cursor.close();

        return contents;
    }

    public long getMostRecentTimestamp(){
        Cursor cursor = database.query(ContentSQLiteHelper.TABLE_CONTENT,
                allColumns, null, null, null, null, null);
        cursor.moveToLast();
        Content content = cursorToContent(cursor);

        return content.getTimestamp();
    }

    public boolean isEmpty(){
        Cursor cursor = database.query(ContentSQLiteHelper.TABLE_CONTENT,
                allColumns, null, null, null, null, null);
        return(cursor.getCount() == 0);
        //return(cursor.getCount()); //TODO: make a better implementation
        //return false;
    }

    private Content cursorToContent(Cursor cursor){
        Content content = new Content();
        content.setId(cursor.getLong(0));
        content.setName(cursor.getString(1));
        content.setTimestamp(cursor.getLong(2));
        return content;
    }
}
