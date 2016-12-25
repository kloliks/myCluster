package com.example.yos.mycluster.Cluster.Layers;

import com.example.yos.mycluster.Cluster.Markers.Marker;

import java.util.ArrayList;

abstract class Layer {
    ArrayList<Marker> markers;

    Layer() {
        markers = new ArrayList<Marker>();
    }

    public abstract void disableClustering();
    public abstract void enableClustering();
    public abstract void reCluster();

    public abstract void addMarker(Marker marker);
    public abstract void deleteMarker(Marker marker);

    abstract void markersList(ArrayList<Marker> markers_list, int deepness);
}
