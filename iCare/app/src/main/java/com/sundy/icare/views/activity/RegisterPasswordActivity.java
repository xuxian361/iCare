package com.sundy.icare.views.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.MyJsonParser;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sundy on 16/1/17.
 */
public class RegisterPasswordActivity extends BaseActivity {

    private final String TAG = "RegisterPasswordActivity";
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_set_password);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.register);
        aq.id(R.id.txtRight).text(R.string.finish).clicked(onClick);

        aq.id(R.id.btnBack).clicked(onClick);

        edtPassword = aq.id(R.id.edtPassword).getEditText();

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.txtRight:
                    goRegister();
                    break;
            }
        }
    };

    //注册
    private void goRegister() {
        final String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            MyToast.rtToast(this, getString(R.string.login_password_cannot_empty));
            return;
        }

        SharedPreferences preferences = getSharedPreferences(MyConstant.APP_NAME, MODE_PRIVATE);
        String username = preferences.getString(MyPreference.PREFERENCE_USERNAME, "");
        final String mobile = preferences.getString(MyPreference.PREFERENCE_MOBILE, "");

        ResourceTaker.register(username, mobile, password, new HttpCallback<JSONObject>(this) {
            @Override
            public void callback(String url, JSONObject result, String status) {
                super.callback(url, result, status);
                try {
                    if (result != null) {
                        String code = MyJsonParser.getResp_Code(result);
                        String msg = MyJsonParser.getResp_Msg(result);
                        if (code.equals("0000")) {
                            JSONObject item = MyJsonParser.getResp_Detail(result);
                            if (item != null) {
                                boolean is_success = item.getBoolean("is_success");
                                if (is_success) {   //register success
                                    login(mobile, password);
                                } else {    //register fail
                                    MyToast.rtToast(RegisterPasswordActivity.this, msg);
                                }
                            }
                        } else {
                            MyToast.rtToast(RegisterPasswordActivity.this, msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //登陆服务器
    private void login(String mobile, String password) {
        ResourceTaker.login(mobile, password, new HttpCallback<JSONObject>(this) {
            @Override
            public void callback(String url, JSONObject result, String status) {
                super.callback(url, result, status);
                try {
                    if (result != null) {
                        String code = MyJsonParser.getResp_Code(result);
                        String msg = MyJsonParser.getResp_Msg(result);
                        if (code.equals("0000")) {
                            JSONObject detail = MyJsonParser.getResp_Detail(result);
                            if (detail != null) {
                                //Login to 环信
                                login2HuanXin(detail);
                            } else {
                                MyToast.rtToast(RegisterPasswordActivity.this, msg);
                            }
                        } else {
                            MyToast.rtToast(RegisterPasswordActivity.this, msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //登陆环信
    private void login2HuanXin(final JSONObject detail) throws JSONException {
        String im_user_name = detail.getString("im_user_name");
        String im_encrypted_password = detail.getString("im_encrypted_password");


    }

    //保存登陆用户信息
    private void saveUserInfo(JSONObject detail) {
        try {
            String im_user_name = detail.getString("im_user_name");
            String im_encrypted_password = detail.getString("im_encrypted_password");
            String token = detail.getString("token");
            String user_id = detail.getString("user_id");
            MyPreference.saveUserInfo(this, im_user_name, im_encrypted_password, token, user_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //跳转主页
    private void go2Main() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}