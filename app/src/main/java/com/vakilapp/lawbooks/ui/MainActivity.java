package com.vakilapp.lawbooks.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.vakilapp.lawbooks.R;
import com.vakilapp.lawbooks.adapters.BooksAdapter;
import com.vakilapp.lawbooks.interfaces.BookClickListener;
import com.vakilapp.lawbooks.loaders.BooksOnlineLoader;
import com.vakilapp.lawbooks.models.Book;
import com.vakilapp.lawbooks.provider.DBContract;
import com.vakilapp.lawbooks.utils.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks {

    private static final String SAVED_INSTANCE_MOVIE_LIST = "SAVED_INSTANCE_MOVIE_LIST";

    private static final int ONLINE_BOOKS_DATA_LOADER = 22;
    private static final int OFFLINE_BOOKS_DATA_LOADER = 23;

    private ArrayList<Book> books_list;
    private SparseArray<Book> booksOffline = new SparseArray<Book>();


    private RecyclerView myRecycler;
    private SwipeRefreshLayout swipeContainer;
    private CoordinatorLayout activityMainRoot;

    private BooksAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecycler = (RecyclerView) findViewById(R.id.my_recycler);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        activityMainRoot = (CoordinatorLayout) findViewById(R.id.activity_main_root);
        setAdapter(this);
        checkForSavedInstanceState(savedInstanceState);
    }

    private void checkForSavedInstanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_INSTANCE_MOVIE_LIST)) {

            }
            else
                processFlow();
        }
        else
            processFlow();
    }

    private void processFlow()
    {
        Bundle queryBundle = new Bundle();
        startLoader(OFFLINE_BOOKS_DATA_LOADER,queryBundle);
        if(NetworkUtils.isNetworkAvailable(this)) {
            queryBundle.putString(BooksOnlineLoader.BBID_PARAM,"NA");
            queryBundle.putSparseParcelableArray(BooksOnlineLoader.OFFLINE_BOOKS_PARAM,booksOffline);
            startLoader(ONLINE_BOOKS_DATA_LOADER, queryBundle);
        }
    }

    private void startLoader(int LOADER_CODE, Bundle queryBundle)
    {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader booksLoader = loaderManager.getLoader(LOADER_CODE);
        if (booksLoader == null) {
            loaderManager.initLoader(LOADER_CODE, queryBundle, this);
        } else {
            loaderManager.restartLoader(LOADER_CODE, queryBundle, this);
        }
    }

    private void restoreDatafromSavedInstance(Bundle savedInstanceState)
    {
        books_list = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_MOVIE_LIST);
    }

    private void setAdapter(final Activity act)
    {
        int numberOfColumns = 2;
        if(act.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            numberOfColumns = 1;
        }
        else{
            numberOfColumns = 3;
        }
        RecyclerView.LayoutManager moviesLayoutManager = new GridLayoutManager(act,numberOfColumns);
        myRecycler.setLayoutManager(moviesLayoutManager);

        myRecycler.setHasFixedSize(true);

        books_list = new ArrayList<Book>();
        mAdapter = new BooksAdapter(act,books_list, new BookClickListener() {
            @Override
            public void onBookDownAsked(Book bookName, View view) {

            }

//            @Override
//            public void onMovieCLick(MovieObject movieItem) {
//                Intent movieDetailActivityClickIntent = new Intent(act,MovieDetailActivity.class);
//                Bundle extras = new Bundle();
//                extras.putParcelable(MovieDetailActivity.MOVIE_OBJECT_KEY,movieItem);
//                movieDetailActivityClickIntent.putExtras(extras);
//                act.startActivity(movieDetailActivityClickIntent);
//            }
        });

        myRecycler.setAdapter(mAdapter);

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch(id)
        {
            case ONLINE_BOOKS_DATA_LOADER:
                return new BooksOnlineLoader(this, args);
            case OFFLINE_BOOKS_DATA_LOADER:
                return new CursorLoader(this,
                        DBContract.Books.CONTENT_URI,
                        DBContract.BOOK_PROJECTION,
                        null,
                        null,
                        null);
            default:return new BooksOnlineLoader(this, args);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if(loader.getId() == ONLINE_BOOKS_DATA_LOADER)
        {
            books_list = (ArrayList<Book>) data;
//            no_internet_text_view.setText(R.string.change_order_zero);
            processLoader();
        }
        else if (loader.getId() == OFFLINE_BOOKS_DATA_LOADER)
        {
            Cursor mCursor = (Cursor) data;
            books_list = new ArrayList<Book>();
            booksOffline = new SparseArray<Book>();
            for(int i = 0; i < mCursor.getCount(); i++)
            {
                mCursor.moveToPosition(i);
                Book k = new Book(mCursor);
                booksOffline.append((int)k.getId(),k);
                books_list.add(k);
            }
            processLoader();
        }
    }


    private void processLoader()
    {
        if (mAdapter!=null)
               mAdapter.setBooksData(books_list);
//        wasDataLoadedPerfectlyForSelectedApiCode = true;
//        if(movies_list != null && movies_list.size() == 0)
//            showErrorMessage();
//        else
//            showMovies();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}