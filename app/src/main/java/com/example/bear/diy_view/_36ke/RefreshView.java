package com.example.bear.diy_view._36ke;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by BigFaceBear on 2018.03.15
 */

public class RefreshView extends View {

    public static final int STATE_CLOSED = 0;
    public static final int STATE_EXPANDING = 1;
    public static final int STATE_FILLING = 2;

    private int mState = STATE_CLOSED;
    private float mExpandRatio;
    private float mFillRatio;

    private Paint mPaint;
    private RectF mFrameRect;
    private int mStrokeWidth;

    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFrameRect = new RectF();
        mStrokeWidth = 2;

        if (isInEditMode()) {
            mState = STATE_EXPANDING;
            mExpandRatio = 1;
        }
    }

    /**
     * 设置RefreshView的状态{@value STATE_CLOSED,STATE_EXPANDING, STATE_FILLING}
     * RefreshView根据状态控制绘制的内容
     *
     * @param state
     */
    public void setState(int state) {
        switch (state) {
            case STATE_CLOSED:
                mState = STATE_CLOSED;
                break;
            case STATE_EXPANDING:
                mState = STATE_CLOSED;
                break;
            case STATE_FILLING:
                mState = STATE_CLOSED;
                break;
            default:
                throw new IllegalStateException("不是RefreshView可识别的状态");
        }
    }

    /**
     * 当mState=STATE_EXPANDING的时候，控制边框距当前View中心点的距离
     * 当percent=0时，边框收缩到中心点；当percent=1时，边框扩张到View的边界
     *
     * @param ratio 0~1
     */
    public void setExpandRatio(float ratio) {
        if (ratio < 0) {
            mExpandRatio = 0;
        } else if (ratio > 1) {
            mExpandRatio = 1;
        } else {
            mExpandRatio = ratio;
        }

        if (mState != STATE_EXPANDING) {
            mState = STATE_EXPANDING;
        }

        postInvalidate();
    }

    /**
     * 当mState=STATE_REFRESHING的时候，控制边框内部颜色填充的百分比
     * 当percent=0时，边框内不填充颜色；当percent=1时，边框内填充满颜色，并且展示文字
     *
     * @param ratio 0~1
     */
    public void setFillCenterRatio(float ratio) {
        if (ratio < 0) {
            mFillRatio = 0;
        } else if (ratio > 1) {
            mFillRatio = 1;
        } else {
            mFillRatio = ratio;
        }

        if (mState != STATE_FILLING) {
            mState = STATE_FILLING;
        }

        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mState == STATE_CLOSED) {
            return;
        }

        final int width = getWidth();
        final int height = getHeight();

        switch (mState) {
            case STATE_EXPANDING:
                drawFrame(width, height, mExpandRatio, canvas);
                break;
            case STATE_FILLING:
                drawFrame(width, height, 1, canvas);
                drawContent(width, height, mFillRatio, canvas);
                break;
        }
    }

    private void drawFrame(int width, int height, float expandRatio, Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(Color.BLUE);

        final int centerX = width / 2;
        final int centerY = height / 2;

        final float left = (centerX - mStrokeWidth) * (1 - expandRatio);
        final float top = (centerY - mStrokeWidth) * (1 - expandRatio);
        final float right = centerX * (1 + expandRatio) - mStrokeWidth * expandRatio;
        final float bottom = centerY * (1 + expandRatio) - mStrokeWidth * expandRatio;
        mFrameRect.set(left, top, right, bottom);

        canvas.drawRoundRect(mFrameRect, 2, 2, mPaint);
    }

    private void drawContent(int width, int height, float fillRatio, Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);

        final float left = 0;
        final float top = height * (1 - fillRatio);
        final float right = width - mStrokeWidth;
        final float bottom = height - mStrokeWidth;
        mFrameRect.set(left, top, right, bottom);

        canvas.drawRoundRect(mFrameRect, 2, 2, mPaint);

        if (Math.abs(1 - fillRatio / 1) < 0.001) {
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize((float) (height));
            canvas.drawText("c", 0, height, mPaint);
        }
    }

}
