package com.dylanfastdev

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
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

        var findViewById = findViewById<TextView>(R.id.tv_text)
        var errorView = LayoutInflater.from(mContext).inflate(R.layout.ac_error_page, null)
        ( errorView.findViewById<Button>(R.id.button)).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                smartWebView.getWebView().reload()
            }


        })
        smartWebView.setErrorPage(errorView)
        smartWebView.loadUrl("https://player.bilibili.com/player.html?aid=582731140&bvid=BV1S64y1u7bL&cid=174633886&page=1")
    }

    override fun initData() {
    }

    override fun addListener() {
    }
}