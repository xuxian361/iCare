package com.sundy.icare.views.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.ActivityController;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyPreference;

/**
 * Created by sundy on 15/12/20.
 */
public class RegisterMobileActivity extends BaseActivity {

    private final String TAG = "RegisterMobileActivity";
    private EditText edtMobile;
    private EditText edtCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_mobile);
        ActivityController.addActivity(this);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.register);
        aq.id(R.id.txtRight).text(R.string.next_step).clicked(onClick);
        aq.id(R.id.btnBack).clicked(onClick);

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
                    goRegister();
                    break;
            }
        }
    };

    private void goRegister() {
        String mobile = edtMobile.getText().toString().trim();
        String code = edtCode.getText().toString().trim();

//        if (TextUtils.isEmpty(mobile)) {
//            MyToast.rtToast(this, getString(R.string.mobile_cannot_empty));
//            return;
//        }
//
//        if (TextUtils.isEmpty(code)) {
//            MyToast.rtToast(this, getString(R.string.verify_code_cannot_empty));
//            return;
//        }

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
        ActivityController.removeActivity(this);
    }
}

