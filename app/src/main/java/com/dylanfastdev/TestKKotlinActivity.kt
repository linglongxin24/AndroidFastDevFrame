package com.dylanfastdev

import android.os.Bundle
import android.view.View
import cn.bluemobi.dylan.base.BaseActivity
import kotlinx.android.synthetic.main.ac_start_activity_for_result.*

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
        bt_start_activity_for_result.text = "跳转activity"
    }

    override fun initData() {
    }

    override fun addListener() {
    }
}