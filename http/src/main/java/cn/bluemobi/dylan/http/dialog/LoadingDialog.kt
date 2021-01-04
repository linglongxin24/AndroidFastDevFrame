package cn.bluemobi.dylan.http.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import cn.bluemobi.dylan.http.R
import com.bjtsn.dylan.lifecycleobserver.LifecycleCallback
import com.bjtsn.dylan.lifecycleobserver.LifecycleObserver

/**
 * @author YDL
 * @date 2020/12/31/15:56
 * @version 1.0
 */
class LoadingDialog(private val context: Context) {
    private val dialog: Dialog = Dialog(context, R.style.loadingDialogStyle)

    /**
     * 初始化
     */
    init {
        //设置对话框背景透明
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        addLifeCycle()
    }

    /**
     * 添加对话框生命周期监听，activity或fragment关闭后自动关闭对话框
     */
    private fun addLifeCycle() {
        if (context is FragmentActivity) {
            val activity = context
            LifecycleObserver(activity).observer(object : LifecycleCallback() {
                override fun onDestroy() {
                    super.onDestroy()
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                }
            })
        }
    }

    /**
     * 显示加载对话框不带文字
     */
    fun show(): Dialog {
        return show(null)
    }

    /**
     * 显示加载对话框带文字
     * @param loadingMessage 要提示的文字
     */
    fun show(loadingMessage: String?): Dialog {
        if (Http.getLoadingDialogLayoutId() != null) {
            Http.getLoadingDialogLayoutId()?.let { dialog.setContentView(it) }
        } else {
            val inflate = LayoutInflater.from(context).inflate(R.layout.pub_loading, null)
            dialog.setContentView(inflate)
        }
        val tv_text = dialog.findViewById<TextView>(R.id.tv_text)
        if (tv_text != null && !TextUtils.isEmpty(loadingMessage)) {
            tv_text.visibility = View.VISIBLE
            tv_text.text = loadingMessage
        }
        if (!dialog.isShowing) {
            if (context is FragmentActivity) {
                val activity = context
                if (!activity.isFinishing) {
                    dialog.show()
                }
            } else {
                dialog.show()
            }
        }

        return dialog
    }

    fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener) {
        dialog.setOnKeyListener(onKeyListener)
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        dialog.setOnDismissListener(onDismissListener)
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun dismiss() {
        dialog.dismiss()
    }

}