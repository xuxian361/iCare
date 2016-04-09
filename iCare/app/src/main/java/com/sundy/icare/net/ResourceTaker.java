package com.sundy.icare.net;

import com.sundy.icare.MyApp;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyPreference;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by sundy on 15/12/6.
 */
public class ResourceTaker {

    private static final String TAG = "ResourceTaker";

    //发送手机验证码
    public static void sendSMSCode(String areaCode, String phone, String smsType, HttpCallback callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("areaCode", areaCode);
        hashMap.put("phone", phone);
        hashMap.put("smsType", smsType);
        getHttpRequestGet(MyURL.URL_SEND_SMS_CODE, hashMap, JSONObject.class, callback);
    }

    //检查手机验证码
    public static void checkSmsCode(String areaCode, String phone, String smsType, String smsCode,
                                    HttpCallback callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("areaCode", areaCode);
        hashMap.put("phone", phone);
        hashMap.put("smsType", smsType);
        hashMap.put("smsCode", smsCode);
        getHttpRequestGet(MyURL.URL_CHECK_SMS_CODE, hashMap, JSONObject.class, callback);
    }

    //用户注册
    public static void register(String areaCode, String phone, String name, String gender,
                                String password, File file, HttpCallback callback) {
        try {
            HashMap<String, String> stringHashMap = new HashMap<>();
            stringHashMap.put("areaCode", areaCode);
            stringHashMap.put("phone", phone);
            stringHashMap.put("name", name);
            stringHashMap.put("gender", gender);
            stringHashMap.put("password", password);
            stringHashMap = getCommonParameter(stringHashMap);
            callback.doFilePost(MyURL.URL_REGISTRATION, stringHashMap, "profileImage", file, JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //用户登陆
    public static void login(String areaCode, String phone, String password, HttpCallback callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("areaCode", areaCode);
        hashMap.put("phone", phone);
        hashMap.put("password", password);
        getHttpRequestPost(MyURL.URL_LOGIN, hashMap, JSONObject.class, callback);
    }

    //用户登出
    public static void logout(String memberId, String sessionKey, HttpCallback callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("memberId", memberId);
        hashMap.put("sessionKey", sessionKey);
        getHttpRequestGet(MyURL.URL_LOGOUT, hashMap, JSONObject.class, callback);
    }

    //忘记密码
    public static void forgetPassword(String areaCode, String phone, String smsType, String smsCode,
                                      String password, String verifyPassword, HttpCallback callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("areaCode", areaCode);
        hashMap.put("phone", phone);
        hashMap.put("smsType", smsType);
        hashMap.put("smsCode", smsCode);
        hashMap.put("password", password);
        hashMap.put("verifyPassword", verifyPassword);
        getHttpRequestPost(MyURL.URL_FORGET_PASSWORD, hashMap, JSONObject.class, callback);
    }

    //修改密码
    public static void changePassword(String memberId, String sessionKey, String oldPassword, String password,
                                      String verifyPassword, HttpCallback callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("memberId", memberId);
        hashMap.put("sessionKey", sessionKey);
        hashMap.put("oldPassword", oldPassword);
        hashMap.put("password", password);
        hashMap.put("verifyPassword", verifyPassword);
        getHttpRequestPost(MyURL.URL_CHANGE_PASSWORD, hashMap, JSONObject.class, callback);
    }

    //发送邮箱验证码
    public static void sendEmailCode(String memberId, String sessionKey, String email, HttpCallback callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("memberId", memberId);
        hashMap.put("sessionKey", sessionKey);
        hashMap.put("email", email);
        getHttpRequestGet(MyURL.URL_SEND_EMAIL_CODE, hashMap, JSONObject.class, callback);
    }

    //绑定邮箱
    public static void bingMailBox(String memberId, String sessionKey, String email, String emailCode,
                                   HttpCallback callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("memberId", memberId);
        hashMap.put("sessionKey", sessionKey);
        hashMap.put("email", email);
        hashMap.put("emailCode", emailCode);
        getHttpRequestGet(MyURL.URL_BIND_MAILBOX, hashMap, JSONObject.class, callback);
    }

    //我的账号
    public static void getMyAccount(String memberId, String sessionKey, HttpCallback callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("memberId", memberId);
        hashMap.put("sessionKey", sessionKey);
        getHttpRequestGet(MyURL.URL_GET_MY_ACCOUNT, hashMap, JSONObject.class, callback);
    }

    //用户资料
    public static void getMemberProfile(String memberId, String sessionKey, String profileId,
                                        HttpCallback callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("memberId", memberId);
        hashMap.put("sessionKey", sessionKey);
        hashMap.put("profileId", profileId);
        getHttpRequestGet(MyURL.URL_GET_MEMBER_PROFILE, hashMap, JSONObject.class, callback);
    }

    //Get 请求
    public static void getHttpRequestGet(String url, HashMap hashMap, Class stype, HttpCallback callback) {
        hashMap = getCommonParameter(hashMap);
        callback.httpGet(url, hashMap, stype);
    }

    //Post 请求
    public static void getHttpRequestPost(String url, HashMap hashMap, Class stype, HttpCallback callback) {
        hashMap = getCommonParameter(hashMap);
        callback.httpPost(url, hashMap, stype);
    }

    //公共参数不带文件
    public static HashMap getCommonParameter(HashMap<String, String> stringHashMap) {
        if (stringHashMap == null)
            stringHashMap = new HashMap();
        stringHashMap.put("userType", MyConstant.USER_TYPE);
        stringHashMap.put("version", MyConstant.APP_VERSION);
        stringHashMap.put("system", MyConstant.SYSTEM_TYPE);
        stringHashMap.put("deviceCode", MyPreference.getUUID(MyApp.getInstance()));
        stringHashMap.put("language", MyConstant.APP_LANGUAGE);
        return stringHashMap;
    }

}
