package com.example.yos.mycluster;

import java.util.ArrayList;

class Layer {
    private Layer[] layers;
    private Cluster cluster;
    private ArrayList<Marker> markers;

    void addMarker(Marker marker) {
        if (markers == null) {
            markers = new ArrayList<Marker>();
        }

        if (markers.size() > 10) {

        } else {
            markers.add(marker);
        }
    }

    void deleteMarker(Marker marker) {}

    ArrayList<Marker> markersList(int deepness) {
        ArrayList<Marker> r = new ArrayList<Marker>();

        if (markers == null && cluster == null)
            return r;

        if (markers != null)
            return markers;

        r.add(cluster);

        if (deepness == 0)
            return r;

        for (int i = 0; i < 4; ++i) {
            if (layers[i] != null) {
                r.addAll(markersList(deepness-1));
            }
        }

        return r;
    }
}
