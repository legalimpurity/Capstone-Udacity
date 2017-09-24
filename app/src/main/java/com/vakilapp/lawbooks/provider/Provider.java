package com.vakilapp.lawbooks.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by rajatkhanna on 20/09/17.
 */

public class Provider extends ContentProvider {

    private static final int BOOK_ITEM = 100;
    private static final int BOOK_DIR = 101;

    private static final int CHAPTER_ITEM = 200;
    private static final int CHAPTER_DIR = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper mDbHelper;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DBContract.PATH_BOOK + "/#", BOOK_ITEM);
        matcher.addURI(authority, DBContract.PATH_BOOK, BOOK_DIR);

        matcher.addURI(authority, DBContract.PATH_CHAPTER + "/#", CHAPTER_ITEM);
        matcher.addURI(authority, DBContract.PATH_CHAPTER, CHAPTER_DIR);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case BOOK_ITEM:
                retCursor = mDbHelper.getReadableDatabase().query(
                        DBContract.Books.TABLE_NAME_BOOK,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BOOK_DIR:
                retCursor = mDbHelper.getReadableDatabase().query(
                        DBContract.Books.TABLE_NAME_BOOK,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CHAPTER_ITEM:
                retCursor = mDbHelper.getReadableDatabase().query(
                        DBContract.Chapters.TABLE_NAME_CHAPTER,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CHAPTER_DIR:
                retCursor = mDbHelper.getReadableDatabase().query(
                        DBContract.Chapters.TABLE_NAME_CHAPTER,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK_ITEM:
                return DBContract.Books.CONTENT_USER_ITEM_TYPE;
            case BOOK_DIR:
                return DBContract.Books.CONTENT_USER_TYPE;
            case CHAPTER_ITEM:
                return DBContract.Chapters.CONTENT_USER_ITEM_TYPE;
            case CHAPTER_DIR:
                return DBContract.Chapters.CONTENT_USER_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri;
        long _id = 0;
        switch (sUriMatcher.match(uri)) {
            case BOOK_DIR:
                try {
                    _id = db.insertOrThrow(DBContract.Books.TABLE_NAME_BOOK, null, contentValues);
                } catch (SQLiteConstraintException e) {
                    _id = update(uri, contentValues, DBContract.Books.TABLE_BOOK_COLUMN_ID + "=?",
                            new String[]{contentValues.getAsString(DBContract.Books.TABLE_BOOK_COLUMN_ID)});
                }
                if (_id > 0)
                    returnUri = DBContract.Books.buildBooksUri(_id);
                else
                    throw new SQLException("Failed to insert or update row " + uri);
                break;
            case CHAPTER_DIR:
                try {
                    _id = db.insert(DBContract.Chapters.TABLE_NAME_CHAPTER, null, contentValues);
                } catch (SQLiteConstraintException e) {
                    _id = update(uri, contentValues, DBContract.Books.TABLE_BOOK_COLUMN_ID + "=?",
                            new String[]{contentValues.getAsString(DBContract.Books.TABLE_BOOK_COLUMN_ID)});
                }
                if (_id > 0)
                    returnUri = DBContract.Chapters.buildChaptersUri(_id);
                else
                    throw new SQLException("Failed to insert row " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case BOOK_DIR:
                rowsDeleted = db.delete(DBContract.Books.TABLE_NAME_BOOK, selection, selectionArgs);
                break;
            case CHAPTER_DIR:
                rowsDeleted = db.delete(DBContract.Chapters.TABLE_NAME_CHAPTER, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }
        if (selection == null || 0 != rowsDeleted)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int update;
        switch (sUriMatcher.match(uri)) {
            case BOOK_DIR:
                update = db.update(DBContract.Books.TABLE_NAME_BOOK, values, selection, selectionArgs);
                break;
            case CHAPTER_DIR:
                update = db.update(DBContract.Chapters.TABLE_NAME_CHAPTER, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }
        if (update > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return update;
    }
}