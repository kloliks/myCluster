package com.example.yos.mycluster.Cluster.Utils;

public class SphereMercatorProjection {
    public static final double MIN_LONGITUDE = -180;
    public static final double MAX_LONGITUDE = 180;

    public static final double MIN_LATITUDE = -85.0511287798;
    public static final double MAX_LATITUDE = 85.0511287798;

    public static double fromLatitude(double latitude) {
        double radians = Math.toRadians(latitude + 90) / 2;
        return Math.toDegrees(Math.log(Math.tan(radians)));
    }
    public static double toLatitude(double mercator) {
        double radians = Math.atan(Math.exp(Math.toRadians(mercator)));
        return Math.toDegrees(2 * radians) - 90;
    }
}
