package cn.bluemobi.dylan.http.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.logging.Logger;

import cn.bluemobi.dylan.http.Http;
import cn.bluemobi.dylan.http.R;


/**
 * 进度加载对话框
 * Author: dylan
 * Date: 2014-12-29
 * Time: 13:34
 */
public class LoadingDialog {
    protected Dialog dialog;
    protected Context context;
    private TextView tv_text;

    public LoadingDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.loadingDialogStyle);
        /**设置对话框背景透明*/
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
    }

    public Dialog show() {
        if (Http.getHttp().getLoadingDialogLayoutId() != null) {
            dialog.setContentView(Http.getHttp().getLoadingDialogLayoutId());
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.pub_loading, null);
            dialog.setContentView(view);
            tv_text = (TextView) dialog.findViewById(R.id.tv_text);
        }
        if (!dialog.isShowing()) {
            //get the Context object that was used to great the dialog
            Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();

            //if the Context used here was an activity AND it hasn't been finished or destroyed
            //then dismiss it
            if (context instanceof Activity) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Activity) context).isFinishing()) {
                        dialog.show();
                    }
                }
            } else {
                //if the Context used wasnt an Activity, then dismiss it too
                dialog.show();
            }
        }
        return dialog;
    }


    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        dialog.setOnKeyListener(onKeyListener);
    }

    public void setMessage(String message) {
        if (tv_text != null) {
            tv_text.setText(message);
        }
    }

    public void dismiss() {
        hideProgress();
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        } else {
            return false;
        }

    }

    public void hideProgress() {
        if (dialog != null) {
            //check if dialog is showing.
            if (dialog.isShowing()) {

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();

                //if the Context used here was an activity AND it hasn't been finished or destroyed
                //then dismiss it
                if (context instanceof Activity) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                            dialog.dismiss();
                        }
                    }
                } else {
                    //if the Context used wasnt an Activity, then dismiss it too
                    dialog.dismiss();
                }
            }
        }
    }
} 