package com.example.diksha.thirdeye.PhotoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diksha on 8/6/17.
 */

public class PhotosCollection{

    private List<PhotoItem> photoItems;
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

    public List<PhotoItem> getPhotoItems() {
        return photoItems;
    }

    public void setPhotoItems(List<PhotoItem> photoItems) {
        this.photoItems = photoItems;
    }

    public void addPhoto(PhotoItem photoItem){
        photoItems.add(photoItem);
    }

    public boolean notIn(PhotoItem item){
        for(PhotoItem photoItem : photoItems){
            if((item.getmId().equals(photoItem.getmId())) &&
                    (item.getmName().equals(photoItem.getmName())))
                return false;
        }
        return true;
    }

    public int sizeOfPhotosList(){
        return photoItems.size();
    }
}
