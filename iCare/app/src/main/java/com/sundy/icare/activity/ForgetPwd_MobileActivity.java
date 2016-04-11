package com.sundy.icare.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
public class ForgetPwd_MobileActivity extends BaseActivity {

    private final String TAG = "ForgetPwd_MobileActivity";
    private EditText edtMobile;
    private EditText edtCode;
    private final String AREA_CODE = "86";
    private final int TIME = 60;
    private int times = TIME;
    private Button btnGetCode;
    private final int SMS_CODE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SMS_CODE:
                    times--;
                    btnGetCode.setText(times + "秒");
                    if (times == 0) {
                        times = TIME;
                        setCodeEnable();
                    } else {
                        Message message = new Message();
                        message.what = SMS_CODE;
                        if (mHandler != null)
                            mHandler.sendMessageDelayed(message, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_mobile);

        aq = new AQuery(this);
        init();
    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.register);
        aq.id(R.id.txtRight).text(R.string.next_step).clicked(onClick);
        aq.id(R.id.btnBack).clicked(onClick);
        btnGetCode = aq.id(R.id.btnGetCode).getButton();
        aq.id(R.id.btnGetCode).clicked(onClick);
        edtMobile = aq.id(R.id.edtMobile).getEditText();
        edtCode = aq.id(R.id.edtCode).getEditText();

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.txtRight:
                    verifyMobile();
                    break;
                case R.id.btnGetCode:
                    getVerifyCode();
                    break;
            }
        }
    };

    //设置获取验证码可用
    private void setCodeEnable() {
        btnGetCode.setEnabled(true);
        btnGetCode.setBackgroundResource(R.drawable.corner_btn_green);
        btnGetCode.setText(R.string.re_send_verify_code);
    }

    //设置获取验证码不可用
    private void setCodeDisable() {
        btnGetCode.setEnabled(false);
        btnGetCode.setBackgroundResource(R.drawable.corner_btn_gray);
    }

    //获取手机验证码
    private void getVerifyCode() {
        String mobile = edtMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            MyToast.rtToast(this, getString(R.string.mobile_cannot_empty));
            return;
        }

        ResourceTaker.sendSMSCode(AREA_CODE, mobile, "forgetPassword", new HttpCallback<JSONObject>(this) {
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
                                times = TIME;
                                setCodeDisable();
                                mHandler.sendEmptyMessage(SMS_CODE);
                            } else if (code.equals("3000")) {
                                times = TIME;
                                setCodeEnable();
                                MyToast.rtToast(ForgetPwd_MobileActivity.this, message);
                            } else {
                                times = TIME;
                                setCodeEnable();
                                MyToast.rtToast(ForgetPwd_MobileActivity.this, message);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //验证手机号码
    private void verifyMobile() {
        final String mobile = edtMobile.getText().toString().trim();
        String code = edtCode.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            MyToast.rtToast(this, getString(R.string.mobile_cannot_empty));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            MyToast.rtToast(this, getString(R.string.verify_code_cannot_empty));
            return;
        }

        ResourceTaker.checkSmsCode(AREA_CODE, mobile, "forgetPassword", code, new HttpCallback<JSONObject>(this) {
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
                                if (data.has("info")) {
                                    JSONObject info = data.getJSONObject("info");
                                    if (info != null) {
                                        if (info.has("is_effective")) {
                                            boolean isEffective = info.getBoolean("is_effective");
                                            if (isEffective) {
                                                go2ForgetPwdResetPassword(AREA_CODE, mobile);
                                            } else {
                                                MyToast.rtToast(ForgetPwd_MobileActivity.this, getString(R.string.verify_code_is_wrong));
                                            }
                                        }
                                    }
                                }
                            } else {
                                MyToast.rtToast(ForgetPwd_MobileActivity.this, message);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void go2ForgetPwdResetPassword(String area_code, String phone) {
        Intent intent = new Intent(this, ForgetPwd_PasswordActivity.class);
        intent.putExtra("area_code", area_code);
        intent.putExtra("phone", phone);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mHandler != null) {
                mHandler = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

