package cn.bluemobi.dylan.http;

/**
 * Created by yuandl on 2017-03-31.
 */

import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;

import cn.bluemobi.dylan.http.dialog.LoadingDialog;
import rx.Subscription;

/**
 * Dialog 监听返回事件
 *
 * @author lizhiting
 */
public class DialogOnKeyListener implements DialogInterface.OnKeyListener {
    private LoadingDialog dialog;
    private Subscription subscribe;
    private boolean canCancel;

    public DialogOnKeyListener(LoadingDialog dialog, Subscription subscribe, boolean canCancel) {
        this.dialog = dialog;
        this.subscribe = subscribe;
        this.canCancel = canCancel;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        Log.d("onKey", "onKey");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canCancel) {
                if (this.dialog != null
                        && this.dialog.isShowing()) {
                    this.dialog.dismiss();
                    return true;
                }
                Log.d("onKey", "keyCode == KeyEvent.KEYCODE_BACK");
                if (subscribe != null && !subscribe.isUnsubscribed()) {
                    Log.d("onKey", "subscribe != null && subscribe.isUnsubscribed()");
                    subscribe.unsubscribe();
                }
            } else {
                return true;
            }

        }
        return false;
    }

}