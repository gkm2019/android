package com.example.kkobugi.DTO;

import android.content.ContentValues;
import android.content.Context;

public class Location {
    private String locname;
    private String loclat;
    private String loclon;
    private Context context;

    public String getLocname() {
        return locname;
    }

    public void setLocname(String locname) {
        this.locname = locname;
    }

    public String getLoclat() {
        return loclat;
    }

    public void setLoclat(String loclat) {
        this.loclat = loclat;
    }

    public String getLoclon() {
        return loclon;
    }

    public void setLoclon(String loclon) {
        this.loclon = loclon;
    }
}
