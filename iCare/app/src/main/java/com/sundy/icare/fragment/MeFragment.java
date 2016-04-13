package com.sundy.icare.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.activity.MyFamilyActivity;
import com.sundy.icare.activity.QRScannerActivity;
import com.sundy.icare.activity.SettingActivity;
import com.sundy.icare.activity.UserDetailActivity;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 15/12/20.
 */
public class MeFragment extends LazyLoadFragment {

    private final String TAG = "MeFragment";
    private View mView;

    private ProgressBar progressBar;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "---------->initViews");
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.fragment_me, container, false);
        aq = new AQuery(mView);

        init();

        return mView;
    }

    @Override
    protected void initData() {
        MyUtils.rtLog(TAG, "---------->initData");

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.me);
        aq.id(R.id.imgHeader).clicked(onClick);
        aq.id(R.id.btnSwitch).clicked(onClick);
        aq.id(R.id.btnQR).clicked(onClick);
        aq.id(R.id.rel_MyFamily).clicked(onClick);
        aq.id(R.id.rel_setting).clicked(onClick);

        progressBar = aq.id(R.id.progress_bar).getProgressBar();
        progressBar.setVisibility(View.GONE);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgHeader:
                    goUserDetail();
                    break;
                case R.id.btnQR:
                    scanQRCode();
                    break;
                case R.id.rel_MyFamily:
                    goMyFamily();
                    break;
                case R.id.rel_setting:
                    goSettings();
                    break;
            }
        }
    };

    //跳转用户详情页面
    private void goUserDetail() {
        Intent intent2 = new Intent(getActivity(), UserDetailActivity.class);
        startActivity(intent2);
    }

    //跳转设置页面
    private void goSettings() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //跳转我的家人页面
    private void goMyFamily() {
        Intent intent = new Intent(getActivity(), MyFamilyActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //跳转扫描二维码页面
    private void scanQRCode() {
        Intent intent = new Intent(getActivity(), QRScannerActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onDestroy() {
        MyUtils.rtLog(TAG, "---------->onDestroy");
        super.onDestroy();
    }
}
