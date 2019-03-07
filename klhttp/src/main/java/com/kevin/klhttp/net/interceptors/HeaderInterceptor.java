package com.kevin.klhttp.net.interceptors;


import com.kevin.klhttp.net.config.ClientConfigKeys;
import com.kevin.klhttp.net.config.RestClientConfigurator;
import com.kevin.klhttp.net.header.IHttpHeader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author kevinliu
 * @date 2018/12/11
 */

public class HeaderInterceptor extends BaseInterceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        IHttpHeader iHttpHeader = RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.HTTP_REQUEST_HEADER_IMPL);
        if (iHttpHeader != null) {
            HashMap<String, String> headMap = iHttpHeader.httpHeader(chain);
            if (headMap != null) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }
        return chain.proceed(builder.build());
    }
}
