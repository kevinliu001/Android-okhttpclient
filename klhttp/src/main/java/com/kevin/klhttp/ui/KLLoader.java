package com.kevin.klhttp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.kevin.klhttp.R;
import com.wang.avi.AVLoadingIndicatorView;


/**
 *
 * @author kevinliu
 * @date 2018/12/4
 */

public class KLLoader {

    private static final LoaderStyle DEFAULT_LOADER = LoaderStyle.BallClipRotatePulseIndicator;

    private static Dialog LOAD_DIALOG;

    public static void showLoading(Context context,LoaderStyle type){

        stopLoading();

        LOAD_DIALOG = new Dialog(context, R.style.dialog);

        LOAD_DIALOG.setCanceledOnTouchOutside(false);
        final AVLoadingIndicatorView avLoadingIndicatorView = LoaderCreator.create(type.name(),context);
        LOAD_DIALOG.setContentView(avLoadingIndicatorView);

        final Window dialogWindow = LOAD_DIALOG.getWindow();

        if(dialogWindow != null){
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = DensityUtil.dip2px(context,50);
            lp.height = DensityUtil.dip2px(context,50);
            lp.gravity = Gravity.CENTER;
        }

        LOAD_DIALOG.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LOAD_DIALOG = null;
            }
        });

        LOAD_DIALOG.show();
    }

    public static void showLoading(Context context){
        showLoading(context,DEFAULT_LOADER);
    }

    public static void stopLoading(){
        if (LOAD_DIALOG != null){
            if (LOAD_DIALOG.isShowing()){
                LOAD_DIALOG.cancel();
            }
            LOAD_DIALOG = null;
        }
    }
}
