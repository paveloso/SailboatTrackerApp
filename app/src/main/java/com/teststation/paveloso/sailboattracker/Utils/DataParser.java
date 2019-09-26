package com.teststation.paveloso.sailboattracker.Utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DataParser {

    private static final String TAG = DataParser.class.getSimpleName();

    private void parseGetData() {

        String parseUrl = ""; // url goes here

        try {
            Document doc = Jsoup.connect(parseUrl)
                    .timeout(0)
                    .get();

            Element standingTable = doc.getElementById("currentstandings");

            for (Element table : doc.select("table")) {
                for (Element row : table.select("tr")) {
                    Elements tds = row.select("td");
                    if (tds.size() > 6) {
                        System.out.println(tds.get(0).text() + ":" + tds.get(1).text());
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Can't connect to " + parseUrl, e);
        }

    }

}
