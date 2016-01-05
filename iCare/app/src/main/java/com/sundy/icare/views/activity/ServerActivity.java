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
import com.sundy.icare.views.fragment.server.ServerLatestFragment;
import com.sundy.icare.views.fragment.server.ServerMeFragment;
import com.sundy.icare.views.fragment.server.ServerMsgFragment;
import com.sundy.icare.views.fragment.server.ServerTabMenuFragment;

/**
 * Created by sundy on 15/12/26.
 */
public class ServerActivity extends BaseActivity implements BaseFragment.OnBaseListener,
        View.OnClickListener {

    private final String TAG = "ServerActivity";
    private Fragment mContent;
    private LayoutInflater inflater;
    private ServerTabMenuFragment frameMenu;
    private ViewPager pager;
    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "------------->onCreate");
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
        setContentView(R.layout.layout_server);
        inflater = getLayoutInflater();
        aq = new AQuery(this);

        initViewPager();

    }

    private void initViewPager() {
        pager = (ViewPager) aq.id(R.id.pager).getView();
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                android.support.v4.app.Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new ServerMsgFragment();
                        break;
                    case 1:
                        fragment = new ServerLatestFragment();
                        break;
                    case 2:
                        fragment = new ServerMeFragment();
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };

        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
//                MyUtils.rtLog(TAG, "---------->" + position);
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
                switchContent(new ServerMsgFragment());
                break;
            case R.id.btnService:
                switchContent(new ServerLatestFragment());
                break;
            case R.id.btnMe:
                switchContent(new ServerMeFragment());
                break;
        }
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
    public void addContent(Fragment fragment) {
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
