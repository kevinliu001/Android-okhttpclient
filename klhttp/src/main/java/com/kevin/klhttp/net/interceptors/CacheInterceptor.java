package com.kevin.klhttp.net.interceptors;

import android.content.Context;

import com.kevin.klhttp.utils.NetWorkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author kevinliu
 */
public class CacheInterceptor extends BaseInterceptor {

    Context mContext;

    public CacheInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if (NetWorkUtil.isNetAvailable(mContext)) {
            //有网络
            Response response = chain.proceed(request);
            // 有网络时5s内使用缓存 5s外部使用缓存
            int maxAge = 5;
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .addHeader("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            //无网络
            //无网络时强制使用缓存数据 一个小时
            int maxStale = 60 * 60 * 1;
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }
}
