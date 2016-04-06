package com.sundy.icare.net;

import com.sundy.icare.MyApp;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyPreference;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.ByteArrayBody;
import internal.org.apache.http.entity.mime.content.StringBody;

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
    public static void register(String areaCode, String phone, String smsType,
                                String name, String gender, String smsCode,
                                String password, byte[] icon, HttpCallback callback) {
        MultipartEntity multipart = new MultipartEntity();
        try {
            multipart.addPart("areaCode", new StringBody(areaCode, Charset.forName("UTF-8")));
            multipart.addPart("phone", new StringBody(phone, Charset.forName("UTF-8")));
            multipart.addPart("smsType", new StringBody(smsType, Charset.forName("UTF-8")));
            multipart.addPart("name", new StringBody(name, Charset.forName("UTF-8")));
            multipart.addPart("gender", new StringBody(gender, Charset.forName("UTF-8")));
            multipart.addPart("smsCode", new StringBody(smsCode, Charset.forName("UTF-8")));
            multipart.addPart("password", new StringBody(password, Charset.forName("UTF-8")));
            multipart.addPart("profileImage", new ByteArrayBody(icon, MyConstant.APP_NAME + "_profile" + "_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getHttpRequestPostWithFile(MyURL.URL_REGISTRATION, multipart, JSONObject.class, callback);
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

    //Post 带文件请求
    public static void getHttpRequestPostWithFile(String url, MultipartEntity multipart, Class stype, HttpCallback callback) {
        multipart = getCommonParameterWithFile(multipart);
        callback.doFilePost(url, multipart, stype);
    }

    //公共参数带文件
    private static MultipartEntity getCommonParameterWithFile(MultipartEntity multipart) {
        if (multipart == null)
            multipart = new MultipartEntity();
        try {
            multipart.addPart("userType", new StringBody(MyConstant.USER_TYPE, Charset.forName("UTF-8")));
            multipart.addPart("version", new StringBody(MyConstant.APP_VERSION, Charset.forName("UTF-8")));
            multipart.addPart("system", new StringBody(MyConstant.SYSTEM_TYPE, Charset.forName("UTF-8")));
            multipart.addPart("deviceCode", new StringBody(MyPreference.getUUID(MyApp.getInstance()), Charset.forName("UTF-8")));
            multipart.addPart("language", new StringBody(MyConstant.APP_LANGUAGE, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return multipart;
    }

    //公共参数不带文件
    public static HashMap getCommonParameter(HashMap hashMap) {
        if (hashMap == null)
            hashMap = new HashMap();
        hashMap.put("userType", MyConstant.USER_TYPE);
        hashMap.put("version", MyConstant.APP_VERSION);
        hashMap.put("system", MyConstant.SYSTEM_TYPE);
        hashMap.put("deviceCode", MyPreference.getUUID(MyApp.getInstance()));
        hashMap.put("language", MyConstant.APP_LANGUAGE);
        return hashMap;
    }
}
