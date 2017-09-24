package com.vakilapp.lawbooks.interfaces;

import android.view.View;

import com.vakilapp.lawbooks.models.Book;

/**
 * Created by rajatkhanna on 02/08/17.
 */

public interface BookClickListener {
    public void onBookDownAsked(Book bookName, View view);
}
