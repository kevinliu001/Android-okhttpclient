package com.kevin.sample.app;

import android.app.Application;

import com.kevin.klhttp.net.config.RestClientConfigurator;
import com.kevin.sample.interceptor.DoubanResponseInterceptor;

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RestClientConfigurator.getInstance().
                with(this)
                .withApiHost("http://api.douban.com")
                .withInterceptor(new DoubanResponseInterceptor())
                .withHttpLog(true)
                .configure();
    }
}
