package com.sundy.icare.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.ActivityController;

/**
 * Created by sundy on 15/12/20.
 */
public class RegisterMobileActivity extends BaseActivity {

    private final String TAG = "RegisterMobileActivity";

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
        aq.id(R.id.txtRight).text(R.string.finish).clicked(onClick);

        aq.id(R.id.btnBack).clicked(onClick);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.txtRight:
                    go2Main();
                    break;
            }
        }
    };

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

