package com.teststation.paveloso.sailboattracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teststation.paveloso.sailboattracker.Utils.ConnectionCheckerAsync;
import com.teststation.paveloso.sailboattracker.Utils.PropertiesProvider;

public class MainActivity extends AppCompatActivity {

    private static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();
        setContentView(R.layout.activity_main);

        final TextView connResultText = (TextView) findViewById(R.id.conn_res);
        final Button showMapButton = (Button) findViewById(R.id.showmap_but);
        showMapButton.setEnabled(false);
        showMapButton.setBackgroundColor(getResources().getColor(R.color.disabled_button));

        ConnectionCheckerAsync ccAsync = new ConnectionCheckerAsync(connResultText);
        ccAsync.execute();

        connResultText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (connResultText.getText().toString().equals("Available")) {
                    showMapButton.setEnabled(true);
                    showMapButton.setBackgroundColor(getResources().getColor(R.color.app_buttons_red));
                } else {
                    showMapButton.setEnabled(false);
                    showMapButton.setBackgroundColor(getResources().getColor(R.color.disabled_button));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void openRaceMap(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void openAbout(View view) {
        AlertDialog aboutDialog = new AlertDialog.Builder(this).create();
        aboutDialog.setTitle("About");

        aboutDialog.setView(getLayoutInflater().inflate(R.layout.about_dialog, null));

        aboutDialog.setButton(this.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface aboutDialog, int which) {
                aboutDialog.dismiss();
            }
        });

        aboutDialog.show();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
