package com.sundy.icare.net;

import com.sundy.icare.utils.MyUtils;

import org.json.JSONObject;

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
        getHttpRequest(MyURL.MYURL_login, "", JSONObject.class, callback);
    }

    //用户注册API
    public static void register(String username, String mobile, String password,
                                HttpCallback callback) {

        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<app_id>app_01</app_id>");
        sb.append("<app_user_id>15088086697</app_user_id>");
        sb.append("<auth_token></auth_token>");
        sb.append("<device_info>" + MyUtils.getUUID(callback.context) + "</device_info>");
        sb.append("<device_type>android</device_type>");
        sb.append("<language>cn</language>");
        sb.append("<login_passwd>123456</login_passwd>");
        sb.append("<mobile>15088086697</mobile>");
        sb.append("<user_name>Sundy Xu</user_name>");
        sb.append("<user_type>01</user_type>");
        sb.append("</request>");

        getHttpRequest(MyURL.MYURL_register, sb.toString(), JSONObject.class, callback);
    }

    public static void getHttpRequest(String method, String content, Class stype, HttpCallback callback) {
        Hashtable htParameters = new Hashtable();

        AESTool aes = new AESTool();
        SignatureUtil signatureUtil = new SignatureUtil();

        String appid = "canairport001";
        String token = signatureUtil.findTokenById(appid);
        String key = aes.findKeyById(appid);
        long millis = System.currentTimeMillis();

        String xml = content;
        try {
            xml = aes.encrypt(xml, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encrpt = signatureUtil.digest(xml, "MD5");
        String sign = signatureUtil.generateSignature(appid, token, encrpt, millis);

        htParameters.put("sign", sign);
        htParameters.put("time", String.valueOf(System.currentTimeMillis()));
        htParameters.put("device", MyUtils.getUUID(callback.context));
        htParameters.put("method", method);
        htParameters.put("encrpt", encrpt);
        htParameters.put("content", xml);
        callback.httpGet(HTTP_BASE, htParameters, stype);
    }

}
