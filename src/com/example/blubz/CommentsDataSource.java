package com.example.blubz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macalester on 2/19/14.
 */

public class CommentsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_COMMENT, MySQLiteHelper.COLUMN_TIMESTAMP};

    public CommentsDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Comment createComment(String comment, long timestamp){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        values.put(MySQLiteHelper.COLUMN_TIMESTAMP, timestamp);
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
                values);

        /*Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);*/

        Comment newComment = new Comment();// = cursorToComment(cursor);
        //cursor.close();
        return newComment;
    }

    public void deleteComment(Comment comment){
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID +
                " = " + id, null);
    }

    public List<Comment> getAllComments(){
        List<Comment> comments = new ArrayList<Comment>();


        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }

        // Make sure to close the cursor
        cursor.close();

        return comments;
    }

    public long getMostRecentTimestamp(){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);
        cursor.moveToLast();
        Comment comment = cursorToComment(cursor);

        return comment.getTimestamp();
    }

    public boolean isEmpty(){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
               allColumns, null, null, null, null, null);
        return(cursor.getCount() == 0);
        //return(cursor.getCount()); //TODO: make a better implementation
        //return false;
    }

    private Comment cursorToComment(Cursor cursor){
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        comment.setTimestamp(cursor.getLong(2));
        return comment;
    }
}