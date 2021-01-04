package cn.bluemobi.dylan.http.dialog

import android.content.DialogInterface
import android.view.KeyEvent

/**
 * Dialog 监听返回事件
 * @author YDL
 * @date 2020/12/31/15:49
 * @version 1.0
 */
class DialogOnKeyListener(private val dialog: LoadingDialog?, private val canCancel: Boolean) : DialogInterface.OnKeyListener {
    override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canCancel) {
                if (this.dialog != null && this.dialog.isShowing()) {
                    this.dialog.dismiss()
                }
            }
            return true
        }
        return false
    }
}