package com.dylanfastdev;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.Map;

import cn.bluemobi.dylan.base.BaseActivity;
import cn.bluemobi.dylan.base.view.iOSOneButtonDialog;
import cn.bluemobi.dylan.http.Http;
import cn.bluemobi.dylan.http.HttpCallBack;

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


       Http.with(this)
               .setObservable( Http.getApiService(ApiService4.class)
                       .addRunRecorder("4356",123665444,12366324,0,"","","","","","","","","","","","","",""))
                .setDataListener(new HttpCallBack() {
                    @Override
                    public void netOnSuccess(Map<String, Object> data) {

                    }

                    @Override
                    public void netOnSuccessServerError(int code, String errorMessage) {
                        super.netOnSuccessServerError(code, errorMessage);
                        Logger.d("code="+code+"errorMessage="+errorMessage);
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
