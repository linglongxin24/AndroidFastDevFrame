package com.dylanfastdev;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bluemobi.dylan.base.BaseActivity;
import cn.bluemobi.dylan.http.Http;
import cn.bluemobi.dylan.http.HttpCallBack;
import cn.bluemobi.dylan.http.MD5Utils;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/11/28.
 */

public class TextActivity extends BaseActivity {
    @Override
    public void initTitleBar() {
        setTitle("测试");
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

//        startActivity(new Intent(mContext, PayActivity.class));
        Http.with(mContext)
                .setObservable(
                        Http.getApiService(ApiService.class)
                                .test("13284", 10,1))
                .setDataListener(new HttpCallBack() {
                    @Override
                    public void netOnSuccess(Map<String, Object> data) {
//                      List<Map<String,Object>> list= JsonParse.getList(data, "data");
//                      List<String> s= JsonParse.getList(data, "data");
//                        startActivity(new Intent(mContext,MainActivity.class));
//                        finish();
                    }

                    @Override
                    public void netOnFinish() {
                        super.netOnFinish();
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
