package com.kevin.sample.app;

import android.app.Application;

import com.kevin.klhttp.net.config.RestClientConfigurator;
import com.kevin.klhttp.net.header.IHttpHeader;
import com.kevin.sample.interceptor.DoubanResponseInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RestClientConfigurator.getInstance()
                .with(this)
                .withApiHost("http://api.douban.com")
                .withHttpLog(true)
                .configure();
    }
}
