package com.sundy.icare.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sundy on 15/12/6.
 */
public class MyUtils {

    private static final String TAG = "MyUtils";

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    //判断是否登陆
    public static boolean isLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        String sessionid = preferences.getString(MyPreference.SESSION_ID, "");
        if (!"".equals(sessionid))
            return true;
        return false;
    }

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

    public static String getDeviceID(Context ctx) {
        TelephonyManager tManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tManager.getDeviceId();
    }

    //MD5加密
    public static String getMD5(Context context, String string) {
        String result = "";
        try {
            char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            try {
                byte[] btInput = string.getBytes();
                // 获得MD5摘要算法的 MessageDigest 对象
                MessageDigest mdInst = MessageDigest.getInstance("MD5");
                // 使用指定的字节更新摘要
                mdInst.update(btInput);
                // 获得密文
                byte[] md = mdInst.digest();
                // 把密文转换成十六进制的字符串形式
                int j = md.length;
                char str[] = new char[j * 2];
                int k = 0;
                for (int i = 0; i < j; i++) {
                    byte byte0 = md[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                result = new String(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
