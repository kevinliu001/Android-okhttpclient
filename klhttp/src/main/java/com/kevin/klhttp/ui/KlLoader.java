package com.kevin.klhttp.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.kevin.klhttp.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 *
 * @author kevinliu
 * @date 2018/12/4
 */

public class KlLoader {

    private static final ArrayList<Dialog> LOADERS = new ArrayList<>();

    private static final LoaderStyle DEFAULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator;

    public static void showLoading(Context context,LoaderStyle type){
        final Dialog dialog = new Dialog(context, R.style.dialog);

        dialog.setCanceledOnTouchOutside(false);
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type.name(),context);
        dialog.setContentView(avLoadingIndicatorView);

        final Window dialogWindow = dialog.getWindow();

        if(dialogWindow != null){
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = DensityUtil.dip2px(context,50);
            lp.height = DensityUtil.dip2px(context,50);
            lp.gravity = Gravity.CENTER;
        }

        LOADERS.add(dialog);
        dialog.show();
    }

    public static void showLoading(Context context){
        showLoading(context,DEFAULT_LOADER);
    }

    public static void stopLoading(){
        for (Dialog dialog:LOADERS){
            if (dialog != null){
                if (dialog.isShowing()){
                    dialog.cancel();
                }
            }
        }
    }
}
