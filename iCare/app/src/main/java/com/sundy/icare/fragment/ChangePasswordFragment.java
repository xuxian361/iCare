package com.sundy.icare.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.ui.MyProgressDialog;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONObject;

/**
 * Created by sundy on 16/4/16.
 */
public class ChangePasswordFragment extends LazyLoadFragment {

    private final String TAG = "ChangePasswordFragment";
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
        root = inflater.inflate(R.layout.fragment_change_password, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.btn_back).clicked(onClick);
        aq.id(R.id.txtTitle).text(R.string.change_password);
        aq.id(R.id.txtRight).text(R.string.ok).clicked(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.txtRight:
                    changePassword();
                    break;
                case R.id.btn_back:
                    mCallback.onBack();
                    break;
            }
        }
    };

    //修改密码
    private void changePassword() {
        String oldPwd = aq.id(R.id.edtOldPassword).getText().toString().trim();
        String newPwd = aq.id(R.id.edtNewPassword).getText().toString().trim();
        String confirmPwd = aq.id(R.id.edtConfirmPassword).getText().toString().trim();

        if (TextUtils.isEmpty(oldPwd)) {
            MyToast.rtToast(context, getString(R.string.old_password_cannot_empty));
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            MyToast.rtToast(context, getString(R.string.new_password_cannot_empty));
            return;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            MyToast.rtToast(context, getString(R.string.confirmpassword_cannot_empty));
            return;
        }
        if (oldPwd.equals(newPwd)) {
            MyToast.rtToast(context, getString(R.string.new_password_cannot_be_equal_old_password));
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            MyToast.rtToast(context, getString(R.string.confirm_password_not_equal_new_password));
            return;
        }
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
        String memberId = preferences.getString(MyPreference.Preference_User_ID, "");
        String sessionKey = preferences.getString(MyPreference.Preference_User_sessionKey, "");

        showLoading();
        ResourceTaker.changePassword(memberId, sessionKey, oldPwd, newPwd, confirmPwd, new HttpCallback<JSONObject>(context) {
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
                                MyToast.rtToast(context, getString(R.string.change_password_success));
                                mCallback.onBack();
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


}
