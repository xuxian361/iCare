package com.sundy.icare.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.sundy.icare.R;
import com.sundy.icare.fragment.LazyLoadFragment;
import com.sundy.icare.fragment.MainFragment;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 15/12/6.
 */
public class BaseActivity extends FragmentActivity implements LazyLoadFragment.OnBaseListener {

    private final String TAG = "BaseActivity";
    protected Context context;
    protected AQuery aq;
    private Fragment mContent;
    private long exitTime;


    public BaseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        aq = new AQuery(this);

    }

    @Override
    public void switchContent(Fragment fragment) {
        try {
            if (fragment == null && mContent == fragment) {
                return;
            } else {
                mContent = fragment;
                if (fragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(fragment).commit();
                } else {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameContent, fragment)
                            .commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addContent(Fragment fragment) {
        try {
            if (fragment == null && mContent == fragment) {
                return;
            } else {
                mContent = fragment;
                if (fragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().show(fragment).commit();
                } else {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.frameContent, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBack() {
        MyUtils.rtLog(TAG, "-------->onBack");
        try {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count > 0) {
                getSupportFragmentManager().popBackStack();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContent = getSupportFragmentManager().findFragmentById(R.id.frameContent);
                    }
                }, 350);
            } else if (mContent instanceof MainFragment) {
                exitApp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //退出APP
    private void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            MyToast.rtToast(this, getString(R.string.exit_app));
            exitTime = System.currentTimeMillis();
        } else {
            if (isTaskRoot()) {
                AQUtility.cleanCacheAsync(this);
            }
            finish();
        }
    }

    @Override
    public void reloadActivity() {

    }

    public void onClick(View v) {
        MyUtils.rtLog(TAG, "------------------------onClick");
        if (mContent instanceof LazyLoadFragment) {
            ((LazyLoadFragment) mContent).onClick(v);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyUtils.rtLog(TAG, "------------------------onActivityResult");
        if (mContent != null) {
            mContent.onActivityResult(requestCode, resultCode, data);
        }
    }
}
