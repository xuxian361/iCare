package com.sundy.icare.net;

import org.json.JSONObject;

/**
 * Created by sundy on 16/1/28.
 */
public class MyJsonParser {

    private static JSONObject parseResponse(JSONObject response) {
        try {
            if (response != null) {
                return response.getJSONObject("icare_response");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //获取返回的Response Code
    public static String getResp_Code(JSONObject response) {
        try {
            JSONObject result = parseResponse(response);
            if (result != null) {
                String code = result.getString("resp_code");
                if (code != null && !"".equals(code)) {
                    return code;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //获取返回的Response Message
    public static String getResp_Msg(JSONObject response) {
        try {
            JSONObject result = parseResponse(response);
            if (result != null) {
                String msg = result.getString("resp_msg");
                if (msg != null && !"".equals(msg)) {
                    return msg;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //获取返回的Response Message
    public static JSONObject getResp_Detail(JSONObject response) {
        try {
            JSONObject result = parseResponse(response);
            if (result != null) {
                JSONObject resp_detail = result.getJSONObject("resp_detail");
                if (resp_detail != null) {
                    return resp_detail;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

}
