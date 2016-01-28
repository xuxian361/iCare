package com.sundy.icare.net;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sundy.icare.MyApp;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sundy on 15/12/6.
 */
public class HttpCallback<T> {

    private final String TAG = "HttpCallback";
    private Hashtable htParameters;
    private String url;
    private T result;
    private String status;
    private Class<T> type;
    public Activity context;

    public HttpCallback() {
    }

    public HttpCallback(Activity context) {
        this.context = context;
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            callback(url, result, status);
        }
    };

    public void callback(String url, T result, String status) {
    }

    //Http Get Request - Method 1
    public void httpGet(String surl, Hashtable shtParameters, Class<T> stype) {
        this.url = surl;
        this.htParameters = shtParameters;
        this.type = stype;
        try {
            final String reqUrl = getHttpReqUrl(url, shtParameters);
            MyUtils.rtLog(TAG, "--------->reqUrl = " + reqUrl);
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    reqUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    MyUtils.rtLog(TAG, "------->result = " + response);
                    try {
                        if (response != null) {
                            if (type.equals(JSONObject.class)) {
                                try {
                                    JSONObject r = (JSONObject) new JSONTokener(
                                            response).nextValue();
                                    result = (T) r;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (type.equals(JSONArray.class)) {
                                try {
                                    JSONArray r = (JSONArray) new JSONTokener(
                                            response).nextValue();
                                    result = (T) r;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (type.equals(String.class)) {
                                try {
                                    result = (T) response;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null) {
                        MyUtils.rtLog(TAG, "----------->Error 404: 网络异常");
                        url = reqUrl;
                        result = null;
                        status = "error404";
                    } else {
                        MyUtils.rtLog(TAG, "----------->Error 500: 服务器异常");
                        url = reqUrl;
                        result = null;
                        status = "error500";
                    }
                    handler.sendEmptyMessage(0);
                }
            });
            MyApp.getInstance().addToRequestQueue(strReq, "tag_" + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getHttpReqUrl(String url, Hashtable shtParameters) {
        String sURL = url;
        boolean isFirst = true;
        try {
            for (Enumeration e = shtParameters.keys(); e.hasMoreElements(); ) {
                String strKey = (String) e.nextElement();
                String strKey_value = (String) shtParameters
                        .get(strKey);
                strKey_value = Uri.encode(strKey_value);
                if (isFirst) {
                    sURL = sURL + "?" + strKey + "=" + strKey_value;
                    isFirst = false;
                } else {
                    sURL = sURL + "&" + strKey + "=" + strKey_value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sURL;
    }

    //Http Get Request - Method 2
    public void httpGet(String surl, Map<String, String> map, Class<T> stype) {
        this.url = surl;
        this.type = stype;
        try {
            final String reqUrl = buildUri(url, map);
            MyUtils.rtLog(TAG, "--------->reqUrl = " + reqUrl);
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    reqUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    MyUtils.rtLog(TAG, "------->result = " + response);
                    try {
                        if (response != null) {
                            if (type.equals(JSONObject.class)) {
                                try {
                                    JSONObject r = (JSONObject) new JSONTokener(
                                            response).nextValue();
                                    result = (T) r;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (type.equals(JSONArray.class)) {
                                try {
                                    JSONArray r = (JSONArray) new JSONTokener(
                                            response).nextValue();
                                    result = (T) r;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (type.equals(String.class)) {
                                try {
                                    result = (T) response;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null) {
                        MyUtils.rtLog(TAG, "----------->Error 404: 网络异常");
                        url = reqUrl;
                        result = null;
                        status = "error404";
                    } else {
                        MyUtils.rtLog(TAG, "----------->Error 500: 服务器异常");
                        url = reqUrl;
                        result = null;
                        status = "error500";
                    }
                    handler.sendEmptyMessage(0);
                }
            });
            MyApp.getInstance().addToRequestQueue(strReq, "tag_" + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String buildUri(String uri, Map<String, String> paraMap) {
        StringBuilder sb = new StringBuilder();
        uri = uri.trim();
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length());
        }
        if (uri.endsWith("?")) {
            uri = uri.substring(0, uri.length());
        }
        sb.append(uri);
        if (paraMap != null && !paraMap.isEmpty()) {
            sb.append("?");
            Iterator<Map.Entry<String, String>> iterator = paraMap.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> pair = iterator.next();
                try {
                    String keyString = pair.getKey();
                    String valueString = pair.getValue();
                    sb.append(keyString);
                    sb.append("=");
                    sb.append(valueString);
                    sb.append("&");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String str = sb.toString();
        if (str.endsWith("&")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

}


