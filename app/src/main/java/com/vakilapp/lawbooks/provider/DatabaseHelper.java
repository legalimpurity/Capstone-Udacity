package com.vakilapp.lawbooks.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rajatkhanna on 20/09/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LawBooksDb.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBContract.Books.getBooksCreateQuery());
        sqLiteDatabase.execSQL(DBContract.Chapters.getChaptersCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DBContract.Books.getBooksDeleteQuery());
        sqLiteDatabase.execSQL(DBContract.Chapters.getChaptersDeleteQuery());
        onCreate(sqLiteDatabase);
    }
}