package com.example.diksha.thirdeye;

import com.example.diksha.thirdeye.PhotoData.PhotoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by diksha on 4/6/17.
 */

public class FacebookFetcher {

    public static List<PhotoItem> parseItem(List<PhotoItem> items, JSONObject jsonBody) throws IOException, JSONException {
        //JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photosJsonArray = jsonBody.getJSONArray("data");

        for(int i=0; i<photosJsonArray.length(); ++i){
            JSONObject photoJsonObject = photosJsonArray.getJSONObject(i);
            PhotoItem item = new PhotoItem();

            item.setmId(photoJsonObject.getString("id"));
            if(!photoJsonObject.has("name"))
                item.setmName("Image");
            else
                item.setmName(photoJsonObject.getString("name"));
            item.setmUrl(photoJsonObject.getString("link"));

            items.add(item);
        }
        return items;
    }
}
