package com.sundy.icare.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sundy.icare.utils.MyUtils;

/**
 * NetWork 管理类
 * Created by sundy on 16/1/17.
 */
public class NetWorkUtils {

    private final static String TAG = "NetWorkUtils";

    public static Boolean isNetAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo etherInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

            if ((wifiInfo != null && wifiInfo.isConnected()) ||
                    (mobileInfo != null && mobileInfo.isConnected()) ||
                    (etherInfo != null && etherInfo.isConnected())) {
                MyUtils.rtLog(TAG, "------->网络已连接");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyUtils.rtLog(TAG, "------->网络已断开");
        return false;
    }

}
