package com.dylanfastdev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

import cn.bluemobi.dylan.base.BaseActivity;
import cn.bluemobi.dylan.http.Http;
import cn.bluemobi.dylan.http.HttpCallBack;
import cn.bluemobi.dylan.http.JsonParse;

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
        Http.with(mContext)
                .setObservable(
                        Http.getApiService(ApiService.class)
                                .login("17010120022", "111111"))
                .setDataListener(new HttpCallBack() {
                    @Override
                    public void netOnSuccess(Map<String, Object> data) {
                      List<Map<String,Object>> list= JsonParse.getList(data, "data");
                      List<String> s= JsonParse.getList(data, "data");
                        startActivity(new Intent(mContext,MainActivity.class));
                        finish();
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
