package com.teststation.paveloso.sailboattracker.Utils;

import android.content.Context;
import android.net.TrafficStats;
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

    private static final String TAG = DataParser.class.getSimpleName();

    private List<Sailboat> sailboatsData;
    private Context context;

    private void parseGetData() {

        sailboatsData = new ArrayList<>();

//        String info = "";
//
//        info += "Mobile interface:\n";
//        info += ("\tReceived: " + TrafficStats.getMobileRxBytes() + " bytes\n");
//        info += ("\tTransmitted: " + TrafficStats.getMobileTxBytes() + "bytes\n");
//
//        info += "All network:\n";
//        info += ("\tReceived: " + TrafficStats.getTotalRxBytes() + " bytes\n");
//        info += ("\tTransmitted: " + TrafficStats.getTotalTxBytes() + " bytes\n");

        String parseUrl = "https://www.clipperroundtheworld.com/race/standings"; // url goes here

        try {
            Document doc = Jsoup.connect("https://www.clipperroundtheworld.com/race/standings")
                    .timeout(0)
                    .get();

            Element currentStandingTable = doc.getElementById("currentstandings");

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
                    sailboat.setSog(String.format("%.1f", Double.parseDouble(tds.get(5).text().replace("KN", ""))));
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH.mm");
                    sailboat.setLastReport(tds.get(8).text().replace(" (UTC)", ""));
                    sailboat.setResourceColor(findColorForName(sailboat.getName()));
                    sailboat.setJoker(joker);

                    sailboatsData.add(sailboat);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Can't connect to " + parseUrl, e);
        }

//        String infoAfter = "";
//
//        infoAfter += "Mobile interface:\n";
//        infoAfter += ("\tReceived: " + TrafficStats.getMobileRxBytes() + " bytes\n");
//        infoAfter += ("\tTransmitted: " + TrafficStats.getMobileTxBytes() + "bytes\n");
//
//        infoAfter += "All network:\n";
//        infoAfter += ("\tReceived: " + TrafficStats.getTotalRxBytes() + " bytes\n");
//        infoAfter += ("\tTransmitted: " + TrafficStats.getTotalTxBytes() + " bytes\n");
//
//        System.out.println(info + "\n" + infoAfter);

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
