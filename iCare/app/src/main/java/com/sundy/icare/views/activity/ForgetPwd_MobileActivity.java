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

/**
 * Created by sundy on 16/1/18.
 */
public class ForgetPwd_MobileActivity extends BaseActivity {

    private final String TAG = "ForgetPwd_MobileActivity";
    private EditText edtMobile;
    private EditText edtCode;
    private final String AREA_CODE = "86";

    private final int MSG_MOB = 1000;
    private final int MSG_Timer = 10001;

    private int times = 10;
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

    //重发按钮状态更变
    private void changeColor() {
        if (isStart) {
            btnGetCode.setEnabled(false);
            btnGetCode.setBackgroundResource(R.drawable.corner_btn_gray);
        } else {
            btnGetCode.setEnabled(true);
            btnGetCode.setBackgroundResource(R.drawable.corner_btn_green);
            btnGetCode.setText(R.string.re_send);
        }
    }

    //获取手机验证码
    private void getVerifyCode() {
        String mobile = edtMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            MyToast.rtToast(this, getString(R.string.mobile_cannot_empty));
            return;
        }
        times = 10;
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
    }

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
    }
}

