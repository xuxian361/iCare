package com.sundy.icare.views.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.ActivityController;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyPreference;
import com.sundy.icare.utils.MyToast;

/**
 * Created by sundy on 15/12/6.
 */
public class RegisterUserNameActivity extends BaseActivity {

    private final String TAG = "RegisterUserNameActivity";
    private EditText edtUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_username);
        ActivityController.addActivity(this);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.register);
        aq.id(R.id.txtRight).text(R.string.next_step).clicked(onClick);

        edtUserName = aq.id(R.id.edtUserName).getEditText();

        aq.id(R.id.btnCancel).clicked(onClick);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnCancel:
                    finish();
                    break;
                case R.id.txtRight:
                    go2RegisterMobile();
                    break;
            }
        }
    };

    private void go2RegisterMobile() {
        String username = edtUserName.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            MyToast.rtToast(this, getString(R.string.username_cannot_empty));
            return;
        }
        SharedPreferences preferences = getSharedPreferences(MyConstant.APP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MyPreference.PREFERENCE_USERNAME, username);
        editor.commit();

        Intent intent = new Intent(this, RegisterMobileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
