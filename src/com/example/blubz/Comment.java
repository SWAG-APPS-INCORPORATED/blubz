package com.example.blubz;

/**
 * Created by macalester on 2/19/14.
 */

public class Comment {

    private long id;
    private String comment;
    private long timestamp;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public long getTimestamp() {return timestamp;}

    public void setTimestamp(long timestamp) {this.timestamp = timestamp;}

    @Override
    public String toString(){
        return comment;
    }
}
