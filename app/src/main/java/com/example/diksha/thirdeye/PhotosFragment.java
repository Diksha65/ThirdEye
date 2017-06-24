package com.example.diksha.thirdeye;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.diksha.thirdeye.Models.Album;
import com.example.diksha.thirdeye.Models.DataStash;
import com.example.diksha.thirdeye.Models.Photo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by diksha on 22/6/17.
 */

public class PhotosFragment extends Fragment {

    private static final String TAG = "PhotosFragment";
    private Album album;
    private RecyclerView recyclerView;
    private Boolean isPaging = false;
    private RecyclerView.Adapter adapter;
    private GridLayoutManager layoutManager;
    private FacebookFetcher facebookFetcher = new FacebookFetcher();
    private DataStash dataStash = DataStash.get();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        String albumId = bundle.getString("Album");
        album = dataStash.getAlbum(albumId);
        if(album == null) {
            (getActivity()).finish();
            Toast.makeText(getActivity(), "Album not found", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, "HI");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_layout, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.fragment_photo_recycler_view);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotoAdapter(album.getAlbumPhotos());
        recyclerView.setAdapter(adapter);

        /*
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount        = layoutManager.getChildCount();
                int totalItemCount          = layoutManager.getItemCount();
                int firstVisibleItemPosition= layoutManager.findFirstVisibleItemPosition();

                if ( (visibleItemCount + firstVisibleItemPosition) >= (totalItemCount - 2) &&
                        firstVisibleItemPosition >= 0 && !(facebookFetcher.stopLoadingData)) {
                    isPaging = true;
                    Log.i(TAG, "Scrolled end");
                    Log.i(TAG, facebookFetcher.stopLoadingData.toString());
                    if(fetchItemsTask.getStatus() == AsyncTask.Status.FINISHED) {
                        fetchItemsTask = new AlbumsFragment.FetchItemsTask();
                        fetchItemsTask.execute();
                    }

                }

            }
        });
        */

        return view;
    }
/*
    private class FetchItemsTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            facebookFetcher.fetchItems(isPaging);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            List<Album> albums = dataStash.getAlbumList();
            if(isPaging && adapter != null) {
                adapter.notifyDataSetChanged();
                Log.i(TAG, String.valueOf(dataStash.getTotalAlbums()));
            }
            else{
                adapter = new AlbumsFragment.PhotoAdapter(albums);
                recyclerView.setAdapter(adapter);
            }
        }

    }
*/
    private class PhotoHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView imageView;

        private PhotoHolder(View itemView){
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.photo_gallery_item);
        }

        private void bindDrawable(Photo photo){
            Uri uri = Uri.parse(photo.getUrl());
            imageView.setImageURI(uri);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<Photo> photoItemList;

        private PhotoAdapter(List<Photo> photoList){
            photoItemList = photoList;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.photo_item, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            Photo photo = photoItemList.get(position);
            holder.bindDrawable(photo);
        }

        @Override
        public int getItemCount() {
            return photoItemList.size();
        }

    }
}
