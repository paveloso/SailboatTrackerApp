package com.teststation.paveloso.sailboattracker.Entity;

import com.teststation.paveloso.sailboattracker.R;

import java.util.Date;

public class Sailboat {

    private String id; //CV number
    private String name;
    private int position; //current rank
    private int cog;
    private String sog; //kn
    private double dtf; //nm
    private float latitude;
    private float longitude;
    private boolean joker;
    private boolean stealth;
    private String lastReport;

    private int resourceColor;

    public int getResourceColor() {
        return resourceColor;
    }

    public void setResourceColor(int resourceColor) {
        this.resourceColor = resourceColor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCog() {
        return cog;
    }

    public void setCog(int cog) {
        this.cog = cog;
    }

    public String getSog() {
        return sog;
    }

    public void setSog(String sog) {
        this.sog = sog;
    }

    public double getDtf() {
        return dtf;
    }

    public void setDtf(double dtf) {
        this.dtf = dtf;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public boolean isJoker() {
        return joker;
    }

    public void setJoker(boolean joker) {
        this.joker = joker;
    }

    public boolean isStealth() {
        return stealth;
    }

    public void setStealth(boolean stealth) {
        this.stealth = stealth;
    }

    public String getLastReport() {
        return lastReport;
    }

    public void setLastReport(String lastReport) {
        this.lastReport = lastReport;
    }
}
