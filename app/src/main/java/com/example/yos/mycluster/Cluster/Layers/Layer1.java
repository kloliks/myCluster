package com.example.yos.mycluster.Cluster.Layers;

import com.example.yos.mycluster.Cluster.Markers.Marker;

import java.util.ArrayList;

public class Layer1 extends LayerFrom1To16 {
    public Layer1() {
        super();
        middle_longitude = 0;
        middle_latitude = 0;
    }
    @Override
    Layer createLayer(int x, int y) {
        double[] mid_lat = middlesLatitude(1);
        double[] mid_lon = middlesLongitude(1);

        switch (LayerLeaf.values()[(x << 1) + y]) {
            case LeftTop:     return new LayerLeftTop     (-mid_lat[0],  mid_lon[0], 2);
            case LeftBottom:  return new LayerLeftBottom  (-mid_lat[0], -mid_lon[0], 2);
            case RightTop:    return new LayerRightTop    ( mid_lat[0],  mid_lon[0], 2);
            case RightBottom: return new LayerRightBottom ( mid_lat[0], -mid_lon[0], 2);
        }
        throw new RuntimeException("Illegal layer leaf numbers: "+ x +", "+ y);
    }
    public ArrayList<Marker> markersList(int deepness) {
        ArrayList<Marker> markers_list = new ArrayList<>();

        markersList(markers_list, deepness);
        return markers_list;
    }
}
