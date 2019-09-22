package com.teststation.paveloso.sailboattracker.Utils;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;

import com.teststation.paveloso.sailboattracker.R;

public class ConnectionCheckerAsync extends AsyncTask<Void, Void, Boolean> {

    private TextView textView;

    public ConnectionCheckerAsync(TextView textView) {
        this.textView = textView;
    }

    ConnectonChecker cc = new ConnectonChecker();

    @Override
    protected Boolean doInBackground(Void... voids) {

        boolean result = cc.isInternetConnectionPossible();

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            textView.setText("Available");
            textView.setTextColor(Color.GREEN);
        } else {
            textView.setText("Not Available");
            textView.setTextColor(Color.RED);
        }
    }
}
