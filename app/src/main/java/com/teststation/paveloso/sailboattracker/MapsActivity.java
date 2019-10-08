package com.teststation.paveloso.sailboattracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teststation.paveloso.sailboattracker.Entity.Sailboat;
import com.teststation.paveloso.sailboattracker.Utils.DataParserAsync;
import com.teststation.paveloso.sailboattracker.Utils.LayoutUtils;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();

    final Context context = this;

    private GoogleMap mMap;

    private BitmapDescriptor locationMarkerIcon;

    private Button showDetails;

    private List<Sailboat> sailboatListPrepared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        showDetails = (Button) findViewById(R.id.show_details_map);
//        showDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.detailed_race_info);
//                dialog.setTitle(R.string.details);
//
//                dialog.show();
//            }
//        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        Context context = getApplicationContext();

//        locationMarkerIcon = LayoutUtils.getBitmapFromVector(context, R.drawable.boat_icon,
//                ContextCompat.getColor(context, R.color.app_buttons_red));

        DataParserAsync dpa = new DataParserAsync(MapsActivity.this, mMap, locationMarkerIcon, sailboatListPrepared);
        dpa.execute();
        sailboatListPrepared = dpa.getSailboatListPrepared();

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.boat_icon))
//                .rotation(200));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//        mMap.addMarker(new MarkerOptions().icon(locationMarkerIcon).position(sydney).rotation(200));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void openDetails(View view) {
        int rows = 11;
        int columns = 4;

        List<Sailboat> sailboatList = DataParserAsync.getSailboatListPrepared();

        AlertDialog dialog = new AlertDialog.Builder(this).create();

//        Dialog dialog = new Dialog(view.getContext());
//        dialog.setContentView(R.layout.detailed_race_info);
        dialog.setTitle(this.getResources().getString(R.string.details) + "\n(" + (sailboatList == null || sailboatList.isEmpty() ? "-" : sailboatList.get(0).getLastReport()) + " UTC)");

        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        tableLayout.setStretchAllColumns(true);

        TextView textViewH = new TextView(this);
        textViewH.setText("");

        TextView textViewH0 = new TextView(this);
        textViewH0.setText("");
//        textViewH0.setTypeface(textViewH0.getTypeface(), Typeface.BOLD);

        TextView textViewH1 = new TextView(this);
        textViewH1.setText(this.getResources().getText(R.string.pos));
        textViewH1.setTypeface(textViewH1.getTypeface(), Typeface.BOLD);
        textViewH1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        TextView textViewH2 = new TextView(this);
        textViewH2.setText(this.getResources().getText(R.string.name));
        textViewH2.setTypeface(textViewH2.getTypeface(), Typeface.BOLD);

        TextView textViewH3 = new TextView(this);
        textViewH3.setText(this.getResources().getText(R.string.sog));
        textViewH3.setTypeface(textViewH3.getTypeface(), Typeface.BOLD);

        TextView textViewH4 = new TextView(this);
        textViewH4.setText(this.getResources().getText(R.string.dtf));
        textViewH4.setTypeface(textViewH4.getTypeface(), Typeface.BOLD);

        TableRow tableRowHeader = new TableRow(this);
        tableRowHeader.addView(textViewH);
        tableRowHeader.addView(textViewH0);
        tableRowHeader.addView(textViewH1);
        tableRowHeader.addView(textViewH2);
        tableRowHeader.addView(textViewH3);
        tableRowHeader.addView(textViewH4);
        tableLayout.addView(tableRowHeader);

        if (sailboatList != null && !sailboatList.isEmpty()) {
            for (Sailboat sb : sailboatList) {

                TextView empty = new TextView(this);
                empty.setText(" ");

                TextView color = new TextView(this);
                color.setText(" ");
                color.setBackgroundColor(sb.getResourceColor());

                TextView pos = new TextView(this);
                pos.setText(sb.isStealth() ? "-" : String.valueOf(sb.getPosition()));
                pos.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                TextView name = new TextView(this);
                name.setText(sb.getName() + ((sb.isJoker() || sb.isStealth()) ? (sb.isStealth() ? " (S)" : " (J)") : ""));

                TextView sog = new TextView(this);
//                String formate = dfSog.format(sb.getSog());
                sog.setText(sb.isStealth() ? "-" : sb.getSog());

                TextView dtf = new TextView(this);
                dtf.setText(sb.isStealth() ? "-" : String.valueOf(sb.getDtf()));

                TableRow boatRow = new TableRow(this);
                boatRow.addView(empty);
                boatRow.addView(color);
                boatRow.addView(pos);
                boatRow.addView(name);
                boatRow.addView(sog);
                boatRow.addView(dtf);

                tableLayout.addView(boatRow);
            }
        }

        dialog.setView(tableLayout);

        dialog.setButton(this.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

//        setContentView(tableLayout);

        dialog.show();
    }
}
