package com.sundy.icare;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bugtags.library.Bugtags;
import com.sundy.icare.utils.LruBitmapCache;
import com.sundy.icare.utils.MyConstant;

/**
 * Created by sundy on 15/12/6.
 */
public class MyApp extends Application {

    public static final String TAG = MyApp.class.getSimpleName();

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        //初始化BugTags: 跟踪Bugs
        Bugtags.start(MyConstant.BUG_TAGS_KEY, this, Bugtags.BTGInvocationEventBubble);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static synchronized MyApp getInstance() {
        return myApp;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue,
                    new LruBitmapCache());
        }
        return this.imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

}
