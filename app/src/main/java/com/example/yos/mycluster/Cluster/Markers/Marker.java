package com.example.yos.mycluster.Cluster.Markers;

import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;

public class Marker {
    public String title;
    public LatLng coordinates;

    public Marker(String title, double lat, double lon) {
        this.title = title;
        coordinates = new LatLng(lat, lon);
    }
    public Marker(String title, LatLng coordinates) {
        this.title = title;
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return title;
    }
}
