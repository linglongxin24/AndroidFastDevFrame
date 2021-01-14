package com.dylanfastdev

import android.os.Bundle
import android.view.View
import cn.bluemobi.dylan.base.BaseActivity
import com.bjtsh.dylan.selectphoto.SelectPhoto
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.ac_start_activity_for_result.*
import java.io.File

/**
 * @author YDL
 * @date 2020/12/31/9:41
 * @version 1.0
 */
class TestKKotlinActivity : BaseActivity() {
    override fun onClick(v: View?) {
    }

    override fun initTitleBar() {
    }

    override fun getContentView(): Int {
        return R.layout.ac_start_activity_for_result
    }

    override fun initViews(savedInstanceState: Bundle?) {
        bt_start_activity_for_result.text = "选择图片"
    }

    override fun initData() {
    }

    override fun addListener() {
        bt_start_activity_for_result.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                SelectPhoto(mActivity).gallery().setCallBack(object :SelectPhoto.CallBack{
                    override fun onSelectPhoto(file: File) {
                        Logger.d("文件大小${convertFileSize(file.length())}")
                        Glide.with(mContext).load(file).into(iv)
                    }
                })
            }

        })
    }

    fun convertFileSize(size: Long): String? {
        val kb: Long = 1024
        val mb = kb * 1024
        val gb = mb * 1024
        return if (size >= gb) {
            String.format("%.1f GB", size.toFloat() / gb)
        } else if (size >= mb) {
            val f = size.toFloat() / mb
            String.format(if (f > 100) "%.0f MB" else "%.1f MB", f)
        } else if (size >= kb) {
            val f = size.toFloat() / kb
            String.format(if (f > 100) "%.0f KB" else "%.1f KB", f)
        } else {
            String.format("%d B", size)
        }
    }

}