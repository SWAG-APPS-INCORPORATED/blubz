package com.example.blubz;

/**
 * Created by veritoff on 3/11/14.
 */
public class Content {

    private long id;
    private String name;
    private long timestamp;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return name;
    }
}
