package com.example.yos.mycluster.Cluster.Utils;


import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.MAX_LATITUDE;
import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.MAX_LONGITUDE;
import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.MIN_LATITUDE;
import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.MIN_LONGITUDE;

public class SphereMercatorScene extends Scene {
    public SphereMercatorScene() {
        super();
        CoordinateTransform transform = new SphereMercatorTransform();
        this.min_x = transform.x(MIN_LONGITUDE); // 0
        this.max_x = transform.x(MAX_LONGITUDE); // 360
        this.min_y = transform.y(MAX_LATITUDE);  // ~ 0
        this.max_y = transform.y(MIN_LATITUDE);  // ~ 360
    }
    @Override
    public Edge clipping(Edge edge) {
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
}
