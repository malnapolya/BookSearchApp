package com.example.android.booksearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by malna on 3/30/2018.
 */

public class BookAdapter extends ArrayAdapter<Book>  {

    private String thumbnailUrl;
    /**
     *
     * @param context The current context used to inflate the layout file
     * @param books the (@link ArrayList) of books
     */
    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, if not inflate one
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get current book
        Book currentBook = getItem(position);

        // Find the textview that is supposed to hold the title and set the book's title on the view
        TextView titleView = (TextView) listView.findViewById(R.id.book_title);
        titleView.setText(currentBook.getTitle());

        // Find the textview that is supposed to hold the authors
        //  and set the book's authors on the view
        TextView authorView = (TextView) listView.findViewById(R.id.book_authors);

        authorView.setText(makeAuthorsString(currentBook.getAuthors()));


        // Find the textview that is supposed to hold the published date and set the appropriate field
        TextView publishedView = (TextView) listView.findViewById(R.id.book_year);
        // Set only year
        publishedView.setText(makePublishedYear(currentBook.getPublishedDate()));

        // Find the textview that is supposed to hold the description and set the description
        // on the view.
        TextView descriptionView = (TextView) listView.findViewById(R.id.book_description);
        descriptionView.setText(currentBook.getDescription());

        // Set image on ImageView
        ImageView thumbnailView = (ImageView) listView.findViewById(R.id.book_image);
        thumbnailView.setImageResource(R.drawable.blank);
        thumbnailUrl = currentBook.getThumbnail();
        new ImageDownloaderTask(thumbnailView).execute(currentBook.getThumbnail());

        return listView;
    }

    private String makeAuthorsString (ArrayList<String> authorsList) {
        String authorsString = "";
        if (authorsList.size() == 0) {
            return authorsString;
        }
        // Loop through the authorsList ArrayList and append the next author and a comma
        // to the end of the string until you reach the second to last one
        for (int i=0; i<authorsList.size()-1; i++) {
            authorsString += authorsList.get(i) + ", ";
        }
        // Append the last author to the list
        authorsString += authorsList.get(authorsList.size()-1);

        return authorsString;
    }

    private String makePublishedYear (String publishedDate) {
        if (publishedDate.length()<4) {
            return publishedDate;
        }
        // Return first 4 characters
        return  publishedDate.substring(0,4);
    }

}
