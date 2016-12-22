package com.example.yos.mycluster;

import com.google.android.gms.maps.model.LatLng;

class Marker {
    String title;
    LatLng coordinates;

    Marker(String title, double lat, double lon) {
        this.title = title;
        coordinates = new LatLng(lat, lon);
    }
    Marker(String title, LatLng coordinates) {
        this.title = title;
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return title;
    }
}
