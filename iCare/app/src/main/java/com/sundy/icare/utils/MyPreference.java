package com.sundy.icare.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sundy on 16/1/17.
 */
public class MyPreference {


    //User Info
    public static final String PREFERENCE_USERNAME = "PREFERENCE_USERNAME"; //用户登陆名

    public static final String PREFERENCE_MOBILE = "PREFERENCE_MOBILE";     //用户登陆手机号
    public static final String PREFERENCE_TOKEN = "PREFERENCE_TOKEN";       //用户Token
    public static final String PREFERENCE_USER_ID = "PREFERENCE_USER_ID";   //用户ID
    public static final String PREFERENCE_IM_USER_NAME = "PREFERENCE_IM_USER_NAME"; //环信用户登陆名
    public static final String PREFERENCE_IM_ENCRYPTED_PASSWORD = "PREFERENCE_IM_ENCRYPTED_PASSWORD"; //环信用户登陆密码
    public static final String PREFERENCE_REMEMBER_PASSWORD = "PREFERENCE_REMEMBER_PASSWORD"; //记住密码
    public static final String PREFERENCE_REMEMBER_USERNAME = "PREFERENCE_REMEMBER_USERNAME"; //记住密码-用户名


    public static final String UUID_STR = "UUID";       //UUID
    public static final String PREFERENCE_APP_ID = "PREFERENCE_APP_ID"; //APP_ID: 1>老人机；2>子女端；


    //Save APP ID
    public static void saveAppID(String appID, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MyPreference.PREFERENCE_APP_ID, appID);
        editor.commit();
    }

    //Get APP ID
    public static String getAppID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        return preferences.getString(MyPreference.PREFERENCE_APP_ID, "app_02");
    }

    //Save UUID
    public static void saveUDID(Context context) {
        String udid = android.provider.Settings.System.getString(
                context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MyPreference.UUID_STR, udid);
        editor.commit();
    }

    //Get UUID
    public static String getUUID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        String uuid = preferences.getString(MyPreference.UUID_STR, "");
        return uuid;
    }

    //Get APP_User_ID
    public static String getAPP_User_ID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        String mobile = preferences.getString(MyPreference.PREFERENCE_MOBILE, "");
        return mobile;
    }

    //Save Token
    public static void saveToken(String token, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MyPreference.PREFERENCE_TOKEN, token);
        editor.commit();
    }

    //Get Token
    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        return preferences.getString(MyPreference.PREFERENCE_TOKEN, "");
    }

    //Save User Login Info
    public static void saveUserInfo(Context context, String im_user_name,
                                    String im_encrypted_password, String token, String user_id) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MyPreference.PREFERENCE_USER_ID, user_id);
        editor.putString(MyPreference.PREFERENCE_TOKEN, token);
        editor.putString(MyPreference.PREFERENCE_IM_USER_NAME, im_user_name);
        editor.putString(MyPreference.PREFERENCE_IM_ENCRYPTED_PASSWORD, im_encrypted_password);
        editor.commit();
    }

    //判断是否登陆
    public static boolean isLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        String sessionid = preferences.getString(MyPreference.PREFERENCE_TOKEN, "");
        if (!"".equals(sessionid))
            return true;
        return false;
    }

    //设置》记住密码
    public static void setRememberPWD(Context context, String username, String pwd) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MyPreference.PREFERENCE_REMEMBER_USERNAME, username);
        editor.putString(MyPreference.PREFERENCE_REMEMBER_PASSWORD, pwd);
        editor.commit();
    }

    //获取》记住密码
    public static String getRememberPwd(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        return preferences.getString(MyPreference.PREFERENCE_REMEMBER_PASSWORD, "");
    }

    //获取》记住密码-用户名
    public static String getRememberPwdUserName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        return preferences.getString(MyPreference.PREFERENCE_REMEMBER_USERNAME, "");
    }

}
