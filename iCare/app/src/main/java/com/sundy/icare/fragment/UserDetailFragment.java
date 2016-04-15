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
import com.sundy.icare.activity.QRScannerActivity;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.ui.MyProgressDialog;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONObject;

/**
 * Created by sundy on 16/4/15.
 */
public class UserDetailFragment extends LazyLoadFragment {

    private final String TAG = "UserDetailFragment";
    private LayoutInflater inflater;
    private View root;

    private SimpleDraweeView imgHeader;
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
        root = inflater.inflate(R.layout.fragment_user_detail, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.user_detail);
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.btnQR).clicked(onClick);
        imgHeader = (SimpleDraweeView) aq.id(R.id.imgHeader).getView();

        getMemberProfile();

    }

    //获取用户详情
    private void getMemberProfile() {
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
        String userId = preferences.getString(MyPreference.Preference_User_ID, "");
        String sessionKey = preferences.getString(MyPreference.Preference_User_sessionKey, "");

        showLoading();
        ResourceTaker.getMemberProfile(userId, sessionKey, userId, new HttpCallback<JSONObject>(context) {
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
                                JSONObject info = data.getJSONObject("info");
                                if (info != null) {
                                    updateUserInfo(info);
                                    showView(info);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //显示详情
    private void showView(JSONObject userInfo) {
        try {
            String user_id = userInfo.has("id") ? userInfo.getString("id") : "";
            String areaCode = userInfo.has("areaCode") ? userInfo.getString("areaCode") : "";
            String phone = userInfo.has("phone") ? userInfo.getString("phone") : "";
            String name = userInfo.has("name") ? userInfo.getString("name") : "";
            String profileImage = userInfo.has("profileImage") ? userInfo.getString("profileImage") : "";
            String email = userInfo.has("email") ? userInfo.getString("email") : "";
            String address = userInfo.has("address") ? userInfo.getString("address") : "";
            String label = userInfo.has("label") ? userInfo.getString("label") : "";


            imgHeader.setImageURI(Uri.parse(profileImage));
            aq.id(R.id.txtUsername).text(name);
            aq.id(R.id.txtGender).text(name);
            aq.id(R.id.txtMobile).text(phone);
            if (email.length() == 0) {
                aq.id(R.id.txtEmail).getTextView().setHint(getString(R.string.not_yet_bind_email));
            } else {
                aq.id(R.id.txtEmail).text(email);
            }
            aq.id(R.id.txtAddress).text(address);
            if (label.length() != 0)
                aq.id(R.id.edtTag).text(label);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新用户信息
    private void updateUserInfo(JSONObject detail) {
        MyPreference.updateUserInfo(context, detail);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
                case R.id.btnQR:
                    scanQRCode();
                    break;
            }
        }
    };

    private void scanQRCode() {
        Intent intent = new Intent(context, QRScannerActivity.class);
        startActivity(intent);
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
