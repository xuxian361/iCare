package com.sundy.icare.views.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.ActivityController;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.views.fragment.BaseFragment;
import com.sundy.icare.views.fragment.MarketFragment;
import com.sundy.icare.views.fragment.MeFragment;
import com.sundy.icare.views.fragment.MsgFragment;
import com.sundy.icare.views.fragment.ServiceFragment;
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
    private ViewPager pager;
    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "------------->onCreate");
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
        setContentView(R.layout.activity_main);
        inflater = getLayoutInflater();
        aq = new AQuery(this);

        initBottomMenu();
        initViewPager();
    }

    private void initBottomMenu() {
        frameMenu = (TabMenuFragment) getSupportFragmentManager().findFragmentById(R.id.frameMenu);
        frameMenu.setPosition(0);
    }

    private void initViewPager() {
        pager = (ViewPager) aq.id(R.id.pager).getView();
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                android.support.v4.app.Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new MsgFragment();
                        break;
                    case 1:
                        fragment = new ServiceFragment();
                        break;
                    case 2:
                        fragment = new MarketFragment();
                        break;
                    case 3:
                        fragment = new MeFragment();
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                frameMenu.setPosition(position);
                super.onPageSelected(position);
            }
        });

    }

    /**
     * 切换Fragment
     *
     * @param id
     */
    public void switchFragment(int id) {
        switch (id) {
            case R.id.btnMsg:
                pager.setCurrentItem(0);
                break;
            case R.id.btnService:
                pager.setCurrentItem(1);
                break;
            case R.id.btnMarket:
                pager.setCurrentItem(2);
                break;
            case R.id.btnMe:
                pager.setCurrentItem(3);
                break;
        }
    }

    /**
     * 替换当前Fragment
     */
    @Override
    public void switchContent(android.support.v4.app.Fragment fragment) {
        try {
            if (fragment == null && mContent == fragment) {
                return;
            } else {
                mContent = fragment;
                if (fragment.isAdded()) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .show(fragment)
                            .commit();
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

    /**
     * 添加一个Fragment到视图顶层
     */
    @Override
    public void addContent(android.support.v4.app.Fragment fragment) {
        if (fragment == null && mContent == fragment) {
            return;
        } else {
            mContent = fragment;
            if (fragment.isAdded()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(fragment)
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
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
                        mContent = getSupportFragmentManager().findFragmentById(R.id.frameContent);
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

