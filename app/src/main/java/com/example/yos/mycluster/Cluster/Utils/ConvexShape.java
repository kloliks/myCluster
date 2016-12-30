package com.example.yos.mycluster.Cluster.Utils;

import java.util.ArrayList;

public class ConvexShape {
    public ArrayList<PointD> vertices;
    ConvexShape() {
        vertices = new ArrayList<>();
    }
    ConvexShape addVertex(PointD vertex) {
        if (vertices.isEmpty()) {
            vertices.add(vertex);
        }
        else {
            PointD first = vertices.get(0);
            PointD last = vertices.get(vertices.size() - 1);
            if (vertex != last && vertex != first)
                vertices.add(vertex);
        }
        return this;
    }
    protected void transform(CoordinateTransform transform, ConvexShape shape) {
        for (PointD vertex: vertices) {
            shape.vertices.add(new PointD(transform.x(vertex.x), transform.y(vertex.y)));
        }
    }
    public ConvexShape transform(CoordinateTransform transform) {
        ConvexShape result = new ConvexShape();
        transform(transform, result);
        return result;
    }
}
