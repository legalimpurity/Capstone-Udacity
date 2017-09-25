package com.vakilapp.lawbooks.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.vakilapp.lawbooks.provider.DBContract;

/**
 * Created by rajatkhanna on 24/09/17.
 */

public class Chapter implements Parcelable {
    public long id;
    public String chapterName;
    public String content;
    public long book_id;

    public Chapter(long id, long book_id, String chapterName, String content) {
        this.id = id;
        this.book_id = book_id;
        this.chapterName = chapterName;
        this.content = content;
    }

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public ContentValues getContentValues() {
        ContentValues bookObjectValues = new ContentValues();
        bookObjectValues.put(DBContract.Chapters.TABLE_CHAPTER_COLUMN_ID, id);
        bookObjectValues.put(DBContract.Chapters.TABLE_CHAPTER_BOOK_COLUMN_ID, book_id);
        bookObjectValues.put(DBContract.Chapters.TABLE_CHAPTER_COLUMN_CHAPTER_NAME_STRING, chapterName);
        bookObjectValues.put(DBContract.Chapters.TABLE_CHAPTER_COLUMN_CHAPTER_CONTENT_STRING, content);
        return bookObjectValues;
    }

    public Chapter(Cursor mCursor) {
        this.id = mCursor.getLong(DBContract.CHAPTER_PROJECTION_INDEXES.TABLE_CHAPTER_COLUMN_ID);
        this.book_id = mCursor.getLong(DBContract.CHAPTER_PROJECTION_INDEXES.TABLE_BOOK_CHAPTER_COLUMN_ID);
        this.chapterName = mCursor.getString(DBContract.CHAPTER_PROJECTION_INDEXES.TABLE_CHAPTER_COLUMN_CHAPTER_NAME_STRING);
        this.content = mCursor.getString(DBContract.CHAPTER_PROJECTION_INDEXES.TABLE_CHAPTER_COLUMN_CHAPTER_CONTENT_STRING);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.chapterName);
        dest.writeString(this.content);
        dest.writeLong(this.book_id);
    }

    protected Chapter(Parcel in) {
        this.id = in.readLong();
        this.chapterName = in.readString();
        this.content = in.readString();
        this.book_id = in.readLong();
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel source) {
            return new Chapter(source);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
}
