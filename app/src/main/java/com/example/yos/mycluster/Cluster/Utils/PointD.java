package com.example.yos.mycluster.Cluster.Utils;

public class PointD {
    public double x, y;
    PointD(double x, double y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "[x: "+ x +"; y: "+ y +"]";
    }
}
