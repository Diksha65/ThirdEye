package com.example.diksha.thirdeye;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diksha.thirdeye.PhotoData.PhotoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diksha on 8/6/17.
 */

public class PhotosListAdapter extends RecyclerView.Adapter<PhotosListHolder> {

    private Context context;
    private List<PhotoItem> photoItems = new ArrayList<>();

    public PhotosListAdapter(Context context, List<PhotoItem> photoItems) {
        this.photoItems = photoItems;
        this.context = context;
    }

    @Override
    public PhotosListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item,parent,false);
        return new PhotosListHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosListHolder holder, int position) {
        PicassoClient.downloadImage(context,
                photoItems.get(position).getmUrl(), holder.photoView);
    }

    @Override
    public int getItemCount() {
        return photoItems.size();
    }
}
