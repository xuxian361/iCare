package com.sundy.icare.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.MyToast;

import org.json.JSONObject;

/**
 * Created by sundy on 16/4/12.
 */
public class ForgetPwd_PasswordFragment extends BaseFragment {

    private LayoutInflater inflater;
    private View root;

    private final String TAG = "ForgetPwd_PasswordFragment";
    private EditText edtPassword, edtConfirmPassword;
    private String area_code, phone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.root = inflater.inflate(R.layout.fragment_forget_password_reset_password, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.reset_password);
        aq.id(R.id.txtRight).text(R.string.finish).clicked(onClick);
        aq.id(R.id.btn_back).clicked(onClick);
        edtPassword = aq.id(R.id.edtPassword).getEditText();
        edtConfirmPassword = aq.id(R.id.edtConfirmPassword).getEditText();

        area_code = getArguments().getString("area_code");
        phone = getArguments().getString("phone");
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_back:
                    mCallback.onBack();
                    break;
                case R.id.txtRight:
                    resetPwd();
                    break;
            }
        }
    };

    private void resetPwd() {
        String password = edtPassword.getText().toString().trim();
        String confirmPwd = edtConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            MyToast.rtToast(context, getString(R.string.login_password_cannot_empty));
            return;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            MyToast.rtToast(context, getString(R.string.confirmpassword_cannot_empty));
            return;
        }
        if (!confirmPwd.equals(password)) {
            MyToast.rtToast(context, getString(R.string.confirmpwd_not_equal_password));
            return;
        }

        mCallback.showLoading(context);
        ResourceTaker.forgetPassword(area_code, phone, password, confirmPwd, new HttpCallback<JSONObject>(context) {
            @Override
            public void callback(String url, JSONObject data, String status) {
                super.callback(url, data, status);
                mCallback.closeLoading();
                try {
                    if (data != null) {
                        JSONObject result = data.getJSONObject("result");
                        if (result != null) {
                            String code = result.getString("code");
                            String message = result.getString("message");
                            if (code.equals("1000")) {
                                go2Login();
                            } else {
                                MyToast.rtToast(context, message);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void go2Login() {
        context.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
