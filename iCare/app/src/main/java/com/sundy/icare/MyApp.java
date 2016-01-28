package com.sundy.icare;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bugtags.library.Bugtags;
import com.easemob.chat.EMChat;
import com.facebook.drawee.backends.pipeline.Fresco;
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

        //-----------------Fresco init------------------//
        Fresco.initialize(this);

        //-----------------环信SDK init------------------//
        EMChat.getInstance().init(this);
        /**
         * debugMode == true 时为打开，sdk 会在log里输入调试信息
         * @param debugMode
         * 在做代码混淆的时候需要设置成false
         */
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源

        //-----------------Bugtags init-----------------//
        if (MyConstant.Is_BugTags_Enable) {
            //初始化BugTags: 跟踪Bugs
            Bugtags.start(MyConstant.BUG_TAGS_KEY, this, Bugtags.BTGInvocationEventBubble);
        }
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
