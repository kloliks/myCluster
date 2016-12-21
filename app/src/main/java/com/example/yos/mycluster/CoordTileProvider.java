package com.example.yos.mycluster;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

class CoordTileProvider implements TileProvider {

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

    private final CityRow[][] layers;

    private static int getResId(String resName, Class c) {
        try {
            Field idField = c.getField(resName);
            return idField.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

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

    CoordTileProvider(Resources resources) {
        /* Scale factor based on density, with a 0.6 multiplier to increase tile generation
         * speed */
        mScaleFactor = resources.getDisplayMetrics().density;// * 0.6f;

        mOver50000 = prepareArc(resources,  1, "50000+", R.color.Over50000_b, R.color.Over50000);
        mOver10000 = prepareArc(resources,  2, "10000+", R.color.Over10000_b, R.color.Over10000);
        mOver5000  = prepareArc(resources,  3,  "5000+", R.color.Over5000_b,  R.color.Over5000);
        mOver1000  = prepareArc(resources,  4,  "1000+", R.color.Over1000_b,  R.color.Over1000);
        mOver500   = prepareArc(resources,  7,   "500+", R.color.Over500_b,   R.color.Over500);
        mOver100   = prepareArc(resources,  8,   "100+", R.color.Over100_b,   R.color.Over100);
        mOver50    = prepareArc(resources,  9,    "50+", R.color.Over50_b,    R.color.Over50);
        mOver10    = prepareArc(resources, 10,    "10+", R.color.Over10_b,    R.color.Over10);
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
        Bitmap bitmap = mOver10000;
        canvas.drawBitmap(bitmap,
                new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect((int) ((tile_size_scale - bitmap.getWidth()) / 2),
                         (int) ((tile_size_scale - bitmap.getHeight()) / 2),
                         (int) ((tile_size_scale + bitmap.getWidth()) / 2),
                         (int) ((tile_size_scale + bitmap.getHeight()) / 2)
                ),
                null
        );

        layers = new CityRow[21][];
        for (int i = 5; i <= 17; i += 2)
            layers[i] = new CityRow[1 << i];
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        Log.e("TILE", "x: "+ x +", y: "+ y +", zoom: "+ zoom);
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
//        String tileCoords = "(" + x + ", " + y + ")";
        String zoomLevel = "zoom = " + zoom;
        /* Paint is not thread safe. */
        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(18 * mScaleFactor);
//        canvas.drawText(tileCoords, TILE_SIZE_DP * mScaleFactor / 2,
//                TILE_SIZE_DP * mScaleFactor / 2, mTextPaint);
        canvas.drawText(zoomLevel, mBorderTile.getWidth() / 2,
                mBorderTile.getHeight() * 2 / 3, mTextPaint);
        return copy;
    }
}

