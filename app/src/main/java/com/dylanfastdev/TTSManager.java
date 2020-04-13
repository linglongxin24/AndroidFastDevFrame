package com.dylanfastdev;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

/**
 * @author YDL
 * @version 1.0
 * @date 2020/03/31/9:22
 */
public class TTSManager {
    private  final String TAG="LifecycleObserver";
    private Activity mActivity;
    private LifecycleRegistry mRegistry ;
    public TTSManager(Activity mActivity) {
        this.mActivity = mActivity;
        mRegistry=new LifecycleRegistry(() -> mRegistry);
        mRegistry.addObserver(new LifecycleObserver() {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            public void onCreated(){
                Log.d(TAG, "onCreated: ");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            public void onStart(){
                Log.d(TAG, "onStart: ");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public void onResume(){
                Log.d(TAG, "onResume: ");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            public void onPause(){
                Log.d(TAG, "onPause: ");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            public void onStop(){
                Log.d(TAG, "onStop: ");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public void customMethod(){
                Log.d(TAG, "customMethod: ");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            public void onAny(){//此方法可以有参数，但类型必须如两者之一(LifecycleOwner owner,Lifecycle.Event event)
                Log.d(TAG, "onAny: ");
            }
        });
    }
}
