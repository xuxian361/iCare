package com.sundy.icare.views.fragment.server;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.views.activity.MainActivity;
import com.sundy.icare.views.activity.SettingActivity;
import com.sundy.icare.views.activity.UserDetailActivity;
import com.sundy.icare.views.fragment.LazyLoadFragment;

/**
 * Created by sundy on 15/12/27.
 */
public class ServerMeFragment extends LazyLoadFragment {

    private final String TAG = "ServerMeFragment";
    private View mView;

    Handler handler = new Handler();
    ProgressBar progressBar;
    private static final int DELAY_TIME = 2000;


    public ServerMeFragment() {
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "---------->initViews");
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.server_me, container, false);
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
        aq.id(R.id.txtTitle).text(R.string.me);
        aq.id(R.id.btnRight).image(R.mipmap.icon_settings).clicked(onClick);
        aq.id(R.id.imgMe).clicked(onClick);
        aq.id(R.id.btnSwitch).clicked(onClick);

        progressBar = aq.id(R.id.progress_bar).getProgressBar();
        progressBar.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnRight:
                    Intent intent1 = new Intent(getActivity(), SettingActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.imgMe:
                    Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnSwitch:
                    //切花至子女端
                    Intent intent3 = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent3);
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
