package com.sundy.icare.activity;

import android.os.Bundle;

import com.sundy.icare.R;
import com.sundy.icare.fragment.BindEmailFragment;

/**
 * Created by sundy on 15/12/20.
 */
public class BindEmailActivity extends BaseActivity {

    private final String TAG = "BindEmailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email);

        initBindEmailFragment();
    }

    private void initBindEmailFragment() {
        switchContent(new BindEmailFragment());
    }

}
