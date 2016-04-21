package com.sundy.icare.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 15/12/25.
 */
public class MarketFragment extends LazyLoadFragment {
    private final String TAG = "MarketFragment";
    private View mView;

    Handler handler = new Handler();
    private static final int DELAY_TIME = 2000;

    public MarketFragment() {
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "---------->initViews");
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.fragment_market, container, false);
        aq = new AQuery(mView);

        init();
        return mView;
    }

    @Override
    protected void initData() {
        MyUtils.rtLog(TAG, "---------->initData");
    }

    private void init() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        MyUtils.rtLog(TAG, "---------->onDestroy");
        super.onDestroy();
    }

}
