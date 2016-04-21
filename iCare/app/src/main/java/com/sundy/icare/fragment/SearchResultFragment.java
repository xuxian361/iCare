package com.sundy.icare.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.adapters.SearchResultAdapter;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.MyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sundy on 16/4/19.
 */
public class SearchResultFragment extends LazyLoadFragment {

    private final String TAG = "SearchResultFragment";
    private View root;
    private String tag;
    private List list = new ArrayList();
    private SwipeRefreshLayout layout_refresh;
    private RecyclerView rv_Search;
    private SearchResultAdapter adapter;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        root = mInflater.inflate(R.layout.fragment_search_result, container, false);
        aq = new AQuery(root);

        init();

        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.txtTitle).text(getString(R.string.search_result));
        initRefreshView();
        rv_Search = (RecyclerView) aq.id(R.id.rv_Search).getView();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv_Search.setLayoutManager(layoutManager);
        rv_Search.setItemAnimator(new DefaultItemAnimator());
        adapter = new SearchResultAdapter(context);
        rv_Search.setAdapter(adapter);

        tag = getArguments().getString("tag");
        search();
    }

    //初始化刷新控件
    private void initRefreshView() {
        layout_refresh = (SwipeRefreshLayout) aq.id(R.id.layout_refresh).getView();
        layout_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout_refresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (layout_refresh.isRefreshing()) {
                            layout_refresh.setRefreshing(false);
                        }
                    }
                }, 500);
                search();
            }
        });
        layout_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    //搜索内容
    private void search() {
        ResourceTaker.searchByPhoneOrEmail(tag, 20, new HttpCallback<JSONObject>(context) {
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
                                JSONArray info = data.getJSONArray("info");
                                if (info != null && info.length() != 0) {
                                    if (list != null)
                                        list.clear();
                                    for (int i = 0; i < info.length(); i++) {
                                        JSONObject item = (JSONObject) info.get(i);
                                        list.add(item);
                                    }
                                }
                                adapter.setData(list);
                                adapter.notifyDataSetChanged();
                            } else {
                                MyToast.rtToast(context, getString(R.string.no_result));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
            }
        }
    };

}
