package com.sundy.icare.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.adapters.ChooseServantListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sundy on 16/1/29.
 */
public class ChooseServantActivity extends BaseActivity {

    private final String TAG = "ChooseServantActivity";
    private ListView lv_Data;
    private List list = new ArrayList();
    private ChooseServantListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_servant);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.txtTitle).text(R.string.choose_servant);

        lv_Data = aq.id(R.id.lv_Data).getListView();
        adapter = new ChooseServantListAdapter(this);
        lv_Data.setAdapter(adapter);
        lv_Data.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

