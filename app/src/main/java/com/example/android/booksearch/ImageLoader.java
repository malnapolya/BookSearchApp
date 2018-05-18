package com.example.android.booksearch;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by malna on 3/31/2018.
 */

public class ImageLoader extends AsyncTaskLoader<Bitmap> {

    private String mUrl;


    public ImageLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public Bitmap loadInBackground() {
        if (mUrl == null || mUrl=="") {
            return null;
        }
        return QueryUtils.downloadBitmap(mUrl);
    }
}
