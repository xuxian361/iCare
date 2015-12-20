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
public class BindEmailActivity extends BaseActivity {

    private final String TAG = "BindEmailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_email);
        ActivityController.addActivity(this);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.bind_email);
        aq.id(R.id.txtRight).text(R.string.next_step).clicked(onClick);

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
                    go2ValidateEmail();
                    break;
            }
        }
    };

    private void go2ValidateEmail() {
        Intent intent = new Intent(this, EmailValidationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
