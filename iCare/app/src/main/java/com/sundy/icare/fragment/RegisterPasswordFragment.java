package com.sundy.icare.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.activity.MainActivity;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyToast;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by sundy on 16/4/12.
 */
public class RegisterPasswordFragment extends BaseFragment {

    private LayoutInflater inflater;
    private View root;

    private final String TAG = "RegisterPasswordFragment";
    private EditText edtPassword, edtConfirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        root = inflater.inflate(R.layout.fragment_register_set_password, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.register);
        aq.id(R.id.txtRight).text(R.string.finish).clicked(onClick);
        edtPassword = aq.id(R.id.edtPassword).getEditText();
        edtConfirmPassword = aq.id(R.id.edtConfirmPassword).getEditText();
        aq.id(R.id.btn_back).clicked(onClick);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_back:
                    mCallback.onBack();
                    break;
                case R.id.txtRight:
                    register();
                    break;
            }
        }
    };

    //注册
    private void register() {
        final String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            MyToast.rtToast(context, getString(R.string.login_password_cannot_empty));
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            MyToast.rtToast(context, getString(R.string.confirmpassword_cannot_empty));
            return;
        }
        if (!password.equals(confirmPassword)) {
            MyToast.rtToast(context, getString(R.string.confirmpwd_not_equal_password));
            return;
        }

        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_Registration, Context.MODE_PRIVATE);
        String username = preferences.getString(MyPreference.Preference_Registration_username, "");
        final String areaCode = preferences.getString(MyPreference.Preference_Registration_areaCode, "");
        final String phone = preferences.getString(MyPreference.Preference_Registration_phone, "");
        String gender = preferences.getString(MyPreference.Preference_Registration_gender, "");
        String profileImage = preferences.getString(MyPreference.Preference_Registration_profileImage, "");
        File file = null;
        if (profileImage != null && profileImage.length() != 0) {
            file = new File(profileImage);
        }
        ResourceTaker.register(areaCode, phone, username, gender, password, file,
                new HttpCallback<JSONObject>(context) {
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
                                        login(areaCode, phone, password);
                                    } else if (code.equals("5000")) {
                                        MyToast.rtToast(context, message);
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

    //登陆服务器
    private void login(String areaCode, String phone, String password) {
        ResourceTaker.login(areaCode, phone, password, new HttpCallback<JSONObject>(context) {
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
                                JSONObject info = data.getJSONObject("info");
                                if (info != null) {
                                    saveUserInfo(info);
                                    clearRegisterInfo();
                                    finishLoginPage();
                                    go2Main();
                                }
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

    //关闭登陆页
    private void finishLoginPage() {
        Intent intent = new Intent("com.sundy.icare.activity.LoginActivity");
        intent.putExtra("msg", "Register_Success");
        context.sendBroadcast(intent);
    }

    //保存登陆用户信息
    private void saveUserInfo(JSONObject detail) {
        try {
            String id = detail.getString("id");
            String areaCode = detail.getString("areaCode");
            String phone = detail.getString("phone");
            String name = detail.getString("name");
            String profileImage = detail.getString("profileImage");
            String sessionKey = detail.getString("sessionKey");
            String easemobAccount = detail.getString("easemobAccount");
            String easemobPassword = detail.getString("easemobPassword");
            boolean isServiceProvider = detail.getBoolean("isServiceProvider");
            MyPreference.saveUserInfo(context, id, name, areaCode, phone, profileImage, sessionKey,
                    easemobAccount, easemobPassword, isServiceProvider);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //清除注册信息
    private void clearRegisterInfo() {
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_Registration, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    //跳转主页
    private void go2Main() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        context.finish();
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
