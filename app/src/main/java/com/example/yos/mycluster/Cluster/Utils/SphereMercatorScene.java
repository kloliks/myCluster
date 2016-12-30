package com.example.yos.mycluster.Cluster.Utils;


import android.util.Log;

import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.MAX_LATITUDE;
import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.MAX_LONGITUDE;
import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.MIN_LATITUDE;
import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.MIN_LONGITUDE;
import static com.example.yos.mycluster.Cluster.Utils.SphereMercatorProjection.fromLatitude;

public class SphereMercatorScene extends Scene {
    public SphereMercatorScene() {
        super();
        this.min_x = MIN_LONGITUDE;
        this.max_x = MAX_LONGITUDE;
        this.min_y = fromLatitude(MIN_LATITUDE);
        this.max_y = fromLatitude(MAX_LATITUDE);
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
        Log.e("calculate_point", "new_y: "+ new_y +", a: "+ a + ", b:"+ b);
        double y = (new_y - a.y) / (b.y - a.y);
        Log.e("calculate_point", "y: "+ y);
        Log.e("calculate_point", "a.x * (1 - y): "+ (a.x * (1 - y)));
        Log.e("calculate_point", "b.x * y: "+ (b.x * y));
        return new PointD(a.x * (1 - y) + b.x * y, new_y);
    }
}
