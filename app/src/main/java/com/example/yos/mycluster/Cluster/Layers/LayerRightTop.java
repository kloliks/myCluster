package com.example.yos.mycluster.Cluster.Layers;

// +x, +y
class LayerRightTop extends LayerFrom2To16 {
    LayerRightTop(double middle_latitude, double middle_longitude, int deepness) {
        super(middle_latitude, middle_longitude, deepness);
    }
    @Override
    DirectionsValues getDirectionsValues() {
        return new DirectionsValues(low, high, high, low);
    }
    @Override
    LayerFrom2To16 createLayer(double latitude, double longitude) {
        return new LayerLeftTop(latitude, -longitude, deepness + 1);
    }
}
