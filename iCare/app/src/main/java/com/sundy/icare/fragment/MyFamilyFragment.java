package com.sundy.icare.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.activity.MyFamilyDetailActivity;
import com.sundy.icare.adapters.MyFamilyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sundy on 16/4/17.
 */
public class MyFamilyFragment extends LazyLoadFragment {
    private final String TAG = "MyFamilyFragment";
    private View root;
    private ListView lv_MyFamily;
    private MyFamilyAdapter adapter;
    private List list = new ArrayList<>();

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        root = mInflater.inflate(R.layout.fragment_my_family, container, false);
        aq = new AQuery(root);

        init();

        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.my_family);
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.rel_AddFamily).clicked(onClick);

        lv_MyFamily = aq.id(R.id.lv_MyFamily).getListView();
        adapter = new MyFamilyAdapter(context);
        lv_MyFamily.setAdapter(adapter);
        lv_MyFamily.setOnItemClickListener(onItemClickListener);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            goMyFamilyDetail();
        }
    };

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
                case R.id.rel_AddFamily:
                    addFamily();
                    break;
            }
        }
    };

    //跳转绑定家人页面
    private void addFamily() {
        mCallback.addContent(new AddFamilyFragment());
    }

    //跳转家人详情页
    private void goMyFamilyDetail() {
        Intent intent = new Intent(context, MyFamilyDetailActivity.class);
        startActivity(intent);
    }

}
