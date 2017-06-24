package com.example.diksha.thirdeye.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diksha on 22/6/17.
 */

public class Album{

    private String name;
    private String id;
    private String photos_count;
    private List<Photo> photoList;

    public Album() {
        this.photoList = new ArrayList<>();
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
        return photoList;
    }

    public void setAlbumPhotos(List<Photo> albumPhotos) {
        this.photoList = albumPhotos;
    }

    public void addPhotoToAlbum(Photo photo){
        photoList.add(photo);
    }

    public Boolean photoNotInAlbum(Photo item){
        for(Photo photo : photoList){
            if((item.getId().equals(photo.getId())) &&
                    (item.getName().equals(photo.getName())))
                return false;
        }
        return true;
    }
}
