package com.sundy.icare.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.baidu.mapapi.SDKInitializer;
import com.sundy.icare.R;
import com.sundy.icare.fragment.BaseFragment;
import com.sundy.icare.ui.MyProgressDialog;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 15/12/6.
 */
public class BaseActivity extends FragmentActivity implements BaseFragment.OnBaseListener {

    private final String TAG = "BaseActivity";
    protected Context context;
    protected AQuery aq;
    private long exitTime;
    private MyProgressDialog progressDialog;
    private Fragment mContent;


    public BaseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        context = this;
        aq = new AQuery(this);

        SDKInitializer.initialize(getApplicationContext());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (this.getClass() == MainActivity.class || this.getClass() == LoginActivity.class) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        Toast.makeText(getApplicationContext(), getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        if (isTaskRoot()) {
                            AQUtility.cleanCacheAsync(this);
                        }
                        this.exitApp();
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onKeyDown(keyCode, event);
    }

    //退出APP
    public void exitApp() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
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
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reloadActivity() {

    }

    @Override
    public void switchContent(int rid) {

    }

    @Override
    public void showLoading(Activity context) {
        MyUtils.rtLog(TAG, "-------->showLoading");
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            progressDialog = new MyProgressDialog(context, context.getWindow().getDecorView());
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeLoading() {
        MyUtils.rtLog(TAG, "-------->closeLoading");
        try {
            if (progressDialog != null)
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
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
