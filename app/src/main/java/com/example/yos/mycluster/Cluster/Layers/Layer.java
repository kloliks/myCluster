package com.example.yos.mycluster.Cluster.Layers;

import com.example.yos.mycluster.Cluster.Markers.Cluster;
import com.example.yos.mycluster.Cluster.Markers.Marker;

import java.util.ArrayList;

public abstract class Layer {
    public enum LayersLeaf { LeftTop, LeftBottom, RightTop, RightBottom }

    protected Layer[][] layers;
    protected Cluster cluster;
    protected ArrayList<Marker> markers;
    protected boolean clustering;
    protected double middle_latitude;
    protected double middle_longitude;


    public void disableClustering() {
        clustering = false;
    }
    public void enableClustering() {
        clustering = true;
        reCluster();
    }
    public void reCluster() {
        if (!clustering)
            return;
    }

    public abstract void addMarker(Marker marker);
    public abstract void deleteMarker(Marker marker);

    public ArrayList<Marker> markersList(int deepness) {
        ArrayList<Marker> r = new ArrayList<>();

        if (markers == null && cluster == null)
            return r;

        if (markers != null)
            return markers;

        r.add(cluster);

        if (deepness == 0)
            return r;

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (layers[i][j] != null) {
                    r.addAll(markersList(deepness - 1));
                }
            }
        }

        return r;
    }
}
