package com.example.diksha.thirdeye;

import android.util.Log;

import com.example.diksha.thirdeye.Models.Album;
import com.example.diksha.thirdeye.Models.DataStash;
import com.example.diksha.thirdeye.Models.Photo;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by diksha on 22/6/17.
 */

public class FacebookFetcher {

    private static final String TAG = "FacebookFetcher";
    private static String pagingURL;
    public Boolean stopLoadingData = false;
    private static DataStash dataStash = DataStash.get();

    private String urlString = "https://graph.facebook.com/"
            + dataStash.getPageId()
            + "/albums?fields=name%2Cid%2Cphoto_count%2Cphotos%7Bimages%7D&limit=10&access_token="
            + AccessToken.getCurrentAccessToken().getToken();


    private byte[] getUrlBytes(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0){
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public void fetchItems(Boolean isPaging){

        urlString = isPaging ? pagingURL : urlString;
        try {
            String jsonString = getUrlString(urlString);
            Log.i(TAG, "Received JSON:" + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItem(jsonBody);
        } catch (IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        }
    }

    public void fetchPhotos(Boolean isPaging){

    }

    private void parseItem(JSONObject jsonBody) throws IOException, JSONException{

        JSONArray albumsJsonArray = jsonBody.getJSONArray("data");
        for(int i = 0; i<albumsJsonArray.length(); ++i){
            JSONObject albumsJsonObject = albumsJsonArray.getJSONObject(i);
            if(albumsJsonObject.has("photos")) {
                JSONObject photosJsonObject = albumsJsonObject.getJSONObject("photos");
                JSONArray data = photosJsonObject.getJSONArray("data");
                if (data.length() > 1) {
                    Album album = new Album();
                    album.setId(albumsJsonObject.getString("id"));
                    album.setName(albumsJsonObject.getString("name"));
                    album.setPhotos_count(albumsJsonObject.getString("photo_count"));
                    for (int j = 0; j < 3; ++j) {
                        JSONObject dataObject = data.getJSONObject(j);
                        JSONArray images = dataObject.getJSONArray("images");
                        Photo photo = new Photo();
                        photo.setId(dataObject.getString("id"));
                        photo.setName("Image");
                        JSONObject imageObject = images.getJSONObject(images.length() - 1);
                        photo.setUrl(imageObject.getString("source"));
                        if (album.photoNotInAlbum(photo))
                            album.addPhotoToAlbum(photo);
                    }
                    dataStash.addAlbumToList(album);
                }
            }
        }
        if(jsonBody.has("paging")){
            Log.i(TAG, "has paging");
            JSONObject JOPaging = jsonBody.getJSONObject("paging");
            if (JOPaging.has("next")) {
                String initialPagingURL = JOPaging.getString("next");

                String[] parts = initialPagingURL.split("limit=10");
                String getLimit = parts[1];
                pagingURL = urlString + getLimit;

                Log.e(TAG, pagingURL);
            } else {
                stopLoadingData = true;
            }
        } else {
            stopLoadingData = true;
        }
    }
}
