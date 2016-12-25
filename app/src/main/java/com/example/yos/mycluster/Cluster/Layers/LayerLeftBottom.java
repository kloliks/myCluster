package com.example.yos.mycluster.Cluster.Layers;

// -x, -y
class LayerLeftBottom extends LayerFrom2To16 {
    LayerLeftBottom(double middle_latitude, double middle_longitude, int deepness) {
        super(middle_latitude, middle_longitude, deepness);
    }
    @Override
    DirectionsValues getDirectionsValues() {
        return new DirectionsValues(high, low, low, high);
    }
    @Override
    LayerFrom2To16 createLayer(double latitude, double longitude) {
        return new LayerLeftBottom(-latitude, -longitude, deepness+1);
    }
}
