package com.example.diksha.thirdeye;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by diksha on 21/6/17.
 */

public class AlbumsFragment extends Fragment {

    public static final String TAG = "AlbumsFragment";
    private RecyclerView recyclerView;
    private Boolean isPaging = false;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private FacebookFetcher facebookFetcher = new FacebookFetcher();
    private FetchItemsTask fetchItemsTask = new FetchItemsTask();
    private static DataStash dataStash = DataStash.get();

    public static AlbumsFragment newInstance(){
        return new AlbumsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.i(TAG, "HI");
        Fresco.initialize(getActivity());
        fetchItemsTask.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if ((fetchItemsTask.getStatus() == AsyncTask.Status.FINISHED)
                        && !facebookFetcher.stopLoadingData) {
                    isPaging = true;
                    fetchItemsTask = new FetchItemsTask();
                    fetchItemsTask.execute();
                } else {
                    //Check if no toast is visible. then only show toast./.
                    Toast.makeText(getActivity(), "No more albums", Toast.LENGTH_SHORT).show();
                }

                int visibleItemCount        = layoutManager.getChildCount();
                int totalItemCount          = layoutManager.getItemCount();
                int firstVisibleItemPosition= layoutManager.findFirstVisibleItemPosition();
/*
                if ( (visibleItemCount + firstVisibleItemPosition) >= (totalItemCount - 2) &&
                        firstVisibleItemPosition >= 0 && !(facebookFetcher.stopLoadingData)) {
                    isPaging = true;
                    Log.i(TAG, "Scrolled end");
                    Log.i(TAG, facebookFetcher.stopLoadingData.toString());
                    if(fetchItemsTask.getStatus() == AsyncTask.Status.FINISHED) {
                        fetchItemsTask = new FetchItemsTask();
                        fetchItemsTask.execute();
                    }

                }
*/
            }
        });
        return v;
    }

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
                adapter = new AlbumAdapter(albums);
                recyclerView.setAdapter(adapter);
            }
        }

    }

    private class AlbumHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Album sendAlbum;
        private TextView nameTextView;
        private SimpleDraweeView imageView1;
        private SimpleDraweeView imageView2;
        private SimpleDraweeView imageView3;

        private AlbumHolder(View itemView){
            super(itemView);
            nameTextView = (TextView)itemView.findViewById(R.id.textview_album_name);
            imageView1 = (SimpleDraweeView) itemView.findViewById(R.id.imageview_image1);
            imageView2 = (SimpleDraweeView) itemView.findViewById(R.id.imageview_image2);
            imageView3 = (SimpleDraweeView) itemView.findViewById(R.id.imageview_image3);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, String.valueOf(sendAlbum.getPhotos_count()));
            PhotosFragment photosFragment = new PhotosFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Album", sendAlbum.getId());
            photosFragment.setArguments(bundle);
            (getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, photosFragment, null)
                    .addToBackStack(null)
                    .commit();
        }

        private void bindDrawable(final Album album){
            sendAlbum = album;
            int i=0;
            nameTextView.setText(album.getName());
            for(Photo photo: album.getAlbumPhotos()) {
                Uri uri = Uri.parse(photo.getUrl());
                if(i == 0 && i < 3) {
                    imageView1.setImageURI(uri);
                    i += 1;
                } else if(i == 1 && i < 3){
                    imageView2.setImageURI(uri);
                    i += 1;
                } else if(i < 3){
                    imageView3.setImageURI(uri);
                }
            }
        }
    }

    private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {
        private List<Album> albumItemList;

        private AlbumAdapter(List<Album> albumItems){
            albumItemList = albumItems;
        }

        @Override
        public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.album_item, parent, false);
            return new AlbumHolder(view);
        }

        @Override
        public void onBindViewHolder(AlbumHolder holder, int position) {
            Album album = albumItemList.get(position);
            //Drawable placeholder = getResources().getDrawable(R.drawable.telogo);
            //holder.bindDrawable(placeholder);
            holder.bindDrawable(album);
        }

        @Override
        public int getItemCount() {
            return albumItemList.size();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Resume Hi");
        if(fetchItemsTask.getStatus() == FetchItemsTask.Status.FINISHED){
            fetchItemsTask = new FetchItemsTask();
            fetchItemsTask.execute();
        }
    }
}
