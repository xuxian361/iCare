package com.sundy.icare.fragment;

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
public class BindEmailFragment extends LazyLoadFragment {
    private final String TAG = "BindEmailFragment";
    private LayoutInflater inflater;
    private View root;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        root = inflater.inflate(R.layout.fragment_bind_email, container, false);

        aq = new AQuery(root);
        init();
        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.bind_email);
        aq.id(R.id.txtRight).text(R.string.next_step).clicked(onClick);

        aq.id(R.id.btnBack).clicked(onClick);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    context.finish();
                    break;
                case R.id.txtRight:
                    sendEmailCode();
                    break;
            }
        }
    };

    //发送邮箱验证码
    private void sendEmailCode() {
        final String email = aq.id(R.id.edtEmail).getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            MyToast.rtToast(context, getString(R.string.email_cannot_be_empty));
            return;
        }
        ResourceTaker.sendEmailCode(email, new HttpCallback<JSONObject>(context) {
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
                                go2ValidateEmail(email);
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

    private void go2ValidateEmail(String email) {
        BindEmailValidationFragment fragment = new BindEmailValidationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Email", email);
        fragment.setArguments(bundle);
        mCallback.addContent(fragment);
    }
}
