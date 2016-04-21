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
import com.sundy.icare.adapters.NotificationAdapter;
import com.sundy.icare.net.HttpCallback;
import com.sundy.icare.net.ResourceTaker;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sundy on 16/4/20.
 */
public class NotificationFragment extends LazyLoadFragment {

    private final String TAG = "NotificationFragment";
    private View root;

    private List listData = new ArrayList();
    private SwipeRefreshLayout layout_refresh;
    private RecyclerView rv_Notification;
    private NotificationAdapter adapter;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        root = mInflater.inflate(R.layout.fragment_notification, container, false);
        aq = new AQuery(root);

        init();

        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.txtTitle).text(getString(R.string.notification));
        initRefreshView();

        rv_Notification = (RecyclerView) aq.id(R.id.rv_Notification).getView();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv_Notification.setLayoutManager(layoutManager);
        rv_Notification.setItemAnimator(new DefaultItemAnimator());
        adapter = new NotificationAdapter(context);
        rv_Notification.setAdapter(adapter);
        adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyUtils.rtLog(TAG, "------->onItemClick =" + position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                MyUtils.rtLog(TAG, "------->onItemLongClick =" + position);
            }
        });
        getNotifications();

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
                listData.clear();
                getNotifications();
            }
        });
        layout_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    //获取通知数据
    private void getNotifications() {
        ResourceTaker.applyHistory(1, 10, new HttpCallback<JSONObject>(context) {
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
                                JSONObject info = data.getJSONObject("info");
                                if (info != null) {
                                    JSONArray list = info.getJSONArray("list");
                                    if (list != null && list.length() != 0) {
                                        for (int i = 0; i < list.length(); i++) {
                                            JSONObject item = (JSONObject) list.get(i);
                                            if (item != null) {
                                                listData.add(item);
                                            }
                                        }
                                    }
                                    adapter.setData(listData);
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                MyToast.rtToast(context, message);
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
