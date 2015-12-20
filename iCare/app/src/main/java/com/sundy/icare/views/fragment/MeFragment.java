package com.sundy.icare.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;

/**
 * Created by sundy on 15/12/20.
 */
public class MeFragment extends BaseFragment {

    private final String TAG = "MeFragment";
    private View mView;

    public MeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mView = mInflater.inflate(R.layout.layout_me, container, false);
        aq = new AQuery(mView);

        init();

        return mView;
    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.me);
        aq.id(R.id.btnRight).image(R.mipmap.icon_settings);
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
        super.onDestroy();
    }
}
