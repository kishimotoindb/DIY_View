package com.example.bear.diy_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by haichen.cui on 20/07/2017.
 * 标签墙效果
 */

public class LabelWall extends ViewGroup {
    private static final int LABLE_HORIZONTAL_SPACE_DEFAULT = 10;
    private static final int LABLE_VERTICAL_SPACE_DEFAULT = 10;

    private int mLabelHorizontalSpace;
    private int mLabelVerticalSpace;

    //记录每一行Label的行高
    private ArrayList<Integer> mRowHeights;

    public LabelWall(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelWall);
        mLabelHorizontalSpace = ta.getDimensionPixelSize(
                R.styleable.LabelWall_label_horizontal_space, LABLE_HORIZONTAL_SPACE_DEFAULT);
        mLabelVerticalSpace = ta.getDimensionPixelSize(
                R.styleable.LabelWall_label_vertical_space, LABLE_VERTICAL_SPACE_DEFAULT);
        ta.recycle();
        mRowHeights = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int maxHeight = 0;
        int maxWidth = 0;
        int prevRowMaxChildHeight;
        int currentRowMaxChildHeight = 0;
        int currentRowHeight = 0;
        int usedWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, getPaddingLeft() + getPaddingRight(),
                        heightMeasureSpec, getPaddingTop() + getPaddingBottom());
                int childHeight = child.getMeasuredHeight();
                int childWidth = child.getMeasuredWidth();
                prevRowMaxChildHeight = currentRowMaxChildHeight;
                currentRowMaxChildHeight = Math.max(currentRowMaxChildHeight, childHeight);
                usedWidth = usedWidth == 0 ? childWidth : usedWidth + mLabelHorizontalSpace + childWidth;

                //一旦进入这个if，就表明Label一定摆放了至少两行。最后一行的高度在当前循环外最后
                // 再添加进maxHeight，这里的maxHeight不包含最后一行的高度。
                if (usedWidth > widthSize) {
                    if (maxWidth == 0) {
                        maxWidth = widthSize;
                    }
                    //如果一行填满了，那么将当前child放到下一行的第一个
                    usedWidth = childWidth;
                    maxHeight = maxHeight + prevRowMaxChildHeight + mLabelVerticalSpace;
                    currentRowMaxChildHeight = childHeight;
                    currentRowHeight = childHeight;
                    mRowHeights.add(prevRowMaxChildHeight);
                } else {
                    //当前行的高度由当前行中最高的一个View决定
                    currentRowHeight = currentRowMaxChildHeight;
                }
            }
        }

        if (maxWidth == 0) maxWidth = usedWidth;
        maxWidth = maxWidth + getPaddingLeft() + getPaddingRight();
        maxHeight = maxHeight + currentRowHeight + getPaddingTop() + getPaddingBottom();
        if (currentRowHeight != 0) mRowHeights.add(currentRowHeight);

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int usedHeight = 0;
        int usedWidth = 0;
        int width = r - l - getPaddingLeft() - getPaddingRight();
        int height = b - t - getPaddingTop() - getPaddingBottom();
        int row = 0;
        boolean isNoHeight = false;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childHeight = child.getMeasuredHeight();
                int childWidth = child.getMeasuredWidth();
                int cl, ct, cr, cb;

                if (childWidth <= (usedWidth == 0 ? width : width - usedWidth - mLabelHorizontalSpace)) {
                    cl = usedWidth == 0 ? 0 : usedWidth + mLabelHorizontalSpace;
                    cr = cl + childWidth;
                    usedWidth = cr;
                } else {
                    if (isNoHeight) break;
                    usedHeight = usedHeight + mRowHeights.get(row) + mLabelVerticalSpace;
                    cl = 0;
                    cr = childWidth;
                    usedWidth = childWidth;
                }

                ct = usedHeight;
                cb = usedHeight + childHeight;
                if (cb > height) {
                    isNoHeight = true;
                }

                child.layout(getPaddingLeft() + cl, getPaddingTop() + ct,
                        getPaddingLeft() + cr, getPaddingTop() + cb);
            }
        }
    }

    @SuppressWarnings("all")
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    public static abstract class LabelAdapter<T> {
        private Observer mObserver;
        private Context mContext;
        private ArrayList<T> mData;

        public LabelAdapter(Context context, ArrayList<T> data) {
            mContext = context;
            mData = new ArrayList<>();
            if (data != null && data.size() != 0) {
                mData.addAll(data);
            }
        }

        public ArrayList<T> getData() {
            return mData;
        }

        public int getCount() {
            return mData.size();
        }

        public T getItem(int position) {
            return mData.get(position);
        }

        public abstract View getView(int position);

        public void registerDataChangeObserver(Observer observer) {
            mObserver = observer;
        }

        public void notifyDataSetChanged() {
            if (mObserver != null) {
                mObserver.update(null, null);
            }
        }
    }
}
