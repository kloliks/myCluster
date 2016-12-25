package com.example.yos.mycluster.Cluster.Layers;

abstract class LayerFrom2To16 extends LayerFrom1To16 {
    int deepness;

    LayerFrom2To16(double middle_latitude, double middle_longitude, int deepness) {
        super();
        this.middle_latitude = middle_latitude;
        this.middle_longitude = middle_longitude;
        this.deepness = deepness;
    }

    final int low = 0;
    final int high = 1;
    class DirectionsValues {
        int left, right, top, bottom;
        DirectionsValues(int left, int right, int top, int bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }
    }

    @Override
    Layer createLayer(int x, int y) {
        if (deepness == 16) {
            return new Layer17();
        }
        double[] mid_lat = middlesLatitude(deepness);
        double[] mid_lon = middlesLongitude(deepness);

        DirectionsValues dv = getDirectionsValues();
        switch (LayerLeaf.values()[(x << 1) + y]) {
            case LeftTop:     return createLayer(mid_lat[dv.left],  mid_lon[dv.top]);
            case LeftBottom:  return createLayer(mid_lat[dv.left],  mid_lon[dv.bottom]);
            case RightTop:    return createLayer(mid_lat[dv.right], mid_lon[dv.top]);
            case RightBottom: return createLayer(mid_lat[dv.right], mid_lon[dv.bottom]);
        }
        throw new RuntimeException("Illegal layer leaf numbers: "+ x +", "+ y);
    }
    abstract DirectionsValues getDirectionsValues();
    abstract LayerFrom2To16 createLayer(double latitude, double longitude);
}
