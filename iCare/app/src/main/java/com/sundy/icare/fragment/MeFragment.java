package com.sundy.icare.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.activity.MyFamilyActivity;
import com.sundy.icare.activity.QRScannerActivity;
import com.sundy.icare.activity.SettingActivity;
import com.sundy.icare.activity.UserDetailActivity;
import com.sundy.icare.activity.VerifyPasswordActivity;

/**
 * Created by sundy on 15/12/20.
 */
public class MeFragment extends LazyLoadFragment {

    private final String TAG = "MeFragment";
    private View mView;

    Handler handler = new Handler();
    ProgressBar progressBar;
    private static final int DELAY_TIME = 2000;

    public MeFragment() {
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "---------->initViews");
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.layout_me, container, false);
        aq = new AQuery(mView);

        init();

        return mView;
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

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.me);
        aq.id(R.id.btnRight).image(R.mipmap.icon_settings).clicked(onClick);
        aq.id(R.id.imgMe).clicked(onClick);
        aq.id(R.id.btnSwitch).clicked(onClick);
        aq.id(R.id.btnQR).clicked(onClick);
        aq.id(R.id.rel_MyFamily).clicked(onClick);
        aq.id(R.id.btn_Email).clicked(onClick);

        progressBar = aq.id(R.id.progress_bar).getProgressBar();
        progressBar.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnRight:
                    Intent intent1 = new Intent(getActivity(), SettingActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.imgMe:
                    Intent intent2 = new Intent(getActivity(), UserDetailActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.btnQR:
                    scanQRCode();
                    break;
                case R.id.rel_MyFamily:
                    goMyFamily();
                    break;
                case R.id.btn_Email:
                    bindEmail();
                    break;
            }
        }
    };

    private void bindEmail() {
        Intent intent = new Intent(getActivity(), VerifyPasswordActivity.class);
        startActivity(intent);
    }

    private void goMyFamily() {
        Intent intent = new Intent(getActivity(), MyFamilyActivity.class);
        startActivity(intent);
    }

    private void scanQRCode() {
        Intent intent = new Intent(getActivity(), QRScannerActivity.class);
        startActivity(intent);
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
