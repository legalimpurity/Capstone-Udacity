package com.vakilapp.lawbooks.provider;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

/**
 * Created by rajatkhanna on 19/09/17.
 */

public class DBContract
{
    public static final String CONTENT_AUTHORITY = "com.vakilapp.lawbooks.cp";
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);

    public static final String PATH_BOOK = "book";
    public static final String PATH_CHAPTER = "chapter";

    public DBContract() {
    }

    public static abstract class Books implements BaseColumns {
        @NonNull
        public static final String CONTENT_URI_STRING = "content://" + CONTENT_AUTHORITY + "/" + PATH_BOOK;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

        public static final String CONTENT_USER_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;
        public static final String CONTENT_USER_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;

        public static final String TABLE_NAME_BOOK = "Books";

        public static final String TABLE_BOOK_COLUMN_ID = "_id";
        public static final String TABLE_BOOK_COLUMN_NAME_STRING = "name";
        public static final String TABLE_BOOK_COLUMN_VERSION_INT = "version";
        public static final String TABLE_BOOK_NOOFCHAP = "chapter_count";
        // 0 = NOT DOWNLOADED, 1 = DOWNLOADED, 2 = UPDATED
        public static final String TABLE_BOOK_COLUMN_DOWNLOADED_INT = "downloaded";


        public static String getBooksCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME_BOOK + " (" +
                    TABLE_BOOK_COLUMN_ID + " LONG NOT NULL PRIMARY KEY, " +
                    TABLE_BOOK_COLUMN_NAME_STRING + " TEXT NOT NULL, " +
                    TABLE_BOOK_COLUMN_VERSION_INT + " LONG NOT NULL, " +
                    TABLE_BOOK_NOOFCHAP + " LONG NOT NULL, " +
                    TABLE_BOOK_COLUMN_DOWNLOADED_INT + " BOOL NOT NULL " + ")";
        }
        public static String getBooksDeleteQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME_BOOK;
        }

        public static Uri buildBooksUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final String[] BOOK_PROJECTION = {
            Books.TABLE_BOOK_COLUMN_ID,
            Books.TABLE_BOOK_COLUMN_NAME_STRING,
            Books.TABLE_BOOK_COLUMN_VERSION_INT,
            Books.TABLE_BOOK_NOOFCHAP,
            Books.TABLE_BOOK_COLUMN_DOWNLOADED_INT
    };


    public static final class BOOK_PROJECTION_INDEXES {
        public static final int TABLE_BOOK_COLUMN_ID = 0;
        public static final int TABLE_BOOK_COLUMN_NAME_STRING = 1;
        public static final int TABLE_BOOK_COLUMN_VERSION_INT = 2;
        public static final int TABLE_BOOK_NOOFCHAP = 3;
        public static final int TABLE_BOOK_COLUMN_DOWNLOADED_INT = 3;
    };

    public static abstract class Chapters implements BaseColumns {
        @NonNull
        public static final String CONTENT_URI_STRING = "content://" + CONTENT_AUTHORITY + "/" + PATH_CHAPTER;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

        public static final String CONTENT_USER_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CHAPTER;
        public static final String CONTENT_USER_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CHAPTER;

        public static final String TABLE_NAME_CHAPTER = "Chapters";

        public static final String TABLE_CHAPTER_COLUMN_ID = "_id";
        public static final String TABLE_BOOK_CHAPTER_COLUMN_ID = "book_id";
        public static final String TABLE_CHAPTER_COLUMN_CHAPTER_NAME_STRING = "chapter_name";
        public static final String TABLE_CHAPTER_COLUMN_CHAPTER_CONTENT_STRING = "chapter_content";

        public static String getChaptersCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME_CHAPTER + " (" +
                    TABLE_CHAPTER_COLUMN_ID + " LONG NOT NULL PRIMARY KEY, " +
                    TABLE_BOOK_CHAPTER_COLUMN_ID + " LONG NOT NULL, " +
                    TABLE_CHAPTER_COLUMN_CHAPTER_NAME_STRING + " TEXT NOT NULL, " +
                    TABLE_CHAPTER_COLUMN_CHAPTER_CONTENT_STRING + " TEXT NOT NULL " + ")";
        }

        public static String getChaptersDeleteQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME_CHAPTER;
        }

        public static Uri buildChaptersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final String[] CHAPTER_PROJECTION = {
            Chapters.TABLE_CHAPTER_COLUMN_ID,
            Chapters.TABLE_BOOK_CHAPTER_COLUMN_ID,
            Chapters.TABLE_CHAPTER_COLUMN_CHAPTER_NAME_STRING,
            Chapters.TABLE_CHAPTER_COLUMN_CHAPTER_CONTENT_STRING
    };


    public static final class CHAPTER_PROJECTION_INDEXES {
        public static final int TABLE_CHAPTER_COLUMN_ID = 0;
        public static final int TABLE_BOOK_CHAPTER_COLUMN_ID = 1;
        public static final int TABLE_CHAPTER_COLUMN_CHAPTER_NAME_STRING = 2;
        public static final int TABLE_CHAPTER_COLUMN_CHAPTER_CONTENT_STRING = 3;
    };

}