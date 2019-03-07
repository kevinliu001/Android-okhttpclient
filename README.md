# okhttpclient-android
基于Retrofit、OkHttp、RxJava实现的Android网络框架，集成加载等待框，建造者模式调用。

根据Retrofit的特性，可以每一次请求都创建一个请求接口，本人不太习惯，所以只定义了几个通用的请求方法，如GET、POST、PUT 等。
这样在发送请求的时候只需要传入对应的请求方法就可以，不用区分接口，返回数据支持json格式解析

注册RegisterBus可支持将请求结果返回到任何页面

欢迎大家讨论指正

使用方法：

一、初始化，可在Application onCreate中初始化

1、基础:

    RestClientConfigurator.getInstance()
                .with(this)
                .withApiHost("http://api.douban.com")
                .configure();

2、高级

    RestClientConfigurator.getInstance()
        .with(this)
        .withApiHost("http://api.douban.com")
        .withInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //do something
                Response response = chain.proceed(request);
                //do something
                return response;
            }
        })
        .withRequestHeader(new IHttpHeader() {
            @Override
            public HashMap<String, String> httpHeader(Interceptor.Chain chain) {
                HashMap<String,String> map = new HashMap<>();
                map.put("device","Android");
                return map;
            }
        })
        //打开网络缓存，默认路径是当前应用的CacheDir，默认存储空间是10M
        //目前的规则是有网络时缓存5s，无网络时缓存1小时
        .withCacheEnable()
        //设置缓存路径
        .withCachePath("your cache path")
        //设置缓存空间大小
        .withCacheSize(10*1024*1024)
        //设置是否打开httplog
        .withHttpLog(true)
        //设置超时时间，默认60s
        .withTimeOut(60)
        .configure();
        
二、请求（拿豆瓣openApi举例）:

      //请求豆瓣电影列表
      RestClient.builder()
        .url("/v2/movie/top250")
        //提交参数，也可以用.params(map)方式提交
        .param("start", "0")
        .param("count", "20")
        //请求时显示默认等待框,可以用.loader(this, LoaderStyle.BallBeatIndicator)改变等待框样式
        //等待框功能来源于https://github.com/81813780/AVLoadingIndicatorView
        .loader(this)
        //请求方法
        .method(KLHttpMethod.GET)
        //请求返回实体类型，不设置默认返回string类型
        .entityType(DoubanResponse.class)
        //是否显示错误提示Toast，默认显示
        .showErrorToast(true)
        //请求成功回调
        .success(new ISuccess<DoubanResponse>() {
            @Override
            public void onSuccess(DoubanResponse response) {
              //请求成功回调
            }
        })
        //请求失败回调
        .failure(new IFailure() {
            @Override
            public void onFailure(RestException e) {
                if (e.isNetWorkError()){
                    //网络连接错误
                }
                else if (e.isServerError()){
                    //服务器错误
                }
                else if (e.isDataError()){
                    //数据错误
                }
            }
        })
        .build()
        .request();
 
3、注册RegisterBug，当返回成功时可以将返回结果回调到任何页面，使用方法如下：
    
    比如在Activity的onCreate中注册
    RxBus.getInstance().register(this);
    
    在onDestroy中取消注册
    RxBus.getInstance().unRregister(this);
    
    在当前页面写一个注解方法
    @RegisterBus
    public void doubanMovies(DoubanResponse doubanResponse){
        //请求成功后会回调此处（同时也会回调success（））
        
    }
