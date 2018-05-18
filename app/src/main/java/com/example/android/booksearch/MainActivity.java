package com.example.android.booksearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {


//    private static final String API_KEY = "379630849235-ujbobopgej9k5jvknakrljnnld2mftj1.apps.googleusercontent.com";

    private String bookUrlStart = "https://www.googleapis.com/books/v1/volumes?q=";
    private String bookUrlSearchTerm;
    private String bookUrlEnd = "&orderBy=newest&maxResults=30";

    private BookAdapter bookAdapter;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // Defining the loadingIndicator, so that it can be accessed
    private View loadingIndicator;

    // Defining the emptyview, so that it can be accessed
    private RelativeLayout emptyView;

    // Defining the image, title and subtitles for the emptyview
    private ImageView emptyImageView;
    private TextView emptyViewTitle;
    private TextView emptyViewSubtitle;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;


    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(R.layout.activity_main);


        // Find the ListView with the id list
        listView = (ListView) findViewById(R.id.list);

        // Get a reference to the LoaderManager, in order to interact with loaders.
        final LoaderManager loaderManager = getLoaderManager();


        // Create a new (@link BookAdapter) to use with the list
        bookAdapter = new BookAdapter(this, new ArrayList<Book>());

        listView.setAdapter(bookAdapter);

        // Reference the search bar
        final SearchView searchView = (SearchView) findViewById(R.id.search_bar);

        // If the view is empty, set empty view as view
        emptyView = (RelativeLayout) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        // Finding the empty view title and subtitle TextViews
        emptyImageView = (ImageView) findViewById(R.id.empty_shelf_image);
        emptyViewTitle = (TextView) findViewById(R.id.empty_title_text);
        emptyViewSubtitle = (TextView) findViewById(R.id.empty_subtitle_text);

        loadingIndicator = findViewById(R.id.progress_bar);
        // Hide loading indicator initially
        loadingIndicator.setVisibility(View.GONE);

        // Check for Internet Connection
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is no internet, write "No internet connection"
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            loadingIndicator.setVisibility(View.GONE);
            emptyImageView.setImageResource(R.drawable.no_internet);
            emptyViewTitle.setText(R.string.no_internet);
            emptyViewSubtitle.setText(R.string.try_reconnecting);
        }


        // Set OnQueryTextListener to searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Hide focus from the searchview (hide keyboard)
                searchView.clearFocus();
                // If there is no internet, write "No internet connection"
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    loadingIndicator.setVisibility(View.GONE);
                    // Clear the adapter of previous earthquake data
                    bookAdapter.clear();
                    emptyImageView.setImageResource(R.drawable.no_internet);
                    emptyViewTitle.setText(R.string.no_internet);
                    emptyViewSubtitle.setText(R.string.try_reconnecting);
                    return true;
                } else {
                    bookUrlSearchTerm = query;
//                    mSearchQuery = query;
                    emptyView.setVisibility(View.GONE);
                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String query) {
//                if (mSearchQuery == query) {
//                    searchView.clearFocus();
//                }
                return false;
            }
        });

        // Set OnItemClickListener to listview
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Book book = (Book) bookAdapter.getItem(position);

                // Parse uri from the Book link
                Uri bookUri = Uri.parse(book.getInfoLink());

                // Create an appropriate website intent
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch the activity
                startActivity(websiteIntent);
            }
        });
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle bundle) {
        Log.v(LOG_TAG, bookUrlStart + bookUrlSearchTerm + bookUrlEnd);
        // Show loading indicator at the start of the loading
        loadingIndicator.setVisibility(View.VISIBLE);
        return new BookLoader(this, bookUrlStart + bookUrlSearchTerm + bookUrlEnd);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        // Return early if there is no result
        if (books == null) {
            return;
        }
        // Clear the adapter of previous earthquake data
        bookAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        bookAdapter.addAll(books);

        // Scroll to the top
        listView.setSelectionAfterHeaderView();

        // Setting the empty textview text only after the first load is done
        // Source of shelf image: https://www.freevector.com/various-shelf-vector-24746
        emptyImageView.setImageResource(R.drawable.empty_shelf);
        emptyViewTitle.setText(R.string.empty_view_title_text);
        emptyViewSubtitle.setText(R.string.empty_view_subtitle_text);

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookAdapter.clear();
    }
}
