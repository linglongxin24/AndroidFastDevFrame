package com.bjtsn.dylan.lifecycleobserver

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * @author YDL
 * @date 2020/12/30/10:21
 * @version 1.0
 */
class LifecycleObserver {
    private var lifecycle: Lifecycle

    constructor(activity: FragmentActivity) {
        lifecycle = activity.lifecycle
    }

    constructor(fragment: Fragment) {
        lifecycle = fragment.lifecycle
    }

    fun observer(lifecycleCallback: LifecycleCallback) {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                lifecycleCallback.onCreate()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                lifecycleCallback.onStart()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                lifecycleCallback.onResume()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                lifecycleCallback.onPause()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                lifecycleCallback.onStop()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                lifecycleCallback.onDestroy()
                lifecycle.removeObserver(this)
            }
        })
    }

}