package com.teststation.paveloso.sailboattracker.Utils;

import android.content.Context;
import android.net.TrafficStats;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.teststation.paveloso.sailboattracker.Entity.Sailboat;
import com.teststation.paveloso.sailboattracker.MainActivity;
import com.teststation.paveloso.sailboattracker.MapsActivity;
import com.teststation.paveloso.sailboattracker.R;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
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

        String parseUrl = PropertiesProvider.getProperties().getProperty("race-data-parse-url"); // url goes here

        try {
            Connection.Response execute = Jsoup.connect(parseUrl).execute();
//            Document doc = Jsoup.connect(parseUrl)
//                    .timeout(0)
//                    .get();
            Document doc = Jsoup.parse(execute.body());

            Element currentStandingTable = doc.getElementById("currentstandings");

            Element overallRaceTable = doc.getElementById("leaderboard");

            for (Element row : currentStandingTable.select("tr")) {
                Elements tds = row.select("td");
                if (tds.size() > 0) {
                    Sailboat sailboat = new Sailboat();
                    if (tds.get(9).text().equals("Stealth")) {
                        sailboat.setStealth(true);
                    }
                    Elements boatNameBlock = tds.get(1).getElementsByAttribute("class");
                    sailboat.setName(boatNameBlock.get(0).text());
                    boolean joker = tds.get(1).text().contains("Joker");
                    sailboat.setJoker(joker);

                    String yachtStatus = tds.get(9).text().toLowerCase();
                    switch (yachtStatus) {
                        case "racing":
                            sailboat.setYachtStatus('r');
                            break;
                        case "stealth":
                            sailboat.setYachtStatus('s');
                            sailboat.setStealth(true);
                            break;
                        case "finished":
                            sailboat.setYachtStatus('f');
                            break;
                        default:
                            sailboat.setYachtStatus('-');
                    }

                    if (!tds.get(10).text().isEmpty()) {
                        sailboat.setYachtStatus('f');
                    }

                    if (!sailboat.isStealth()) {
                        sailboat.setPosition(Integer.parseInt(tds.get(0).text()));
                        sailboat.setLatitude(Float.parseFloat(tds.get(2).text()));
                        sailboat.setLongitude(Float.parseFloat(tds.get(3).text()));
                        if (!sailboat.getYachtStatus().equals('f')) {
                            sailboat.setDtf(Double.parseDouble(tds.get(4).text().replace("NM", "")));
                            sailboat.setSog(String.format("%.1f", Double.parseDouble(tds.get(5).text().replace("KN", ""))));
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH.mm");
                            sailboat.setLastReport(tds.get(8).text().replace(" (UTC)", ""));
                        }
                    }
                    sailboat.setResourceColor(findColorForName(sailboat.getName()));

                    sailboatsData.add(sailboat);
                }
            }

            for (Element row : overallRaceTable.select("tr")) {
                Elements tds = row.select("td");
                if (tds.size() > 0) {
                    Elements boatNameBlock = tds.get(1).getElementsByAttribute("class");
                    for (Sailboat sb : sailboatsData) {
                        if (sb.getName().equals(boatNameBlock.get(0).text())) {
                            sb.setOverallPosition(Integer.parseInt(tds.get(0).text()));
                            sb.setOverallPoints(Integer.parseInt(tds.get(20).text()));
                        }
                    }
                }
            }
            //debug
//            sailboatsData.clear();
            if (sailboatsData.isEmpty()) {
                throw new IllegalStateException("Sailboats list is empty");
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "Empty Sailboats list", e);
            MapsActivity.displayToastOnMap("Received empty data");
        } catch (HttpStatusException e) {
            Log.e(TAG, "Status code: " + e.getStatusCode(), e);
            MapsActivity.displayToastOnMap("Can't get data from source");
        } catch (IOException e) {
            Log.e(TAG, "Can't connect to " + parseUrl, e);
            MapsActivity.displayToastOnMap("Something went wrong");
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
