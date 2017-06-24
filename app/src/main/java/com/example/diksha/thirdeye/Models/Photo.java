package com.example.diksha.thirdeye.Models;

/**
 * Created by diksha on 4/6/17.
 */

public class Photo {

    private String url;
    private String id;
    private String name;

    public Photo(){

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;

    }
}

