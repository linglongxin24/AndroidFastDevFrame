package cn.bluemobi.dylan.httputils.http;

/**
 * Created by yuandl on 2017-03-31.
 */

import android.content.DialogInterface;
import android.view.KeyEvent;

import cn.bluemobi.dylan.httputils.LoadingDialog;
import rx.Subscription;

/**
 * Dialog 监听返回事件
 *
 * @author lizhiting
 */
public class DialogOnKeyListener implements DialogInterface.OnKeyListener {
    private LoadingDialog dialog;
    private Subscription subscribe;

    public DialogOnKeyListener(LoadingDialog dialog, Subscription subscribe) {
        this.dialog = dialog;
        this.subscribe = subscribe;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (subscribe != null && subscribe.isUnsubscribed()) {
                subscribe.unsubscribe();
            }
            if (this.dialog != null
                    && this.dialog.isShowing()) {
                this.dialog.dismiss();
                return true;
            }
        }
        return false;
    }

}