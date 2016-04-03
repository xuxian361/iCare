package com.sundy.icare.views.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;

import com.androidquery.AQuery;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by sundy on 15/12/6.
 */
public class BaseActivity extends FragmentActivity {

    private final String TAG = "BaseActivity";
    protected Context context;
    protected AQuery aq;

    public BaseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        aq = new AQuery(this);

        SDKInitializer.initialize(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
