package cn.bluemobi.dylan.http.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bjtsn.dylan.lifecycleobserver.LifecycleCallback;
import com.bjtsn.dylan.lifecycleobserver.LifecycleObserver;

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
        addLifeCycle(context);
    }

    public Dialog show() {
        return show(null);
    }

    public Dialog show(String loadingMessage) {
        if (Http.getHttp().getLoadingDialogLayoutId() != null) {
            dialog.setContentView(Http.getHttp().getLoadingDialogLayoutId());
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.pub_loading, null);
            dialog.setContentView(view);
        }
        try {
            tv_text = (TextView) dialog.findViewById(R.id.tv_text);
            if (tv_text != null && !TextUtils.isEmpty(loadingMessage)) {
                tv_text.setText(loadingMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!dialog.isShowing()) {
            //get the Context object that was used to great the dialog
            Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();

            //if the Context used here was an activity AND it hasn't been finished or destroyed
            //then dismiss it
            if (context instanceof Activity) {
                Activity activity = (Activity) this.context;
                if (!activity.isFinishing() && !activity.isDestroyed()) {
                    dialog.show();
                }
            } else {
                //if the Context used wasnt an Activity, then dismiss it too
                dialog.show();
            }
        }
        return dialog;
    }

    public Dialog showDefaultStyle(String loadingMessage) {
        View view = LayoutInflater.from(context).inflate(R.layout.pub_loading, null);
        dialog.setContentView(view);
        try {
            tv_text = (TextView) dialog.findViewById(R.id.tv_text);
            if (tv_text != null && !TextUtils.isEmpty(loadingMessage)) {
                tv_text.setText(loadingMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        dialog.setOnDismissListener(onDismissListener);
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

    private void addLifeCycle(Context mContext) {
        if (mContext instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) mContext;
            new LifecycleObserver(activity).observer(new LifecycleCallback() {
                @Override
                public void onDestroy() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    super.onDestroy();
                }
            });
        }

    }
}