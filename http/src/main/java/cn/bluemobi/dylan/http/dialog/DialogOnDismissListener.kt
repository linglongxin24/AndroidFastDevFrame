package cn.bluemobi.dylan.http.dialog

import android.content.DialogInterface
import rx.Subscription

/**
 * Created by yuandl on 2017-03-31.
 */
/**
 * Dialog 监听返回事件
 *
 * @author lizhiting
 */
class DialogOnDismissListener(private val subscribe: Subscription?) : DialogInterface.OnDismissListener {
    override fun onDismiss(dialog: DialogInterface) {
        if (subscribe != null && !subscribe.isUnsubscribed) {
            subscribe.unsubscribe()
        }
    }
}