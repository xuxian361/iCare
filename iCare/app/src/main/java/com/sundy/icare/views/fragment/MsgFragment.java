package com.sundy.icare.views.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 15/12/6.
 */
public class MsgFragment extends LazyLoadFragment {

    private final String TAG = "MsgFragment";
    private View mView;

    Handler handler = new Handler();
    ProgressBar progressBar;
    private static final int DELAY_TIME = 2000;

    public MsgFragment() {
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "---------->initViews");
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.fragment_msg, container, false);
        aq = new AQuery(mView);

        init();
        return mView;
    }

    private void init() {
        progressBar = aq.id(R.id.progress_bar).getProgressBar();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        MyUtils.rtLog(TAG, "---------->initData");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, DELAY_TIME);
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
