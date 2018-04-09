package com.dylanfastdev;

import android.app.Application;

import cn.bluemobi.dylan.base.AppConfig;
import cn.bluemobi.dylan.http.Http;
import cn.bluemobi.dylan.http.MessageManager;
import cn.bluemobi.dylan.uncaughtexception.CustomActivityOnCrash;
import com.orhanobut.logger.Logger;

import org.xutils.x;

import cn.bluemobi.dylan.fastdev.config.Config;

/**
 * Created by yuandl on 2016/9/1 0001.
 */
public class Myapplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        Logger.init("yanhao");
        Config.initTitleBar(android.R.color.white,android.R.color.white,R.drawable.pub_arrow_back_write);
        Config.setIsImmersionStatus(false);
        CustomActivityOnCrash.install(this);
        CustomActivityOnCrash.setDebugMode(BuildConfig.DEBUG);
        CustomActivityOnCrash.setEmailTo(new String[]{"13468857714@qq.com"});
        //http请求初始化设置
        Http.getHttp().setDebugMode(BuildConfig.DEBUG);
        Http.getHttp().init(ApiService.class, ApiService.baseUrl, "returnCode", "data", "returnMsg", 200);
        Http.getHttp().setShowMessageModel(MessageManager.MessageModel.All);
        Http.getHttp().setErrorMessage("网络开小差了");
        AppConfig.setAppLeftResId(R.drawable.pub_arrow_back_write);
    }
}
