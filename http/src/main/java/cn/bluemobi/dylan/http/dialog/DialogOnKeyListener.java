package cn.bluemobi.dylan.http.dialog;

/**
 * Created by yuandl on 2017-03-31.
 */

import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;

import com.orhanobut.logger.Logger;

import cn.bluemobi.dylan.http.dialog.LoadingDialog;
import rx.Subscription;

/**
 * Dialog 监听返回事件
 *
 * @author lizhiting
 */
public class DialogOnKeyListener implements DialogInterface.OnKeyListener {
    private LoadingDialog dialog;
    private boolean canCancel;

    public DialogOnKeyListener(LoadingDialog dialog,  boolean canCancel) {
        this.dialog = dialog;
        this.canCancel = canCancel;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canCancel) {
                if (this.dialog != null && this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
            }
            return true;
        }
        return false;
    }

}