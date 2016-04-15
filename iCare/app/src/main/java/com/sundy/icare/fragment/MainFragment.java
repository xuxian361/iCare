package com.sundy.icare.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.ui.MyProgressDialog;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 16/4/15.
 */
public class MainFragment extends LazyLoadFragment {

    private final String TAG = "MainFragment";
    private LayoutInflater inflater;
    private View root;
    private Button btnMsg;
    private Button btnService;
    private Button btnMarket;
    private Button btnMe;

    private ViewPager pager;
    private FragmentPagerAdapter pagerAdapter;
    private int current_Position = 0;

    private MyProgressDialog progressDialog;

    public void showLoading() {
        MyUtils.rtLog(TAG, "-------->showLoading");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = new MyProgressDialog(context, context.getWindow().getDecorView());
        progressDialog.show();
    }

    public void closeLoading() {
        MyUtils.rtLog(TAG, "-------->closeLoading");
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        root = inflater.inflate(R.layout.fragment_main, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        btnMsg = aq.id(R.id.btnMsg).getButton();
        btnService = aq.id(R.id.btnService).getButton();
        btnMarket = aq.id(R.id.btnMarket).getButton();
        btnMe = aq.id(R.id.btnMe).getButton();
        aq.id(R.id.btnMsg).clicked(onClick);
        aq.id(R.id.btnService).clicked(onClick);
        aq.id(R.id.btnMarket).clicked(onClick);
        aq.id(R.id.btnMe).clicked(onClick);

        current_Position = 0;
        initViewPager();
    }

    private void initViewPager() {
        pager = (ViewPager) aq.id(R.id.pager).getView();
        pagerAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
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

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnMsg:
                    clickMsg();
                    break;
                case R.id.btnService:
                    clickService();
                    break;
                case R.id.btnMarket:
                    clickMarket();
                    break;
                case R.id.btnMe:
                    clickMe();
                    break;
            }
            switchFragment(view.getId());
        }
    };


    //点击: 消息
    private void clickMsg() {
        btnMsg.setTextColor(Color.parseColor("#a90003"));
        btnService.setTextColor(Color.parseColor("#ff424242"));
        btnMarket.setTextColor(Color.parseColor("#ff424242"));
        btnMe.setTextColor(Color.parseColor("#ff424242"));
    }

    //点击: 服务
    private void clickService() {
        btnMsg.setTextColor(Color.parseColor("#ff424242"));
        btnService.setTextColor(Color.parseColor("#a90003"));
        btnMarket.setTextColor(Color.parseColor("#ff424242"));
        btnMe.setTextColor(Color.parseColor("#ff424242"));
    }

    //点击: 商城
    private void clickMarket() {
        btnMsg.setTextColor(Color.parseColor("#ff424242"));
        btnService.setTextColor(Color.parseColor("#ff424242"));
        btnMarket.setTextColor(Color.parseColor("#a90003"));
        btnMe.setTextColor(Color.parseColor("#ff424242"));
    }

    //点击: 我
    private void clickMe() {
        btnMsg.setTextColor(Color.parseColor("#ff424242"));
        btnService.setTextColor(Color.parseColor("#ff424242"));
        btnMarket.setTextColor(Color.parseColor("#ff424242"));
        btnMe.setTextColor(Color.parseColor("#a90003"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
