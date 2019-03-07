package com.kevin.klhttp.net.response;


import android.content.Context;

import com.kevin.klhttp.databus.RxBus;
import com.kevin.klhttp.net.callback.IFailure;
import com.kevin.klhttp.net.callback.ISuccess;
import com.kevin.klhttp.net.config.ClientConfigKeys;
import com.kevin.klhttp.net.config.RestClientConfigurator;
import com.kevin.klhttp.net.exception.RestException;
import com.kevin.klhttp.ui.KlLoader;
import com.kevin.klhttp.ui.LoaderStyle;
import com.kevin.klhttp.utils.GsonUtil;
import com.kevin.klhttp.utils.ToastUtil;

import java.lang.reflect.Method;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 *
 * @author kevinliu
 * @date 2018/12/4
 */

public class RestObserver implements Observer<String> {

    private ISuccess mISuccess;
    private IFailure mIFailure;
    private LoaderStyle mLoaderStyle;
    private Class mEntityType;
    private boolean mShowErrorToast;

    public RestObserver(ISuccess mSuccess, IFailure mFailure, Class entityType, LoaderStyle loaderStyle,boolean showErrorToast) {
        this.mISuccess = mSuccess;
        this.mIFailure = mFailure;
        this.mLoaderStyle = loaderStyle;
        this.mEntityType = entityType;
        this.mShowErrorToast = showErrorToast;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(String s) {
        stopLoading();
        if (mISuccess != null) {
            excuteSuccessCallback(s);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        stopLoading();
        if (mIFailure != null) {
            if (e instanceof RestException){
                handleError((RestException) e);
            }
            else {
                RestException restException = new RestException(e);
                handleError(restException);
            }
        }
    }

    @Override
    public void onComplete() {
        stopLoading();
    }


    /**
     * 正确返回处理
     * @param data
     */
    private void excuteSuccessCallback(String data) {
        if (mEntityType != null) {
            try {
                Object object = GsonUtil.StringToObject(data, mEntityType);
                mISuccess.onSuccess(object);
                RxBus.getInstance().send(object);
            } catch (Exception e) {
                e.printStackTrace();
                dataError();
            }
        }
        else{
            //默认回传String
            mISuccess.onSuccess(data);
        }
    }

    /**
     * 数据解析出错等
     */
    private void dataError(){
        //数据错误
        if (mIFailure != null) {
            handleError(new RestException(new Throwable(), RestException.EXCEPTION_DATA, "数据错误"));
        }
    }



    private void handleError(RestException restException){
        mIFailure.onFailure(restException);
        if (mShowErrorToast){
            Context context = (Context) RestClientConfigurator.getInstance().getConfiguration(ClientConfigKeys.APPLICATION_CONTEXT);
            if (context == null) {
                throw new RuntimeException("no application context!!!");
            }
            ToastUtil.showToast(context,restException.getMessage());
        }
    }

    private void stopLoading() {
        if (mLoaderStyle != null) {
            KlLoader.stopLoading();
        }
    }


    public Class getClsType() {
        if (mISuccess != null) {
            Class claz;
            Method[] ms = mISuccess.getClass().getDeclaredMethods();
            for (Method m : ms) {
                if (!"onSuccess".equals(m.getName())) {
                    continue;
                }
                Class[] paramTypes = m.getParameterTypes();
                claz = paramTypes[0];
                return claz;
            }
        }
        return null;
    }
}
