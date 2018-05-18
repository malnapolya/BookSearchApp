package com.example.android.booksearch;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.nfc.Tag;

import java.util.List;

/**
 * Created by malna on 3/30/2018.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>>{

    /** Tag for log messages */
    private static final String LOG_TAG = BookLoader.class.getName();

    // URL to download from
    private String mUrl;

    /**
     * Constructs a new (@link Bookloader)
     * @param context of the activity
     * @param url to download from
     */
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }


    /**
     * This is on the background thread
     * @return list of Books
     */
    @Override
    public List<Book> loadInBackground() {
        // check for empty array or empty string and then don't perform the request
        if (mUrl.equals("") || mUrl==null) {
            return null;
        }

        // Return the (@link List) of (@link Earthquake) objects
        return QueryUtils.fetchBookData(mUrl);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
