package com.example.yos.mycluster.Cluster;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.example.yos.mycluster.Cluster.Layers.Layer1;
import com.example.yos.mycluster.Cluster.Markers.Marker;
import com.example.yos.mycluster.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class CoordTileProvider implements TileProvider {

    private static final int TILE_SIZE_DP = 256;

    private final float mScaleFactor;

    private final Bitmap mOver50000;
    private final Bitmap mOver10000;
    private final Bitmap mOver5000;
    private final Bitmap mOver1000;
    private final Bitmap mOver500;
    private final Bitmap mOver100;
    private final Bitmap mOver50;
    private final Bitmap mOver10;
    private final Bitmap mOnce;

    private final Bitmap mBorderTile;

    private Layer1 layer;

//    private ArrayList<com.google.android.gms.maps.model.Marker> mMarkers;

    private Bitmap prepareArc(
            Resources resources, int scale, String level, int strokeColor, int fillColor)
    {
        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(ResourcesCompat.getColor(resources, fillColor, null));

        Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(mScaleFactor * 2);
        strokePaint.setColor(ResourcesCompat.getColor(resources, strokeColor, null));

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize((19 - scale) * mScaleFactor);

        Bitmap bitmap = Bitmap.createBitmap(
                (int) ((TILE_SIZE_DP / scale) * mScaleFactor),
                (int) ((TILE_SIZE_DP / scale) * mScaleFactor),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmap);

        final float hsw = strokePaint.getStrokeWidth() / 2;
        RectF rectF = new RectF(hsw, hsw, bitmap.getWidth() - hsw, bitmap.getHeight() - hsw);

        canvas.drawArc(rectF, 0, 360, false, fillPaint);
        canvas.drawArc(rectF, 0, 360, false, strokePaint);

        canvas.drawText(
                level,
                bitmap.getWidth() / 2,
                (bitmap.getHeight() - textPaint.descent() - textPaint.ascent()) / 2,
                textPaint
        );

        return bitmap;
    }
    public CoordTileProvider(Resources resources) {
        /* Scale factor based on density, with a 0.6 multiplier to increase tile generation
         * speed */
        mScaleFactor = resources.getDisplayMetrics().density;// * 0.6f;

        mOver50000 = prepareArc(resources,  5, "50000+", R.color.Over50000_b, R.color.Over50000);
        mOver10000 = prepareArc(resources,  6, "10000+", R.color.Over10000_b, R.color.Over10000);
        mOver5000  = prepareArc(resources,  7,  "5000+", R.color.Over5000_b,  R.color.Over5000);
        mOver1000  = prepareArc(resources,  8,  "1000+", R.color.Over1000_b,  R.color.Over1000);
        mOver500   = prepareArc(resources,  9,   "500+", R.color.Over500_b,   R.color.Over500);
        mOver100   = prepareArc(resources, 10,   "100+", R.color.Over100_b,   R.color.Over100);
        mOver50    = prepareArc(resources, 11,    "50+", R.color.Over50_b,    R.color.Over50);
        mOver10    = prepareArc(resources, 12,    "10+", R.color.Over10_b,    R.color.Over10);
        mOnce      = prepareArc(resources, 20,       "", R.color.Once_b,      R.color.Once);

        final float tile_size_scale = TILE_SIZE_DP * mScaleFactor;

        mBorderTile = Bitmap.createBitmap(
                (int) (tile_size_scale),
                (int) (tile_size_scale),
                Bitmap.Config.ARGB_8888
        );

        Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(mScaleFactor * 2);

        Canvas canvas = new Canvas();
        canvas.setBitmap(mBorderTile);
        canvas.drawRect(0, 0, tile_size_scale, tile_size_scale, strokePaint);


        layer = new Layer1();
        layer.addMarker(new Marker("dol", -35, -35));
        layer.addMarker(new Marker("bla", -40, -40));
        layer.addMarker(new Marker( "ta",  40, -40));
        layer.addMarker(new Marker( "na", -35,  35));

//        mMarkers = new ArrayList<>();
        ArrayList<Marker> markers = layer.markersList(0);
        Log.e("Markers: ", markers.toString());
    }

//    private GoogleMap mMap;
//    public void setMap(GoogleMap map) {
//        Log.e("setMap", "i'm here");
//        mMap = map;
//
//        ArrayList<Marker> markers = layer.markersList(0);
//        Log.e("Markers: ", markers.toString());
//        for (int i = 0; i < markers.size(); ++i) {
//            Marker marker = markers.get(i);
//            mMarkers.add(
//                    mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(marker.latitude, marker.longitude))
//                        .title(marker.title)
//                        .icon(BitmapDescriptorFactory.fromBitmap(mOnce))
//                )
//            );
//            mMarkers.get(mMarkers.size()-1).setVisible(false);
//        }
//    }

//    public void addMarkers(int zoom) {
//        if (zoom != old_zoom) {
//            mMap.clear();
//        }
//
//        old_zoom = zoom;
//        ArrayList<Marker> markers = layer.markersList(0);
//        Log.e("Markers: ", markers.toString());
//        for (int i = 0; i < markers.size(); ++i) {
//            Marker marker = markers.get(i);
//            mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(marker.latitude, marker.longitude))
//                    .title(marker.title)
//                    .icon(BitmapDescriptorFactory.fromBitmap(mOnce))
//            );
//        }
//    }
//
//    private int old_zoom;
    @Override
    public Tile getTile(int x, int y, int zoom) {
//        Log.e("TILE", "x: "+ x +", y: "+ y +", zoom: "+ zoom);

//        if (zoom == 4) {
//            for (int i = 0; i < mMarkers.size(); ++i) {
//                mMarkers.get(i).setVisible(true);
//            }
//        }
//        if (old_zoom == 4) {
//            for (int i = 0; i < mMarkers.size(); ++i) {
//                mMarkers.get(i).setVisible(false);
//            }
//        }
//        old_zoom = zoom;

        Bitmap coordTile = drawTileCoords(x, y, zoom);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        coordTile.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] bitmapData = stream.toByteArray();
        return new Tile(mBorderTile.getWidth(), mBorderTile.getHeight(), bitmapData);
    }

    private Bitmap drawTileCoords(int x, int y, int zoom) {
        // Synchronize copying the bitmap to avoid a race condition in some devices.
        Bitmap copy = null;
        synchronized (mBorderTile) {
            copy = mBorderTile.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
        }

        Canvas canvas = new Canvas(copy);
        String tileCoords = "(" + x + ", " + y + ")";
        String zoomLevel = "zoom = " + zoom;
        /* Paint is not thread safe. */
        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(22 * mScaleFactor);
        canvas.drawText(tileCoords, copy.getWidth() / 2, copy.getHeight() / 2, mTextPaint);
        canvas.drawText(zoomLevel, copy.getWidth() / 2, copy.getHeight() * 2 / 3, mTextPaint);

        return copy;
    }
}

