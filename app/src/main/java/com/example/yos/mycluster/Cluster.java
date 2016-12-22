package com.example.yos.mycluster;

class Cluster extends Marker {
    int populate;

    Cluster(String lat, String lon, int populate) {
        super("", 0, 0);
        this.populate = populate;
    }
}
