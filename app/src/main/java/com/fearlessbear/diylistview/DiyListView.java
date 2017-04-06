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
 *
 * 总结：
 * 1.如果使用循环addView，那么是在循环结束后，才调用layout和draw流程。所以在每个addView执行结束时，什么尺寸参数也
 * 得不到。
 * 04-06 16:30:36.398 31595-31595/com.fearlessbear.diylistview I/xiong: add data is 49: 672
 * 04-06 16:30:36.507 31595-31595/com.fearlessbear.diylistview I/xiong: layout is 722
 * 04-06 16:30:36.507 31595-31595/com.fearlessbear.diylistview I/xiong: pre draw is 722
 */
public class DiyListView extends LinearLayout {
    private ArrayList<String> mData;
    private float mOldY;
    private float mDy;
    private float mStartY;
    private ViewTreeObserver observer;
    private float mItemHeight;
    int i;

    public DiyListView(Context context) {
        this(context, null);
    }

    public DiyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    public void setData(List<String> data) {
        if (data == null) {
            return;
        }

        if (mData == null) {
            mData = new ArrayList<>();
        }

        mData.addAll(data);
        initListView(mData);
    }

    private void initListView(ArrayList<String> mData) {
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
    }

    private View buildItem(LayoutInflater inflater, int layoutRes, final String s) {
        final View item = inflater.inflate(layoutRes, this, false);
        ((TextView) item.findViewById(R.id.text)).setText(s);
        return item;
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