package com.example.yos.mycluster.Cluster.Utils;


public class SphereMercatorScene extends Scene {
    public static double fromLatitude(double latitude) {
        double radians = Math.toRadians(latitude + 90) / 2;
        return Math.toDegrees(Math.log(Math.tan(radians)));
    }
    public static double toLatitude(double mercator) {
        double radians = Math.atan(Math.exp(Math.toRadians(mercator)));
        return Math.toDegrees(2 * radians) - 90;
    }

    public SphereMercatorScene() {
        super();
        this.min_x = -180;
        this.max_x =  179;
        this.min_y = fromLatitude(-85.0511287798);
        this.max_y = fromLatitude( 85.0511287798) - 1;
    }
    @Override
    Edge clipping(Edge edge) {
        PointD a = clipping(edge.a, edge.b);
        if (a == null)
            return null;

        PointD b = clipping(edge.b, edge.a);
        if (a == edge.a && b == edge.b)
            return edge;

        return new Edge(a, b);
    }
    private PointD clipping(PointD a, PointD b) {
        if (a.y < min_y) {
            if (b.y < min_y)
                return null;
            return calculate_point(min_y, a, b);
        }
        if (a.y > max_y) {
            if (b.y > max_y)
                return null;
            return calculate_point(max_y, a, b);
        }
        return a;
    }
    private PointD calculate_point(double new_y, PointD a, PointD b) {
        double y = (new_y - a.y) / (b.y - a.y);
        return new PointD(a.x * (1 - y) + b.x * y, new_y);
    }

    private class SphereMercatorTransform implements CoordinateTransform {
        @Override
        public double transformX(double x) {
            return x;
        }
        @Override
        public double transformY(double y) {
            return fromLatitude(y);
        }
    }


    private static SphereMercatorTransform sphereMercatorTransform;

    @Override
    public CoordinateTransform getTransform() {
        if (sphereMercatorTransform != null)
            return sphereMercatorTransform;
        sphereMercatorTransform = new SphereMercatorTransform();
        return sphereMercatorTransform;
    }
}
