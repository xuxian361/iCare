package com.sundy.icare.activity;

import android.os.Bundle;

import com.sundy.icare.R;
import com.sundy.icare.fragment.MainFragment;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 15/12/6.
 */
public class MainActivity extends BaseActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "------------->onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMainFragment();
    }

    private void initMainFragment() {
        switchContent(new MainFragment());
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

}

