package com.sundy.icare.views.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyToast;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by sundy on 15/12/20.
 */
public class RegisterMobileActivity extends BaseActivity {

    private final String TAG = "RegisterMobileActivity";
    private EditText edtMobile;
    private EditText edtCode;
    private final String AREA_CODE = "86";

    private final int MSG_MOB = 1000;
    private final int MSG_Timer = 10001;

    private int times = 60;
    private boolean isStart = false;
    private Button btnGetCode;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                if (msg.what == MSG_MOB) {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码，验证成功
                            MyToast.rtToast(RegisterMobileActivity.this, getString(R.string.verify_success));
                            goRegister();
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码
                            MyToast.rtToast(RegisterMobileActivity.this, getString(R.string.verify_code_already_send));
                        }
                    } else {
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                MyToast.rtToast(RegisterMobileActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }
                } else if (msg.what == MSG_Timer) {
                    if (isStart) {
                        if (times == 0) {
                            isStart = false;
                            changeColor();
                        } else {
                            mHandler.sendEmptyMessageDelayed(MSG_Timer, 1000);
                            times--;
                            if (times < 10)
                                btnGetCode.setText(getString(R.string.re_send) + " (0" + times + ")");
                            else
                                btnGetCode.setText(getString(R.string.re_send) + " (" + times + ")");
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_mobile);

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


        //Mob 短信验证码 Init
        SMSSDK.initSDK(this, MyConstant.Mob_API_Key, MyConstant.Mob_APP_Secret);
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = MSG_MOB;
                mHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);

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

    //重发按钮状态更变
    private void changeColor() {
        if (isStart) {
            btnGetCode.setEnabled(false);
            btnGetCode.setBackgroundResource(R.drawable.corner_btn_gray);
        } else {
            btnGetCode.setEnabled(true);
            btnGetCode.setBackgroundResource(R.drawable.corner_btn_green);
            btnGetCode.setText(R.string.re_send_verify_code);
        }
    }

    //获取手机验证码
    private void getVerifyCode() {
        String mobile = edtMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            MyToast.rtToast(this, getString(R.string.mobile_cannot_empty));
            return;
        }
        SMSSDK.getVerificationCode(AREA_CODE, mobile);
        times = 60;
        isStart = true;
        changeColor();
        mHandler.sendEmptyMessageDelayed(MSG_Timer, 1000);
    }

    //验证手机号码
    private void verifyMobile() {
        String mobile = edtMobile.getText().toString().trim();
        String code = edtCode.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            MyToast.rtToast(this, getString(R.string.mobile_cannot_empty));
            return;
        }

        if (TextUtils.isEmpty(code)) {
            MyToast.rtToast(this, getString(R.string.verify_code_cannot_empty));
            return;
        }

        SMSSDK.submitVerificationCode(AREA_CODE, mobile, code);

    }

    //注册
    private void goRegister() {
        String mobile = edtMobile.getText().toString().trim();
        SharedPreferences preferences = getSharedPreferences(MyConstant.APP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MyPreference.PREFERENCE_MOBILE, mobile);
        editor.commit();

        Intent intent = new Intent(this, RegisterPasswordActivity.class);
        startActivity(intent);
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
        SMSSDK.unregisterAllEventHandler();
    }
}

