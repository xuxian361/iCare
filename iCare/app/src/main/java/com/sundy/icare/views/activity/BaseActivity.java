package com.sundy.icare.views.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.androidquery.AQuery;
import com.sundy.icare.MyApp;
import com.sundy.icare.net.MyURL;
import com.sundy.icare.utils.MyUtils;

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

    }

}
