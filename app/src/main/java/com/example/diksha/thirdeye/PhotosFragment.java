package com.example.diksha.thirdeye;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.diksha.thirdeye.PhotoData.Photo;
import com.example.diksha.thirdeye.PhotoData.PhotosCollection;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by diksha on 21/6/17.
 */

public class PhotosFragment extends Fragment {

    public static final String TAG = "PhotosFragment";
    private RecyclerView recyclerView;
    private Boolean isPaging = false;
    private RecyclerView.Adapter adapter;
    private GridLayoutManager layoutManager;
    private PhotosCollection photosCollection = PhotosCollection.get();
    private FacebookFetcher facebookFetcher = new FacebookFetcher();
    private FetchItemsTask fetchItemsTask = new FetchItemsTask();

    public static PhotosFragment newInstance(){
        return new PhotosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        fetchItemsTask.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);

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

                if ( (visibleItemCount + firstVisibleItemPosition) >= (totalItemCount - 9) &&
                        firstVisibleItemPosition >= 0 && !(facebookFetcher.stopLoadingData)) {
                    isPaging = true;
                    Log.i(TAG, "Scrolled end");
                    if(fetchItemsTask.getStatus() == AsyncTask.Status.FINISHED) {
                        fetchItemsTask = new FetchItemsTask();
                        fetchItemsTask.execute();
                    }

                }

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
            List<Photo> photos = photosCollection.getPhotoItems();
            if(isPaging && adapter != null) {
                adapter.notifyDataSetChanged();
                Log.i(TAG, String.valueOf(photosCollection.sizeOfPhotosList()));
            }
            else{
                adapter = new PhotoAdapter(photos);
                recyclerView.setAdapter(adapter);
            }
        }

    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        private PhotoHolder(View itemView){
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.photo_gallery_item);
        }
        private void bindDrawable(Photo photo){
            Picasso.with(getActivity()).load(photo.getmUrl())
                    .placeholder(R.drawable.telogo)
                    .into(imageView);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<Photo> photoItemList;

        private PhotoAdapter(List<Photo> photoItems){
            photoItemList = photoItems;
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
            //Drawable placeholder = getResources().getDrawable(R.drawable.telogo);
            //holder.bindDrawable(placeholder);
            holder.bindDrawable(photo);
        }

        @Override
        public int getItemCount() {
            return photoItemList.size();
        }


    }
}
