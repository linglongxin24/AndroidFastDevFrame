package com.dylanfastdev;

import android.os.Bundle;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bluemobi.dylan.base.*;
import cn.bluemobi.dylan.http.Http;
import cn.bluemobi.dylan.http.HttpCallBack;
import cn.bluemobi.dylan.http.OriginalHttpResponse;
import cn.bluemobi.dylan.http.RequestParameter;

/**
 * Created by lenovo on 2017/11/28.
 */

public class TextActivity extends BaseActivity {
    @Override
    public void initTitleBar() {
        setTitle("测试");
        setRightButton("菜单", R.mipmap.dot_focus, null);
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
//        new iOSOneButtonDialog(mContext).setMessage("三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i  三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i 三到四所地所都哦is地偶死都哦已已已已已已已i  ").show();

//        startActivity(new Intent(mContext, PayActivity.class));
//        Http.getHttp().setResponseInterceptor(new ResponseInterceptor() {
//            @Override
//            public boolean onResponseStart(Context context, String url, Map<String, Object> requestParameter, String responseString, int httpResponseCode) {
//                return false;
//            }
//
//            @Override
//            public boolean onResponse(Context context, int status, String msg, Map<String, Object> data, String url) {
//                return false;
//            }
//
//        });
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("UserName", "");
//            jsonObject.put("Phone", "");
//            jsonObject.put("NewPwd", "");
//            jsonObject.put("YesPws", "");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "张三");
//        map.put("phone", "13468857714");
//        map.put("sex", "男");
//
//        Logger.json(JSON.toJSONString(map));
//        Logger.d(JsonParse.jsonToMap(JSON.toJSONString(map)).toString());
//
//        mapList.add(map);
//        Logger.json(JSON.toJSONString(mapList));
//        Logger.d(JsonParse.jsonToListMap(JSON.toJSONString(mapList)).toString());
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        File file = new File("storage/emulated/0/DCIM/Camera/IMG_20181024_144541.jpg");
        if (file.exists()) {
            Logger.d("文件存在");
        }
        List<File> files = new ArrayList<>();
        files.add(file);

        Http.with(mContext).setObservable(Http.getApiService(ApiService.class).upload(
                RequestParameter.getRequestBody("5250"),
                RequestParameter.getRequestBody("234"),
                RequestParameter.getRequestBody("589"),
                RequestParameter.getFilePartMap("file", files)
        )).setDataListener(new OriginalHttpResponse() {
            @Override
            public void netOnStart() {

            }

            @Override
            public void netOnSuccess(String json) {

            }

            @Override
            public void netOnFinish() {

            }

            @Override
            public void netOnFailure(Throwable ex) {

            }
        });
//        throw new RuntimeException("ssddss");
    }

    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
