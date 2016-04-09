package com.sundy.icare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.sundy.icare.R;

/**
 * Created by sundy on 16/1/18.
 */
public class ContactsActivity extends BaseActivity {

    private final String TAG = "ContactsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.contacts);
        aq.id(R.id.txtRight).gone();
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.btnBindFamily).clicked(onClick);


    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.btnBindFamily:
                    bindFamily();
                    break;
            }
        }
    };

    private void bindFamily() {
        Intent intent = new Intent(this, BindFamilyActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
