package com.sundy.icare.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.sundy.icare.R;
import com.sundy.icare.ui.MyProgressDialog;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sundy on 16/4/17.
 */
public class ChooseAddressFragment extends LazyLoadFragment {

    private final String TAG = "ChooseAddressFragment";
    private View root;
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();

    private MyProgressDialog progressDialog;

    public void showLoading() {
        MyUtils.rtLog(TAG, "-------->showLoading");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = new MyProgressDialog(context, context.getWindow().getDecorView());
        progressDialog.show();
    }

    public void closeLoading() {
        MyUtils.rtLog(TAG, "-------->closeLoading");
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        root = mInflater.inflate(R.layout.fragment_choose_address, container, false);
        aq = new AQuery(root);

        init();

        return root;
    }

    @Override
    protected void initData() {

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.address);
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.txtRight).clicked(onClick);
        aq.id(R.id.rel_Province).clicked(onClick);
        aq.id(R.id.rel_City).clicked(onClick);
        aq.id(R.id.rel_Region).clicked(onClick);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    mCallback.onBack();
                    break;
                case R.id.txtRight:
                    getAddress();
                    break;
                case R.id.rel_Province:
                    chooseProvinceDialog();
                    break;
                case R.id.rel_City:

                    break;
                case R.id.rel_Region:

                    break;
            }
        }
    };

    //选择省份Dialog
    private void chooseProvinceDialog() {
        View view = mInflater.inflate(R.layout.dialog_choose_listview, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);
        ImageButton btn_close = (ImageButton) view.findViewById(R.id.btn_close);
        TextView tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        tv_dialog_title.setText(getString(R.string.choose_province));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.listView);
        List<Map<String, String>> nameList = new ArrayList<Map<String, String>>();
        for (int m = 0; m < 20; m++) {
            Map<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("name", "name =" + m);
            nameList.add(nameMap);
        }
        SimpleAdapter adapter = new SimpleAdapter(context,
                nameList, R.layout.item_textview,
                new String[]{"name"},
                new int[]{R.id.txt_name});
        listView.setAdapter(adapter);
        dialog.show();
    }

    //获取地址
    private void getAddress() {
        String street = aq.id(R.id.edtAddress).getText().toString().trim();
        if (TextUtils.isEmpty(street)) {
            MyToast.rtToast(context, getString(R.string.street_cannot_empty));
            return;
        }


    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MyUtils.rtLog(TAG, "------->Lat = " + ll.latitudeE6);
            MyUtils.rtLog(TAG, "------->Lng = " + ll.longitudeE6);
            MyUtils.rtLog(TAG, "---------------------");
            MyUtils.rtLog(TAG, "------->Lat = " + ll.latitudeE6);
            MyUtils.rtLog(TAG, "------->Lng = " + ll.longitudeE6);
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();

    }
}
