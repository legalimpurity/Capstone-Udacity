package com.vakilapp.lawbooks.utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.SparseArray;

import com.vakilapp.lawbooks.models.Book;
import com.vakilapp.lawbooks.models.Chapter;
import com.vakilapp.lawbooks.provider.DBContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;


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

        JSONObject bookJson = new JSONObject(booksJsonStr).getJSONObject(PARENT_TAG);

        JSONArray booksArray = bookJson.getJSONArray(SUB_TAG);

        parsedBooksData = new ArrayList<Book>();

        for (int i = 0; i < booksArray.length(); i++) {
            JSONObject bookJSONObj = booksArray.getJSONObject(i);
            Book currentBook = new Book(bookJSONObj.getInt(BOOK_OBJ_ATTR_ID),
                    bookJSONObj.getString(BOOK_OBJ_ATTR_NAME),
                    bookJSONObj.getInt(BOOK_OBJ_ATTR_VERSION), 0, bookJSONObj.getInt(BOOK_OBJ_ATTR_NOOFCHAP));

            int f = offlineSet.indexOfKey((int) currentBook.getId());
            if (f == -1)
                context.getContentResolver().insert(DBContract.Books.CONTENT_URI, currentBook.getContentValues());
            else {
                Book currentBookOffline = offlineSet.get((int) currentBook.getId());
                // If book is downloaded
                if (currentBookOffline.getDownloaded() == 1) {
                    currentBook.setDownloaded(1);
                } else if (currentBookOffline.getDownloaded() == 2) {
                    currentBook.setDownloaded(2);
                } else {
                    currentBook.setDownloaded(0);
                }

                if (currentBookOffline.getVersion() != currentBook.getVersion()) {
                    if (currentBookOffline.getDownloaded() == 1)
                        currentBook.setDownloaded(2);
                    // dont update the version number, just update the download flag
                    currentBook.setDownloaded(2);
                }

                String whereClause = DBContract.Books.TABLE_BOOK_COLUMN_ID + "=?";
                String[] whereArgs = {currentBook.getId() + ""};
                context.getContentResolver().update(DBContract.Books.CONTENT_URI, currentBook.getContentValues(), whereClause, whereArgs);
            }
            parsedBooksData.add(currentBook);
        }

        return parsedBooksData;
    }

    public static ArrayList<Chapter> getChapterFromJson(Context context, String booksJsonStr, Book bbid)
            throws JSONException {

        final String PARENT_TAG = "apiResponse";
        final String SUB_TAG = "response";

        final String BOOK_OBJ_ATTR_ID = "id";
        final String BOOK_OBJ_ATTR_MODULE_NAME = "moduleName";
        final String BOOK_OBJ_ATTR_CONTENT = "content";


        ArrayList<Chapter> parsedChaptersData;

        JSONObject chaptersJson = new JSONObject(booksJsonStr).getJSONObject(PARENT_TAG);

        JSONArray chaptersArray = chaptersJson.getJSONArray(SUB_TAG);

        parsedChaptersData = new ArrayList<Chapter>();

        Vector<ContentValues> chaptersVector = new Vector<ContentValues>(chaptersArray.length());

        for (int i = 0; i < chaptersArray.length(); i++) {
            JSONObject chapterJSONObj = chaptersArray.getJSONObject(i);
            Chapter currentChapter = new Chapter(chapterJSONObj.getLong(BOOK_OBJ_ATTR_ID), bbid.getId(), chapterJSONObj.getString(BOOK_OBJ_ATTR_MODULE_NAME), chapterJSONObj.getString(BOOK_OBJ_ATTR_CONTENT));
            chaptersVector.add(currentChapter.getContentValues());
            parsedChaptersData.add(currentChapter);
        }

        int insertedval = 0;
        if (chaptersVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[chaptersVector.size()];
            chaptersVector.toArray(cvArray);
            insertedval = context.getContentResolver().bulkInsert(DBContract.Chapters.CONTENT_URI, cvArray);
        }

        bbid.setDownloaded(1);
        String whereClause = DBContract.Books.TABLE_BOOK_COLUMN_ID + "=?";
        String[] whereArgs = {bbid.getId() + ""};
        context.getContentResolver().update(DBContract.Books.CONTENT_URI, bbid.getContentValues(), whereClause, whereArgs);

        return parsedChaptersData;
    }


}
