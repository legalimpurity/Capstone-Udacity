package com.vakilapp.lawbooks.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.SparseArray;

import com.vakilapp.lawbooks.models.Book;
import com.vakilapp.lawbooks.utils.JsonUtils;
import com.vakilapp.lawbooks.utils.RequestUtils;
import com.vakilapp.lawbooks.utils.UrlUtils;

import java.net.URL;
import java.util.ArrayList;

public class BooksOnlineLoader extends AsyncTaskLoader {

    public static final String BBID_PARAM = "BBID";
    public static final String OFFLINE_BOOKS_PARAM = "OFFLINE_BOOKS";

    private Context context;
    private Bundle args;


    public BooksOnlineLoader(Context context, Bundle args) {
        super(context);
        this.context = context;
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        if (args == null) {
            return;
        }
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        String BBID = args.getString(BBID_PARAM);
        SparseArray<Book> booksOffline = args.getSparseParcelableArray(OFFLINE_BOOKS_PARAM);

        if (BBID == null || TextUtils.isEmpty(BBID)) {
            return null;
        }

        URL bookRequestUrl = UrlUtils.buildBookListUrl();

        try {
            String jsonBooksResponse = RequestUtils
                    .getResponseFromHttpUrl(bookRequestUrl, BBID);
            ArrayList<Book> output = JsonUtils.getBookFromJson(context, jsonBooksResponse, booksOffline);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
