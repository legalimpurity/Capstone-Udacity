package com.vakilapp.lawbooks.ui;

import android.app.Activity;
import android.content.Intent;
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

public class ChapterListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    public static final String BOOK_OBJ = "BOOK_OBJ";

    private static final int OFFLINE_CHAPTERS_DATA_LOADER = 25;

    private static final String SAVED_INSTANCE_BOOK = "SAVED_INSTANCE_BOOK";
    private static final String SAVED_INSTANCE_CHAPTERS_LIST = "SAVED_INSTANCE_CHAPTERS_LISTs";

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

        setAdapter(this);
        checkForSavedInstanceState(savedInstanceState, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_INSTANCE_BOOK, bk);
        outState.putParcelableArrayList(SAVED_INSTANCE_CHAPTERS_LIST, chapters_list);
    }

    private void checkForSavedInstanceState(Bundle savedInstanceState, Activity act) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_INSTANCE_BOOK)) {
                if (savedInstanceState.containsKey(SAVED_INSTANCE_CHAPTERS_LIST)) {
                    restoreDatafromSavedInstance(savedInstanceState);
                } else
                    partialRestoreDatafromSavedInstance(savedInstanceState);
            } else
                processFlow(act);
        } else
            processFlow(act);
    }

    private void restoreDatafromSavedInstance(Bundle savedInstanceState) {
        bk = savedInstanceState.getParcelable(SAVED_INSTANCE_BOOK);
        chapters_list = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_CHAPTERS_LIST);
        mAdapter.setChaptersData(chapters_list);
    }

    private void partialRestoreDatafromSavedInstance(Bundle savedInstanceState) {
        bk = savedInstanceState.getParcelable(SAVED_INSTANCE_BOOK);
        bookFound();
    }

    private void processFlow(Activity act) {
        if (getIntent() != null && getIntent().getExtras() != null) {
            bk = (Book) getIntent().getExtras().getParcelable(BOOK_OBJ);
            bookFound();
        } else
            NavUtils.navigateUpFromSameTask(act);
    }

    private void bookFound() {
        getSupportActionBar().setTitle(bk.getName());
        startLoader();
    }

    private void setAdapter(final Activity act) {
        RecyclerView.LayoutManager booksLayoutManager = new LinearLayoutManager(act);
        myRecycler.setLayoutManager(booksLayoutManager);

        myRecycler.setHasFixedSize(true);

        mAdapter = new ChaptersListAdapter(act, chapters_list, new ChapterClickListener() {
            @Override
            public void onChapterClicked(int chaperPosBook, ArrayList<Chapter> chaptersObjs, View view) {
                Intent chapterDetailActivityClickIntent = new Intent(act, ChapterDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelableArrayList(ChapterDetailActivity.CHAP_OBJS, chaptersObjs);
                extras.putParcelable(ChapterDetailActivity.BOOK_OBJ, bk);
                extras.putInt(ChapterDetailActivity.SELECTED_CHAP_OBJ, chaperPosBook);
                chapterDetailActivityClickIntent.putExtras(extras);
                act.startActivity(chapterDetailActivityClickIntent);
            }
        });

        myRecycler.setAdapter(mAdapter);
    }

    private void startLoader() {
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
        String whereClause = DBContract.Chapters.TABLE_CHAPTER_BOOK_COLUMN_ID + "=?";
        String[] whereArgs = {bk.getId() + ""};
        return new CursorLoader(this, DBContract.Chapters.CONTENT_URI, DBContract.CHAPTER_PROJECTION, whereClause, whereArgs, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (loader.getId() == OFFLINE_CHAPTERS_DATA_LOADER) {
            Cursor mCursor = (Cursor) data;
            chapters_list = new ArrayList<Chapter>();
            for (int i = 0; i < mCursor.getCount(); i++) {
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
