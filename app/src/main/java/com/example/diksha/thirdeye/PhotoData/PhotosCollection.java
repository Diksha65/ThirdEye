package com.example.diksha.thirdeye.PhotoData;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.diksha.thirdeye.FacebookFetcher;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diksha on 8/6/17.
 */

public class PhotosCollection{

    private static List<PhotoItem> photoItems = new ArrayList<>();

    public static List<PhotoItem> getPhotos(AccessToken accessToken){

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            JSONObject photosobject = object.getJSONObject("photos");
                            Log.e("PHOTOS", photosobject.toString());
                            photoItems = FacebookFetcher.parseItem(photoItems, photosobject);
                            Log.e("PHOTOITEMS", photoItems.toString());
/*
                            Gson gson = new Gson();
                            Type photos = new TypeToken<List<PhotoItem>>(){}.getType();
                            photoItems = gson.fromJson(photosobject.toString(), photos);
                            Log.e("PHOTOITEMS", photoItems.toString());
*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture,photos{link}");
        request.setParameters(parameters);
        request.executeAsync();

        return photoItems;
    }
}
