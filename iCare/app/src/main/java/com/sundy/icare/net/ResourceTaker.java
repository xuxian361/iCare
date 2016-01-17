package com.sundy.icare.net;

import com.sundy.icare.utils.MyConstant;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by sundy on 15/12/6.
 */
public class ResourceTaker {

    public static final String HTTP_BASE = MyURL.HTTP_DEV;


    //用户登陆API
    public static void login(String terminalscode, String password,
                             HttpCallback callback) {
        Hashtable htParameters = new Hashtable();
        htParameters.put("terminalscode", terminalscode);
        htParameters.put("password", password);
        getHttpRequestGet(MyURL.MYURL_login, htParameters, JSONObject.class, callback);
    }

    //用户注册API
    public static void register(String user_name, String mobile, String login_passwd,
                                HttpCallback callback) {
        Hashtable htParameters = new Hashtable();
        htParameters.put("user_name", user_name);
        htParameters.put("login_passwd", login_passwd);
        htParameters.put("mobile", mobile);
//        htParameters.put("user_type", user_type);

        getHttpRequestGet(MyURL.MYURL_register, htParameters, JSONObject.class, callback);
    }

    public static void getHttpRequestGet(String strFunName, Hashtable htParameters, Class stype, HttpCallback callback) {
        if (htParameters == null)
            htParameters = new Hashtable();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = df.format(new Date());
        htParameters.put("ver", MyConstant.APP_VER);
        htParameters.put("time", time);
        htParameters.put("language", MyConstant.APP_LANGUAGE);
        callback.httpGet(HTTP_BASE + strFunName, htParameters, stype);
    }

}
