package com.sundy.icare.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sundy.icare.R;
import com.sundy.icare.activity.LoginActivity;
import com.sundy.icare.activity.QRScannerActivity;
import com.sundy.icare.entity.MsgEvent;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;


/**
 * Created by sundy on 15/12/20.
 */
public class MeFragment extends LazyLoadFragment {

    private final String TAG = "MeFragment";
    private View mView;

    private SimpleDraweeView imgHeader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        MyUtils.rtLog(TAG, "-------->onResume");
        super.onResume();
        refreshView();
    }

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

    //刷新界面内容
    private void refreshView() {
        if (MyPreference.isLogin(getActivity())) { //已登录
            MyUtils.rtLog(TAG, "---------->已登录");
            SharedPreferences preferences = getActivity().getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
            String img = preferences.getString(MyPreference.Preference_Registration_profileImage, "");
            if (img.length() != 0)
                imgHeader.setImageURI(Uri.parse(img));
            String userName = preferences.getString(MyPreference.Preference_User_name, "");
            aq.id(R.id.txtName).text(userName);

            getMyAccount();

        } else {//未登陆
            MyUtils.rtLog(TAG, "---------->未登陆");
            imgHeader.setImageURI(null);
            aq.id(R.id.txtName).text(getString(R.string.please_login));

        }
    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.me);
        imgHeader = (SimpleDraweeView) aq.id(R.id.imgHeader).getView();
        aq.id(R.id.imgHeader).clicked(onClick);
        aq.id(R.id.btnSwitch).clicked(onClick);
        aq.id(R.id.btnQR).clicked(onClick);
        aq.id(R.id.rel_MyFamily).clicked(onClick);
        aq.id(R.id.rel_setting).clicked(onClick);
        aq.id(R.id.btnNotify).clicked(onClick);

    }

    //我的账号
    private void getMyAccount() {
        ResourceTaker.getMyAccount(new HttpCallback<JSONObject>(context) {
            @Override
            public void callback(String url, JSONObject data, String status) {
                super.callback(url, data, status);
                try {
                    if (data != null) {
                        JSONObject result = data.getJSONObject("result");
                        if (result != null) {
                            String code = result.getString("code");
                            String message = result.getString("message");
                            if (code.equals("1000")) {

                            } else {
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgHeader:
                    if (MyPreference.isLogin(getActivity())) {
                        goUserDetail();
                    } else {
                        goLogin();
                    }
                    break;
                case R.id.btnQR:
                    scanQRCode();
                    break;
                case R.id.rel_MyFamily:
                    if (MyPreference.isLogin(getActivity())) {
                        goMyFamily();
                    } else {
                        goLogin();
                    }
                    break;
                case R.id.rel_setting:
                    goSettings();
                    break;
                case R.id.btnNotify:
                    if (MyPreference.isLogin(getContext())) {
                        goNotification();
                    } else {
                        goLogin();
                    }
                    break;
            }
        }
    };

    //跳转通知页面
    private void goNotification() {
        mCallback.addContent(new NotificationFragment());
    }

    private void goLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //跳转用户详情页面
    private void goUserDetail() {
        mCallback.addContent(new UserDetailFragment());
    }

    //跳转设置页面
    private void goSettings() {
        mCallback.addContent(new SettingsFragment());
    }

    //跳转我的家人页面
    private void goMyFamily() {
        mCallback.addContent(new MyFamilyFragment());
    }

    //跳转扫描二维码页面
    private void scanQRCode() {
        Intent intent = new Intent(getActivity(), QRScannerActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * @param event
     */
    public void onEventMainThread(MsgEvent event) {
        MyUtils.rtLog(TAG, "------>onEventMainThread =" + event.getMsg());
        if (event != null) {
            String msg = event.getMsg();
            switch (msg) {
                case "Need_Refresh":
                    refreshView();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        MyUtils.rtLog(TAG, "---------->onDestroy");
        super.onDestroy();
        EventBus.getDefault().unregister(getActivity());
    }
}
