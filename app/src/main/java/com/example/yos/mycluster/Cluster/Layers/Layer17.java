package com.example.yos.mycluster.Cluster.Layers;

import com.example.yos.mycluster.Cluster.Markers.Marker;

import java.util.ArrayList;

class Layer17 extends Layer {

    @Override public void disableClustering() {}
    @Override public void enableClustering() {}
    @Override public void reCluster() {}

    @Override
    public void addMarker(Marker marker) {
        markers.add(marker);
    }
    @Override
    public void deleteMarker(Marker marker) {
    }

    @Override
    void markersList(ArrayList<Marker> markers_list, int deepness) {
        markers_list.addAll(markers);
    }
}
