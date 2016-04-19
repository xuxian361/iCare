package com.sundy.icare.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

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
    public static final String Preference_User_email = "email";   //email
    public static final String Preference_User_address = "address";   //地址
    public static final String Preference_User_label = "label";   //标签
    public static final String Preference_User_Login_password = "loginPassword";   //标签


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

    //Save User Login Info
    public static void saveUserInfo(Context context, JSONObject userInfo) {
        try {
            String user_id = userInfo.has("id") ? userInfo.getString("id") : "";
            String areaCode = userInfo.has("areaCode") ? userInfo.getString("areaCode") : "";
            String phone = userInfo.has("phone") ? userInfo.getString("phone") : "";
            String name = userInfo.has("name") ? userInfo.getString("name") : "";
            String profileImage = userInfo.has("profileImage") ? userInfo.getString("profileImage") : "";
            String sessionKey = userInfo.has("sessionKey") ? userInfo.getString("sessionKey") : "";
            String easemobAccount = userInfo.has("easemobAccount") ? userInfo.getString("easemobAccount") : "";
            String easemobPassword = userInfo.has("easemobPassword") ? userInfo.getString("easemobPassword") : "";
            boolean isServiceProvider = userInfo.has("isServiceProvider") ? userInfo.getBoolean("isServiceProvider") : false;
            String email = userInfo.has("email") ? userInfo.getString("email") : "";
            String address = userInfo.has("address") ? userInfo.getString("address") : "";
            String label = userInfo.has("label") ? userInfo.getString("label") : "";

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
            editor.putString(Preference_User_email, email);
            editor.putString(Preference_User_address, address);
            editor.putString(Preference_User_label, label);

            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //修改用户信息
    public static void updateUserInfo(Context context, JSONObject userInfo) {
        try {
            String user_id = userInfo.has("id") ? userInfo.getString("id") : "";
            String areaCode = userInfo.has("areaCode") ? userInfo.getString("areaCode") : "";
            String phone = userInfo.has("phone") ? userInfo.getString("phone") : "";
            String name = userInfo.has("name") ? userInfo.getString("name") : "";
            String profileImage = userInfo.has("profileImage") ? userInfo.getString("profileImage") : "";
            String email = userInfo.has("email") ? userInfo.getString("email") : "";
            String address = userInfo.has("address") ? userInfo.getString("address") : "";
            String label = userInfo.has("label") ? userInfo.getString("label") : "";

            SharedPreferences preferences = context.getSharedPreferences(Preference_User, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Preference_User_ID, user_id);
            editor.putString(Preference_User_name, name);
            editor.putString(Preference_User_areaCode, areaCode);
            editor.putString(Preference_User_phone, phone);
            editor.putString(Preference_User_profileImage, profileImage);
            editor.putString(Preference_User_email, email);
            editor.putString(Preference_User_address, address);
            editor.putString(Preference_User_label, label);

            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    //保存用户登录密码
    public static void saveLoginPassword(Context context, String password) {
        SharedPreferences preferences = context.getSharedPreferences(Preference_User, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        password = EncryptionUtil.getMD5(context, password);
        editor.putString(Preference_User_Login_password, password);

        editor.commit();
    }

    //获取保存的用户登陆密码
    public static String getLoginPassword(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Preference_User, Context.MODE_PRIVATE);
        return preferences.getString(Preference_User_Login_password, "");
    }


}
