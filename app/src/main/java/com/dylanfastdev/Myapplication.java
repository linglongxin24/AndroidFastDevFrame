package com.dylanfastdev;

import android.app.Application;

import com.orhanobut.logger.Logger;

import org.xutils.x;

import java.io.IOException;

import cn.bluemobi.dylan.base.AppConfig;
import cn.bluemobi.dylan.fastdev.config.Config;
import cn.bluemobi.dylan.http.Http;
import cn.bluemobi.dylan.http.MessageManager;
import cn.bluemobi.dylan.uncaughtexception.CustomActivityOnCrash;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author yuandl
 * @date 2016/9/1 0001
 */
public class Myapplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        Logger.init("yanhao");
        Config.initTitleBar(android.R.color.white, android.R.color.white, R.drawable.pub_arrow_back_write);
        Config.setIsImmersionStatus(false);
        CustomActivityOnCrash.install(this);
        CustomActivityOnCrash.setDebugMode(BuildConfig.DEBUG);
        //http请求初始化设置

        Http.getHttp().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

                requestBuilder.addHeader("Userid", "589");
                requestBuilder.addHeader("FAToken", "VTMWBVZO");
                requestBuilder.addHeader("Content-Type", "application/json");
                RequestBody requestBody = original.body();
                requestBuilder.method(original.method(), requestBody);
                Request request = requestBuilder.build();
                Logger.d("request.headers().toString()=" + request.headers().toString());
                return chain.proceed(request);
            }
        });
        Http.getHttp().setDebugMode(BuildConfig.DEBUG);
        Http.getHttp().init(ApiService.class, ApiService.baseUrl, "returnCode", "data", "returnMsg", 200);
        Http.getHttp().setShowMessageModel(MessageManager.MessageModel.All);
        Http.getHttp().setErrorMessage("网络开小差了");
        AppConfig.setAppLeftResId(R.drawable.pub_arrow_back_write);


    }
}
