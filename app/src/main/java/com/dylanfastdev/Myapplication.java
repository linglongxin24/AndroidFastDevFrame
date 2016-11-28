package com.dylanfastdev;

import android.app.Application;

import com.orhanobut.logger.Logger;

import org.xutils.x;

/**
 * Created by yuandl on 2016/9/1 0001.
 */
public class Myapplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        Logger.init("yanhao");
    }
}
