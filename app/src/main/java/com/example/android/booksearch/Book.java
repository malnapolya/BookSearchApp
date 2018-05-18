package com.example.android.booksearch;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by malna on 3/30/2018.
 */

public class Book {

    // Title of the book
    private String mTitle;

    // Author(s) of the book
    private ArrayList<String> mAuthors = null;

    // Year of publishing
    private String mPublishedDate;

    // Description of the book
    private String mDescription;

    // URL string for the small thumbnail of the book
    private String mThumbnail;

    // URL string for the info
    private String mInfoLink;

    /**
     *
     * @param title of the book
     * @param authors (@link ArrayList) of authors
     * @param publishedDate date of publishing
     * @param description of the book
     * @param thumbnail url string for the thumbnail
     * @param infoLink url string for the info link
     */
    public Book(String title, ArrayList<String> authors, String publishedDate, String description, String thumbnail,
                String infoLink) {
        mTitle = title;
        mAuthors = authors;
        mPublishedDate = publishedDate;
        mDescription = description;
        mThumbnail = thumbnail;
        mInfoLink = infoLink;
    }

    /**
     *
     * @return title of the book
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     *
     * @return (@link ArrayList) of authors of the book
     */
    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    /**
     *
     * @return year of publishing
     */
    public String getPublishedDate() {
        return mPublishedDate;
    }

    /**
     *
     * @return description of book
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     *
     * @return link of the thumbnail of the book
     */
    public String getThumbnail() {
        return mThumbnail;
    }

    /**
     *
     * @return infolink that can be used to open a website for new info
     */
    public String getInfoLink() {
        return mInfoLink;
    }
}
