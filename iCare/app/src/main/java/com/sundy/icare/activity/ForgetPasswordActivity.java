package com.sundy.icare.activity;

import android.os.Bundle;

import com.sundy.icare.R;
import com.sundy.icare.fragment.ForgetPwd_MobileFragment;

/**
 * Created by sundy on 16/4/12.
 */
public class ForgetPasswordActivity extends BaseActivity {

    private final String TAG = "ForgetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //默认第一次装载 ForgetPwd_MobileFragment
        switchContent(new ForgetPwd_MobileFragment());
    }

}
