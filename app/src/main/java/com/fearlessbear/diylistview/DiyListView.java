package com.fearlessbear.diylistview;

import android.content.Context;
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
 * 1.create item
 * 2.complete scroll
 * 3.complete reuse item
 */
public class DiyListView extends LinearLayout {
    private ArrayList<String> mData;
    private float mOldY;
    private float mDy;
    private float mStartY;
    private ViewTreeObserver observer;

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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (String s : mData) {
            addView(buildItem(inflater, R.layout.item, s));
        }

        observer = getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                for (int i = 0; i < getChildCount(); i++) {
                    Log.i("xiong", i + ": " + getChildAt(i).getMeasuredHeight());
                }
                observer.removeOnPreDrawListener(this);
                return true;
            }
        });
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