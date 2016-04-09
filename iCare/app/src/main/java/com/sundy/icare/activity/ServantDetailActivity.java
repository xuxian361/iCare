package com.sundy.icare.activity;

import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.sundy.icare.R;

/**
 * //查看服务者详情
 * Created by sundy on 16/1/29.
 */
public class ServantDetailActivity extends BaseActivity {

    private final String TAG = "ServantDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_servant_detail);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.txtTitle).text(R.string.servant_detail);
        aq.id(R.id.btnRight).clicked(onClick);
        aq.id(R.id.relative_More).clicked(onClick);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.btnRight:
                    int visibility = aq.id(R.id.relative_More).getView().getVisibility();
                    if (visibility == View.GONE) {
                        aq.id(R.id.relative_More).visible();
                    } else {
                        aq.id(R.id.relative_More).gone();
                    }
                    break;
                case R.id.relative_More:
                    aq.id(R.id.relative_More).gone();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}