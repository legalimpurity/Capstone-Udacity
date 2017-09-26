package com.vakilapp.lawbooks.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by rajatkhanna on 24/09/17.
 */

public class RequestUtils {

    public static String getResponseFromHttpUrl(URL url, String BBID) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestMethod("POST");

        try {

            JSONObject parent = new JSONObject();
            parent.put("BBID", BBID);
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(parent.toString());
            wr.flush();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(10000);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
