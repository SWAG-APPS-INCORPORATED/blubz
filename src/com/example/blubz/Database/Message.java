package com.example.blubz.Database;

/**
 * Created by Swag Apps on 2/19/14.
 */

public class Message {

    private long id;
    private String message;
    private long timestamp;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public long getTimestamp() {return timestamp;}

    public void setTimestamp(long timestamp) {this.timestamp = timestamp;}

    @Override
    public String toString(){
        return message;
    }
}
