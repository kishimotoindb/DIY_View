package com.example.bear.diy_view.LayoutProcedure;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by BigFaceBear on 2018.03.02
 */

public class RequestLayoutTestView extends AppCompatTextView {
    public RequestLayoutTestView(Context context) {
        super(context);
    }

    public RequestLayoutTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RequestLayoutTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMyHeight(int pixel) {
        setHeight(pixel);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.i("bear", "draw");
        super.draw(canvas);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("bear", "onDraw");
        super.onDraw(canvas);

    }

    @Override
    public void layout(@Px int l, @Px int t, @Px int r, @Px int b) {
        Log.i("bear", "layout");
        super.layout(l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("bear", "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
