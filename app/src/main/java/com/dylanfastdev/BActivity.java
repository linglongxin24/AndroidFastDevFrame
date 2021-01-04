package com.dylanfastdev;

import android.os.Bundle;
import android.view.View;

import java.util.Map;

import cn.bluemobi.dylan.base.BaseActivity;
import cn.bluemobi.dylan.http.Http;
import cn.bluemobi.dylan.http.HttpCallBack;

public class BActivity extends BaseActivity {
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
        Http.INSTANCE.init(ApiService.class,"https://www.travel-network.xin/","status","data","message",200);
        Http.with(mContext)
                .setObservable(
                        Http.getApiService(ApiService.class)
                                .test2("sds", "ss", "ss", "c0RFeGNMcXBySDJkL2xmTjFSaUlTdjlNNlB0ZTV4MnlpVDBHWkN3V3BPeThBS1B5N1o5QVhqbFF2NzM3emVVTWtsaEJMRVFKbEVvTG1ab1RsbHo5S2Z1ZElRT3dwT1FQRy9KTzRoM3J0azdHSGc4QmZsL1JVZ2VDZ2tMeW5IOTBTTHduNWx1OGsyNFNDWU16bWpDVHBTUjhMb3NZNm9KOE1IU2w3elp3MlMwPQ=="))
                .setDataListener(new HttpCallBack() {
                    @Override
                    public void netOnSuccess(Map<String,  Object> data) {
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
