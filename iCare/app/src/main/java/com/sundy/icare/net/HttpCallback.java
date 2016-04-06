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

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import internal.org.apache.http.entity.mime.MultipartEntity;

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

    public void doFilePost(final String uri, final MultipartEntity entity, Class<T> stype) {
        this.type = stype;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpPost httpRequest = new HttpPost(uri);
                String sresult = "";
                HttpResponse httpResponse;
                MyUtils.rtLog(TAG, "--------->reqUrl = " + url);
                try {
                    HttpParams params = httpRequest.getParams();
                    params.setParameter(HttpConnectionParams.SO_TIMEOUT, NET_TIMEOUT);
                    params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, NET_TIMEOUT);
                    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                    httpRequest.setEntity(entity);
                    httpResponse = getClient().execute(httpRequest);


                    switch (httpResponse.getStatusLine().getStatusCode()) {
                        case 200:
                            sresult = EntityUtils.toString(httpResponse.getEntity());
                            MyUtils.rtLog(TAG, "sresult:" + sresult);
                            if (sresult != null && !"".equals(sresult)) {
                                if (sresult.split("\\{")[0] != null) {
                                    sresult = sresult.replace(sresult.split("\\{")[0], "");
                                }
                            }
                            if (type.equals(JSONObject.class)) {
                                try {
                                    JSONObject r = (JSONObject) new JSONTokener(
                                            sresult.toString()).nextValue();
                                    result = (T) r;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (type.equals(JSONArray.class)) {
                                try {
                                    JSONArray r = (JSONArray) new JSONTokener(
                                            sresult.toString()).nextValue();
                                    result = (T) r;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (type.equals(String.class)) {
                                try {
                                    result = (T) sresult.toString();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case 401:
                            sresult = "401";
                            break;
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private DefaultHttpClient getClient() {
        SSLSocketFactory ssf = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            ssf = new MySSLSocketFactory(trustStore);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        HttpParams httpParams = new BasicHttpParams();
        //httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpConnectionParams.setConnectionTimeout(httpParams, NET_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, NET_TIMEOUT);
        //ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(NETWORK_POOL));
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(25));
        //Added this line to avoid issue at: http://stackoverflow.com/questions/5358014/android-httpclient-oom-on-4g-lte-htc-thunderbolt
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", ssf == null ? SSLSocketFactory.getSocketFactory() : ssf, 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, registry);
        return new DefaultHttpClient(cm, httpParams);
    }


}


