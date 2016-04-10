package com.sundy.icare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.fragment.MarketFragment;
import com.sundy.icare.fragment.MeFragment;
import com.sundy.icare.fragment.MsgFragment;
import com.sundy.icare.fragment.ServiceFragment;
import com.sundy.icare.fragment.TabMenuFragment;

/**
 * Created by sundy on 15/12/6.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";
    private Fragment mContent;
    private TabMenuFragment frameMenu;
    private ViewPager pager;
    private FragmentPagerAdapter pagerAdapter;
    private int current_Position = 0;
    private String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "------------->onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aq = new AQuery(this);

        from = getIntent().getStringExtra("from");
        if (from != null && from.length() != 0) {
            if (from.equals("Register")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }

        //初始化View
        initBottomMenu();
        initViewPager();
    }


    //跳转登陆
    private void goLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initBottomMenu() {
        frameMenu = (TabMenuFragment) getSupportFragmentManager().findFragmentById(R.id.frameMenu);
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
            case R.id.btnMarket:
                current_Position = 2;
                break;
            case R.id.btnMe:
                current_Position = 3;
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

