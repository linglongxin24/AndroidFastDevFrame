package cn.bluemobi.dylan.fastdev.config;


import android.app.Application;

import com.orhanobut.logger.Logger;

import org.xutils.x;

import java.util.Map;

import cn.bluemobi.dylan.fastdev.http.HttpUtils;

/**
 * Created by yuandl on 2016/9/1 0001.
 */
public class Config {

    /**
     * 初始化网络请求的各种参数
     *
     * @param code             请求接口返回码
     * @param data             请求接口返回数据
     * @param msg              请求接口返回信息
     * @param successCode      请求接口成功的响应码
     * @param globalParameters 配置请求接口的全局参数
     */
    public static void initHttp(Application application, String code, String data, String msg, int successCode, Map<String, String> globalParameters) {
        x.Ext.init(application);
        HttpUtils.getInstance().init(code, data, msg, successCode, globalParameters);
    }

    /**
     * 初始化存放文件的路径
     *
     * @param path 存放文件的路径
     */
    public static void initFilePath(String path) {
        FilePath.IMAGE_DIR = path;
    }

    /**
     * 初始化标题栏背景颜色
     *
     * @param color 标题栏背景色
     */
    public static void initTitleBarColor(int color) {
        TitleBarColor.color = color;
    }

    /**
     * 初始化日志TAG
     *
     * @param tag 日志TAG
     */
    public static void initLog(String tag) {
        Logger.init(tag);
    }
}
