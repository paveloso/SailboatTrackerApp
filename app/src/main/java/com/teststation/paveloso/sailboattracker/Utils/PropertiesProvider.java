package com.teststation.paveloso.sailboattracker.Utils;

import android.content.Context;

import com.teststation.paveloso.sailboattracker.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesProvider {

    private static Properties properties;

    static {
        try {
            InputStream is = MainActivity.getAppContext().getAssets().open("config.properties");
            Properties props = new Properties();
            props.load(is);
            properties = props;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProperties() {
        return properties;
    }
}
