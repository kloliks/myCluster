package com.example.yos.mycluster.Cluster.Layers;

import com.example.yos.mycluster.Cluster.Markers.ClusterMarker;
import com.example.yos.mycluster.Cluster.Markers.Marker;

import java.util.ArrayList;

abstract class LayerFrom1To16 extends Layer {
    private static double[][] middles_latitude;
    private static double[][] middles_longitude;

    enum LayerLeaf { LeftTop, LeftBottom, RightTop, RightBottom }

    private Layer[][] layers;
    private boolean clustering;
    private ClusterMarker clusterMarker;

    double middle_latitude;
    double middle_longitude;


    LayerFrom1To16() {
        super();
        calculateMiddles();
    }


    @Override
    public void disableClustering() {
        clustering = false;
    }
    @Override
    public void enableClustering() {
        clustering = true;
        reCluster();
    }
    @Override
    public void reCluster() {
        if (!clustering) {
            return;
        }
        if (clusterMarker != null && clusterMarker.populate < 8) {
            markers = new ArrayList<>();
            for (int i = 0; i < 2; ++i) {
                if (layers[i] != null) {
                    for (int j = 0; j < 2; ++j) {
                        if (layers[i][j] != null) {
                            layers[i][j].reCluster();
                            markers.addAll(layers[i][j].markers);
                            layers[i][j] = null;
                        }
                    }
                    layers[i] = null;
                }
            }
            clusterMarker = null;
        }
        if (markers != null && markers.size() > 10) {
            Marker marker = markers.get(0);
            clusterMarker = new ClusterMarker(marker.latitude, marker.longitude, 1);

            disableClustering();
            for (int i = 1; i < markers.size(); ++i) {
                addMarker(markers.get(i));
            }
            enableClustering();

            markers = null;
        }
    }


    private Layer layerLeaf(int x, int y) {
        if (layers[x] == null)
            layers[x] = new Layer[2];

        if (layers[x][y] != null)
            return layers[x][y];

        return layers[x][y] = createLayer(x, y);
    }
    abstract Layer createLayer(int x, int y);

    @Override
    public void addMarker(Marker marker) {
        if (clusterMarker != null) {
            int x = (marker.longitude < middle_longitude) ? 0 : 1;
            int y = (marker.latitude < middle_latitude) ? 0 : 1;

            layerLeaf(x, y).addMarker(marker);
            clusterMarker.addMarker(marker);
        }
        else {
            markers.add(marker);
        }
        reCluster();
    }
    @Override
    public void deleteMarker(Marker marker) {}


    @Override
    void markersList(ArrayList<Marker> markers_list, int deepness) {
        if (markers == null && clusterMarker == null) {
            return;
        }
        if (markers != null) {
            markers_list.addAll(markers);
            return;
        }

        markers_list.add(clusterMarker);

        if (deepness == 0) {
            return;
        }
        for (int i = 0; i < 2; ++i) {
            if (layers[i] != null) {
                for (int j = 0; j < 2; ++j) {
                    if (layers[i][j] != null) {
                        layers[i][j].markersList(markers_list, deepness - 1);
                    }
                }
            }
        }
    }


    private static double fromLatitude(double latitude) {
        double radians = Math.toRadians(latitude + 90) / 2;
        return Math.toDegrees(Math.log(Math.tan(radians)));
    }
    private static double toLatitude(double mercator) {
        double radians = Math.atan(Math.exp(Math.toRadians(mercator)));
        return Math.toDegrees(2 * radians) - 90;
    }
    private static void calculateMiddles() {
        if (middles_latitude != null && middles_longitude != null)
            return;

        middles_latitude = new double[16][2];
        middles_longitude = new double[16][2];

        middles_latitude[0][0] = 85.0511287798;
        middles_latitude[0][1] = 85.0511287798;
        middles_longitude[0][0] = 180.0;
        middles_longitude[0][1] = 180.0;

        double mercator = fromLatitude(middles_latitude[0][0]);
        for (int i = 1; i < 16; ++i) {
            double mercator_i = mercator / 2;

            middles_latitude[i][0] = toLatitude(mercator_i);
            middles_latitude[i][1] = toLatitude(mercator + mercator_i);
            middles_longitude[i][0] = middles_longitude[i-1][0] / 2;
            middles_longitude[i][1] = middles_longitude[i-1][0] + middles_longitude[i][0];

            mercator = mercator_i;
        }
    }
    static double[] middlesLatitude(int level) {
        if (middles_latitude != null)
            return middles_latitude[level];

        calculateMiddles();
        return middles_latitude[level];
    }
    static double[] middlesLongitude(int level) {
        if (middles_longitude != null)
            return  middles_longitude[level];

        calculateMiddles();
        return middles_latitude[level];
    }
}
