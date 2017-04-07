package com.fearlessbear.diylistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final ArrayList<String> mData = new ArrayList<>();

    {
        for (int i = 0; i < 50; i++) {
            mData.add("data is " + i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DiyListView listView = (DiyListView) findViewById(R.id.listView);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.i("xiong", "screen size h*w is " + displayMetrics.heightPixels + " * " + displayMetrics.widthPixels);
        listView.setData(mData);
    }

}
