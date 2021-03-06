package com.example.yos.mycluster.Cluster.Utils;

import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.toLatitude;

public class MercatorSphereTransform implements CoordinateTransform {
    @Override
    public double x(double x) {
        return x - 180;
    }
    @Override
    public double y(double y) {
        return toLatitude(-y + 180);
    }
}
