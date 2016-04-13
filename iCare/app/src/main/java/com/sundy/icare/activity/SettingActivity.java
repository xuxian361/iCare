package com.sundy.icare.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.MyPreference;

import org.json.JSONObject;

/**
 * Created by sundy on 16/1/5.
 */
public class SettingActivity extends BaseActivity {

    private final String TAG = "SettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.txtTitle).text(R.string.settings);
        aq.id(R.id.btnLogout).clicked(onClick);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyPreference.isLogin(this)) {
            aq.id(R.id.btnLogout).visible();
        } else {
            aq.id(R.id.btnLogout).gone();
        }
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.btnLogout:
                    logout();
                    break;
            }
        }
    };

    //登出
    private void logout() {
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
        String memberId = preferences.getString(MyPreference.Preference_User_ID, "");
        String sessionKey = preferences.getString(MyPreference.Preference_User_sessionKey, "");
        showLoading(this);
        ResourceTaker.logout(memberId, sessionKey, new HttpCallback<JSONObject>(this) {
            @Override
            public void callback(String url, JSONObject data, String status) {
                super.callback(url, data, status);
                closeLoading();
                try {
                    if (data != null) {
                        JSONObject result = data.getJSONObject("result");
                        if (result != null) {
                            String code = result.getString("code");
                            String message = result.getString("message");
                            if (code.equals("1000")) {
                                MyPreference.clearUserInfo(SettingActivity.this);
                                finish();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}