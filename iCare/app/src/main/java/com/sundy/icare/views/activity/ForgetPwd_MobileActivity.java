package com.sundy.icare.views.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
    private final int MSG_Timer = 10001;

    private int times = 10;
    private Button btnGetCode;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_Timer:
                    if (times == 0) {
                        changeButtonState(true);
                    } else {
                        mHandler.sendEmptyMessageDelayed(MSG_Timer, 1000);
                        times--;
                        if (times < 10)
                            btnGetCode.setText(getString(R.string.re_send) + " (0" + times + ")");
                        else
                            btnGetCode.setText(getString(R.string.re_send) + " (" + times + ")");
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

    //重发按钮状态更变
    private void changeButtonState(boolean isEnable) {
        if (isEnable) {
            btnGetCode.setEnabled(true);
            btnGetCode.setBackgroundResource(R.drawable.corner_btn_green);
            btnGetCode.setText(R.string.re_send);
        } else {
            btnGetCode.setEnabled(false);
            btnGetCode.setBackgroundResource(R.drawable.corner_btn_gray);
        }
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
            public void callback(String url, JSONObject result, String status) {
                super.callback(url, result, status);

//                times = 10;
//                changeButtonState(false);
//                mHandler.sendEmptyMessageDelayed(MSG_Timer, 1000);

            }
        });

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

