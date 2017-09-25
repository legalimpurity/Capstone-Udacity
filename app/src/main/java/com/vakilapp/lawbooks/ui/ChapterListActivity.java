package com.vakilapp.lawbooks.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vakilapp.lawbooks.R;
import com.vakilapp.lawbooks.adapters.ChaptersListAdapter;
import com.vakilapp.lawbooks.interfaces.ChapterClickListener;
import com.vakilapp.lawbooks.models.Book;
import com.vakilapp.lawbooks.models.Chapter;
import com.vakilapp.lawbooks.provider.DBContract;

import java.util.ArrayList;

public class ChapterListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    public static final String BOOK_OBJ = "BOOK_OBJ";

    private static final int OFFLINE_CHAPTERS_DATA_LOADER = 25;

    private Book bk;
    private ArrayList<Chapter> chapters_list;

    private RecyclerView myRecycler;
    private ChaptersListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        myRecycler = (RecyclerView) findViewById(R.id.my_recycler);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        processFlow(this);
        setAdapter(this);
        startLoader();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private void processFlow(AppCompatActivity act)
    {
        if(getIntent() != null && getIntent().getExtras() != null) {
            bk = (Book) getIntent().getExtras().getParcelable(BOOK_OBJ);
            getSupportActionBar().setTitle(bk.getName());
        }
        else
            NavUtils.navigateUpFromSameTask(act);
    }

    private void setAdapter(Activity act)
    {
        RecyclerView.LayoutManager booksLayoutManager = new LinearLayoutManager(act);
        myRecycler.setLayoutManager(booksLayoutManager);

        myRecycler.setHasFixedSize(true);

        mAdapter = new ChaptersListAdapter(act, chapters_list, new ChapterClickListener() {
            @Override
            public void onChapterClicked(int chaperPosBook, ArrayList<Chapter> chaptersObjs, View view) {

            }
        });

        myRecycler.setAdapter(mAdapter);
    }

    private void startLoader()
    {
        Bundle queryBundle = new Bundle();
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader booksLoader = loaderManager.getLoader(OFFLINE_CHAPTERS_DATA_LOADER);
        if (booksLoader == null) {
            loaderManager.initLoader(OFFLINE_CHAPTERS_DATA_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(OFFLINE_CHAPTERS_DATA_LOADER, queryBundle, this);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String whereClause = DBContract.Chapters.TABLE_CHAPTER_BOOK_COLUMN_ID+"=?";
        String [] whereArgs = {bk.getId()+""};
        return new CursorLoader(this, DBContract.Chapters.CONTENT_URI, DBContract.CHAPTER_PROJECTION, whereClause, whereArgs, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (loader.getId() == OFFLINE_CHAPTERS_DATA_LOADER)
        {
            Cursor mCursor = (Cursor) data;
            chapters_list = new ArrayList<Chapter>();
            for(int i = 0; i < mCursor.getCount(); i++)
            {
                mCursor.moveToPosition(i);
                Chapter k = new Chapter(mCursor);
                chapters_list.add(k);
            }
        }
        mAdapter.setChaptersData(chapters_list);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
