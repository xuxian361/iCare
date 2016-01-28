package com.sundy.icare.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.sundy.icare.utils.ActivityController;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyUtils;

public class LoadingActivity extends BaseActivity {

    private final String TAG = "LoadingActivity";
    private final long DELAY_MILLIS = 500;
    private final int GO_MAIN = 100;
    private final int GO_LOGIN = 101;
    private Runnable mRunnable;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "---------->onCreate");
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.loading);

        ActivityController.addActivity(this);

        //屏幕的信息
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        MyConstant.SCREEN_WIDTH = metrics.widthPixels;
        MyConstant.SCREEN_HEIGHT = metrics.heightPixels;
        MyConstant.SCREEN_DENSITY = metrics.density;

        //Save UUID
        MyUtils.saveUDID(this);

        //Save APP ID : Default is app_02(子女)
        MyUtils.saveAppID(MyConstant.APP_ID_02, this);

        //百度Push Init
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, MyConstant.Baidu_Push_Key);
    }

    @Override
    protected void onResume() {
        MyUtils.rtLog(TAG, "---------->onResume");
        super.onResume();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            if (mHandler == null) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case GO_MAIN:
                                goMain();
                                break;
                            case GO_LOGIN:
                                goLogin();
                                break;
                        }
                    }
                };
            }

            if (mRunnable == null) {
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (MyUtils.isLogin(LoadingActivity.this)) {
                            mHandler.sendEmptyMessage(GO_MAIN);
                        } else {
                            mHandler.sendEmptyMessage(GO_LOGIN);
                        }
                    }
                };
            }

            mHandler.postDelayed(mRunnable, DELAY_MILLIS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        MyUtils.rtLog(TAG, "---------->onPause");
        super.onPause();
        try {
            if (mRunnable != null && mHandler != null)
                mHandler.removeCallbacks(mRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        MyUtils.rtLog(TAG, "---------->onDestroy");
        super.onDestroy();
        try {
            if (mRunnable != null) {
                mRunnable = null;
            }
            if (mHandler != null) {
                mHandler = null;
            }
            ActivityController.removeActivity(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}