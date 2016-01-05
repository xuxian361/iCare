package com.sundy.icare.views.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.ActivityController;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.views.fragment.server.ServerLatestFragment;
import com.sundy.icare.views.fragment.server.ServerMeFragment;
import com.sundy.icare.views.fragment.server.ServerMsgFragment;
import com.sundy.icare.views.fragment.server.ServerTabMenuFragment;

/**
 * Created by sundy on 15/12/26.
 */
public class ServerActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "ServerActivity";
    private Fragment mContent;
    private ServerTabMenuFragment frameMenu;
    private ViewPager pager;
    private FragmentPagerAdapter pagerAdapter;
    private int current_Position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "------------->onCreate");
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
        setContentView(R.layout.layout_server);
        aq = new AQuery(this);

        initBottomMenu();
        initViewPager();
    }

    private void initBottomMenu() {
        frameMenu = (ServerTabMenuFragment) getSupportFragmentManager().findFragmentById(R.id.frameMenu);
        current_Position = 0;
        frameMenu.setPosition(current_Position);
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
                frameMenu.setPosition(position);
                current_Position = position;
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
                current_Position = 0;
                break;
            case R.id.btnService:
                current_Position = 1;
                break;
            case R.id.btnMe:
                current_Position = 2;
                break;
        }
        pager.setCurrentItem(current_Position);
    }

    /**
     * 手机返回键控制
     */
    @Override
    public void onBackPressed() {
        MyUtils.rtLog(TAG, "------------------->onBackPressed");
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
