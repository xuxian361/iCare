package com.sundy.icare.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sundy on 15/12/6.
 */
public class MyUtils {

    private static final String TAG = "MyUtils";

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    //打印方法
    public static void rtLog(String tag, String msg) {
        if (MyConstant.IsDebug) {
            Log.e(tag, msg);
        }
    }

    //日期格式化
    public static String formatDate(String oldFormat, String newFormat, String dateStr) {
        String result = dateStr;
        try {
            SimpleDateFormat sdf_old = new SimpleDateFormat(oldFormat);
            SimpleDateFormat sdf_new = new SimpleDateFormat(newFormat);
            Date date = sdf_old.parse(dateStr);
            result = sdf_new.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //获取APP NAME
    public static String getAppName(Context context, int pID) {
        String processName = null;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List l = am.getRunningAppProcesses();
            Iterator i = l.iterator();
            while (i.hasNext()) {
                ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processName;
    }


}
