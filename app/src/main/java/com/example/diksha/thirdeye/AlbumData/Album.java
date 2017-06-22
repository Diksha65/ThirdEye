package com.example.diksha.thirdeye.AlbumData;

import com.example.diksha.thirdeye.PhotoData.Photo;

import java.util.List;

/**
 * Created by diksha on 22/6/17.
 */

public class Album {

    private String name;
    private String id;
    private String photos_count;
    private List<Photo> albumPhotos;

    public Album(List<Photo> albumPhotos) {
        this.albumPhotos = albumPhotos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotos_count() {
        return photos_count;
    }

    public void setPhotos_count(String photos_count) {
        this.photos_count = photos_count;
    }

    public List<Photo> getAlbumPhotos() {
        return albumPhotos;
    }

    public void setAlbumPhotos(List<Photo> albumPhotos) {
        this.albumPhotos = albumPhotos;
    }
}
