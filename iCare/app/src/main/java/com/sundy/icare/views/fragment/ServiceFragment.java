package com.sundy.icare.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.ActivityController;
import com.sundy.icare.views.activity.AddOrderActivity;
import com.sundy.icare.views.activity.MyOrderActivity;
import com.sundy.icare.views.activity.ServerActivity;

/**
 * Created by sundy on 15/12/6.
 */
public class ServiceFragment extends BaseFragment {

    private final String TAG = "ServiceFragment";
    private View mView;

    public ServiceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.fragment_service, container, false);
        aq = new AQuery(mView);

        init();

        return mView;
    }

    private void init() {
        aq.id(R.id.relLeft).clicked(onClick);
        aq.id(R.id.btnRight).clicked(onClick);
        aq.id(R.id.btnSwitch).clicked(onClick);
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
                case R.id.btnRight:
                    Intent intent2 = new Intent(getActivity(), AddOrderActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.btnSwitch:
                    //切换到服务者
                    Intent intent3 = new Intent(getActivity(), ServerActivity.class);
                    startActivity(intent3);
                    ActivityController.finishAll();
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
        super.onDestroy();
    }

}