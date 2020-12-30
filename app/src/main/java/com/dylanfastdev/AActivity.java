package com.dylanfastdev;

import android.os.Bundle;
import android.view.View;

import com.bjtsn.dylan.lifecycleobserver.LifecycleCallback;
import com.bjtsn.dylan.lifecycleobserver.LifecycleObserver;
import com.orhanobut.logger.Logger;

import cn.bluemobi.dylan.base.BaseActivity;

public class AActivity extends BaseActivity {
    @Override
    public void initTitleBar() {
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        new TTSManager(this);
        new LifecycleObserver(mActivity).observer(new LifecycleCallback() {
            @Override
            public void onStart() {
                Logger.d("AActivity=============onStart");
            }

            @Override
            public void onStop() {
                Logger.d("AActivity=============onStop");
            }

            @Override
            public void onDestroy() {
                Logger.d("AActivity=============onDestroy");
            }

            @Override
            public void onResume() {
                Logger.d("AActivity=============onResume");
            }
        });

    }

    @Override
    public void addListener() {
    }

    @Override
    public void onClick(View v) {

    }
}
