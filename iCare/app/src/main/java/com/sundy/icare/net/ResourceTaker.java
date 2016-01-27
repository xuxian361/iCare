package com.sundy.icare.net;

import com.sundy.icare.utils.MyUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by sundy on 15/12/6.
 */
public class ResourceTaker {

    public static final String HTTP_BASE = MyURL.HTTP_DEV;
    private static final String appid = "app_01";
    private static final String key = "123456789012345678901234567890~!";


    //用户登陆API
    public static void login(String terminalscode, String password,
                             HttpCallback callback) {
        Hashtable htParameters = new Hashtable();
        htParameters.put("terminalscode", terminalscode);
        htParameters.put("password", password);
        getHttpRequest(MyURL.MYURL_login, MyURL.METHOD_REGISTER, "", "", JSONObject.class, callback);
    }

    //用户注册API
    public static void register(String username, String mobile, String password,
                                HttpCallback callback) {

        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<app_id>app_01</app_id>");
        sb.append("<app_user_id>" + mobile + "</app_user_id>");
        sb.append("<auth_token></auth_token>");
        sb.append("<device_info>" + MyUtils.getUUID(callback.context) + "</device_info>");
        sb.append("<device_type>android</device_type>");
        sb.append("<language>cn</language>");
        sb.append("<login_passwd>" + password + "</login_passwd>");
        sb.append("<mobile>" + mobile + "</mobile>");
        sb.append("<user_name>" + username + "</user_name>");
        sb.append("<user_type>01</user_type>");
        sb.append("</request>");

        getHttpRequest(MyURL.MYURL_register, MyURL.METHOD_REGISTER, sb.toString(), "", JSONObject.class, callback);
    }

    public static void getHttpRequest(String url, String method, String content, String token, Class stype, HttpCallback callback) {
        AESTool aes = new AESTool();
        SignatureUtil signatureUtil = new SignatureUtil();

        long millis = System.currentTimeMillis();
        String xml = content;
        try {
            xml = aes.encrypt(xml, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encrpt = signatureUtil.digest(xml, "MD5");
        String sign = signatureUtil.generateSignature(appid, token, encrpt, millis);
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("time", String.valueOf(millis));
        paraMap.put("sign", sign);
        paraMap.put("device", MyUtils.getUUID(callback.context));
        paraMap.put("encrpt", encrpt);
        paraMap.put("method", method);
        paraMap.put("content", xml);

        callback.httpGet(HTTP_BASE + url, paraMap, stype);

    }

}
