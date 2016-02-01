package com.sundy.icare.net;

import com.sundy.icare.utils.MyUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sundy on 15/12/6.
 */
public class ResourceTaker {

    private static final String TAG = "ResourceTaker";
    public static final String HTTP_BASE = MyURL.HTTP_DEV;
    private static final String key = "123456789012345678901234567890~!";

    //用户登陆API
    public static void login(String mobile, String password,
                             HttpCallback callback) {

        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<login_passwd>" + password + "</login_passwd>");
        sb.append("<mobile>" + mobile + "</mobile>");
        sb.append("<user_name></user_name>");
        sb.append("<auth_token>" + MyUtils.getToken(callback.context) + "</auth_token>");
        sb = getCommonParameter(sb, callback);
        sb.append("</request>");

        getHttpRequest(MyURL.MYURL_register, MyURL.METHOD_REGISTER, sb.toString(), JSONObject.class, callback);
    }

    //用户注册API
    public static void register(String username, String mobile, String password,
                                HttpCallback callback) {

        StringBuffer sb = new StringBuffer();
        sb.append("<request>");
        sb.append("<login_passwd>" + password + "</login_passwd>");
        sb.append("<mobile>" + mobile + "</mobile>");
        sb.append("<user_name>" + username + "</user_name>");
        sb.append("<auth_token>" + MyUtils.getToken(callback.context) + "</auth_token>");
        sb.append("<user_type>02</user_type>");
        sb = getCommonParameter(sb, callback);
        sb.append("</request>");

        getHttpRequest(MyURL.MYURL_register, MyURL.METHOD_REGISTER, sb.toString(), JSONObject.class, callback);
    }


    //公共参数
    public static StringBuffer getCommonParameter(StringBuffer sb, HttpCallback callback) {
        sb.append("<app_id>" + MyUtils.getAppID(callback.context) + "</app_id>");
        sb.append("<app_user_id>" + MyUtils.getAPP_User_ID(callback.context) + "</app_user_id>");
        sb.append("<device_info>" + MyUtils.getUUID(callback.context) + "</device_info>");
        sb.append("<device_type>android</device_type>");
        sb.append("<language>cn</language>");
        return sb;
    }

    public static void getHttpRequest(String url, String method, String content, Class stype, HttpCallback callback) {
        AESTool aes = new AESTool();
        SignatureUtil signatureUtil = new SignatureUtil();
        MyUtils.rtLog(TAG, "------->request content = " + content);

        long millis = System.currentTimeMillis();
        String xml = content;
        try {
            xml = aes.encrypt(xml, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encrpt = signatureUtil.digest(xml, "MD5");
        String sign = signatureUtil.generateSignature(MyUtils.getAppID(callback.context), MyUtils.getToken(callback.context), encrpt, millis);
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
