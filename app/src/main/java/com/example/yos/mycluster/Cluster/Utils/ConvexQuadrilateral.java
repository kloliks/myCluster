package com.example.yos.mycluster.Cluster.Utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;

public class ConvexQuadrilateral extends ConvexShape {
    public ConvexQuadrilateral() {
        vertices = new ArrayList<>();
    }
    public void setVisibleRegion(VisibleRegion region) {
        double west = region.latLngBounds.southwest.longitude;
        vertices.clear();
        add(region.nearLeft, west);
        add(region.nearRight, west);
        add(region.farRight, west);
        add(region.farLeft, west);
    }
    private void add(LatLng point, double west) {
        double x = (point.longitude < west) ? point.longitude + 360 : point.longitude;
        vertices.add(new PointD(x, point.latitude));
    }
    public ConvexQuadrilateral transform(CoordinateTransform transform) {
        ConvexQuadrilateral result = new ConvexQuadrilateral();
        transform(transform, result);
        return result;
    }
    public ConvexShape clipping(Scene scene) {
        ConvexShape shape = new ConvexShape();
        PointD current = vertices.get(vertices.size()-1);
        for (PointD next: vertices) {
            Edge edge = scene.clipping(new Edge(current, next));
            if (edge != null) {
                shape.addVertex(edge.a);
                shape.addVertex(edge.b);
            }
            current = next;
        }
        return shape;
    }
}
