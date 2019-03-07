package com.kevin.klhttp.net.callback;

/**
 *
 * @author kevinliu
 * @date 2018/12/3
 */

public interface ISuccess<T> {
    void onSuccess(T response);
}
