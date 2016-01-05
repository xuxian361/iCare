package com.sundy.icare.views.fragment.server;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.views.activity.ServerActivity;

/**
 * Created by sundy on 15/12/26.
 */
public class ServerTabMenuFragment extends Fragment {

    private final String TAG = "ServerTabMenuFragment";
    private View mView;
    private AQuery aq;

    private Button btnMsg;
    private Button btnService;
    private Button btnMe;

    public ServerTabMenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "---------->onCreateView");
        mView = inflater.inflate(R.layout.tab_menu_server, container, false);
        aq = new AQuery(mView);

        init();

        return mView;
    }

    private void init() {
        btnMsg = aq.id(R.id.btnMsg).getButton();
        btnService = aq.id(R.id.btnService).getButton();
        btnMe = aq.id(R.id.btnMe).getButton();
        aq.id(R.id.btnMsg).clicked(onClick);
        aq.id(R.id.btnService).clicked(onClick);
        aq.id(R.id.btnMe).clicked(onClick);
    }

    public void setPosition(int position) {
        MyUtils.rtLog(TAG, "--------->position =" + position);
        switch (position) {
            case 0:
                clickMsg();
                break;
            case 1:
                clickService();
                break;
            case 2:
                clickMe();
                break;
        }
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnMsg:
                    clickMsg();
                    break;
                case R.id.btnService:
                    clickService();
                    break;
                case R.id.btnMe:
                    clickMe();
                    break;
            }
            ((ServerActivity) getActivity()).switchFragment(view.getId());
        }
    };

    //点击: 消息
    private void clickMsg() {
        btnMsg.setTextColor(Color.parseColor("#a90003"));
        btnService.setTextColor(Color.parseColor("#ff424242"));
        btnMe.setTextColor(Color.parseColor("#ff424242"));
    }

    //点击: 最新服务
    private void clickService() {
        btnMsg.setTextColor(Color.parseColor("#ff424242"));
        btnService.setTextColor(Color.parseColor("#a90003"));
        btnMe.setTextColor(Color.parseColor("#ff424242"));
    }

    //点击: 我
    private void clickMe() {
        btnMsg.setTextColor(Color.parseColor("#ff424242"));
        btnService.setTextColor(Color.parseColor("#ff424242"));
        btnMe.setTextColor(Color.parseColor("#a90003"));
    }


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