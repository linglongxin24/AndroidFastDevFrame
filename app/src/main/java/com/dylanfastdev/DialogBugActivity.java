package com.dylanfastdev;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import cn.bluemobi.dylan.base.BaseActivity;
import cn.bluemobi.dylan.base.view.iOSOneButtonDialog;

/**
 * @author dylan
 * @date 2019/10/24
 */
public class DialogBugActivity extends BaseActivity {
    @Override
    public void initTitleBar() {
        setTitle("测试三星手机后台弹框bug");
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
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new iOSOneButtonDialog(mContext).setMessage("测试弹框有没有出来").show();
//            }
//        }, 30 * 1000);
        new iOSOneButtonDialog(mContext)
                .setCenterCustomView(R.layout.dialog)
                .setMessage("测试弹框有没有出来").show();

    }

    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
