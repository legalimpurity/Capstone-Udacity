package com.vakilapp.lawbooks.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rajatkhanna on 24/09/17.
 */

public class UrlUtils {

    private static final String BOOKS_ROOT_URL = "https://secure.legalimpurity.com/vakil/v3/theapi/books.php";

    public static URL buildBookListUrl() {
        Uri builtUri = Uri.parse(BOOKS_ROOT_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
