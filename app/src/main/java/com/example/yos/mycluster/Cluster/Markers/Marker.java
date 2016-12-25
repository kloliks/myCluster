package com.example.yos.mycluster.Cluster.Markers;

import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;

public class Marker {
    public String title;
    public double latitude;
    public double longitude;

    public Marker(String title, double latitude, double longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return title;
    }
}
