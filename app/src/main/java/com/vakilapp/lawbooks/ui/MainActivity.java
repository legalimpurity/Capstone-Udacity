package com.vakilapp.lawbooks.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vakilapp.lawbooks.R;
import com.vakilapp.lawbooks.adapters.BooksAdapter;
import com.vakilapp.lawbooks.interfaces.BookClickListener;
import com.vakilapp.lawbooks.loaders.BooksOnlineLoader;
import com.vakilapp.lawbooks.loaders.ChaptersOnlineLoader;
import com.vakilapp.lawbooks.models.Book;
import com.vakilapp.lawbooks.provider.DBContract;
import com.vakilapp.lawbooks.utils.NetworkUtils;
import com.vakilapp.lawbooks.utils.Snackbarer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks, SwipeRefreshLayout.OnRefreshListener {

    private static final String SAVED_INSTANCE_BOOKS_LIST = "SAVED_INSTANCE_BOOKS_LIST";

    private static final int ONLINE_BOOKS_DATA_LOADER = 22;
    private static final int OFFLINE_BOOKS_DATA_LOADER = 23;

    private static final int ONLINE_CHAPTERS_DATA_LOADER = 24;

    private boolean onlineLoaderCalledOnce = false;
    private ArrayList<Book> books_list;
    private SparseArray<Book> booksOffline = new SparseArray<Book>();

    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.activity_main_root)
    CoordinatorLayout activityMainRoot;

    private BooksAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        swipeContainer.setOnRefreshListener(this);
        setAdapter(this);
        checkForSavedInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lawbooksmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_sortBy:
                break;
            case R.id.action_search:
                break;
            case R.id.action_downloadAll:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_INSTANCE_BOOKS_LIST, books_list);
    }

    private void checkForSavedInstanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_INSTANCE_BOOKS_LIST)) {
                restoreDatafromSavedInstance(savedInstanceState);
            }
            else
                processFlow();
        }
        else
            processFlow();
    }

    private void restoreDatafromSavedInstance(Bundle savedInstanceState)
    {
        books_list = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_BOOKS_LIST);
        processLoader();
    }

    private void processFlow()
    {
        Bundle queryBundle = new Bundle();
        startLoader(OFFLINE_BOOKS_DATA_LOADER,queryBundle);
    }

    private void loadFromApi()
    {
        if(NetworkUtils.isNetworkAvailable(this)) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString(BooksOnlineLoader.BBID_PARAM,"NA");
            queryBundle.putSparseParcelableArray(BooksOnlineLoader.OFFLINE_BOOKS_PARAM,booksOffline);
            startLoader(ONLINE_BOOKS_DATA_LOADER, queryBundle);
            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);
                }
            });
        }
        else
        {
            Snackbarer.showMsg(activityMainRoot, R.string.no_internet_error, R.string.retry, new Snackbarer.SnackbarerInterface() {
                @Override
                public void onActionOccured() {
                    loadFromApi();
                }
            }, Snackbar.LENGTH_INDEFINITE);
            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(false);
                }
            });

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

    private void setAdapter(final Activity act)
    {
        int numberOfColumns = 2;
        if(act.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            numberOfColumns = 1;
        }
        else{
            numberOfColumns = 2;
        }
        RecyclerView.LayoutManager booksLayoutManager = new GridLayoutManager(act,numberOfColumns);
        myRecycler.setLayoutManager(booksLayoutManager);

        myRecycler.setHasFixedSize(true);

        books_list = new ArrayList<Book>();
        mAdapter = new BooksAdapter(act,books_list, new BookClickListener() {
            @Override
            public void onBookDownAsked(int bookPos, View view) {
                Book book = books_list.get(bookPos);
                if(book.getDownloaded() == 1)
                {
                    // Open book
                    openBook(act,book);
                }
                else if (book.getDownloaded() == 0 || book.getDownloaded() == 2) {
                    Bundle queryBundle = new Bundle();
                    queryBundle.putParcelable(ChaptersOnlineLoader.BBID_PARAM_BOOK_OBJ,book);
                    queryBundle.putInt(ChaptersOnlineLoader.BBID_PARAM_BOOK_INDEX,bookPos);
                    startLoader(ONLINE_CHAPTERS_DATA_LOADER,queryBundle);
                }
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

    private void openBook(Activity act, Book book)
    {
        Intent bookDetailActivityClickIntent = new Intent(act,ChapterListActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(ChapterListActivity.BOOK_OBJ,book);
        bookDetailActivityClickIntent.putExtras(extras);
        act.startActivity(bookDetailActivityClickIntent);
    }
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        AsyncTaskLoader loader;
        switch(id)
        {
            case ONLINE_BOOKS_DATA_LOADER:
                loader = new BooksOnlineLoader(this, args);
                break;
            case ONLINE_CHAPTERS_DATA_LOADER:
                loader = new ChaptersOnlineLoader(this, args);
                break;
            case OFFLINE_BOOKS_DATA_LOADER:
                loader = new CursorLoader(this, DBContract.Books.CONTENT_URI, DBContract.BOOK_PROJECTION, null, null, null);
                break;
            default:
                loader = new CursorLoader(this, DBContract.Books.CONTENT_URI, DBContract.BOOK_PROJECTION, null, null, null);
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if(loader.getId() == ONLINE_BOOKS_DATA_LOADER)
        {
            books_list = (ArrayList<Book>) data;
//            no_internet_text_view.setText(R.string.change_order_zero);
            processLoader();
            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(false);
                }
            });
        }
        else if(loader.getId() == ONLINE_CHAPTERS_DATA_LOADER)
        {
            int bookPos = (int) data;
            // Automatically done by cursor loader
//            books_list.get(bookPos).setDownloaded(1);
//            processLoader();
            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(false);
                }
            });
            openBook(this,books_list.get(bookPos));
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
            if(!onlineLoaderCalledOnce) {
                onlineLoaderCalledOnce = true;
                loadFromApi();
            }
        }
    }


    private void processLoader()
    {
        if (mAdapter!=null)
               mAdapter.setBooksData(books_list);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onRefresh() {
        loadFromApi();
    }
}