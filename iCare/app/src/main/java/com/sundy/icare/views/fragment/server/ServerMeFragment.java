package com.sundy.icare.views.fragment.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.ActivityController;
import com.sundy.icare.views.activity.MainActivity;
import com.sundy.icare.views.activity.UserDetailActivity;
import com.sundy.icare.views.fragment.BaseFragment;
import com.sundy.icare.views.fragment.SettingsFragment;

/**
 * Created by sundy on 15/12/27.
 */
public class ServerMeFragment extends BaseFragment {

    private final String TAG = "ServerMeFragment";
    private View mView;

    public ServerMeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.server_me, container, false);
        aq = new AQuery(mView);

        init();

        return mView;
    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.me);
        aq.id(R.id.btnRight).image(R.mipmap.icon_settings).clicked(onClick);
        aq.id(R.id.imgMe).clicked(onClick);
        aq.id(R.id.btnSwitch).clicked(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnRight:
                    mCallback.addContent(new SettingsFragment());
                    break;
                case R.id.imgMe:
                    Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnSwitch:
                    //切花至子女端
                    Intent intent3 = new Intent(getActivity(), MainActivity.class);
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
