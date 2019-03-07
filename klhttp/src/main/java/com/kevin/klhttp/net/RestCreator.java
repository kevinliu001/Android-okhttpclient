package com.kevin.klhttp.net;

import android.content.Context;
import android.text.TextUtils;


import com.kevin.klhttp.net.config.ClientConfigKeys;
import com.kevin.klhttp.net.config.RestClientConfigurator;
import com.kevin.klhttp.net.interceptors.CacheInterceptor;
import com.kevin.klhttp.net.interceptors.HeaderInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 * @author kevinliu
 * @date 2018/12/3
 */

public class RestCreator {

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }

    private static final class RetrofitHolder {
        private static final String BASE_URL = (String) RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.API_HOST);
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static final class OkHttpHolder {
        private static final int DEFAULT_TIMEOUT = 60;
        private static final int TIME_OUT = RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.TIMEOUT) != null?
                (int)RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.TIMEOUT):DEFAULT_TIMEOUT;

        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final ArrayList<Interceptor> INTERCEPTORS =
                (ArrayList<Interceptor>) RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.INTERCEPTOR);


        /**
         * 默认添加HeaderInterceptor
         */
        private static void insertDefaultHeaderInterceptor() {
            if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()) {
                boolean found = false;
                for (Interceptor interceptor : INTERCEPTORS) {
                    if (interceptor instanceof HeaderInterceptor) {
                        found = true;
                    }
                }

                if (found) {
                    return;
                }
            }
            BUILDER.addInterceptor(new HeaderInterceptor());
        }


        private static void enableCache(){
            boolean cacheEnable = false;
            Object enableObj = RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.CACHE_ENABLE);
            if (enableObj != null){
                cacheEnable = (boolean)enableObj;
            }

            if (cacheEnable) {
                Object cacheSizeObj = RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.CACHE_SIZE);
                String cachePath = RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.CACHE_PATH);
                long cacheSize = 0;
                if (cacheSizeObj != null){
                    cacheSize = (long)cacheSizeObj;
                }
                Context context = (Context) RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.APPLICATION_CONTEXT);
                if (context == null) {
                    throw new RuntimeException("no application context!!!");
                }
                int defaultCacheSize = 10 * 1024 * 1024;
                File defaultCacheFile = new File(context.getCacheDir(), "http-cache");

                if (cacheSize <= 0) {
                    cacheSize = defaultCacheSize;
                }

                if (TextUtils.isEmpty(cachePath)) {
                    cachePath = defaultCacheFile.getAbsolutePath();
                }

                Cache cache = new Cache(new File(cachePath), cacheSize);
                BUILDER.cache(cache);
                BUILDER.addInterceptor(new CacheInterceptor(context));
                BUILDER.addNetworkInterceptor(new CacheInterceptor(context));
            }
        }


        private static OkHttpClient.Builder addInterceptor() {
            //是否开启log
            Object obj = RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.HTTP_LOG);
            if (obj != null) {
                boolean needLog = (boolean) obj;
                if (needLog) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    BUILDER.addInterceptor(loggingInterceptor);
                }
            }

            if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()) {
                for (Interceptor interceptor : INTERCEPTORS) {
                    BUILDER.addInterceptor(interceptor);
                }
            }

            insertDefaultHeaderInterceptor();
            return BUILDER;
        }

        private static OkHttpClient.Builder initClient(){
            addInterceptor();
            enableCache();
            return BUILDER;
        }

        private static final OkHttpClient OK_HTTP_CLIENT = initClient()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    private static final class RestServiceHolder {
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }
}
