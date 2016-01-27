package com.sundy.icare.views.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.ActivityController;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONObject;

/**
 * Created by sundy on 16/1/17.
 */
public class RegisterPasswordActivity extends BaseActivity {

    private final String TAG = "RegisterPasswordActivity";
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_set_password);
        ActivityController.addActivity(this);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.register);
        aq.id(R.id.txtRight).text(R.string.finish).clicked(onClick);

        aq.id(R.id.btnBack).clicked(onClick);

        edtPassword = aq.id(R.id.edtPassword).getEditText();

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.txtRight:
                    goRegister();
                    break;
            }
        }
    };

    private void goRegister() {
        String password = edtPassword.getText().toString().trim();
//        if (TextUtils.isEmpty(password)) {
//            MyToast.rtToast(this, getString(R.string.login_password_cannot_empty));
//            return;
//        }

        SharedPreferences preferences = getSharedPreferences(MyConstant.APP_NAME, MODE_PRIVATE);
        String username = preferences.getString(MyPreference.PREFERENCE_USERNAME, "");
        String mobile = preferences.getString(MyPreference.PREFERENCE_MOBILE, "");


        ResourceTaker.register(username, mobile, password, new HttpCallback<JSONObject>(this) {
            @Override
            public void callback(String url, JSONObject result, String status) {
                super.callback(url, result, status);
                MyUtils.rtLog(TAG, "--------->result =" + result);

            }
        });

        go2Main();
    }

    private void go2Main() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        ActivityController.finishAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}