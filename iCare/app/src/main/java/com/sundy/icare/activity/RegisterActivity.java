package com.sundy.icare.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sundy.icare.R;
import com.sundy.icare.fragment.RegisterUserInfoFragment;
import com.sundy.icare.utils.MyPreference;

/**
 * Created by sundy on 16/4/12.
 */
public class RegisterActivity extends BaseActivity {

    private final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        clearRegisterInfo();
        //默认第一次装载 RegisterUserInfoFragment
        switchContent(new RegisterUserInfoFragment());
    }

    //清除注册信息
    private void clearRegisterInfo() {
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_Registration, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
