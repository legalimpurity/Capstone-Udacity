package com.vakilapp.lawbooks.utils;

import android.content.Context;

import com.vakilapp.lawbooks.models.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JsonUtils {

    public static ArrayList<Book> getBookFromJson(Context context, String booksJsonStr)
            throws JSONException {

        final String PARENT_TAG = "apiResponse";
        final String SUB_TAG = "response";

        final String BOOK_OBJ_ATTR_ID = "id";
        final String BOOK_OBJ_ATTR_NAME = "name";
        final String BOOK_OBJ_ATTR_VERSION = "version";
        final String BOOK_OBJ_ATTR_NOOFCHAP = "chapterCount";


        ArrayList<Book> parsedBooksData;

        JSONObject movieJson = new JSONObject(booksJsonStr).getJSONObject(PARENT_TAG);

        JSONArray booksArray = movieJson.getJSONArray(SUB_TAG);

        parsedBooksData = new ArrayList<Book>();

//        MovieDbHelper movieDbHelper = new MovieDbHelper(context);


//        HashSet<Long> bookmarked_ids = new HashSet<Long>();
//        // Get all bookmarks
//        Cursor cursor = movieDbHelper.getReadableDatabase().query(
//                MoviesContract.MoviesEntry.TABLE_NAME,
//                new String[]{MoviesContract.MoviesEntry.COLUMN_API_ID},
//                null,
//                null,
//                null,
//                null,
//                null);
//        while (cursor.moveToNext()) {
//            bookmarked_ids.add(cursor.getLong(MoviesContract.MOVIES_PROJECTION_INDEXES.COLUMN_API_ID_POSITION));
//        }
//
        for (int i = 0; i < booksArray.length(); i++) {
            JSONObject bookJSONObj = booksArray.getJSONObject(i);
            parsedBooksData.add(new Book(bookJSONObj.getInt(BOOK_OBJ_ATTR_ID),
                    bookJSONObj.getString(BOOK_OBJ_ATTR_NAME),
                    bookJSONObj.getInt(BOOK_OBJ_ATTR_VERSION),false,bookJSONObj.getInt(BOOK_OBJ_ATTR_NOOFCHAP)));
        }

        return parsedBooksData;
    }


}
