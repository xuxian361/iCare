package com.sundy.icare.views.fragment.server;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.views.activity.ServerActivity;
import com.sundy.icare.views.fragment.BaseFragment;

/**
 * Created by sundy on 15/12/26.
 */
public class ServerTabMenuFragment extends BaseFragment {

    private final String TAG = "ServerTabMenuFragment";
    private View mView;

    public ServerTabMenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.tab_menu_server, container, false);
        aq = new AQuery(mView);

        init();

        return mView;
    }

    private void init() {
        aq.id(R.id.btnMsg).clicked(onClick);
        aq.id(R.id.btnService).clicked(onClick);
        aq.id(R.id.btnMe).clicked(onClick);
    }

    public void setPosition(int position) {
        MyUtils.rtLog(TAG, "--------->position =" + position);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((ServerActivity) getActivity()).switchFragment(view.getId());
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