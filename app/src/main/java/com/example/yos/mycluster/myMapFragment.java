package com.example.yos.mycluster;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;


public class myMapFragment extends Fragment implements OnMapReadyCallback {
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
            mMap.addTileOverlay(mTileOverlayOptions);
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
        setMapStyle();
        addTileOverlay();
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
