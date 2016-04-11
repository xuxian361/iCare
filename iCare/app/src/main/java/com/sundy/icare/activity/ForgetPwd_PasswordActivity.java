package com.sundy.icare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.MyToast;

import org.json.JSONObject;

/**
 * Created by sundy on 16/1/18.
 */
public class ForgetPwd_PasswordActivity extends BaseActivity {

    private final String TAG = "ForgetPwd_PasswordActivity";
    private EditText edtPassword, edtConfirmPassword;
    private String area_code, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_set_password);

        aq = new AQuery(this);
        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.reset_password);
        aq.id(R.id.txtRight).text(R.string.finish).clicked(onClick);
        aq.id(R.id.btnBack).clicked(onClick);
        edtPassword = aq.id(R.id.edtPassword).getEditText();
        edtConfirmPassword = aq.id(R.id.edtConfirmPassword).getEditText();

        area_code = getIntent().getStringExtra("area_code");
        phone = getIntent().getStringExtra("phone");
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.txtRight:
                    resetPwd();
                    break;
            }
        }
    };

    private void resetPwd() {
        String password = edtPassword.getText().toString().trim();
        String confirmPwd = edtConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            MyToast.rtToast(this, getString(R.string.login_password_cannot_empty));
            return;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            MyToast.rtToast(this, getString(R.string.confirmpassword_cannot_empty));
            return;
        }
        if (!confirmPwd.equals(password)) {
            MyToast.rtToast(this, getString(R.string.confirmpwd_not_equal_password));
            return;
        }

        ResourceTaker.forgetPassword(area_code, phone, password, confirmPwd, new HttpCallback<JSONObject>(this) {
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
                                go2Login();
                            } else {
                                MyToast.rtToast(ForgetPwd_PasswordActivity.this, message);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void go2Login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
