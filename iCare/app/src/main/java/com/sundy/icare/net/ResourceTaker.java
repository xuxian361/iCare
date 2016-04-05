package com.sundy.icare.net;

import com.sundy.icare.MyApp;
import com.sundy.icare.utils.MyConstant;
import com.sundy.icare.utils.MyPreference;

import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Created by sundy on 15/12/6.
 */
public class ResourceTaker {

    private static final String TAG = "ResourceTaker";

    //用户登陆
    public static void login(String areaCode, String phone, String password, HttpCallback callback) {
        Hashtable htParameters = new Hashtable();
        htParameters.put("areaCode", areaCode);
        htParameters.put("phone", phone);
        htParameters.put("password", password);
        getHttpRequest(MyURL.URL_LOGIN, htParameters, JSONObject.class, callback);
    }

    //用户注册
    public static void register(String areaCode, String phone, String smsType,
                                String name, String gender, String smsCode,
                                String password, String profileImage, HttpCallback callback) {
        Hashtable htParameters = new Hashtable();
        htParameters.put("areaCode", areaCode);
        htParameters.put("phone", phone);
        htParameters.put("smsType", smsType);
        htParameters.put("name", name);
        htParameters.put("gender", gender);
        htParameters.put("smsCode", smsCode);
        htParameters.put("password", password);
        htParameters.put("profileImage", profileImage);
        getHttpRequest(MyURL.URL_REGISTRATION, htParameters, JSONObject.class, callback);
    }

    //用户登陆
    public static void sendSMSCode(String areaCode, String phone, String smsType, HttpCallback callback) {
        Hashtable htParameters = new Hashtable();
        htParameters.put("areaCode", areaCode);
        htParameters.put("phone", phone);
        htParameters.put("smsType", smsType);
        getHttpRequest(MyURL.URL_SEND_SMS_CODE, htParameters, JSONObject.class, callback);
    }


    public static void getHttpRequest(String url, Hashtable htParameters, Class stype, HttpCallback callback) {
        getCommonParameter(htParameters);
        callback.httpGet(url, htParameters, stype);
    }

    //公共参数
    public static Hashtable getCommonParameter(Hashtable htParameters) {
        if (htParameters == null)
            htParameters = new Hashtable();
        htParameters.put("userType", MyConstant.USER_TYPE);
        htParameters.put("version", MyConstant.APP_VERSION);
        htParameters.put("system", MyConstant.SYSTEM_TYPE);
        htParameters.put("deviceCode", MyPreference.getUUID(MyApp.getInstance()));
        htParameters.put("language", MyConstant.APP_LANGUAGE);
        return htParameters;
    }

}
