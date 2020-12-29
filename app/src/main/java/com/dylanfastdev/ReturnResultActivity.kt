package com.dylanfastdev

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import cn.bluemobi.dylan.base.BaseActivity

/**
 * @author YDL
 * @date 2020/12/25/15:47
 * @version 1.0
 */
class ReturnResultActivity : BaseActivity() {
    override fun onClick(v: View?) {
    }

    override fun initTitleBar() {
    }

    override fun getContentView(): Int {
        return R.layout.ac_return_result
    }

    override fun initViews(savedInstanceState: Bundle?) {
        findViewById<Button>(R.id.button).also {
            it.setOnClickListener {
                var intent = Intent()
                intent.putExtra("code", 1001)
                intent.putExtra("msg", "回传成功")
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun initData() {
    }

    override fun addListener() {
    }
}