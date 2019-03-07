package com.kevin.klhttp.net;

import android.content.Context;


import com.kevin.klhttp.net.callback.IFailure;
import com.kevin.klhttp.net.callback.ISuccess;
import com.kevin.klhttp.ui.LoaderStyle;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *
 * @author kevinliu
 * @date 2018/12/3
 */

public class RestClientBuilder {
    private String mUrl;
    private Map<String,Object> PARAMS;
    private KLHttpMethod mMethod;
    private ISuccess mISuccess = null;
    private IFailure mIFailure = null;
    private RequestBody mBody = null;
    private Context mContext = null;
    private LoaderStyle mLoaderStyle = null;
    private File mFile = null;
    private Class mEntityType = null;
    private boolean mShowErrorToast = true;

    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(Map<String,Object> params){
        if (PARAMS == null){
            PARAMS = new WeakHashMap<>();
        }
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder param(String key,Object value){
        if (PARAMS == null){
            PARAMS = new WeakHashMap<>();
        }
        this.PARAMS.put(key,value);
        return this;
    }

    public final RestClientBuilder raw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess){
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure){
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder loader(Context context){
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallTrianglePathIndicator;
        return this;
    }

    public final RestClientBuilder loader(Context context, LoaderStyle loaderStyle){
        this.mContext = context;
        this.mLoaderStyle = loaderStyle;
        return this;
    }

    public final RestClientBuilder file(File file){
        this. mFile =file;
        return this;
    }

    public final RestClientBuilder file(String file){
        this. mFile = new File(file);
        return this;
    }

    public final RestClientBuilder method(KLHttpMethod method){
        this.mMethod = method;
        return this;
    }

    public final RestClientBuilder entityType(Class entityType){
        this.mEntityType = entityType;
        return this;
    }

    public final RestClientBuilder showErrorToast(boolean show){
        this.mShowErrorToast = show;
        return this;
    }


    String getUrl() {
        return mUrl;
    }

    Map<String, Object> getPARAMS() {
        return PARAMS;
    }

    KLHttpMethod getMethod() {
        return mMethod;
    }

    ISuccess getISuccess() {
        return mISuccess;
    }

    IFailure getIFailure() {
        return mIFailure;
    }

    RequestBody getBody() {
        return mBody;
    }

    Context getContext() {
        return mContext;
    }

    LoaderStyle getLoaderStyle() {
        return mLoaderStyle;
    }

    File getFile() {
        return mFile;
    }

    boolean isShowErrorToast() {
        return mShowErrorToast;
    }

    Class getEntityType() {
        return mEntityType;
    }

    public final RestClient build(){
        if (mBody == null && PARAMS == null){
            PARAMS = new WeakHashMap<>();
        }
        return new RestClient(this);
    }
}
