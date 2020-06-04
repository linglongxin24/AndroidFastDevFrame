package com.dylanfastdev;

import android.os.Bundle;
import android.view.View;
import cn.bluemobi.dylan.base.*;

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
    }

    @Override
    public void addListener() {
        getSupportFragmentManager().beginTransaction().add(R.id.rl, new TestFragment()).commit();
    }

    @Override
    public void onClick(View v) {

    }
}
