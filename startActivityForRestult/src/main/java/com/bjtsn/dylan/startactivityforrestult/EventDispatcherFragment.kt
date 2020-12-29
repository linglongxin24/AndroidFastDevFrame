package com.bjtsn.dylan.startactivityforrestult

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray

/**
 * @author YDL
 * @date 2020/12/24/17:43
 * @version 1.0
 */
class EventDispatcherFragment : Fragment() {
    var mCallbacks = SparseArray<StartActivityForResult.CallBack>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun startActivityForResult(intent: Intent, callback: StartActivityForResult.CallBack) {
        val key = randomRequestCode()
        mCallbacks.put(key, callback)
        startActivityForResult(intent, key)
    }

    private fun randomRequestCode(): Int {
        var number = (Math.random() * 100) + 1
        while (mCallbacks.indexOfKey(number.toInt()) != -1) {
            number = (Math.random() * 100) + 1
        }
        return number.toInt()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbacks.get(requestCode)?.onActivityResult(resultCode, data)
        mCallbacks.remove(requestCode)
    }
}