package com.teststation.paveloso.sailboattracker.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teststation.paveloso.sailboattracker.Entity.Sailboat;
import com.teststation.paveloso.sailboattracker.R;

import java.util.List;

public class DataParserAsync extends AsyncTask<Void, Integer, List<Sailboat>> {

    private static final String TAG = DataParserAsync.class.getSimpleName();

    private Context context;
    private GoogleMap map;
    private BitmapDescriptor locationMarker;
    private ProgressDialog progressDialog;

    private DataParser dp = new DataParser();

    public DataParserAsync(Context context, GoogleMap map, BitmapDescriptor locationMarker) {
        this.context = context;
        this.map = map;
        this.locationMarker = locationMarker;
        this.progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String text = context.getString(R.string.obtaining_race_data);
        progressDialog.setMessage(text);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected List<Sailboat> doInBackground(Void... voids) {

        List<Sailboat> sailboats = dp.getSailboatsData(context);

        return sailboats;
    }

    @Override
    protected void onPostExecute(List<Sailboat> sailboats) {
        progressDialog.dismiss();
        if (!sailboats.isEmpty()) {
            for (Sailboat sb : sailboats) {
                LatLng boatPosition = new LatLng(sb.getLatitude(), sb.getLongitude());
                int color = getColorForName(sb.getName());
                locationMarker = LayoutUtils.getBitmapFromVector(context,
                        (sb.getCog() > 0 ? R.drawable.boat_icon : R.drawable.boat_icon_blank),
                        ContextCompat.getColor(context, color));

                map.addMarker(new MarkerOptions().icon(locationMarker).position(boatPosition)
                        .title(sb.getName())
                        .rotation(sb.getCog())
                );
                if (sb.getPosition() == 1) {
                    map.moveCamera(CameraUpdateFactory.newLatLng(boatPosition));
                    map.animateCamera(CameraUpdateFactory.zoomTo(7), 2000, null);
                }
            }
            LayoutUtils.getBitmapFromVector(context, R.drawable.boat_icon,
                    ContextCompat.getColor(context, R.color.app_buttons_red));
        }
    }

    private int getColorForName(String name) {
        switch (name) {
            case "Visit Sanya, China":
                return R.color.visit_sanya;
            case "Qingdao":
                return R.color.qingdao;
            case "Ha Long Bay, Viet nam":
                return R.color.ha_long_bay;
            case "Seattle":
                return R.color.seattle;
            case "Dare To Lead":
                return R.color.date_to_lead;
            case "Zhuhai":
                return R.color.zhuhai;
            case "Punta del Este":
                return R.color.punta_del_este;
            case "GoToBermuda":
                return R.color.gotobermuda;
            case "WTC Logistics":
                return R.color.wtc_logistic;
            case "Unicef":
                return R.color.unicef;
            case "Imagine your Korea":
                return R.color.imagine_yor_korea;
                default:
                    return R.color.blank;
        }
    }


}
