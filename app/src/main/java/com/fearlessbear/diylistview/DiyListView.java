package com.fearlessbear.diylistview;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by root on 17-4-5.
 * 1.创建条目
 * 1)根据屏幕的高度创建item，直到item的measuredHeight与其前面的item不同为止。
 * 2.在onTouchEvent中处理滚动事件
 * 2）移动list中的子View。向上移动时，如果
 * 3.complete reuse item
 * <p>
 * 总结：
 * 1.如果使用循环addView，那么是在循环结束后，才调用layout和draw流程。所以在每个addView执行结束时，什么尺寸参数也
 * 得不到。
 * 04-06 16:30:36.398 31595-31595/com.fearlessbear.diylistview I/xiong: add data is 49: 672
 * 04-06 16:30:36.507 31595-31595/com.fearlessbear.diylistview I/xiong: layout is 722
 * 04-06 16:30:36.507 31595-31595/com.fearlessbear.diylistview I/xiong: pre draw is 722
 */
public class DiyListView extends ViewGroup {
    private ArrayList<String> mData;
    private float mOldY;
    private float mDy;
    private float mStartY;
    private ViewTreeObserver observer;
    private int mItemTotalHeight = 0;

    public DiyListView(Context context) {
        this(context, null);
    }

    public DiyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(List<String> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }

        mData.addAll(data);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthUsed = 0;
        int heightUsed = 0;

        /*
         * if:列表中没有item，初始化列表，创建所需的所有item
         * else：刷新列表数据，去掉滚动出屏幕的，增加滚进屏幕的
         */
        if (getChildCount() == 0 && mData != null) {
            for (int i = 0; i < mData.size(); i++) {
                View view = getView(mData.get(i));
                view.setLayoutParams(new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                measureChildWithMargins(view, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
                if (view.getMeasuredHeight() == 0) {
                    break;
                } else {
                    widthUsed += view.getMeasuredWidth();
                    heightUsed += view.getMeasuredHeight();
                    addView(view);
                }
            }
        } else {


        }

        //确定当前DiyListView的大小
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    //自己重写当前方法，当作学习
    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(
                parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin + widthUsed,
                lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(
                parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin + heightUsed,
                lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    /*
      根据父容器的spec得到的child的spec，实际上并不是child的大小，这个childSpec只是告诉child，目前屏幕上剩余的显示
      空间范围为childSpec所圈定的范围，child可以根据这个范围确定自己的大小。child实际上可以比这个范围大，也可以比这个
      范围小，其最终的大小是由child自身的onMeasure方法确定。只不过如果child的大小超出了childSpec限制的范围，屏幕上是
      无法显示的，无法显示也就没有意义。
    */
    public static int getChildMeasureSpec(int parentMeasureSpec, int padding, int childDimension) {
        int parentSize = MeasureSpec.getSize(parentMeasureSpec);
        int parentMode = MeasureSpec.getMode(parentMeasureSpec);
        int childMaxSize = parentSize - padding;
        int resultSize = 0;
        int resultMode = MeasureSpec.UNSPECIFIED;

        switch (parentMode) {
            case MeasureSpec.EXACTLY:
                if (childDimension > 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    resultSize = childMaxSize;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    resultSize = childMaxSize;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;
            case MeasureSpec.AT_MOST:
                if (childDimension > 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    resultSize = childMaxSize;
                    resultMode = MeasureSpec.AT_MOST;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    resultSize = childMaxSize;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;
            case MeasureSpec.UNSPECIFIED:
                //忽略，暂时没弄明白这种情况。代码瞎写的。
                if (childDimension >= 0) {
                    // Child wants a specific size... let him have it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size... find out how big it should
                    // be
                    resultSize = childMaxSize;
                    resultMode = MeasureSpec.UNSPECIFIED;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size.... find out how
                    // big it should be
                    resultSize = childMaxSize;
                    resultMode = MeasureSpec.UNSPECIFIED;
                }
                break;
        }
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int used = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childTop = t + used;
            int childBottom = childTop + child.getMeasuredHeight();
            child.layout(l, childTop, r, childBottom);
        }
    }

    private int getNewHeightMeasureSpec(int heightMeasureSpec, int mItemTotalHeight) {
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        int restHeight = viewHeight - mItemTotalHeight;
        return MeasureSpec.makeMeasureSpec(restHeight, MeasureSpec.getMode(heightMeasureSpec));
    }

    private View getView(String s) {
        View item = inflate(getContext(), R.layout.item, null);
        ((TextView) item.findViewById(R.id.text)).setText(s);
        return item;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i("xiong", "the count of item is " + getChildCount());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float y = event.getY();
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mOldY = y;
                mStartY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                onActionMove(0);

                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void onActionMove(float dy) {

    }
}

//总结1
    /*private void initListView(ArrayList<String> mData) {
        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Log.i("xiong", "pre draw is " + SystemClock.currentThreadTimeMillis() + "");
                return true;
            }
        });
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i("xiong", "layout is " + SystemClock.currentThreadTimeMillis() + "");
            }
        });

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (final String s : mData) {
            View child = buildItem(inflater, R.layout.item, s);
            addView(child);
            Log.i("xiong", "add " + s + ": " + SystemClock.currentThreadTimeMillis() + "");

            if (mItemHeight == 0) {
                mItemHeight = child.getMeasuredHeight();
            }
//            Log.i("xiong", "initListView: item height is " + child.getMeasuredHeight());
            if (child.getMeasuredHeight() != mItemHeight) {
                break;
            }
        }
//        Log.i("xiong", "initListView: item count is " + getChildCount());
    }*/