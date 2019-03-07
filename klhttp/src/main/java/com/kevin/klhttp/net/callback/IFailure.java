package com.kevin.klhttp.net.callback;


import com.kevin.klhttp.net.exception.RestException;

/**
 *
 * @author kevinliu
 * @date 2018/12/3
 */

public interface IFailure {
    void onFailure(RestException e);
}
