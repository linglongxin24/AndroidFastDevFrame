package com.dylanfastdev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orhanobut.logger.Logger;

import cn.bluemobi.dylan.http.lifecycle.LifecycleDetector;
import cn.bluemobi.dylan.http.lifecycle.LifecycleListener;
import cn.bluemobi.dylan.base.*;
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
        LifecycleDetector.getInstance().observer(this, new LifecycleListener() {
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
        startActivityForResult(new Intent(mContext, BActivity.class), (resultCode, data) -> {

        });

    }

    @Override
    public void addListener() {
    }

    @Override
    public void onClick(View v) {

    }
}
