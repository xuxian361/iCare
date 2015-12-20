package com.sundy.icare.views.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.ActivityController;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.views.fragment.BaseFragment;
import com.sundy.icare.views.fragment.TabMenuFragment;

/**
 * Created by sundy on 15/12/6.
 */
public class MainActivity extends BaseActivity implements BaseFragment.OnBaseListener,
        View.OnClickListener {

    private final String TAG = "MainActivity";
    private Fragment mContent;
    private LayoutInflater inflater;
    private TabMenuFragment frameMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "------------->onCreate");
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
        setContentView(R.layout.activity_main);
        inflater = getLayoutInflater();
        aq = new AQuery(this);

        initFragment();

    }

    private void initFragment() {
        frameMenu = (TabMenuFragment) getFragmentManager().findFragmentById(R.id.frameMenu);
        frameMenu.setPosition(6);



    }

    @Override
    protected void onResume() {
        MyUtils.rtLog(TAG, "------------->onResume");
        super.onResume();
    }

    /**
     * 替换当前Fragment
     */
    @Override
    public void switchContent(Fragment fragment) {
        try {
            if (fragment == null && mContent == fragment) {
                return;
            } else {
                mContent = fragment;
                if (fragment.isAdded()) {
                    getFragmentManager()
                            .beginTransaction()
                            .show(fragment)
                            .commit();
                } else {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameContent, fragment)
                            .commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个Fragment到视图顶层
     */
    @Override
    public void addContent(Fragment fragment) {
        if (fragment == null && mContent == fragment) {
            return;
        } else {
            mContent = fragment;
            if (fragment.isAdded()) {
                getFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .show(fragment)
                        .commit();
            } else {
                getFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .add(R.id.frameContent, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    /**
     * 返回上一层
     */
    @Override
    public void onBack() {
        MyUtils.rtLog(TAG, "------------>onBack");
        try {
            android.app.FragmentManager manager = getFragmentManager();
            int count = manager.getBackStackEntryCount();
            if (count > 0) {
                manager.popBackStack();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContent = getFragmentManager().findFragmentById(R.id.frameContent);
                    }
                }, 350);
            } else {
                ActivityController.finishAll();
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新加载Activity,可用于切换语言等
     */
    @Override
    public void reloadActivity() {
        try {
            getFragmentManager().popBackStack();
            MyUtils.rtLog(TAG, "------------>reloadActivity");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过ID切换当前Fragment
     */
    @Override
    public void switchContent(int rid) {
        try {
            getFragmentManager().popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手机返回键控制
     */
    @Override
    public void onBackPressed() {
        MyUtils.rtLog(TAG, "------------------------onBackPressed");
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        MyUtils.rtLog(TAG, "------------------->onDestroy");
        super.onDestroy();
        ActivityController.removeActivity(this);
        try {
            if (mContent != null)
                mContent = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

}

