package com.example.yos.mycluster.Cluster.Utils;

import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.fromLatitude;

public class SphereMercatorTransform implements CoordinateTransform {
    @Override
    public double x(double x) {
        return x + 180;
    }
    @Override
    public double y(double y) {
        return -fromLatitude(y) + 180;
    }
}
