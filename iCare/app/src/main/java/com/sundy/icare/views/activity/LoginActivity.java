package com.sundy.icare.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.ActivityController;

import org.json.JSONObject;

/**
 * Created by sundy on 15/12/6.
 */
public class LoginActivity extends BaseActivity {

    private final String TAG = "LoginActivity";
    private boolean isRememberPwd = false;
    private String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ActivityController.addActivity(this);
        aq = new AQuery(this);
        init();
    }

    private void init() {
        aq.id(R.id.txt_register).clicked(onClickListener);
        aq.id(R.id.btn_login).clicked(onClickListener);
        aq.id(R.id.txt_first_visit).clicked(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.txt_register:
                    goRegister();
                    break;
                case R.id.btn_login:
                    goMain();
                    break;
                case R.id.txt_first_visit:
                    goMain();
                    break;
            }
        }
    };

    private void goRegister() {
        Intent intent = new Intent(this, RegisterUserNameActivity.class);
        startActivity(intent);
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (compoundButton.getId()) {

            }
        }
    };

    private void login() {
        try {
            EditText edt_username = aq.id(R.id.edt_username).getEditText();
            final EditText edt_password = aq.id(R.id.edt_password).getEditText();
            String terminalscode = edt_username.getText().toString();
            password = edt_password.getText().toString();
            if (!"".equals(terminalscode) && !"".equals(password)) {
                ResourceTaker.login(terminalscode, password, new HttpCallback<JSONObject>(this) {
                    @Override
                    public void callback(String url, JSONObject result, String status) {
                        super.callback(url, result, status);
                        try {
                            if (result != null) {
                                String resultStr = result.getString("result");
                                if ("0".equals(resultStr)) {
                                    //login success
                                    goMain();
                                } else {
                                    //login fail
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

}

