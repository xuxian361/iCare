package com.sundy.icare.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.google.zxing.client.android.MyQRCapture;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyUtils;

/**
 * Created by sundy on 15/12/6.
 */
public class QRScannerActivity extends BaseActivity {

    private String TAG = "QRScannerActivity";
    private MyQRCapture qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtils.rtLog(TAG, "----------->onCreate");
        setContentView(R.layout.qr_scanner);

        aq = new AQuery(this);
        init();
    }

    private void init() {
        aq.id(R.id.btnBack).clicked(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
            }
        }
    };

    private void openQRcode() {
        if (qr == null) {
            qr = new MyQRCapture(QRScannerActivity.this);
            qr.setQRCallBack(callBack);
        }
        qr.onResume();
    }

    //扫描后得到的结果
    private MyQRCapture.QRCallBack callBack = new MyQRCapture.QRCallBack() {
        @Override
        public void handleDecode(String strData) {
            qr.onPause();
            Intent intent = new Intent();
            intent.putExtra("data", strData);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    @Override
    protected void onResume() {
        MyUtils.rtLog(TAG, "----------->onResume");
        super.onResume();
        openQRcode();
    }

    @Override
    protected void onPause() {
        MyUtils.rtLog(TAG, "----------->onPause");
        super.onPause();
        if (qr != null) {
            qr.onPause();
        }
    }

    @Override
    protected void onStop() {
        MyUtils.rtLog(TAG, "----------->onStop");
        super.onStop();
        if (qr != null) {
            qr.onDestroy();
            qr = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        MyUtils.rtLog(TAG, "---------->onConfigurationChanged");
    }

    @Override
    public void onDestroy() {
        MyUtils.rtLog(TAG, "----------->onDestroy");
        super.onDestroy();
        if (qr != null) {
            qr.onDestroy();
            qr = null;
        }
    }
}
