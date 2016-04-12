package com.sundy.icare.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.sundy.icare.R;
import com.sundy.icare.fragment.BaseFragment;
import com.sundy.icare.fragment.RegisterUserInfoFragment;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 16/4/12.
 */
public class RegisterActivity extends BaseActivity implements BaseFragment.OnBaseListener {

    private final String TAG = "RegisterActivity";
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        clearRegisterInfo();
        //默认第一次装载 RegisterUserInfoFragment
        switchContent(new RegisterUserInfoFragment());
    }

    //清除注册信息
    private void clearRegisterInfo() {
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_Registration, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyUtils.rtLog(TAG, "------------------------onActivityResult");
        if (mContent != null) {
            mContent.onActivityResult(requestCode, resultCode, data);
        }
    }
}
