package com.example.bear.diy_view.GlideBitmapTransformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by BigFaceBear on 2018.01.29
 */

public class RoundBitmapTransformation extends BitmapTransformation {

    public RoundBitmapTransformation(Context context) {
        super(context);
    }

    public RoundBitmapTransformation(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
//        if (toTransform == null) return null;
//
//        Bitmap result = Bitmap.createScaledBitmap(toTransform, outWidth, outHeight, false);
//
//        Canvas canvas = new Canvas(result);
//        Path clipPath = new Path();
//        clipPath.addCircle(outWidth / 2, outHeight / 2, Math.min(outWidth / 2, outHeight / 2), Path.Direction.CW);
//        canvas.clipPath(clipPath);
//
//        return result;

        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, 10, 10, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(result, 0, 0, paint);

        return result;
    }

    @Override
    public String getId() {
        return "Glide";
    }
}
