package com.example.yos.mycluster.Cluster.Utils;

import java.util.ArrayList;

public class ConvexShape {
    ArrayList<PointD> vertices;
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
}
