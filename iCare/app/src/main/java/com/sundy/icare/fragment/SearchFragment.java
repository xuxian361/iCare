package com.sundy.icare.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.sundy.icare.R;

/**
 * Created by sundy on 16/4/19.
 */
public class SearchFragment extends LazyLoadFragment {
    private final String TAG = "SearchFragment";
    private View root;
    private String searchTag;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        root = mInflater.inflate(R.layout.fragment_search, container, false);
        aq = new AQuery(root);

        init();

        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.edtSearch).getEditText().addTextChangedListener(textWatcher);
        aq.id(R.id.relative_item).clicked(onClick);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
                case R.id.relative_item:
                    goSearchResult();
                    break;
            }
        }
    };

    //跳转搜索结果界面
    private void goSearchResult() {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag", searchTag);
        fragment.setArguments(bundle);
        mCallback.addContent(fragment);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String str = editable.toString();
            searchTag = str;
            if (str != null) {
                if (str.length() == 0) {
                    aq.id(R.id.relative_item).gone();
                } else {
                    aq.id(R.id.relative_item).visible();
                    aq.id(R.id.txtSearch).text(str);
                }
            }
        }
    };


}
