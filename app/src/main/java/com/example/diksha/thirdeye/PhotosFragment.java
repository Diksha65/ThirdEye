package com.example.diksha.thirdeye;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diksha.thirdeye.PhotoData.PhotoItem;
import com.example.diksha.thirdeye.PhotoData.PhotosCollection;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diksha on 4/6/17.
 */

public class PhotosFragment extends Fragment {

    private AccessToken accessToken = AccessToken.getCurrentAccessToken();

    public static PhotosFragment newInstance(){
        return new PhotosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        PhotosListAdapter photosListAdapter = new
                PhotosListAdapter(getActivity(), PhotosCollection.getPhotos(accessToken));

        recyclerView.setAdapter(photosListAdapter);
        return v;
    }

}

/*
public class PhotosFragment extends Fragment {

    private RecyclerView recyclerView;
    private static final String TAG = "PhotosFragment";
    private List<PhotoItem> mItems = new ArrayList<>();

    public static PhotosFragment newInstance(){
        return new PhotosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        setUpAdapter();
        return v;
    }

    private void setUpAdapter(){
        if(isAdded())
            recyclerView.setAdapter(new PhotoAdapter(mItems));
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<PhotoItem>> {
        @Override
        protected List<PhotoItem> doInBackground(Void... params) {
            return new FacebookFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<PhotoItem> photoItems) {
            mItems = photoItems;
            setUpAdapter();
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public PhotoHolder(View itemView){
            super(itemView);
            textView = (TextView)itemView;
        }

        public void bindPhotoItem(PhotoItem item){
            textView.setText(item.toString());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<PhotoItem> mPhotoItems;

        public PhotoAdapter(List<PhotoItem> photoItems){
            mPhotoItems = photoItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            PhotoItem photoItem = mPhotoItems.get(position);
            holder.bindPhotoItem(photoItem);
        }

        @Override
        public int getItemCount() {
            return mPhotoItems.size();
        }
    }
}
*/

/**
 "              URL
 https://graph.facebook.com/v2.9/me/photos?limit=500&access_token=EAACEdEose0cBAP6RWztFt5ZAKSdZCDKW65pLdKNtQETOWaZCqKh4yYyJm4gSBsUIBZCqQ73c3xWJFF4KykdoXvfPM2cLKZC46YGGI0o1pAp8tu8X9nfQlWPtZCiqbZAGdh9tZCJLyZAZCyNzuX4dfuCMHgILpF166N6RO5fg5JHtkNsOiNpUCd2FUegnqJi0saYlwZD
 "
 */

