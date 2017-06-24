package com.example.diksha.thirdeye.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diksha on 22/6/17.
 */

public class DataStash {

    private final String pageId;
    private static DataStash dataStash;
    private List<Album> albumList;

    public static DataStash get(){
        if(dataStash == null)
            dataStash = new DataStash();
        return dataStash;
    }

    private DataStash(){
        albumList = new ArrayList<>();
        pageId  = "386092581469426";
    }

    public String getPageId() {
        return pageId;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public int getTotalAlbums(){
        return albumList.size();
    }

    public void addAlbumToList(Album album){
        albumList.add(album);
    }

    public Boolean albumNotInList(Album item){
        for(Album album : albumList){
            if((item.getId().equals(album.getId())) &&
                    (item.getName().equals(album.getName())))
                return false;
        }
        return true;
    }

    public Album getAlbum(String albumId){
        for(Album album : albumList)
            if(album.getId().equals(albumId))
                return album;
        return null;
    }
}
