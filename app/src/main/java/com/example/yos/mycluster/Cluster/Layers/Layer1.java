package com.example.yos.mycluster.Cluster.Layers;

import com.example.yos.mycluster.Cluster.Markers.Marker;

import java.util.ArrayList;

public class Layer1 extends Layer {
    Layer1() {
        middle_longitude = 0;
        middle_latitude = 0;
    }

    private Layer layerLeaf(int x, int y) {
        if (layers[x] == null)
            layers[x] = new Layer[2];

        if (layers[x][y] != null)
            return layers[x][y];

        switch (LayersLeaf.values()[(x << 1) + y]) {
            case LeftTop:       return new LayerLT();
            case LeftBottom:    return new LayerLB();
            case RightTop:      return new LayerRT();
            case RightBottom:   return new LayerRB();
            default:
                throw new RuntimeException("Illegal layer leaf numbers: "+ x +", "+ y);
        }
    }

    @Override
    public void addMarker(Marker marker) {
        if (cluster != null) {
            int x = (marker.coordinates.longitude < middle_longitude) ? 0 : 1;
            int y = (marker.coordinates.latitude < middle_latitude) ? 0 : 1;


            layerLeaf(x, y).addMarker(marker);
            ++cluster.populate;

        } else if (markers == null) {
            markers = new ArrayList<Marker>();

        }

        if (markers.size() > 10) {

        } else {
            markers.add(marker);
        }

        reCluster();
    }

    @Override
    public void deleteMarker(Marker marker) {}
}
