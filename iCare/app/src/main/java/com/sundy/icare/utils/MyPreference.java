package com.sundy.icare.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sundy on 16/1/17.
 */
public class MyPreference {

    //-------------------------注册------------------------
    public static final String Preference_Registration = "registration";
    public static final String Preference_Registration_username = "username";
    public static final String Preference_Registration_areaCode = "areaCode";
    public static final String Preference_Registration_phone = "phone";
    public static final String Preference_Registration_gender = "gender";
    public static final String Preference_Registration_profileImage = "profileImage";
    //-------------------------用户信息------------------------
    public static final String Preference_User = "user";
    public static final String Preference_User_ID = "id";   //用户ID
    public static final String Preference_User_name = "name";   //用户名
    public static final String Preference_User_areaCode = "areaCode";   //手机区号
    public static final String Preference_User_phone = "phone";   //手机号
    public static final String Preference_User_profileImage = "profileImage";   //用户头像
    public static final String Preference_User_sessionKey = "sessionKey";   //sessionKey
    public static final String Preference_User_easemobAccount = "easemobAccount";   //环信账号
    public static final String Preference_User_easemobPassword = "easemobPassword";   //环信密码
    public static final String Preference_User_isServiceProvider = "isServiceProvider";   //是否是服务提供者
    public static final String Preference_User_AutoLogin = "AutoLogin";   //是否保持登陆状态
    //-------------------------APP------------------------
    public static final String UUID_STR = "UUID";       //UUID


    //Save UUID
    public static void saveUDID(Context context) {
        String udid = android.provider.Settings.System.getString(
                context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(UUID_STR, udid);
        editor.commit();
    }

    //Get UUID
    public static String getUUID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyConstant.APP_NAME, Context.MODE_PRIVATE);
        String uuid = preferences.getString(UUID_STR, "");
        return uuid;
    }

    //Save Token
    public static void saveToken(String token, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Preference_User, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Preference_User_sessionKey, token);
        editor.commit();
    }

    //Get Token
    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Preference_User, Context.MODE_PRIVATE);
        return preferences.getString(Preference_User_sessionKey, "");
    }

    //Save User Login Info
    public static void saveUserInfo(Context context, String user_id, String name,
                                    String areaCode, String phone, String profileImage,
                                    String sessionKey, String easemobAccount, String easemobPassword, boolean isServiceProvider) {
        SharedPreferences preferences = context.getSharedPreferences(Preference_User, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Preference_User_ID, user_id);
        editor.putString(Preference_User_name, name);
        editor.putString(Preference_User_areaCode, areaCode);
        editor.putString(Preference_User_phone, phone);
        editor.putString(Preference_User_profileImage, profileImage);
        editor.putString(Preference_User_sessionKey, sessionKey);
        editor.putString(Preference_User_easemobAccount, easemobAccount);
        editor.putString(Preference_User_easemobPassword, easemobPassword);
        editor.putBoolean(Preference_User_isServiceProvider, isServiceProvider);
        editor.commit();
    }

    //清除保存的用户信息
    public static void clearUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Preference_User, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


    //判断是否登陆
    public static boolean isLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Preference_User, Context.MODE_PRIVATE);
        String sessionid = preferences.getString(Preference_User_sessionKey, "");
        if (!"".equals(sessionid))
            return true;
        return false;
    }

    //设置自动登陆
    public static void saveAutoLogin(Context context, boolean AutoLogin) {
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(MyPreference.Preference_User_AutoLogin, AutoLogin);
        editor.commit();
    }

    //获取》记住密码
    public static Boolean getAutoLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
        return preferences.getBoolean(Preference_User_AutoLogin, false);
    }

    //获取》用户名
    public static String getPhone(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPreference.Preference_User, Context.MODE_PRIVATE);
        return preferences.getString(Preference_User_phone, "");
    }

}
