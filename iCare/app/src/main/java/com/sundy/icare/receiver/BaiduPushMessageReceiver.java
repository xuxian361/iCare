package com.sundy.icare.receiver;

import android.content.Context;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.sundy.icare.utils.MyUtils;

import java.util.List;

/**
 * Created by sundy on 16/1/26.
 */
public class BaiduPushMessageReceiver extends PushMessageReceiver {

    private final String TAG = "BaiduPushMessageReceiver";

    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {

    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {
        MyUtils.rtLog(TAG, "------------>onMessage s = " + s + "; s1 = " + s1);
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {

    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {

    }
}
