package com.sundy.icare.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.adapters.ServiceListAdapter;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.activity.AddOrderActivity;
import com.sundy.icare.activity.MyOrderActivity;
import com.sundy.icare.activity.ServantDetailActivity;

/**
 * Created by sundy on 15/12/6.
 */
public class ServiceFragment extends LazyLoadFragment {

    private final String TAG = "ServiceFragment";
    private View mView;

    Handler handler = new Handler();
    ProgressBar progressBar;
    private static final int DELAY_TIME = 2000;

    private ListView lv_Data;
    private ServiceListAdapter adapter;

    public ServiceFragment() {
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "---------->initViews");
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.fragment_service, container, false);
        aq = new AQuery(mView);

        init();
        return mView;
    }

    @Override
    protected void initData() {
        MyUtils.rtLog(TAG, "---------->initData");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, DELAY_TIME);
    }

    private void init() {
        aq.id(R.id.relLeft).clicked(onClick);
        aq.id(R.id.btnRight).clicked(onClick);
        aq.id(R.id.btnSwitch).clicked(onClick);
        aq.id(R.id.btnAddOrder).clicked(onClick);
        aq.id(R.id.relative_More).clicked(onClick);
        progressBar = aq.id(R.id.progress_bar).getProgressBar();
        progressBar.setVisibility(View.VISIBLE);

        lv_Data = aq.id(R.id.lv_Data).getListView();
        adapter = new ServiceListAdapter(getActivity());
        lv_Data.setAdapter(adapter);
        lv_Data.setOnItemClickListener(onItemClickListener);


    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //查看服务者相关信息
            goServerDetail();
        }
    };

    private void goServerDetail() {
        Intent intent = new Intent(getActivity(), ServantDetailActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.relLeft:
                    //跳转到我的订单
                    Intent intent1 = new Intent(getActivity(), MyOrderActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.btnAddOrder:
                    //跳转到添加订单页面
                    Intent intent2 = new Intent(getActivity(), AddOrderActivity.class);
                    startActivity(intent2);
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        MyUtils.rtLog(TAG, "---------->onDestroy");
        super.onDestroy();
    }

}