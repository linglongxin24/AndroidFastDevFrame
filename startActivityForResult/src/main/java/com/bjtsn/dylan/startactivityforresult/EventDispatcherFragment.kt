package com.bjtsn.dylan.startactivityforresult

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment

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
        val key = mCallbacks.size()
        mCallbacks.put(key, callback)
        startActivityForResult(intent, key)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbacks.get(requestCode)?.apply {
            mCallbacks.remove(requestCode)
            onActivityResult(resultCode, data)
        }
    }
}