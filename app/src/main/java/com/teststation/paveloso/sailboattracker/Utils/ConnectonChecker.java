package com.teststation.paveloso.sailboattracker.Utils;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectonChecker {

    private static final String TAG = ConnectonChecker.class.getName();

    public boolean isInternetConnectionPossible() {

        boolean success = false;

        try {
            String urlCheck = PropertiesProvider.getProperties().getProperty("connect-check-url");
            URL url = new URL(urlCheck);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to web", e);
        }

        return success;
    }
}
