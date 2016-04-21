package com.sundy.icare.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.entity.MsgEvent;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.ui.MyProgressDialog;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by sundy on 16/4/16.
 */
public class SettingsFragment extends LazyLoadFragment {

    private final String TAG = "SettingsFragment";
    private LayoutInflater inflater;
    private View root;

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
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.txtTitle).text(R.string.settings);
        aq.id(R.id.btnLogout).clicked(onClick);
        aq.id(R.id.rel_AccountSecurity).clicked(onClick);

        if (MyPreference.isLogin(context)) {
            aq.id(R.id.btnLogout).visible();
        } else {
            aq.id(R.id.btnLogout).gone();
        }
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
                case R.id.btnLogout:
                    logout();
                    break;
                case R.id.rel_AccountSecurity:
                    goAccountSecurity();
                    break;
            }
        }
    };

    private void goAccountSecurity() {
        mCallback.addContent(new AccountSecurityFragment());
    }

    //登出
    private void logout() {
        showLoading();
        ResourceTaker.logout(new HttpCallback<JSONObject>(context) {
            @Override
            public void callback(String url, JSONObject data, String status) {
                super.callback(url, data, status);
                closeLoading();
                try {
                    if (data != null) {
                        JSONObject result = data.getJSONObject("result");
                        if (result != null) {
                            String code = result.getString("code");
                            String message = result.getString("message");
                            if (code.equals("1000")) {
                                MyPreference.clearUserInfo(context);
                                aq.id(R.id.btnLogout).gone();
                            } else {
                                MyToast.rtToast(context, message);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //刷新上个页面
        MsgEvent event = new MsgEvent();
        event.setMsg("Need_Refresh");
        EventBus.getDefault().post(event);
    }
}
