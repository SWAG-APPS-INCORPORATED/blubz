package com.example.blubz;

/**
 * Created by veritoff on 3/11/14.
 */
public class Content {

    private long id;
    private byte[] imageArray;
    private long timestamp;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(byte[] imageArray) {
        this.imageArray = imageArray;
    }

    public byte[] getName() {
        return imageArray;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
