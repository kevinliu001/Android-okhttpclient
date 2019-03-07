package com.kevin.klhttp.net.config;


import android.content.Context;

import com.kevin.klhttp.net.header.IHttpHeader;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 *
 * @author kevinliu
 * @date 2018/11/29
 */

public class RestClientConfigurator {

    private static final HashMap<Object,Object> REST_CONFIGS = new HashMap<>();

    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();

    private RestClientConfigurator(){
        REST_CONFIGS.put(ClientConfigKeys.CONFIG_READY,false);
    }

    final HashMap<Object,Object> getRestConfigs() {
        return REST_CONFIGS;
    }

    private static final class Holder{
        public static final RestClientConfigurator INSTANCE = new RestClientConfigurator();
    }

    public static RestClientConfigurator getInstance(){
        return Holder.INSTANCE;
    }

    public final void configure(){
        REST_CONFIGS.put(ClientConfigKeys.CONFIG_READY,true);
    }

    public final RestClientConfigurator with(Context context){
        REST_CONFIGS.put(ClientConfigKeys.APPLICATION_CONTEXT,context);
        return this;
    }

    public final RestClientConfigurator withInterceptor(Interceptor interceptor){
        INTERCEPTORS.add(interceptor);
        REST_CONFIGS.put(ClientConfigKeys.INTERCEPTOR,INTERCEPTORS);
        return this;
    }

    public final RestClientConfigurator withInterceptors(ArrayList<Interceptor> interceptors){
        INTERCEPTORS.addAll(interceptors);
        REST_CONFIGS.put(ClientConfigKeys.INTERCEPTOR,interceptors);
        return this;
    }


    public final RestClientConfigurator withApiHost(String host){
        REST_CONFIGS.put(ClientConfigKeys.API_HOST,host);
        return this;
    }

    public final RestClientConfigurator withHttpLog(boolean needLog){
        REST_CONFIGS.put(ClientConfigKeys.HTTP_LOG,needLog);
        return this;
    }

    public final RestClientConfigurator withRequestHeader(IHttpHeader iHttpHeader){
        REST_CONFIGS.put(ClientConfigKeys.HTTP_REQUEST_HEADER_IMPL,iHttpHeader);
        return this;
    }

    public final RestClientConfigurator withTimeOut(int timeOut){
        REST_CONFIGS.put(ClientConfigKeys.TIMEOUT,timeOut);
        return this;
    }

    public final RestClientConfigurator withCachePath(String cachePath){
        withCacheEnable();
        REST_CONFIGS.put(ClientConfigKeys.CACHE_PATH,cachePath);
        return this;
    }

    public final RestClientConfigurator withCacheSize(int cacheSize){
        withCacheEnable();
        REST_CONFIGS.put(ClientConfigKeys.CACHE_SIZE,cacheSize);
        return this;
    }

    public final RestClientConfigurator withCacheEnable(){
        REST_CONFIGS.put(ClientConfigKeys.CACHE_ENABLE,true);
        return this;
    }

    private void checkConfiguration(){
        final boolean isReady = (boolean)REST_CONFIGS.get(ClientConfigKeys.CONFIG_READY);
        if(!isReady){
            throw new RuntimeException("Configuration is not read,call configure");
        }
    }

    @SuppressWarnings("unchecked")
    public final <T> T getConfiguration(Enum<ClientConfigKeys> key){
        checkConfiguration();
        return (T)REST_CONFIGS.get(key);
    }
}
