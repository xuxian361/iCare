package com.sundy.icare.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.adapters.MyFamilyAdapter;
import com.sundy.icare.utils.ActivityController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sundy on 16/1/19.
 */
public class MyFamilyActivity extends BaseActivity {

    private final String TAG = "MyFamilyActivity";
    private ListView lv_MyFamily;
    private GridView gv_MyFamily;
    private MyFamilyAdapter adapter;
    private List list = new ArrayList<>();
    private final int TYPE_LIST = 1;
    private final int TYPE_GRID = 2;
    private int showType = TYPE_LIST;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_family);
        ActivityController.addActivity(this);

        aq = new AQuery(this);

        init();
    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.my_family);
        aq.id(R.id.btnRight).image(R.mipmap.icon_list).clicked(onClick);
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.btn_AddFamily).clicked(onClick);

        lv_MyFamily = aq.id(R.id.lv_MyFamily).getListView();
        gv_MyFamily = aq.id(R.id.gv_MyFamily).getGridView();

        adapter = new MyFamilyAdapter(this);

        lv_MyFamily.setAdapter(adapter);
        gv_MyFamily.setAdapter(adapter);

        lv_MyFamily.setOnItemClickListener(onItemClickListener);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            goMyFamilyDetail();
        }
    };

    private void goMyFamilyDetail() {
        Intent intent = new Intent(this, MyFamilyDetailActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.btnRight:
                    if (showType == TYPE_LIST) {
                        lv_MyFamily.setVisibility(View.GONE);
                        gv_MyFamily.setVisibility(View.VISIBLE);
                        adapter.setShowType(TYPE_GRID);
                        aq.id(R.id.btnRight).image(R.mipmap.icon_grid);
                        showType = TYPE_GRID;
                    } else {
                        lv_MyFamily.setVisibility(View.VISIBLE);
                        gv_MyFamily.setVisibility(View.GONE);
                        adapter.setShowType(TYPE_LIST);
                        aq.id(R.id.btnRight).image(R.mipmap.icon_list);
                        showType = TYPE_LIST;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.btn_AddFamily:
                    addFamily();
                    break;
            }
        }
    };

    private void addFamily() {
        Intent intent = new Intent(this, BindFamilyActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}

