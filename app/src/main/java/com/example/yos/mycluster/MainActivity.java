package com.example.yos.mycluster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yos.mycluster.Cluster.CoordTileProvider;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TileProvider coordTileProvider = new CoordTileProvider(getResources());

        myMapFragment map_fragment =
                (myMapFragment) getFragmentManager().findFragmentById(R.id.my_map_fragment);

//        map_fragment.setMapStyle(
//                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_json));

        map_fragment.addTileOverlay(new TileOverlayOptions()
                .fadeIn(false)
                .zIndex(Float.MAX_VALUE)
                .tileProvider(coordTileProvider));
    }
}
