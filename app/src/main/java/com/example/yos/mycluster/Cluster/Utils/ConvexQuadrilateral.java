package com.example.yos.mycluster.Cluster.Utils;

import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;

public class ConvexQuadrilateral extends ConvexShape {
    private class DefaultTransform implements CoordinateTransform {
        @Override
        public double transformX(double x) {
            return x;
        }
        @Override
        public double transformY(double y) {
            return y;
        }
    }

    private CoordinateTransform coordinateTransform;
    public ConvexQuadrilateral() {
        coordinateTransform = new DefaultTransform();
        vertices = new ArrayList<>();
    }
    public void setTransform(CoordinateTransform coordinateTransform) {
        this.coordinateTransform = coordinateTransform;
    }
    public void setVisibleRegion(VisibleRegion visibleRegion) {
        vertices.clear();
        vertices.add(new PointD(
                coordinateTransform.transformX(visibleRegion.nearLeft.longitude),
                coordinateTransform.transformY(visibleRegion.nearLeft.latitude)
        ));
        vertices.add(new PointD(
                coordinateTransform.transformX(visibleRegion.nearRight.longitude),
                coordinateTransform.transformY(visibleRegion.nearRight.latitude)
        ));
        vertices.add(new PointD(
                coordinateTransform.transformX(visibleRegion.farRight.longitude),
                coordinateTransform.transformY(visibleRegion.farRight.latitude)
        ));
        vertices.add(new PointD(
                coordinateTransform.transformX(visibleRegion.farLeft.longitude),
                coordinateTransform.transformY(visibleRegion.farLeft.latitude)
        ));
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
