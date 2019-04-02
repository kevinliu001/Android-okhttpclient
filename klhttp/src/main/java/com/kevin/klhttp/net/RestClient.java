package com.kevin.klhttp.net;

import android.content.Context;


import com.kevin.klhttp.net.callback.IFailure;
import com.kevin.klhttp.net.callback.ISuccess;
import com.kevin.klhttp.net.response.RestObserver;
import com.kevin.klhttp.ui.KLLoader;
import com.kevin.klhttp.ui.LoaderStyle;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 *
 * @author kevinliu
 * @date 2018/12/3
 */

public class RestClient {

    private final String URL;
    private final Map<String, Object> PARAMS;
    private final KLHttpMethod METHOD;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYPE;
    private final Context CONTEXT;
    private final File FILE;
    private final Class ENTITYTYPE;
    private final boolean SHOWERRORTOAST;

    public RestClient(RestClientBuilder builder) {
        this.URL = builder.getUrl();
        this.PARAMS = builder.getPARAMS();
        this.METHOD = builder.getMethod();
        this.SUCCESS = builder.getISuccess();
        this.FAILURE = builder.getIFailure();
        this.BODY = builder.getBody();
        this.FILE = builder.getFile();
        this.CONTEXT = builder.getContext();
        this.LOADER_STYPE = builder.getLoaderStyle();
        this.ENTITYTYPE = builder.getEntityType();
        this.SHOWERRORTOAST = builder.isShowErrorToast();
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private void request(KLHttpMethod method, RestObserver restObservable) {

        final RestService service = RestCreator.getRestService();

        Observable<String> observable = null;

        if (LOADER_STYPE != null) {
            KLLoader.showLoading(CONTEXT, LOADER_STYPE);
        }
        switch (method) {
            case GET:
                observable = service.get(URL, PARAMS);
                break;
            case POST:
                observable = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                observable = service.postRaw(URL, BODY);
                break;
            case PUT:
                observable = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                observable = service.putRaw(URL, BODY);
                break;
            case DELETE:
                observable = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                observable = service.upload(URL, body);
                break;
            default:
                break;
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restObservable);
    }

    public final void post(RestObserver restObserver) {
        if (BODY == null) {
            request(KLHttpMethod.POST, restObserver);
        } else {
            if (PARAMS != null) {
                throw new RuntimeException("params must be null!");
            }
            request(KLHttpMethod.POST_RAW, restObserver);
        }
    }

    public final void put(RestObserver restObserver) {
        if (BODY == null) {
            request(KLHttpMethod.PUT, restObserver);
        } else {
            if (PARAMS != null) {
                throw new RuntimeException("params must be null!");
            }
            request(KLHttpMethod.PUT_RAW, restObserver);
        }
    }

    public final void request() {
        if (METHOD == null) {
            throw new RuntimeException("http method can not be null");
        }
        final RestObserver restObserver = new RestObserver(SUCCESS, FAILURE, ENTITYTYPE,LOADER_STYPE,SHOWERRORTOAST);
        switch (METHOD) {
            case GET:
            case DELETE:
            case UPLOAD:
                request(METHOD, restObserver);
                break;
            case POST:
            case POST_RAW:
                post(restObserver);
                break;
            case PUT:
            case PUT_RAW:
                put(restObserver);
                break;
            default:
                break;
        }
    }
}
