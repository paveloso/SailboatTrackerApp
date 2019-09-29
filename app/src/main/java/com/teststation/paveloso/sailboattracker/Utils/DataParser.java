package com.teststation.paveloso.sailboattracker.Utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.teststation.paveloso.sailboattracker.Entity.Sailboat;
import com.teststation.paveloso.sailboattracker.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataParser {

    private static final String TAG = "=== " + DataParser.class.getSimpleName();

    private List<Sailboat> sailboatsData;
    private Context context;

    private void parseGetData() {

        Log.i(TAG, "parseGetData: start");
        System.out.println("======= start here ========");

        sailboatsData = new ArrayList<>();

        String parseUrl = "https://www.clipperroundtheworld.com/race/standings"; // url goes here

        try {
            Document doc = Jsoup.connect("https://www.clipperroundtheworld.com/race/standings")
                    .timeout(0)
                    .get();

            Element currentStandingTable = doc.getElementById("currentstandings");

            Log.i(TAG, "parseGetData: " + currentStandingTable);

//            for (Element table : doc.select("table")) {
                for (Element row : currentStandingTable.select("tr")) {
                    Elements tds = row.select("td");
                    if (tds.size() > 0) {
                        Elements boatNameBlock = tds.get(1).getElementsByAttribute("class");
                        boolean joker = tds.get(1).text().contains("Joker");

                        Sailboat sailboat = new Sailboat();
                        sailboat.setPosition(Integer.parseInt(tds.get(0).text()));
                        sailboat.setName(boatNameBlock.get(0).text());
                        sailboat.setLatitude(Float.parseFloat(tds.get(2).text()));
                        sailboat.setLongitude(Float.parseFloat(tds.get(3).text()));
                        sailboat.setDtf(Double.parseDouble(tds.get(4).text().replace("NM", "")));
                        sailboat.setSog(Double.parseDouble(tds.get(5).text().replace("KN", "")));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH.mm");
                        sailboat.setLastReport(sdf.parse(tds.get(8).text().replace(" (UTC)", "")));
                        sailboat.setResourceColor(findColorForName(sailboat.getName()));
                        sailboat.setJoker(joker);

                        Log.w(TAG, "parseGetData: " + sailboat);

                        sailboatsData.add(sailboat);

//                    if (tds.size() > 6) {
//                        System.out.println(tds.get(0).text() + ":" + tds.get(1).text());
//                    }
                    }
                }
//            }
        } catch (IOException e) {
            Log.e(TAG, "Can't connect to " + parseUrl, e);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Can't pase the Date", e);
        }

    }

    private int findColorForName(String name) {
        if (name.equals("Visit Sanya, China")) {
            return ContextCompat.getColor(context, R.color.visit_sanya);
        } else if (name.equals("Qingdao")) {
            return ContextCompat.getColor(context, R.color.qingdao);
        } else if (name.equals("Ha Long Bay, Viet nam")) {
            return ContextCompat.getColor(context, R.color.ha_long_bay);
        } else if (name.equals("Seattle")) {
            return ContextCompat.getColor(context, R.color.seattle);
        } else if (name.equals("Dare To Lead")) {
            return ContextCompat.getColor(context, R.color.date_to_lead);
        } else if (name.equals("Zhuhai")) {
            return ContextCompat.getColor(context, R.color.zhuhai);
        } else if (name.equals("Punta del Este")) {
            return ContextCompat.getColor(context, R.color.punta_del_este);
        } else if (name.equals("GoToBermuda")) {
            return ContextCompat.getColor(context, R.color.gotobermuda);
        } else if (name.equals("WTC Logistics")) {
            return ContextCompat.getColor(context, R.color.wtc_logistic);
        } else if (name.equals("Unicef")) {
            return ContextCompat.getColor(context, R.color.unicef);
        } else if (name.equals("Imagine your Korea")) {
            return ContextCompat.getColor(context, R.color.imagine_yor_korea);
        } else {
            return 0;
        }
    }

    public List<Sailboat> getSailboatsData(Context context) {
        this.context = context;
        parseGetData();
        return sailboatsData;
    }

}
