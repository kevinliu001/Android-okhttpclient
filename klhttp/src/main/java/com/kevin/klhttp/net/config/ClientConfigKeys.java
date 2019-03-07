package com.kevin.klhttp.net.config;

/**
 *
 * @author kevinliu
 * @date 2018/11/29
 */

public enum ClientConfigKeys {
    //全局Application Context
    APPLICATION_CONTEXT,

    //访问地址
    API_HOST,

    //拦截器
    INTERCEPTOR,

    //请求头设置
    HTTP_REQUEST_HEADER_IMPL,

    //log 是否打印
    HTTP_LOG,

    //是否设置完毕
    CONFIG_READY,

    //请求超时时间
    TIMEOUT,

    //启动网络缓存
    CACHE_ENABLE,

    //网络缓存空间
    CACHE_SIZE,

    //网络缓存路径
    CACHE_PATH
}
