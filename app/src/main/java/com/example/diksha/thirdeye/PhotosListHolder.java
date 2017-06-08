package com.example.diksha.thirdeye;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by diksha on 8/6/17.
 */

public class PhotosListHolder extends RecyclerView.ViewHolder {

    public ImageView photoView;

    public PhotosListHolder(View itemView){
        super(itemView);
        photoView = (ImageView)itemView.findViewById(R.id.photo_gallery_item);
    }
}
