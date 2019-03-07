package com.kevin.klhttp.net.header;

import java.util.HashMap;

import okhttp3.Interceptor.Chain;


/**
 *
 * @author kevinliu
 * @date 2018/12/11
 */

public interface IHttpHeader {
    /**
     * 添加header
     * @param chain
     * @return
     */
    HashMap<String,String> httpHeader(Chain chain);
}
