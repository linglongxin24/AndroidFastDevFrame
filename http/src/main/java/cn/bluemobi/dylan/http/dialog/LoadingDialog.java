package cn.bluemobi.dylan.http.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
            dialog.show();
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
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        } else {
            return false;
        }

    }
} 