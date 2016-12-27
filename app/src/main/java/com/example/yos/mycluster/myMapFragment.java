package com.example.yos.mycluster;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;


public class myMapFragment extends Fragment implements
      OnMapReadyCallback
    , GoogleMap.OnCameraIdleListener
    , GoogleMap.OnCameraMoveStartedListener
    , GoogleMap.OnCameraMoveCanceledListener
{
    private static double fromLatitude(double latitude) {
        double radians = Math.toRadians(latitude + 90) / 2;
        return Math.toDegrees(Math.log(Math.tan(radians)));
    }
    private static double toLatitude(double mercator) {
        double radians = Math.atan(Math.exp(Math.toRadians(mercator)));
        return Math.toDegrees(2 * radians) - 90;
    }
    class tileProvider implements TileProvider {
        @Override
        public Tile getTile(int x, int y, int zoom) {
            Log.e("myTILE", "x: "+ x +", y: "+ y +", zoom: "+ zoom);
            return NO_TILE;
        }
    }
    Polygon polygon;
    class PointD {
        double x, y;
        PointD(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    class Scene {
        double min_x, max_x;
        double min_y, max_y;
        Scene(double min_x, double max_x, double min_y, double max_y) {
            this.min_x = min_x;
            this.max_x = max_x;
            this.min_y = min_y;
            this.max_y = max_y;
        }
    }
    class ConvexShape {
        ArrayList<PointD> vertices;
        ConvexShape() {
            vertices = new ArrayList<>();
        }
    }
    class ConvexQuadrilateral extends ConvexShape {
        ConvexQuadrilateral(VisibleRegion visibleRegion) {
            vertices = new ArrayList<>();
            vertices.add(new PointD(
                    visibleRegion.nearLeft.longitude,
                    fromLatitude(visibleRegion.nearLeft.latitude)
            ));
            vertices.add(new PointD(
                    visibleRegion.nearRight.longitude,
                    fromLatitude(visibleRegion.nearRight.latitude)
            ));
            vertices.add(new PointD(
                    visibleRegion.farRight.longitude,
                    fromLatitude(visibleRegion.farRight.latitude)
            ));
            vertices.add(new PointD(
                    visibleRegion.farLeft.longitude,
                    fromLatitude(visibleRegion.farLeft.latitude)
            ));
        }
        PointD calculate_point(double y, double x0, double x1) {
            return new PointD(x0 * (1 - y) + x1 * y, y);
        }
        ConvexShape clipping(Scene scene) {
            ConvexShape shape = new ConvexShape();
            PointD current = vertices.get(vertices.size()-1);
            for (PointD next: vertices) {
                if (current.y < scene.min_y) {
                   if (next.y > scene.min_y) {
                       shape.vertices.add(calculate_point(scene.min_y, current.x, next.x));
                   }
                } else if (current.y > scene.max_y) {
                    if (next.y < scene.max_y) {
                        shape.vertices.add(calculate_point(scene.max_y, current.x, next.x));
                    }
                } else {
                    shape.vertices.add(current);
                }
                current = next;
            }
            return shape;
        }
    }
    @Override
    public void onCameraIdle() {
//        Log.e("TADA", "onCameraIdle");
//        TileProvider tileProvider = new tileProvider();
//        TileOverlay to = mMap.addTileOverlay(new TileOverlayOptions()
//                .tileProvider(tileProvider)
//        );
//        to.remove();
        CameraPosition cp = mMap.getCameraPosition();
        Log.e("CameraPosition", cp.toString());
        int zoom = (int) cp.zoom;

//        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
        VisibleRegion visibleRegion = new VisibleRegion(
                new LatLng(-90, 0),
                new LatLng(-90, 100),
                new LatLng(-70, 0),
                new LatLng(-70, 100),
                null
        );
        Log.e("Visible region", visibleRegion.toString());

        final double MIN_LATITUDE = fromLatitude(-85.0511);
        final double MAX_LATITUDE = fromLatitude( 85.0511);

        final double MIN_LONGITUDE = -180;
        final double MAX_LONGITUDE =  180;

        Scene scene = new Scene(MIN_LONGITUDE, MAX_LONGITUDE-1, MIN_LATITUDE, MAX_LATITUDE-1);
        ConvexQuadrilateral quad = new ConvexQuadrilateral(visibleRegion);
        ConvexShape shape = quad.clipping(scene);
        ArrayList<LatLng> visibleShape = new ArrayList<>();
        for (PointD vertex : shape.vertices) {
            visibleShape.add(new LatLng(toLatitude(vertex.y), vertex.x));
        }
        Log.e("Visible shape", visibleShape.toString());

        double l_lat = MAX_LATITUDE - MIN_LATITUDE;
        double l_lon = MAX_LONGITUDE - MIN_LONGITUDE;

        double lat_normal = l_lat / (1 << zoom);
        double lon_normal = l_lon / (1 << zoom);
        Log.e("Lat normal", ""+ lat_normal);
        Log.e("Lon normal", ""+ lon_normal);

        if (polygon != null) {
            polygon.remove();
        }
        polygon = mMap.addPolygon(new PolygonOptions()
                .addAll(visibleShape)
//                .add(
//                        new LatLng(visibleRegion.nearLeft.latitude,  visibleRegion.nearLeft.longitude),
//                        new LatLng(visibleRegion.nearRight.latitude, visibleRegion.nearRight.longitude),
//                        new LatLng(visibleRegion.farRight.latitude,  visibleRegion.farRight.longitude),
//                        new LatLng(visibleRegion.farLeft.latitude,   visibleRegion.farLeft.longitude)
//                )
                .strokeColor(Color.RED)
                .strokeWidth(17)
        );
//        polygon = mMap.addPolygon(new PolygonOptions()
//                .add(
//                        new LatLng(toLatitude(MAX_LATITUDE-1*lat_normal), MAX_LONGITUDE-1*lon_normal),
//                        new LatLng(toLatitude(MAX_LATITUDE-2*lat_normal), MAX_LONGITUDE-1*lon_normal),
//                        new LatLng(toLatitude(MAX_LATITUDE-2*lat_normal), MAX_LONGITUDE-2*lon_normal),
//                        new LatLng(toLatitude(MAX_LATITUDE-1*lat_normal), MAX_LONGITUDE-2*lon_normal)
//                )
//                .strokeColor(Color.RED)
//                .strokeWidth(17)
//                .fillColor(Color.YELLOW)
//        );
    }
    @Override
    public void onCameraMoveStarted(int var1) {
        Log.e("TADA", "onCameraMoveStarted");
    }
    @Override
    public void onCameraMoveCanceled() {
        Log.e("TADA", "onCameraMoveCanceled");
    }
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private GoogleMap mMap;
    private MapView mMapView;
    private TileOverlayOptions mTileOverlayOptions;
    private MapStyleOptions mMapStyleOptions;

    @Override
    public View onCreateView( LayoutInflater inflater
                            , ViewGroup container
                            , Bundle savedInstanceState
                            )
    {
        View v = inflater.inflate(R.layout.map_layout, container, false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    private void addTileOverlay() {
        if (mMap != null && mTileOverlayOptions != null) {
//            ((CoordTileProvider) mTileOverlayOptions.getTileProvider()).setMap(mMap);
            mMap.addTileOverlay(mTileOverlayOptions);
//            ((CoordTileProvider) mTileOverlayOptions.getTileProvider()).addMarkers(3);
//            ((CoordTileProvider) mTileOverlayOptions.getTileProvider()).addMarkers(3);
//            ((CoordTileProvider) mTileOverlayOptions.getTileProvider()).addMarkers(3);
        }
    }

    private void setMapStyle() {
        if (mMap != null && mMapStyleOptions != null) {
            mMap.setMapStyle(mMapStyleOptions);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        setMapStyle();
        addTileOverlay();

        double[][] latitudes = new double[17][2];
        latitudes[0][0] = 85.0511287798;
        latitudes[0][1] = 85.0511287798;
        latitudes[1][0] = 66.5132604431;
        latitudes[1][1] = 66.5132604431;
        latitudes[2][0] = 40.9798980696;
        latitudes[2][1] = 79.1713346408;
        latitudes[3][0] = 21.9430455334;
        latitudes[3][1] = 55.7765730187;
        latitudes[4][0] = 11.1784018737;
        latitudes[4][1] = 31.9521622380;
        latitudes[5][0] = 5.6159858192;
        latitudes[5][1] = 16.6361918784;
        latitudes[6][0] = 2.8113711933;
        latitudes[6][1] = 8.4071681636;
        latitudes[7][0] = 1.4061088354;
        latitudes[7][1] = 4.2149431414;
        latitudes[8][0] = 0.7031073524;
        latitudes[8][1] = 2.1088986592;
        latitudes[9][0] = 0.3515602940;
        latitudes[9][1] = 1.0546279423;
        latitudes[10][0] = 0.1757809742;
        latitudes[10][1] = 0.5273363048;
        latitudes[11][0] = 0.0878905905;
        latitudes[11][1] = 0.2636709443;
        latitudes[12][0] = 0.0439453082;
        latitudes[12][1] = 0.1318358212;
        latitudes[13][0] = 0.0219726557;
        latitudes[13][1] = 0.0659179542;
        latitudes[14][0] = 0.0109863281;
        latitudes[14][1] = 0.0329589826;
        latitudes[15][0] = 0.0054931641;
        latitudes[15][1] = 0.0164794920;
        latitudes[16][0] = 0.0027465820;
        latitudes[16][1] = 0.0082397461;


        for (int i = 0; i < 2; ++i) {
            map.addPolyline((new PolylineOptions())
                .add(new LatLng(0, 45+i), new LatLng(latitudes[i][0], 45+i))
                .width(17)
                .color(Color.CYAN)
            );
        }
        for (int i = 2; i < 17; ++i) {
            map.addPolyline((new PolylineOptions())
                .add(new LatLng(-latitudes[i][1], 45+i), new LatLng(latitudes[i][1], 45+i))
                .width(17)
                .color(Color.MAGENTA)
            );
            map.addPolyline((new PolylineOptions())
                .add(new LatLng(-latitudes[i][0], 45+i), new LatLng(latitudes[i][0], 45+i))
                .width(17)
                .color(Color.CYAN)
            );
        }
        map.addPolyline((new PolylineOptions())
            .add(new LatLng(0, 180), new LatLng(85.0511287798, -180))
            .width(17)
            .color(Color.BLUE)
        );
        map.addPolyline((new PolylineOptions())
            .add(new LatLng(0, 90), new LatLng(66.5132604431, 90))
            .width(17)
            .color(Color.GREEN)
        );
        map.addPolyline((new PolylineOptions())
            .add(new LatLng(0, -90), new LatLng(66.5132604431, -90))
            .width(17)
            .color(Color.RED)
        );
        map.addPolyline((new PolylineOptions())
            .add(new LatLng(0, 0), new LatLng(85.0511287798, 0))
            .width(17)
            .color(Color.YELLOW)
        );

//        Marker marker = new Marker("bla", 35, 35);
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(marker.latitude, marker.longitude))
//                .title(marker.title)
//                .icon(BitmapDescriptorFactory.fromBitmap(mOnce))
//        );
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(marker.latitude, marker.longitude))
//                .title(marker.title)
//                .icon(BitmapDescriptorFactory.fromBitmap(mOnce))
//        );
//        com.google.android.gms.maps.model.Marker m = mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(marker.latitude, marker.longitude))
//                .title(marker.title)
//                .icon(BitmapDescriptorFactory.fromBitmap(mOnce))
//        );
//        m.setVisible(false);
//        Drawable shape = ResourcesCompat.getDrawable(getResources(), R.drawable.arc, null);
//        int w = shape.getIntrinsicWidth();
//        int h = shape.getIntrinsicHeight();
//        Bitmap mImage = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(mImage);
//        shape.setBounds(0, 0, w, h);
//        shape.draw(canvas);
//
//        LatLng NEWARC = new LatLng(40.714086, -74.228697);
//        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
//                .image(BitmapDescriptorFactory.fromBitmap(mImage))
//                .position(NEWARC, 8600f, 6500f);
//        GroundOverlay imageOverlay = map.addGroundOverlay(newarkMap);
    }

    void setMapStyle(MapStyleOptions mapStyleOptions) {
        mMapStyleOptions = mapStyleOptions;
        setMapStyle();
    }
    void addTileOverlay(TileOverlayOptions tileOverlayOptions) {
        mTileOverlayOptions = tileOverlayOptions;
        addTileOverlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
