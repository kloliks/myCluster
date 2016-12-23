package com.example.yos.mycluster.Cluster.Markers;

import com.example.yos.mycluster.Cluster.Markers.Marker;

public class Cluster extends Marker {
    public int populate;

    public Cluster(double lat, double lon, int populate) {
        super("", lat, lon);
        this.populate = populate;
    }
}
