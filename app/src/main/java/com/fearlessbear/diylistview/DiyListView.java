package com.fearlessbear.diylistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-4-5.
 * 1.create item
 * 2.complete scroll
 * 3.complete reuse item
 */
public class DiyListView extends LinearLayout {
    private ArrayList<String> mData;

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
        
    }




}