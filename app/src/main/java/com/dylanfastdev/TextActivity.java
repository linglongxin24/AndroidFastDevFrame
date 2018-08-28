package com.dylanfastdev;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.Map;

import cn.bluemobi.dylan.base.BaseActivity;
import cn.bluemobi.dylan.base.view.iOSOneButtonDialog;
import cn.bluemobi.dylan.http.Http;
import cn.bluemobi.dylan.http.HttpCallBack;
import cn.bluemobi.dylan.http.ResponseInterceptor;

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
        new iOSOneButtonDialog(mContext).setMessage("三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i  三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i  ").show();

//        startActivity(new Intent(mContext, PayActivity.class));
//        Http.with(mContext)
//                .setObservable(
//                        Http.getApiService(ApiService.class)
//                                .test("13284", 10,1))
//                .setDataListener(new HttpCallBack() {
//                    @Override
//                    public void netOnSuccess(Map<String, Object> data) {
////                      List<Map<String,Object>> list= JsonParse.getList(data, "data");
////                      List<String> s= JsonParse.getList(data, "data");
////                        startActivity(new Intent(mContext,MainActivity.class));
////                        finish();
//                    }
//
//                    @Override
//                    public void netOnFinish() {
//                        super.netOnFinish();
//                    }
//                });
        Http.getHttp().setResponseInterceptor(new ResponseInterceptor() {
            @Override
            public boolean onResponseStart(Context context, String url, Map<String, Object> requestParameter, String responseString, int httpResponseCode) {
                return false;
            }

            @Override
            public boolean onResponse(Context context, int status, String msg, Map<String, Object> data, String url) {
                return false;
            }

        });
        Http.with(mContext)
                .setObservable(
                        Http.getApiService(ApiService.class)
                                .test2("sds", "ss", "ss", "c0RFeGNMcXBySDJkL2xmTjFSaUlTdjlNNlB0ZTV4MnlpVDBHWkN3V3BPeThBS1B5N1o5QVhqbFF2NzM3emVVTWtsaEJMRVFKbEVvTG1ab1RsbHo5S2Z1ZElRT3dwT1FQRy9KTzRoM3J0azdHSGc4QmZsL1JVZ2VDZ2tMeW5IOTBTTHduNWx1OGsyNFNDWU16bWpDVHBTUjhMb3NZNm9KOE1IU2w3elp3MlMwPQ=="))
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
