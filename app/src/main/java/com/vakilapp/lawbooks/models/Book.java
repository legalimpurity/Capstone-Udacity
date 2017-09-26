package com.vakilapp.lawbooks.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.vakilapp.lawbooks.provider.DBContract;

/**
 * Created by rajatkhanna on 24/09/17.
 */

public class Book implements Parcelable {

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private long id;
    private String name;
    private long version;
    // 0 = NOT DOWNLOADED, 1 = DOWNLOADED, 2 = UPDATED
    private long downloaded;
    private long numberOfChapters;

    public Book(int id, String name, int version, int downloaded, int numberOfChapters) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.downloaded = downloaded;
        this.numberOfChapters = numberOfChapters;
    }

    public Book(Cursor mCursor) {
        this.id = mCursor.getLong(DBContract.BOOK_PROJECTION_INDEXES.TABLE_BOOK_COLUMN_ID);
        this.name = mCursor.getString(DBContract.BOOK_PROJECTION_INDEXES.TABLE_BOOK_COLUMN_NAME_STRING);
        this.version = mCursor.getLong(DBContract.BOOK_PROJECTION_INDEXES.TABLE_BOOK_COLUMN_VERSION_INT);
        this.downloaded = mCursor.getLong(DBContract.BOOK_PROJECTION_INDEXES.TABLE_BOOK_COLUMN_DOWNLOADED_INT);
        this.numberOfChapters = mCursor.getLong(DBContract.BOOK_PROJECTION_INDEXES.TABLE_BOOK_NOOFCHAP);
    }

    protected Book(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.version = in.readLong();
        this.downloaded = in.readLong();
        this.numberOfChapters = in.readLong();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(long downloaded) {
        this.downloaded = downloaded;
    }

    public long getNumberOfChapters() {
        return numberOfChapters;
    }

    public void setNumberOfChapters(long numberOfChapters) {
        this.numberOfChapters = numberOfChapters;
    }

    public ContentValues getContentValues() {
        ContentValues bookObjectValues = new ContentValues();
        bookObjectValues.put(DBContract.Books.TABLE_BOOK_COLUMN_ID, id);
        bookObjectValues.put(DBContract.Books.TABLE_BOOK_COLUMN_NAME_STRING, name);
        bookObjectValues.put(DBContract.Books.TABLE_BOOK_COLUMN_VERSION_INT, version);
        bookObjectValues.put(DBContract.Books.TABLE_BOOK_COLUMN_DOWNLOADED_INT, downloaded);
        bookObjectValues.put(DBContract.Books.TABLE_BOOK_NOOFCHAP, numberOfChapters);
        return bookObjectValues;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.version);
        dest.writeLong(this.downloaded);
        dest.writeLong(this.numberOfChapters);
    }
}
