package com.kevin.klhttp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-9
 */
public class ToastUtil {
    private static Toast toast = null; //Toast的对象！

    public static void showToast(Context mContext, String content) {
        if (toast == null) {
            toast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
        }
        else {
            toast.setText(content);
        }
        toast.show();
    }

}

