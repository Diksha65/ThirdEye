package com.example.diksha.thirdeye;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by diksha on 8/6/17.
 */

public class PicassoClient {

    public static void downloadImage(Context context, String url, ImageView imageView){

        if(url != null && url.length() > 0){
            Picasso.with(context).load(url).placeholder(R.drawable.telogo).into(imageView);
        } else {
            Picasso.with(context).load(R.drawable.telogo).into(imageView);
        }
    }
}
