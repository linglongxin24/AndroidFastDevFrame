package cn.bluemobi.dylan.http.dialog;

/**
 * Created by yuandl on 2017-03-31.
 */

import android.content.DialogInterface;

import com.orhanobut.logger.Logger;

import rx.Subscription;

/**
 * Dialog 监听返回事件
 *
 * @author lizhiting
 */
public class DialogOnDismissListener implements DialogInterface.OnDismissListener {
    private Subscription subscribe;

    public DialogOnDismissListener( Subscription subscribe) {
        this.subscribe = subscribe;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}