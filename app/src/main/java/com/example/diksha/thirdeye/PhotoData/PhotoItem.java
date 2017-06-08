package com.example.diksha.thirdeye.PhotoData;

/**
 * Created by diksha on 4/6/17.
 */

public class PhotoItem {

    private String mUrl;
    private String mId;
    private String mName;

    public PhotoItem(){

    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    @Override
    public String toString() {
        return mName;

    }
}

