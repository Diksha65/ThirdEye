package com.example.diksha.thirdeye;

import android.content.Context;
import android.util.Log;

import com.example.diksha.thirdeye.PhotoData.PhotoItem;
import com.example.diksha.thirdeye.PhotoData.PhotosCollection;
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
    private Context context;
    private PhotosCollection photosCollection = PhotosCollection.get();
    private String urlString = "https://graph.facebook.com/"
            + AccessToken.getCurrentAccessToken().getUserId()
            + "/photos?fields=link%2Cname%2Cid%2Cimages&access_token="
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

    private void parseItem(JSONObject jsonBody) throws IOException, JSONException {
        //JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photosJsonArray = jsonBody.getJSONArray("data");

        for (int i = 0; i < photosJsonArray.length(); ++i) {
            JSONObject photoJsonObject = photosJsonArray.getJSONObject(i);
            PhotoItem item = new PhotoItem();

            item.setmId(photoJsonObject.getString("id"));
            if (!photoJsonObject.has("name"))
                item.setmName("Image");
            else
                item.setmName(photoJsonObject.getString("name"));

            JSONArray imagesJsonArray = photoJsonObject.getJSONArray("images");
            JSONObject sourceJsonObject = imagesJsonArray.getJSONObject(imagesJsonArray.length() - 1);
            item.setmUrl(sourceJsonObject.getString("source"));

            if (photosCollection.notIn(item))
                photosCollection.addPhoto(item);
        }

        if(jsonBody.has("paging")){
            Log.i(TAG, "has paging");
            JSONObject JOPaging = jsonBody.getJSONObject("paging");

            if (JOPaging.has("next")) {
                String initialPagingURL = JOPaging.getString("next");

                String[] parts = initialPagingURL.split("limit=25");
                String getLimit = parts[1];
                pagingURL = "https://graph.facebook.com/"
                        + AccessToken.getCurrentAccessToken().getUserId()
                        + "/photos?fields=link%2Cname%2Cid%2Cimages&access_token="
                        + AccessToken.getCurrentAccessToken().getToken()
                        + "&limit=25" + getLimit;


                Log.e(TAG, pagingURL);
            } else {
                stopLoadingData = true;
            }
        } else {
            stopLoadingData = true;
        }
    }

}

