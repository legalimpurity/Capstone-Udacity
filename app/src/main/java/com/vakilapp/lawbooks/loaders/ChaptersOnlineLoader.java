package com.vakilapp.lawbooks.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.vakilapp.lawbooks.models.Book;
import com.vakilapp.lawbooks.utils.JsonUtils;
import com.vakilapp.lawbooks.utils.RequestUtils;
import com.vakilapp.lawbooks.utils.UrlUtils;

import java.net.URL;

public class ChaptersOnlineLoader extends AsyncTaskLoader {

    public static final String BBID_PARAM_BOOK_OBJ = "BBID_PARAM_BOOK_OBJ";
    public static final String BBID_PARAM_BOOK_INDEX = "BBID_PARAM_BOOK_INDEX";

    private Context context;
    private Bundle args;


    public ChaptersOnlineLoader(Context context, Bundle args) {
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
    public Integer loadInBackground() {
        Book BBID = args.getParcelable(BBID_PARAM_BOOK_OBJ);

        URL bookRequestUrl = UrlUtils.buildBookListUrl();

        try {
            String jsonBooksResponse = RequestUtils
                    .getResponseFromHttpUrl(bookRequestUrl,BBID.getId()+"");
            JsonUtils.getChapterFromJson(context,jsonBooksResponse,BBID);
//            return JsonUtils.getChapterFromJson(context,jsonBooksResponse,BBID);
            return args.getInt(BBID_PARAM_BOOK_INDEX);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
