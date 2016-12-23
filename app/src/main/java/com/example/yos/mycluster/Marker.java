package com.example.yos.mycluster;

import android.graphics.Point;

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

    private static double fromLatitude(double latitude) {
        double radians = Math.toRadians(latitude + 90) / 2;
        return Math.toDegrees(Math.log(Math.tan(radians)));
    }
    Point getTileCoordinates(int zoom) {
        int dimension = 1 << zoom;
        double longitudeSpan = 360.0 / dimension;
        double mercator = fromLatitude(coordinates.latitude);
        int x = (int) (coordinates.longitude / longitudeSpan) + 180;
        int y = (int) (mercator / 360 * dimension) + 180;
        return new Point(x, y);
    }

    @Override
    public String toString() {
        return title;
    }
}
