package com.example.diksha.thirdeye.PhotoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diksha on 8/6/17.
 */

public class PhotosCollection{

    private List<Photo> photoItems;
    private static PhotosCollection photosCollection;

    public static PhotosCollection get(){
        if(photosCollection == null){
            photosCollection = new PhotosCollection();
        }
        return photosCollection;
    }

    private PhotosCollection(){
        photoItems = new ArrayList<>();
    }

    public List<Photo> getPhotoItems() {
        return photoItems;
    }

    public void setPhotoItems(List<Photo> photoItems) {
        this.photoItems = photoItems;
    }

    public void addPhoto(Photo photo){
        photoItems.add(photo);
    }

    public boolean notIn(Photo item){
        for(Photo photo : photoItems){
            if((item.getmId().equals(photo.getmId())) &&
                    (item.getmName().equals(photo.getmName())))
                return false;
        }
        return true;
    }

    public int sizeOfPhotosList(){
        return photoItems.size();
    }
}
