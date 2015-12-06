package com.sundy.icare.views.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 15/12/6.
 */
public class BaseFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "BaseFragment";
    protected OnBaseListener mCallback;
    protected FragmentActivity context;
    protected LayoutInflater inflater;
    protected AQuery aq;

    public BaseFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnBaseListener) (context = (FragmentActivity) activity);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aq = new AQuery(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // Container Activity must implement this interface
    public interface OnBaseListener {

        public void switchContent(Fragment fragment);

        public void addContent(Fragment fragment);

        public void onBack();

        public void reloadActivity();

        public void switchContent(int rid);

    }

    @Override
    public void onClick(View view) {
        MyUtils.rtLog(TAG, "---------->onClick");
        if (mCallback == null)
            return;
        switch (view.getId()) {

        }
    }

}
