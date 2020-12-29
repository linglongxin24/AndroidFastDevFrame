package com.bjtsn.dylan.requestpermission

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

/**
 * @author YDL
 * @date 2020/12/25/17:04
 * @version 1.0
 */
class RequestPermission {
    private val TAG = "REQUEST_PERMISSION_FRAGMENT"
    private var fragment: PermissionDispatcherFragment
    private lateinit var permissions: Array<out String>

    constructor(activity: FragmentActivity) {
        fragment = getEventDispatchFragment(activity.supportFragmentManager);
    }

    constructor(fragment: Fragment) {
        this.fragment = getEventDispatchFragment(fragment.childFragmentManager);
    }

    private fun getEventDispatchFragment(fragmentManager: FragmentManager): PermissionDispatcherFragment {
        var fragment = findEventDispatchFragment(fragmentManager)
        if (fragment == null) {
            fragment = PermissionDispatcherFragment()
            fragmentManager.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return fragment as PermissionDispatcherFragment
    }

    private fun findEventDispatchFragment(fragmentManager: FragmentManager): Fragment? {
        return fragmentManager.findFragmentByTag(TAG)
    }

    fun requestPermission(vararg permissions: String): RequestPermission {
        this.permissions = permissions
        return this
    }

    fun setPermissionCheckCallBack(permissionCheckCallBack: PermissionCheckCallBack): RequestPermission {
        fragment.requestPermissions(permissions, permissionCheckCallBack)
        return this
    }

    interface PermissionCheckCallBack {
        /**
         * 用户已授予权限
         */
        fun onSucceed()

        /**
         * 用户已拒绝过权限
         *
         * @param permission:被拒绝的权限
         */
        fun onReject(permission: Array<out String>)

        /**
         * 用户已拒绝过并且已勾选不再询问选项、用户第一次申请权限;
         *
         * @param permission:被拒绝的权限
         */
        fun onRejectAndNoAsk(permission: Array<out String>)
    }
}