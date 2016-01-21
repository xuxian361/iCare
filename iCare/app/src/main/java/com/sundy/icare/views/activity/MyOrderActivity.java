package com.sundy.icare.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.adapters.MyOrderListAdapter;
import com.sundy.icare.utils.ActivityController;

/**
 * Created by sundy on 15/12/24.
 */
public class MyOrderActivity extends BaseActivity {

    private final String TAG = "MyOrderActivity";
    private ListView lv_Data;
    private MyOrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order);
        ActivityController.addActivity(this);

        aq = new AQuery(this);
        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.my_order);
        aq.id(R.id.btnBack).clicked(onClick);

        lv_Data = aq.id(R.id.lv_Data).getListView();
        adapter = new MyOrderListAdapter(this);
        lv_Data.setAdapter(adapter);

        lv_Data.setOnItemClickListener(onItemClickListener);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            goOrderDetail(); //跳转至订单详情页
        }
    };

    private void goOrderDetail() {
        Intent intent = new Intent(this, MyOrderDetailActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
