package com.sundy.icare.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.ui.MyProgressDialog;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 16/4/16.
 */
public class AccountSecurityFragment extends LazyLoadFragment {
    private final String TAG = "AccountSecurityFragment";
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
        root = inflater.inflate(R.layout.fragment_account_security, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.txtTitle).text(R.string.account_security);
        aq.id(R.id.rel_ChangePassword).clicked(onClick);

        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
        String phone = preferences.getString(MyPreference.Preference_User_phone, "");
        String email = preferences.getString(MyPreference.Preference_User_email, "");

        aq.id(R.id.txt_phone).text(phone);
        aq.id(R.id.txt_email).text(email);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rel_ChangePassword:
                    goChangePassword();
                    break;
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
            }
        }
    };

    //跳转修改密码
    private void goChangePassword() {
        mCallback.addContent(new ChangePasswordFragment());
    }
}
