package com.example.bear.diy_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by haichen.cui on 20/07/2017.
 * 标签墙效果
 * <p>
 * 1.空白信息的对齐位置只有两种：1)Gravity.LEFT 左上角 2)其他 正中间。如需增加，请在onLayout()中
 * 增加处理代码。
 */

public class LabelWall extends ViewGroup {
    private static final int LABLE_HORIZONTAL_SPACE_DEFAULT = 10;
    private static final int LABLE_VERTICAL_SPACE_DEFAULT = 10;
    private static final int LABLE_EMPTY_MSG_TEXT_COLOR_DEFAULT = 0x000000;
    private static final int LABLE_EMPTY_MSG_TEXT_SIZE_DEFAULT = 20;
    private static final boolean LABLE_EMPTY_MSG_ON_LEFT_CONNER_DEFAULT = false;

    private TextView mEmptyMsg;

    private int mLabelHorizontalSpace;
    private int mLabelVerticalSpace;
    private boolean mEmptyMsgOnLeftCorner;

    //记录每一行Label的行高
    private ArrayList<Integer> mRowHeights;

    private LabelAdapter mAdapter;
    private OnLabelClickListener mOnLabelClickListener;
    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnLabelClickListener != null) {
                mOnLabelClickListener.onLabelClick(LabelWall.this, v, indexOfChild(v), v.getId());
            }
        }
    };

    private DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            if (mAdapter != null && mAdapter.getCount() > 0) {
                for (int i = getChildCount() - 1; i >= mAdapter.getCount(); i--) {
                    removeViewAt(i);
                }
                update();
            } else {
                removeAllViews();
                addView(mEmptyMsg);
            }

        }
    };

    public LabelWall(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelWall);
        mLabelHorizontalSpace = ta.getDimensionPixelSize(
                R.styleable.LabelWall_labelHorizontalSpace, LABLE_HORIZONTAL_SPACE_DEFAULT);
        mLabelVerticalSpace = ta.getDimensionPixelSize(
                R.styleable.LabelWall_labelVerticalSpace, LABLE_VERTICAL_SPACE_DEFAULT);
        mEmptyMsg = new TextView(getContext());
        mEmptyMsg.setText(ta.getString(R.styleable.LabelWall_emptyMsgText));
        mEmptyMsg.setTextColor(ta.getColor(R.styleable.LabelWall_emptyMsgTextColor, LABLE_EMPTY_MSG_TEXT_COLOR_DEFAULT));
        float dimension = ta.getDimension(R.styleable.LabelWall_emptyMsgTextSize, LABLE_EMPTY_MSG_TEXT_SIZE_DEFAULT);
        mEmptyMsg.setTextSize(dimension);
        mEmptyMsgOnLeftCorner = ta.getBoolean(R.styleable.LabelWall_emptyMsgOnLeftConner, LABLE_EMPTY_MSG_ON_LEFT_CONNER_DEFAULT);
        ta.recycle();
        mRowHeights = new ArrayList<>();
        addView(mEmptyMsg);
    }

    public void setAdapter(LabelAdapter adapter) {
        if (adapter != null) {
            if (mAdapter != null) {
                mAdapter.unRegisterDataChangeObserver(mObserver);
            }
            mAdapter = adapter;
            mAdapter.registerDataChangeObserver(mObserver);
            removeAllViews();
            if (mAdapter.getCount() > 0) {
                init();
            } else {
                addView(mEmptyMsg);
            }
        }
    }

    public LabelAdapter getAdapter() {
        return mAdapter;
    }

    private void init() {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View item = mAdapter.getView(this, null, i);
            item.setOnClickListener(mOnClickListener);
            addView(item);
        }
    }

    private void update() {
        boolean isNewChild;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View convertView = i < getChildCount() ? getChildAt(i) : null;
            isNewChild = convertView == null;
            View child = mAdapter.getView(this, convertView, i);
            if (isNewChild) {
                child.setOnClickListener(mOnClickListener);
                addView(child);
            }
        }
    }

    public void addOnLabelClickListener(OnLabelClickListener listener) {
        mOnLabelClickListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        /*
         * 有标签的数据可供展示，展示标签
         * 无标签的数据，展示emptyMsg
         */
        if (mAdapter != null && mAdapter.getCount() > 0) {
            int childCount = getChildCount();
            int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
            int prevRowMaxChildHeight;
            int currentRowMaxChildHeight = 0;
            int currentRowHeight = 0;
            int usedWidth = 0;


            for (int i = 0; i < childCount; i++) {
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
        } else {
            measureChildWithMargins(mEmptyMsg, widthMeasureSpec, getPaddingLeft() + getPaddingRight(),
                    heightMeasureSpec, getPaddingTop() + getPaddingBottom());
            maxWidth = mEmptyMsg.getMeasuredWidth() + getPaddingLeft() + getPaddingRight();
            maxHeight = mEmptyMsg.getMeasuredHeight() + getPaddingTop() + getPaddingBottom();
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /*
         * 有标签的数据，展示标签
         * 无标签的数据，展示emptyMsg
         */
        int cl, ct, cr, cb;
        if (mAdapter != null && mAdapter.getCount() > 0) {
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
        } else {
            //如果padding之间相互影响，只考虑left和top
            if (mEmptyMsgOnLeftCorner) {
                cl = getPaddingLeft();
                cr = cl + mEmptyMsg.getMeasuredWidth();
                ct = getPaddingTop();
                cb = ct + mEmptyMsg.getMeasuredHeight();
            } else {
                cl = (r - l + getPaddingLeft() - getPaddingRight() - mEmptyMsg.getMeasuredWidth()) / 2;
                cr = cl + mEmptyMsg.getMeasuredWidth();
                ct = (b - t + getPaddingTop() - getPaddingBottom() - mEmptyMsg.getMeasuredHeight()) / 2;
                cb = ct + mEmptyMsg.getMeasuredHeight();
            }
            mEmptyMsg.layout(getPaddingLeft() + cl, getPaddingTop() + ct,
                    getPaddingLeft() + cr, getPaddingTop() + cb);
        }
    }

    @SuppressWarnings("all")
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(@Px int width, @Px int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    public static abstract class LabelAdapter<T> {
        private DataSetObservable mObservable = new DataSetObservable();
        protected ArrayList<T> mData;

        public LabelAdapter(ArrayList<T> data) {
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

        public abstract View getView(LabelWall parent, View convertView, int position);

        public void registerDataChangeObserver(DataSetObserver observer) {
            mObservable.registerObserver(observer);
        }

        public void unRegisterDataChangeObserver(DataSetObserver observer) {
            mObservable.unregisterObserver(observer);
        }

        public void notifyDataSetChanged() {
            mObservable.notifyChanged();
        }
    }

    public interface OnLabelClickListener {
        void onLabelClick(LabelWall labelWall, View v, int position, int id);
    }
}
