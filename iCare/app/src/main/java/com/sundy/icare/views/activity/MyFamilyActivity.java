package com.sundy.icare.views.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.adapters.MyFamilyAdapter;
import com.sundy.icare.utils.ActivityController;

/**
 * Created by sundy on 16/1/19.
 */
public class MyFamilyActivity extends BaseActivity {

    private final String TAG = "MyFamilyActivity";
    private ListView lv_MyFamily;
    private GridView gv_MyFamily;
    private MyFamilyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_family);
        ActivityController.addActivity(this);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.reset_password);
        aq.id(R.id.txtRight).text(R.string.finish).clicked(onClick);
        aq.id(R.id.btnBack).clicked(onClick);

        lv_MyFamily = aq.id(R.id.lv_MyFamily).getListView();
        gv_MyFamily = aq.id(R.id.gv_MyFamily).getGridView();

        adapter = new MyFamilyAdapter(this);

        lv_MyFamily.setAdapter(adapter);
        gv_MyFamily.setAdapter(adapter);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.txtRight:
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

