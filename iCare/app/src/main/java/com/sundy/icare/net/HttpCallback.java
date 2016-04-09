package com.sundy.icare.net;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sundy.icare.MyApp;
import com.sundy.icare.utils.MyUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sundy on 15/12/6.
 */
public class HttpCallback<T> {

    private final String TAG = "HttpCallback";
    private HashMap hashMap;
    private String url;
    private T result;
    private String status;
    private Class<T> type;
    public Activity context;
    private static final int NET_TIMEOUT = 20000;
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String CHARSET = "utf-8"; // 设置编码

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

    public void callback(String url, T data, String status) {
    }

    //Http Get Request
    public void httpGet(String surl, HashMap hashMap, Class<T> stype) {
        this.url = surl;
        this.hashMap = hashMap;
        this.type = stype;
        try {
            final String reqUrl = getHttpReqUrl(url, hashMap);
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

    //Http Post Request
    public void httpPost(String surl, final HashMap hashMap, Class<T> stype) {
        this.url = surl;
        this.hashMap = hashMap;
        this.type = stype;
        try {
            final String reqUrl = url;
            MyUtils.rtLog(TAG, "--------->reqUrl = " + reqUrl);
            StringRequest strReq = new StringRequest(Request.Method.POST,
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
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return hashMap;
                }
            };
            MyApp.getInstance().addToRequestQueue(strReq, "tag_" + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getHttpReqUrl(String url, HashMap hashMap) {
        String sURL = url;
        boolean isFirst = true;
        try {
            Iterator iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String strKey = (String) entry.getKey();
                String strKey_value = (String) entry.getValue();
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

    //上传单张图片
    public void doFilePost(final String surl, final HashMap<String, String> stringHashMap,
                           final String fileKey, final File file, Class<T> stype) {
        this.type = stype;
        this.url = surl;
        final String reqUrl = url;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL uUrl = new URL(reqUrl);
                    HttpURLConnection conn = (HttpURLConnection) uUrl.openConnection();
                    conn.setReadTimeout(NET_TIMEOUT);
                    conn.setConnectTimeout(NET_TIMEOUT);
                    conn.setDoInput(true); // 允许输入流
                    conn.setDoOutput(true); // 允许输出流
                    conn.setUseCaches(false); // 不允许使用缓存
                    conn.setRequestMethod("POST"); // 请求方式
                    conn.setRequestProperty("Charset", CHARSET); // 设置编码
                    conn.setRequestProperty("connection", "keep-alive");
                    conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                    conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    StringBuffer sb = null;
                    String params = "";

                    if (stringHashMap != null && stringHashMap.size() > 0) {
                        Iterator<String> it = stringHashMap.keySet().iterator();
                        while (it.hasNext()) {
                            sb = null;
                            sb = new StringBuffer();
                            String key = it.next();
                            String value = stringHashMap.get(key);
                            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                            sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                            sb.append(value).append(LINE_END);
                            params = sb.toString();
                            dos.write(params.getBytes());
                        }
                    }

                    sb = null;
                    params = null;
                    sb = new StringBuffer();
                    /**
                     * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的 比如:abc.png
                     */
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition:form-data; name=\"" + fileKey
                            + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
                    sb.append("Content-Type:image/pjpeg" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
                    sb.append(LINE_END);
                    params = sb.toString();
                    sb = null;
                    dos.write(params.getBytes());
                    InputStream is = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    int curLen = 0;
                    while ((len = is.read(bytes)) != -1) {
                        curLen += len;
                        dos.write(bytes, 0, len);
                    }
                    is.close();
                    dos.write(LINE_END.getBytes());
                    byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                    dos.write(end_data);
                    dos.flush();
                    int res = conn.getResponseCode();
                    if (res == 200) {
                        InputStream input = conn.getInputStream();
                        StringBuffer sb1 = new StringBuffer();
                        int ss;
                        while ((ss = input.read()) != -1) {
                            sb1.append((char) ss);
                        }
                        String response = sb1.toString();
                        MyUtils.rtLog(TAG, "----------->result =" + response);
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
                        return;
                    } else {
                        MyUtils.rtLog(TAG, "----------->Error 404: 网络异常");
                        url = reqUrl;
                        result = null;
                        status = "error";
                        handler.sendEmptyMessage(0);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}


