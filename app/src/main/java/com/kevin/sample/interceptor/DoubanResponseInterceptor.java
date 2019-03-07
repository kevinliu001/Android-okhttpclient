package com.kevin.sample.interceptor;

import com.kevin.klhttp.net.interceptors.BaseInterceptor;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author kevinliu
 */
public class DoubanResponseInterceptor extends BaseInterceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        MediaType mediaType = response.body().contentType();
        String content= response.body().string();

        if (response.isSuccessful()) {

        }

        return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build();
    }
}
