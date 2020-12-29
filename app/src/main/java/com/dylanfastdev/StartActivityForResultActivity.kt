package com.dylanfastdev

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import cn.bluemobi.dylan.base.BaseActivity
import com.bjtsn.dylan.startactivityforrestult.StartActivityForResult
import com.orhanobut.logger.Logger

/**
 * @author YDL
 * @date 2020/12/25/14:51
 * @version 1.0
 */
class StartActivityForResultActivity : BaseActivity() {

    override fun initTitleBar() {
    }

    override fun getContentView(): Int {
        return R.layout.ac_start_activity_for_result
    }

    override fun initViews(savedInstanceState: Bundle?) {
        var button: Button = findViewById(R.id.bt_start_activity_for_result)
        button.setOnClickListener {
            StartActivityForResult(mActivity)
                    .startActivityForResult(Intent(mContext, ReturnResultActivity::class.java))
                    .setOnActivityResultCallBack(object : StartActivityForResult.CallBack {
                        override fun onActivityResult(resultCode: Int, data: Intent) {
                            Logger.d("resultCode=${resultCode} data=${data.extras}")
                            for (dataKey in data.extras.keySet()) {
                                Logger.d("${dataKey}=${data.extras.get(dataKey)}")
                            }
                        }

                    })
//            RequestPermission(this)
//                    .requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
//                    .setPermissionCheckCallBack(object : RequestPermission.PermissionCheckCallBack {
//                        override fun onSucceed() {
//                            Logger.d("同意权限")
//                        }
//
//                        override fun onReject(permission: Array<out String>) {
//                            iOSOneButtonDialog(mContext).setMessage("你已经拒绝权限,将无法正常使用").show()
//                        }
//
//                        override fun onRejectAndNoAsk(permission: Array<out String>) {
//                            iOSOneButtonDialog(mContext).setMessage("你已经拒绝权限,将无法正常使用").show()
//                        }
//
//                    })

        }
    }

    override fun initData() {
    }

    override fun addListener() {
    }

    override fun onClick(v: View?) {
    }

}