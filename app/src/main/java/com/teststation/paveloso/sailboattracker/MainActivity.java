package com.teststation.paveloso.sailboattracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teststation.paveloso.sailboattracker.Utils.ConnectionCheckerAsync;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView connResultText = (TextView) findViewById(R.id.conn_res);

        ConnectionCheckerAsync ccAsync = new ConnectionCheckerAsync(connResultText);
        ccAsync.execute();
    }

    public void openRaceMap(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }
}
