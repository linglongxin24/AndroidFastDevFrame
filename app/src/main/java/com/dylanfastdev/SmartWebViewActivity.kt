package com.dylanfastdev

import android.os.Bundle
import android.view.View
import android.widget.TextView
import cn.bluemobi.dylan.base.BaseActivity
import kotlinx.android.synthetic.main.ac_smart_webview.*

/**
 * @author YDL
 * @date 2020/12/31/14:18
 * @version 1.0
 */
class SmartWebViewActivity : BaseActivity() {
    override fun onClick(v: View?) {
    }

    override fun initTitleBar() {
    }

    override fun getContentView(): Int {
        return R.layout.ac_smart_webview
    }

    override fun initViews(savedInstanceState: Bundle?) {
//        smartWebView.loadUrl("https://move.boxkj.com/admin/login")
        smartWebView.loadUrl("https://player.bilibili.com/player.html?aid=582731140&bvid=BV1S64y1u7bL&cid=174633886&page=1")
        var findViewById = findViewById<TextView>(R.id.tv_text)

        MyListen().setCallBack(object : MyListen.CallBack {
            override fun open(data: Map<String, Any>) {
            }
        })

    }

    override fun initData() {
    }

    override fun addListener() {
    }
}