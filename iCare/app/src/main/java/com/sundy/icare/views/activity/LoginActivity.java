package com.sundy.icare.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sundy on 15/12/6.
 */
public class LoginActivity extends BaseActivity {

    private final String TAG = "LoginActivity";
    private boolean isRememberPwd = false;
    private CheckBox cb_remember_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "---------->onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        aq = new AQuery(this);
        init();
    }

    private void init() {
        aq.id(R.id.txt_register).clicked(onClickListener);
        aq.id(R.id.btn_login).clicked(onClickListener);
        aq.id(R.id.txt_first_visit).clicked(onClickListener);
        aq.id(R.id.txt_forgetpwd).clicked(onClickListener);
        aq.id(R.id.edt_username).getEditText().addTextChangedListener(textWatcher);

        cb_remember_pwd = aq.id(R.id.cb_remember_pwd).getCheckBox();
        cb_remember_pwd.setOnCheckedChangeListener(checkedChangeListener);
        String rememberPwdUserName = MyPreference.getRememberPwdUserName(context);
        String rememberPwd = MyPreference.getRememberPwd(context);
        if (!TextUtils.isEmpty(rememberPwdUserName)) {
            if (!TextUtils.isEmpty(rememberPwd)) {
                cb_remember_pwd.setChecked(true);
            } else {
                cb_remember_pwd.setChecked(false);
            }
            aq.id(R.id.edt_username).getEditText().setText(rememberPwdUserName);
            aq.id(R.id.edt_password).getEditText().setText(rememberPwd);
        }

    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (compoundButton.getId()) {
                case R.id.cb_remember_pwd:
                    if (b) {
                        isRememberPwd = true;
                    } else {
                        isRememberPwd = false;
                    }
                    break;
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().length() == 0) {
                aq.id(R.id.btn_login).background(R.drawable.corner_bg_gray_light);
            } else {
                aq.id(R.id.btn_login).background(R.drawable.corner_btn_green);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.txt_register:
                    goRegister();
                    break;
                case R.id.btn_login:
                    login();
//                    showLoginChoiceDialog();

//                    EMChatManager.getInstance().login("15088086691_icare", "124578", new EMCallBack() {
//                        @Override
//                        public void onSuccess() {
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    EMGroupManager.getInstance().loadAllGroups();
//                                    EMChatManager.getInstance().loadAllConversations();
//                                    MyUtils.rtLog(TAG, "----------->登陆聊天服务器成功!");
//                                    //保存登陆用户信息
//                                    showLoginChoiceDialog();
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onError(int i, String s) {
//                            MyUtils.rtLog(TAG, "----------->登陆聊天服务器失败!");
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    MyToast.rtToast(LoginActivity.this, getString(R.string.login_fail));
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onProgress(int i, String s) {
//
//                        }
//                    });

                    break;
                case R.id.txt_first_visit:
                    goMain();
                    break;
                case R.id.txt_forgetpwd:
                    goForgetPwd();
                    break;
            }
        }
    };

    //登陆
    private void login() {
        try {
            EditText edt_username = aq.id(R.id.edt_username).getEditText();
            EditText edt_password = aq.id(R.id.edt_password).getEditText();

            final String username = edt_username.getText().toString();
            final String password = edt_password.getText().toString();

            if (TextUtils.isEmpty(username)) {
                MyToast.rtToast(this, getString(R.string.login_username_cannot_empty));
                return;
            }
            if (TextUtils.isEmpty(password)) {
                MyToast.rtToast(this, getString(R.string.login_password_cannot_empty));
                return;
            }

//            ResourceTaker.login(username, password, new HttpCallback<JSONObject>(this) {
//                @Override
//                public void callback(String url, JSONObject result, String status) {
//                    super.callback(url, result, status);
//                    try {
//                        if (result != null) {
//                            String code = MyJsonParser.getResp_Code(result);
//                            String msg = MyJsonParser.getResp_Msg(result);
//                            if (code.equals("0000")) {
//                                JSONObject detail = MyJsonParser.getResp_Detail(result);
//                                if (detail != null) {
//                                    if (isRememberPwd)
//                                        MyPreference.setRememberPWD(LoginActivity.this, username, password);
//                                    else
//                                        MyPreference.setRememberPWD(LoginActivity.this, username, "");
//                                    //Login to 环信
//                                    login2HuanXin(detail);
//                                } else {
//                                    MyToast.rtToast(LoginActivity.this, msg);
//                                }
//                            } else {
//                                MyToast.rtToast(LoginActivity.this, msg);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    //跳转主页（子女）
    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //忘记密码
    private void goForgetPwd() {
        Intent intent = new Intent(this, ForgetPwd_MobileActivity.class);
        startActivity(intent);
    }

    //注册
    private void goRegister() {
        Intent intent = new Intent(this, RegisterUserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        MyUtils.rtLog(TAG, "---------->onDestroy");
        super.onDestroy();
    }

}

