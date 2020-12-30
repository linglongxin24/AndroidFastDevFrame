package com.bjtsn.dylan.startactivityforresult

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

/**
 * @author YDL
 * @date 2020/12/25/8:59
 * @version 1.0
 */
class StartActivityForResult {
    private var fragment: EventDispatcherFragment
    private val TAG = "START_ACTIVITY_FOR_RESULT_DISPATCHER_FRAGMENT"
    private lateinit var intent:Intent

    constructor(activity: FragmentActivity) {
        this.fragment = getEventDispatchFragment(activity.supportFragmentManager)
    }

    constructor(fragment: Fragment) {
        this.fragment = getEventDispatchFragment(fragment.childFragmentManager)
    }

    private fun getEventDispatchFragment(fragmentManager: FragmentManager): EventDispatcherFragment {
        var fragment = findEventDispatchFragment(fragmentManager)
        if (fragment == null) {
            fragment = EventDispatcherFragment()
            fragmentManager.beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return fragment as EventDispatcherFragment
    }

    private fun findEventDispatchFragment(manager: FragmentManager): Fragment? {
        return manager.findFragmentByTag(TAG)
    }

    fun startActivityForResult(intent: Intent) :StartActivityForResult{
        this.intent=intent;
        return this
    }
    fun setOnActivityResultCallBack(callBack: CallBack){
        fragment.startActivityForResult(intent, callBack)
    }

    interface CallBack {
        fun onActivityResult(resultCode: Int, data: Intent?)
    }

}