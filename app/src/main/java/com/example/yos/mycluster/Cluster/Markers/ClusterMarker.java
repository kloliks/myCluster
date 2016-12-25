package com.example.yos.mycluster.Cluster.Markers;

import com.example.yos.mycluster.Cluster.Markers.Marker;

public class ClusterMarker extends Marker {
    public int populate;

    public ClusterMarker(double lat, double lon, int populate) {
        super("", lat, lon);
        this.populate = populate;
    }

    private double helperAddCoordinate(double coordinate, double new_coordinate) {
        return coordinate + (new_coordinate - coordinate) / populate;
    }
    public void addMarker(Marker marker) {
        ++populate;
        latitude = helperAddCoordinate(latitude, marker.latitude);
        longitude = helperAddCoordinate(longitude, marker.longitude);
    }
}
