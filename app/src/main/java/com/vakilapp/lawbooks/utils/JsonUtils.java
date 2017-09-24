package com.vakilapp.lawbooks.utils;

import android.content.Context;
import android.util.SparseArray;

import com.vakilapp.lawbooks.models.Book;
import com.vakilapp.lawbooks.provider.DBContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JsonUtils {

    public static ArrayList<Book> getBookFromJson(Context context, String booksJsonStr, SparseArray<Book> offlineSet)
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

        for (int i = 0; i < booksArray.length(); i++) {
            JSONObject bookJSONObj = booksArray.getJSONObject(i);
            Book currentBook = new Book(bookJSONObj.getInt(BOOK_OBJ_ATTR_ID),
                    bookJSONObj.getString(BOOK_OBJ_ATTR_NAME),
                    bookJSONObj.getInt(BOOK_OBJ_ATTR_VERSION),0,bookJSONObj.getInt(BOOK_OBJ_ATTR_NOOFCHAP));

            int f = offlineSet.indexOfKey((int)currentBook.getId());
            if(f == -1)
                context.getContentResolver().insert(DBContract.Books.CONTENT_URI, currentBook.getContentValues());
            else
            {
                Book currentBookOffline = offlineSet.get((int) currentBook.getId());
                // If book is downloaded
                if (currentBookOffline.getDownloaded() == 1) {
                    currentBook.setDownloaded(1);
                } else {
                    currentBook.setDownloaded(0);
                }

                if (currentBookOffline.getVersion() != currentBook.getVersion()) {
                    if (currentBookOffline.getDownloaded() == 1)
                        currentBook.setDownloaded(2);
                    // dont update the version number, just update the download flag
                    String whereClause = DBContract.Books.TABLE_BOOK_COLUMN_ID+"=?";
                    String [] whereArgs = {"2"};
                    context.getContentResolver().update(DBContract.Books.CONTENT_URI, currentBook.getContentValues(),whereClause,whereArgs);
                }
            }
            parsedBooksData.add(currentBook);
        }

        return parsedBooksData;
    }


}
