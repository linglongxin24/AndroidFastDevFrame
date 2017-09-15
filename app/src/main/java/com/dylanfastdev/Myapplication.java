package com.dylanfastdev;

import android.app.Application;

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

    }
}
