package com.bjtsn.dylan.requestpermission

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * @author YDL
 * @version 1.0
 * @date 2020/12/25/17:05
 */
class PermissionDispatcherFragment : Fragment() {
    private var mCallbacks = SparseArray<RequestPermission.PermissionCheckCallBack>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun requestPermissions(permissions: Array<out String>, callBack: RequestPermission.PermissionCheckCallBack) {
        val checkMorePermissions = checkMorePermissions(permissions)
        if (checkMorePermissions.isNotEmpty()) {
            val key = mCallbacks.size() + 1
            mCallbacks.put(key, callBack)
            requestPermissions(permissions, key)
        } else {
            callBack.onSucceed()
        }
    }

    private fun chePermission(permission: String): Boolean {
        return context?.let { ContextCompat.checkSelfPermission(it, permission) } == PackageManager.PERMISSION_GRANTED
    }

    private fun checkMorePermissions(permissions: Array<out String>): List<String> {
        val permissionList = ArrayList<String>()
        for (permission in permissions) {
            if (!chePermission(permission)) {
                permissionList.add(permission)
            }
        }
        return permissionList
    }

    private fun judgePermission(permission: String): Boolean {
        return activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, permission) } == true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var requestPermissionCallback = mCallbacks.get(requestCode)
        mCallbacks.remove(requestCode)
        if (requestPermissionCallback == null) {
            return
        }
        var checkMorePermissions = checkMorePermissions(permissions)
        if (checkMorePermissions.isEmpty()) {
            requestPermissionCallback.onSucceed()
        } else {
            var isBannedPermission = false
            for (checkMorePermission in checkMorePermissions) {
                if (!judgePermission(checkMorePermission)) {
                    isBannedPermission = true
                    break
                }
            }
            if (isBannedPermission) {
                requestPermissionCallback.onRejectAndNoAsk(permissions)
            } else {
                requestPermissionCallback.onReject(permissions)
            }
        }
    }
}