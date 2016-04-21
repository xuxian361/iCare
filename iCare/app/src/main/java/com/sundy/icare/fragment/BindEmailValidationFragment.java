package com.sundy.icare.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.MyToast;

import org.json.JSONObject;

/**
 * Created by sundy on 16/4/15.
 */
public class BindEmailValidationFragment extends LazyLoadFragment {

    private final String TAG = "BindEmailValidationFragment";
    private LayoutInflater inflater;
    private View root;
    private String email;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        root = inflater.inflate(R.layout.fragment_validate_email, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.bind_email);
        aq.id(R.id.txtRight).text(R.string.finish).clicked(onClick);

        aq.id(R.id.btnBack).clicked(onClick);

        email = getArguments().getString("Email");
        aq.id(R.id.txtEmail).text(email);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
                case R.id.txtRight:
                    bindEmail();
                    break;
            }
        }
    };

    //绑定邮箱
    private void bindEmail() {
        String code = aq.id(R.id.edtCode).getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            MyToast.rtToast(context, getString(R.string.verify_code_cannot_empty));
            return;
        }

        ResourceTaker.bingMailBox(email, code, new HttpCallback<JSONObject>(context) {
            @Override
            public void callback(String url, JSONObject data, String status) {
                super.callback(url, data, status);
                try {
                    if (data != null) {
                        JSONObject result = data.getJSONObject("result");
                        if (result != null) {
                            String code = result.getString("code");
                            String message = result.getString("message");
                            if (code.equals("1000")) {
                                context.setResult(Activity.RESULT_OK);
                                context.finish();
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

}
