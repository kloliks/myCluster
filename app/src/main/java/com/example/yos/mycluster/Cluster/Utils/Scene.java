package com.example.yos.mycluster.Cluster.Utils;

public abstract class Scene {
    double min_x, max_x;
    double min_y, max_y;

    public abstract Edge clipping(Edge edge);
}
