package com.kevin.klhttp.net.exception;

import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import retrofit2.HttpException;

/**
 *
 * @author kevinliu
 * @date 2018/12/11
 */

public class RestException extends RuntimeException{

    //自定义
    public static final int EXCEPTION_NET = 0;
    public static final int EXCEPTION_DATA = 1;
    public static final int EXCEPTION_SERVER = 2;
    public static final int EXCEPTION_OTHER = 3;

    int mCode;
    String mMsg;

    public RestException(Throwable cause) {
        super(cause);
        check(cause);
    }

    public RestException(Throwable cause, int code,String msg) {
        super(cause);
        this.mCode = code;
        this.mMsg = msg;
    }

    private void check(Throwable throwable){
        throwable.printStackTrace();

        if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException
                || throwable instanceof JsonSyntaxException) {
            //数据错误
            mCode = EXCEPTION_DATA;
            mMsg = "数据解析错误";
        } else if (throwable instanceof UnknownHostException
                || throwable instanceof UnknownServiceException
                || throwable instanceof SocketTimeoutException
                || throwable instanceof ConnectException) {
            //网络连接错误
            mCode = EXCEPTION_NET;
            mMsg = "网络连接错误";
        }
        else if (throwable instanceof HttpException){
            HttpException httpException = (HttpException) throwable;
            //服务器错误
            mCode = httpException.code();
            if (httpException.response()!= null &&
                    !TextUtils.isEmpty(httpException.response().message())){
                mMsg = httpException.response().message();
            }
            else {
                mMsg = "错误（"+httpException.code()+"）";
            }
        }
        else{
            //其它错误
            mCode = EXCEPTION_OTHER;
            mMsg = "访问出错";
        }
    }

    /**
     * 网络错误
     * @return
     */
    public boolean isNetWorkError(){
        return mCode == EXCEPTION_NET;
    }

    /**
     * 数据错误
     * @return
     */
    public boolean isDataError(){
        return mCode == EXCEPTION_DATA;
    }

    /**
     * 其它错误
     * @return
     */
    public boolean isServerError(){
        return mCode == EXCEPTION_SERVER;
    }

    @Override
    public String getMessage() {
        return mMsg;
    }

    public int getCode() {
        return mCode;
    }
}
